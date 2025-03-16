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
    val Black get() = Colors["#141414"]
    val Blue get() = Colors["#6f6fff"]
    val Red get() = Colors["#911100"]
}

fun Container.block(color: RGBA, blockType: BlockType, startPosition: StartPosition) =
    Block(color, blockType, startPosition).addTo(this)

class Block(private var color: RGBA, blockType: BlockType, startPosition: StartPosition) : Container() {
    var placed: Boolean = true

    init {
        val theWhole = this // Was originally a container() but should work like this too
        when (startPosition) {
            StartPosition.LEFT -> this.position(0, 680)
            StartPosition.MIDDLE -> this.position(200, 680)
            StartPosition.RIGHT -> this.position(400, 680)
        }
        when (blockType) {
            BlockType.ONEbyONE -> theWhole.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
            BlockType.TWObyTWO -> twobytwo(theWhole)
            else -> println("ERROR")
        }
        this.draggable {
            if (it.end) {
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

                    if (blockPosition1 == fieldPosition) {
                        if (checkIfCorrectlyPlaced(this)){
                            println("Placed correctly snapping:")
                            it.view.position(field.globalPos)
                            placed = true
                        }


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

            if (blockPosition1 == fieldPosition) testsPassed++



        }

    }
    return testsPassed == testsToPass
}
