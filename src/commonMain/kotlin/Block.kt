import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*

enum class BlockType{
    ONEbyONE, TWObyTWO, BigL
}
enum class StartPosition{
    LEFT, MIDDLE, RIGHT
}

object BlockColors {
    val Black get() = Colors["#141414"]
    val Blue get() = Colors["#6f6fff"]
    val Red get() = Colors["#911100"]
}

fun Container.block(color: RGBA, blockType: BlockType, startPosition: StartPosition) = Block(color, blockType, startPosition).addTo(this)

class Block(private var color: RGBA, blockType: BlockType, startPosition: StartPosition) : Container() {
    var placed: Boolean = true
    init {
        val theWhole = container()
        when (startPosition){
            StartPosition.LEFT -> this.position(0,680)
            StartPosition.MIDDLE -> this.position(200, 680)
            StartPosition.RIGHT -> this.position(400, 680)
        }
        when (blockType){
            BlockType.ONEbyONE -> theWhole.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
            BlockType.TWObyTWO -> twobytwo(theWhole)
            else -> println("ERROR")
        }
    }
    private fun twobytwo(container: Container){
        val one = container.roundRect(Size(cs,cs), RectCorners(5f), fill = color)
        var two = container.roundRect(Size(cs,cs), RectCorners(5f), fill = color).alignLeftToRightOf(one)
        val three = container.roundRect(Size(cs,cs), RectCorners(5f), fill = color).alignTopToBottomOf(one)
        val four = container.roundRect(Size(cs,cs), RectCorners(5f), fill = color).alignLeftToRightOf(three).alignTopToBottomOf(one)
    }

}

