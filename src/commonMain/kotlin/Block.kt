import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.math.geom.*

enum class BlockType{
    ONEbyONE, TWObyTWO, BigL
}


object BlockColors {
    val Black get() = Colors["#141414"]
    val Blue get() = Colors["#6f6fff"]
    val Red get() = Colors["#911100"]
}

fun Container.block(color: RGBA, blockType: BlockType) = Block(color, blockType).addTo(this)

class Block(private var color: RGBA, blockType: BlockType) : Container() {
    var draggable: Boolean = true
    init {
        val theWhole = container()
        this.position(200, 650)
        when (blockType){
            BlockType.ONEbyONE -> theWhole.roundRect(Size(cs, cs), RectCorners(5f), fill = color)
            BlockType.TWObyTWO -> twobytwo(theWhole)
            else -> println("ERROR")
        }
    }
    private fun twobytwo(container: Container){
        container.roundRect(Size(cs,cs), RectCorners(5f), fill = color)
    }

}

