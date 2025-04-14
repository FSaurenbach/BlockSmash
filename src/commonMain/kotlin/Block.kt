import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.random.*
import kotlin.collections.random
import kotlin.math.*

enum class BlockType {
    // Single block
    ONE_BY_ONE,

    // Straight blocks (all rotations are the same)
    ONE_BY_TWO,
    TWO_BY_ONE,
    ONE_BY_THREE,
    THREE_BY_ONE,
    ONE_BY_FOUR,
    FOUR_BY_ONE,
    ONE_BY_FIVE,
    FIVE_BY_ONE,

    // Square blocks (all rotations are the same)
    TWO_BY_TWO,
    THREE_BY_THREE,

    // L-Shaped blocks (all rotations)
    L_2X2_0,
    L_2X2_90,
    L_2X2_180,
    L_2X2_270,

    L_2X3_0,
    L_2X3_90,
    L_2X3_180,
    L_2X3_270,

    L_3X3_0,
    L_3X3_90,
    L_3X3_180,
    L_3X3_270,

    // T-Shaped blocks (all rotations)
    T_2X3_0,
    T_2X3_90,
    T_2X3_180,
    T_2X3_270,

    // Rectangle blocks (all rotations)
    TWO_BY_THREE_0,
    TWO_BY_THREE_90,
    THREE_BY_TWO_0,
    THREE_BY_TWO_90,

    // S-Shaped blocks (all rotations)
    S_2X3_0,
    S_2X3_90,
    S_2X3_180,
    S_2X3_270
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

fun Container.block(color: RGBA, blockType: BlockType, startPosition: StartPosition) =
    Block(color, blockType, startPosition).addTo(this)

class Block(private var color: RGBA, blockType: BlockType, startPosition: StartPosition) : Container() {
    private var placed: Boolean = false
    private var master: RoundRect? = null

    init {

        val theWhole = this // Was originally a container() but should work like this too
        when (startPosition) {
            StartPosition.LEFT -> this.position(windowWidth * 0.2, windowHeight * 0.8)
            StartPosition.MIDDLE -> this.position(windowWidth * 0.4, windowHeight * 0.8)
            StartPosition.RIGHT -> this.position(windowWidth * 0.6, windowHeight * 0.8)
        }
        when (blockType) {
            BlockType.ONE_BY_ONE -> oneByOne(theWhole)
            BlockType.TWO_BY_TWO -> twoByTwo(theWhole)
            BlockType.L_3X3_0 -> bigL(theWhole)
            else -> bigL(theWhole)
            //else -> bigL(theWhole)
        }
        this.scale(0.5)

        var closeable: DraggableCloseable? = null
        closeable = this.draggableCloseable { it ->
            //println("viewNextX: ${round(master!!.getPositionRelativeTo(first!!).x).toInt()}, viewNextY: ${round(master!!.getPositionRelativeTo(first!!).y).toInt()}")

            if (it.start) {
                println("master: $master")
                println(this.zIndex)
                this.zIndex(99)

                this.scale(1)
            }
            if (it.end) {
                this.zIndex(0)
                println("dragging ended: snapping!")
                println("viewNextX: ${master!!.globalPos.x}, viewNextY: ${master!!.globalPos.y}")
                val blockPosition1 = Point(
                    convertToCoordinateX(round(master!!.globalPos.x).toInt()), convertToCoordinateY(
                        round(master!!.globalPos.y).toInt()
                    )
                )
                for (field in fields) {
                    val fieldPosition = Point(
                        convertToCoordinateX(round(field.globalPos.x).toInt()), convertToCoordinateY(
                            round(field.globalPos.y).toInt()
                        )
                    )

                    //println("Block position converted ${blockPosition1.x}, ${blockPosition1.y}")
                    //println("Field position converted ${fieldPosition.x}, ${fieldPosition.y}")

                    if (blockPosition1 == fieldPosition && checkIfCorrectlyPlaced(this)) {
                        println("Placed correctly snapping:")
                        //field.colorMul = Colors.RED
                        //master!!.position(field.globalPos)
                        master!!.globalPos = field.globalPos
                        println("this global: ${this.globalPos}")
                        this.forEachChild {
                            //if (it == master) return@forEachChild
                            println("one child")

                            sContainer!!.placedBlock(
                                color,
                                convertToCoordinateX(it.globalPos.x.toInt()),
                                convertToCoordinateY(it.globalPos.y.toInt())
                            )
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
                        allBlocks.remove(this)
                        println("count:" + occupiedFields.count())
                        master?.removeFromParent()
                        this.removeFromParent()
                        checkForBlast()

                    }


                }
                if (!placed) {
                    when (startPosition) {
                        StartPosition.LEFT -> this.position(windowWidth * 0.3, windowHeight * 0.7)
                        StartPosition.MIDDLE -> this.position(windowWidth * 0.6, windowHeight * 0.7)
                        StartPosition.RIGHT -> this.position(windowWidth * 0.9, windowHeight * 0.7)
                    }
                }
            }


        }
    }

    private fun oneByOne(container: Container) {
        val one = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
        master = one
    }

    private fun twoByTwo(container: Container) {
        val one = container.roundRect(Size(cs, cs), RectCorners(5f), fill = Colors.RED)
        master = one
        container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(one)
        val three = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(one)
        container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(three)
            .alignTopToBottomOf(one)
    }

    private fun bigL(container: Container) {
        val rotation = random.get(range = 0..3)
        println("Rotation: $rotation")
        container.size = Size(cs * 3, cs * 3)

        when (rotation) {
            0 -> {
                // Original: vertical line + rightward tail
                val one = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
                val two = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(one)
                val three = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(two)
                val four = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(three)
                val five = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(four)
                master = three
            }

            1 -> {
                // 90°: horizontal line + upward tail
                val one = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
                val two = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(one)
                val three = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(two)
                val four = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(two)
                val five = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(four)
                master = two
            }

            2 -> {
                // 180°: vertical line + leftward tail
                val one = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
                val two = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(one)
                val three = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignTopToBottomOf(two)
                val four = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignRightToLeftOf(one)
                val five = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignRightToLeftOf(four)
                master = three
            }

            3 -> {
                // 270°: horizontal line + downward tail
                val one = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
                val two = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(one)
                val three = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignLeftToRightOf(two)
                val four = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignBottomToTopOf(two)
                val five = container.roundRect(Size(cs, cs), RectCorners(5f), fill = color).alignBottomToTopOf(four)
                master = two
            }
        }

        //master!!.color = Colors.RED
    }


}

fun checkIfCorrectlyPlaced(wholeBlock: Block): Boolean {
    val testsToPass = wholeBlock.children.count()
    println("tests to pass: $testsToPass")
    var testsPassed = 0

    for (block in wholeBlock.children) {
        val blockPosition1 = Point(
            convertToCoordinateX(round(block.globalPos.x).toInt()), convertToCoordinateY(
                round(block.globalPos.y).toInt()
            )
        )
        for (field in fields) {
            val fieldPosition = Point(
                convertToCoordinateX(round(field.globalPos.x).toInt()), convertToCoordinateY(
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
    println("test passed: ${testsPassed == testsToPass}")
    return testsPassed == testsToPass
}


