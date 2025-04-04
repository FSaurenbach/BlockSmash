import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import kotlin.math.*

enum class BlockType {
    ONEbyONE, TWObyTWO, BigL,
  //  OnebyThree, TWObyTHREE, THREEbyTHREE,


}
enum class BlockRotation {
    zero, quarter, half, threequarters
}
object BlockRotationHelper {
    fun getRandomRotation(): BlockRotation {
        val blockRotations = BlockRotation.values().toList()
        return blockRotations.random()  // Picks a random BlockType
    }
}

enum class StartPosition {
    LEFT, MIDDLE, RIGHT
}

object BlockTypeHelper {
    fun getRandomBlockType(): BlockType {
        val blockTypes = BlockType.values().toList()
        return blockTypes.random()  // Picks a random BlockType
    }
}

object BlockColors {
    private val Blue get() = Colors["#264597"]
    private val Red get() = Colors["#e53935"]
    private val Green get() = Colors["#0cdc2d"]
    private val Yellow get() = Colors["#ffff00"]
    private val Purple get() = Colors["#800080"]
    private val Orange get() = Colors["#ff6600"]

    fun getRandomColor(): RGBA {
        val colors = listOf(Blue, Red, Green, Yellow, Purple, Orange)
        return colors.random()  // Picks a random RGBA object
    }
}

fun Container.block(color: RGBA, blockType: BlockType, startPosition: StartPosition, rotation: BlockRotation) =
    Block(color, blockType, startPosition, rotation).addTo(this)

class Block(private var color: RGBA, blockType: BlockType, startPosition: StartPosition, rotation: BlockRotation) : Container() {
    private var placed: Boolean = false

    init {
        val theWhole = this // Was originally a container() but should work like this too
        when (startPosition) {
            StartPosition.LEFT -> this.position(-40, 680)
            StartPosition.MIDDLE -> this.position(170, 680)
            StartPosition.RIGHT -> this.position(380, 680)
        }
        when (blockType) {
            BlockType.ONEbyONE -> theWhole.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
            BlockType.TWObyTWO -> twobytwo(theWhole)
            BlockType.BigL -> bigL(theWhole)
            else -> throw error("Block has to be defined")
        }
        when (rotation){
            BlockRotation.zero -> this.rotation = Angle.ZERO
            BlockRotation.quarter -> this.rotation = Angle.QUARTER
            BlockRotation.half -> this.rotation = Angle.HALF
            BlockRotation.threequarters -> this.rotation = 270.degrees
        }
        this.scale(0.5)
        var closeable: DraggableCloseable? = null
        closeable = this.draggableCloseable {
            if (it.start) {

                println(this.zIndex)
                this.zIndex(99)

                this.scale(1)
            }
            if (it.end) {
                this.zIndex(0)
                println("dragging ended: snapping!")
                println("viewNextX: ${round(it.viewNextX).toInt()}, viewNextY: ${round(it.viewNextY).toInt()}")

                val blockPosition1 = Point(
                    convertToCoordX(round(it.view.globalPos.x).toInt()).toInt(), convertToCoordY(
                        round(it.view.globalPos.y).toInt()
                    ).toInt()
                )
                for (field in fields) {
                    val fieldPosition = Point(
                        convertToCoordX(round(field.globalPos.x).toInt()).toInt(), convertToCoordY(
                            round(field.globalPos.y).toInt()
                        ).toInt()
                    )

                    //println("Block position converted ${blockPosition1.x}, ${blockPosition1.y}")
                    //println("Field position converted ${fieldPosition.x}, ${fieldPosition.y}")

                    if (blockPosition1 == fieldPosition && checkIfCorrectlyPlaced(this)) {
                        println("Placed correctly snapping:")
                        it.view.position(field.globalPos)
                        this.forEachChild {
                            sContainer!!.placedBlock(color, convertToCoordX(it.globalPos.x.toInt()), convertToCoordY(it.globalPos.y.toInt()))
                            //var oc = occupiedFields.find { it.fieldX == convertToCoordX(it.globalPos.x.toInt())&& it.fieldY == convertToCoordY(it.globalPos.y.toInt()) }
                            //occupiedFields.remove(oc)
                            //println("removing $oc")
                        }
                        placed = true
                        closeable!!.close()
                        when (startPosition) {
                            StartPosition.LEFT -> leftOccupied = false
                            StartPosition.MIDDLE -> middleOccupied = false
                            StartPosition.RIGHT -> rightOccupied = false
                        }
                        //println("Left: $leftOccupied, middle: $middleOccupied, right: $rightOccupied")
                        addNewPieces()
                        allblocks.remove(this)
                        println("count:"+ occupiedFields.count())
                        this.removeFromParent()
                        checkForBlast()

                    }


                }
                if (!placed) {
                    when (startPosition) {
                        StartPosition.LEFT -> this.position(-40, 680)
                        StartPosition.MIDDLE -> this.position(170, 680)
                        StartPosition.RIGHT -> this.position(380, 680)
                    }
                }
            }


        }
    }

    private fun twobytwo(container: Container) {
        val one = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
        container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(one)
        val three = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(one)
        container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(three)
            .alignTopToBottomOf(one)
    }

    private fun bigL(container: Container) {
        val one = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
        val two = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(one)
        val three = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(two)
        val four = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(three)
            .alignTopToBottomOf(two)
        container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(four)
            .alignTopToBottomOf(two)
    }

}

fun checkIfCorrectlyPlaced(wholeBlock: Block): Boolean {
    val testsToPass = wholeBlock.children.count()
    println("tests to pass: $testsToPass")
    var testsPassed = 0

    for (block in wholeBlock.children) {
        val blockPosition1 = Point(
            convertToCoordX(round(block.globalPos.x).toInt()), convertToCoordY(
                round(block.globalPos.y).toInt()
            )
        )
        for (field in fields) {
            val fieldPosition = Point(
                convertToCoordX(round(field.globalPos.x).toInt()), convertToCoordY(
                    round(field.globalPos.y).toInt()
                )
            )
            //if (field.occupied) println("field: $field is occupied")

            if (blockPosition1 == fieldPosition && !field.occupied) {
                testsPassed++
                occupiedFields.add(field)
            }


        }

    }
    if (testsPassed == testsToPass) {
        for (field in occupiedFields) {
            field.occupied = true
        }
    }
    println("test passed: ${testsPassed==testsToPass}")
    return testsPassed == testsToPass
}


