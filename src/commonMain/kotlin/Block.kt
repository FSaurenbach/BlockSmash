import korlibs.image.color.*
import korlibs.image.paint.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*

fun Container.block(color: RGBA) = Block(color).addTo(this)

class Block(color: ColorPaint) : Container() {
    var draggable: Boolean = true
    init {
        roundRect(Size(cs, cs), RectCorners(5f), fill = color).draggable {  }

    }
}

