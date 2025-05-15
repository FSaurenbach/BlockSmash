import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import kotlin.math.*
import kotlin.properties.*

var cell11: RoundRect by Delegates.notNull()
var cell12: RoundRect by Delegates.notNull()
var cell13: RoundRect by Delegates.notNull()
var cell21: RoundRect by Delegates.notNull()
var cell22: RoundRect by Delegates.notNull()
var cell23: RoundRect by Delegates.notNull()
var cell31: RoundRect by Delegates.notNull()
var cell32: RoundRect by Delegates.notNull()
var cell33: RoundRect by Delegates.notNull()


enum class StartPosition {
    LEFT, MIDDLE, RIGHT
}


object BlockColors {
    private val Blue = Colors["#264597"]
    private val Red = Colors["#e53935"]
    private val Green = Colors["#0cdc2d"]
    private val Yellow = Colors["#ffff00"]
    private val Purple = Colors["#800080"]
    private val Orange = Colors["#ff6600"]

    private val colorList = listOf(Blue, Red, Green, Yellow, Purple, Orange)

    fun getRandomColor(): RGBA {
        return colorList.random()
    }
}
fun Container.block(color: RGBA, blockType: Array<Array<Int>>, startPosition: StartPosition) =
    Block(color, blockType, startPosition).addTo(this)

class Block(private var color: RGBA, blockType: Array<Array<Int>>, startPosition: StartPosition) : Container() {
    private var placed: Boolean = false
    private var master: RoundRect by Delegates.notNull()


    init {
        val templateColor = Colors.PURPLE

        cell11 = roundRect(Size(cs, cs), RectCorners(5f), fill = templateColor)

        cell12 = roundRect(Size(cs, cs), RectCorners(5f), fill = templateColor).alignLeftToRightOf(cell11)
        cell13 = roundRect(Size(cs, cs), RectCorners(5f), fill = templateColor).alignLeftToRightOf(cell12)
        cell21 = roundRect(Size(cs, cs), RectCorners(5f), fill = templateColor).alignTopToBottomOf(cell11)
        cell22 = roundRect(Size(cs, cs), RectCorners(5f), fill = templateColor).alignLeftToRightOf(cell21)
            .alignTopToBottomOf(cell12)
        cell23 = roundRect(Size(cs, cs), RectCorners(5f), fill = templateColor).alignLeftToRightOf(cell22)
            .alignTopToBottomOf(cell13)
        cell31 = roundRect(Size(cs, cs), RectCorners(5f), fill = templateColor).alignTopToBottomOf(cell21)
        cell32 = roundRect(Size(cs, cs), RectCorners(5f), fill = templateColor).alignLeftToRightOf(cell31)
            .alignTopToBottomOf(cell22)
        cell33 = roundRect(Size(cs, cs), RectCorners(5f), fill = templateColor).alignLeftToRightOf(cell32)
            .alignTopToBottomOf(cell23)

        when (startPosition) {
            StartPosition.LEFT -> position(leftStart)
            StartPosition.MIDDLE -> position(middleStart)
            StartPosition.RIGHT -> position(rightStart)
        }

        initBlock(blockType)
        this.scale(0.5)

        var closeable: DraggableCloseable? = null
        closeable = this.draggableCloseable(autoMove = false) { it ->

            if (it.start) {
                println("master: $master")
                println(this.zIndex)
                this.zIndex(99)

                this.scale(1)
                this.y -= 150
            }
            it.view.x = it.viewNextXY.x
            it.view.y = it.viewNextXY.y - 150
            if (it.end) {
                this.zIndex(0)
                println("dragging ended: snapping!")
                println("viewNextX: ${this[0].globalPos.x}, viewNextY: ${this[0].globalPos.y}")
                val blockPosition1 = Point(
                    convertToCoordinateX(round(this[0].globalPos.x).toInt()), convertToCoordinateY(
                        round(this[0].globalPos.y).toInt()
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
                        this[0].globalPos = field.globalPos
                        println("this global: ${this.globalPos}")
                        this.forEachChild {
                            sContainer.placedBlock(
                                color,
                                convertToCoordinateX(it.globalPos.x.toInt()),
                                convertToCoordinateY(it.globalPos.y.toInt())
                            )
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
                        //println("count:" + occupiedFields.count())
                        master.removeFromParent()
                        this.removeFromParent()
                        checkForBlast()
                        updateScore(0)

                    }


                }
                if (!placed) {
                    this.scale(0.5)

                    when (startPosition) {
                        StartPosition.LEFT -> position(leftStart)
                        StartPosition.MIDDLE -> position(middleStart)
                        StartPosition.RIGHT -> position(rightStart)
                    }
                }
            }


        }
    }


    private fun initBlock(array: Array<Array<Int>>) {

        if (array[0][0] == 1) {
            this.roundRect(Size(cs, cs), RectCorners(5f), fill = color).centerOn(cell11)
        }
        if (array[0][1] == 1) {
            this.roundRect(Size(cs, cs), RectCorners(5f), fill = color).centerOn(cell12)
        }
        if (array[0][2] == 1) {
            this.roundRect(Size(cs, cs), RectCorners(5f), fill = color).centerOn(cell13)
        }
        if (array[1][0] == 1) {
            this.roundRect(Size(cs, cs), RectCorners(5f), fill = color).centerOn(cell21)
        }
        if (array[1][1] == 1) {
            this.roundRect(Size(cs, cs), RectCorners(5f), fill = color).centerOn(cell22)
        }
        if (array[1][2] == 1) {
            this.roundRect(Size(cs, cs), RectCorners(5f), fill = color).centerOn(cell23)
        }
        if (array[2][0] == 1) {
            this.roundRect(Size(cs, cs), RectCorners(5f), fill = color).centerOn(cell31)
        }
        if (array[2][1] == 1) {
            this.roundRect(Size(cs, cs), RectCorners(5f), fill = color).centerOn(cell32)
        }
        if (array[2][2] == 1) {
            this.roundRect(Size(cs, cs), RectCorners(5f), fill = color).centerOn(cell33)
        }
        //println("c: ${this[0].globalPos}")
        if (this[0] is RoundRect) master = this[0] as RoundRect
        //println(this[9])
        cell11.removeFromParent()
        cell12.removeFromParent()
        cell13.removeFromParent()
        cell21.removeFromParent()
        cell22.removeFromParent()
        cell23.removeFromParent()
        cell31.removeFromParent()
        cell32.removeFromParent()
        cell33.removeFromParent()


    }
}

fun checkIfCorrectlyPlaced(wholeBlock: Block): Boolean {
    val testsToPass = wholeBlock.children.count()
    //println("tests to pass: $testsToPass")
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
    //println("test passed: ${testsPassed == testsToPass}")
    return testsPassed == testsToPass
}


