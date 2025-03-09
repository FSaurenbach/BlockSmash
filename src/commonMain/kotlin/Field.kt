import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*

fun Container.field() = Field().addTo(this)

class Field : Container() {

    init {
        roundRect(Size(480, 480), RectCorners(5f), fill = Colors.BLUE).centerOnStage()

    }
}
