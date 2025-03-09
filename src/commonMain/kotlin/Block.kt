import korlibs.image.color.*
import korlibs.image.paint.*
import korlibs.korge.view.*
import korlibs.math.geom.*

fun Container.block(color: RGBA) = Block(color).addTo(this)

class Block(color: ColorPaint) : Container() {

    init {
        roundRect(Size(cellSize, cellSize), RectCorners(5f), fill = color)

    }
}

