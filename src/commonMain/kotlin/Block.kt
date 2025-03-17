import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import kotlin.math.*

enum class BlockType {
    ONEbyONE, TWObyTWO, BigL
}

enum class StartPosition {
    LEFT, MIDDLE, RIGHT
}

object BlockColors {
    private val Black get() = Colors.BLACK
    private val Blue get() = Colors["#0a00ff"]
    private val Red get() = Colors["#911100"]
    private val Green get() = Colors["#00ff00"]
    private val Yellow get() = Colors["#ffff00"]
    private val Purple get() = Colors["#800080"]
    private val Orange get() = Colors["#ff6600"]

    fun getRandomColor(): RGBA {
        val colors = listOf(Black, Blue, Red, Green, Yellow, Purple, Orange)
        return colors.random()  // Picks a random RGBA object
    }
}

fun Container.block(color: RGBA, blockType: BlockType, startPosition: StartPosition) =
    Block(color, blockType, startPosition).addTo(this)

class Block(private var color: RGBA, blockType: BlockType, startPosition: StartPosition) : Container() {
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
            else -> println("ERROR")
        }
        var closeable:DraggableCloseable? = null
        closeable = this.draggableCloseable {
            if (it.start){
                println(this.zIndex)
                this.zIndex(99)
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
                        placed = true
                        closeable!!.close()
                        when (startPosition) {
                            StartPosition.LEFT -> leftOccupied = false
                            StartPosition.MIDDLE -> middleOccupied = false
                            StartPosition.RIGHT -> rightOccupied = false
                        }
                        println("Left: $leftOccupied, middle: $middleOccupied, right: $rightOccupied")
                        checker()

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
        var two = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(one)
        val three = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(one)
        val four = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(three)
            .alignTopToBottomOf(one)
    }

}

fun checkIfCorrectlyPlaced(wholeBlock: Block): Boolean {
    val testsToPass = wholeBlock.children.count()
    println("tests to pass: $testsToPass")
    var testsPassed = 0
    var occupiedFields = mutableListOf<Field>()
    for (block in wholeBlock.children) {
        val blockPosition1 = Point(
            convertToCoordX(round(block.globalPos.x).toInt()).toInt(), convertToCoordY(
                round(block.globalPos.y).toInt()
            ).toInt()
        )
        for (field in fields) {
            val fieldPosition = Point(
                convertToCoordX(round(field.globalPos.x).toInt()).toInt(), convertToCoordY(
                    round(field.globalPos.y).toInt()
                ).toInt()
            )

        if (blockPosition1 == fieldPosition && !field.occupied) {
            testsPassed++
            occupiedFields.add(field)
        }


        }

    }
    if (testsPassed == testsToPass){
        for (field in occupiedFields){
            field.occupied = true
        }
    }
    return testsPassed == testsToPass
}
