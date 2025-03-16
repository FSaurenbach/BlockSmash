import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.math.geom.*

fun Container.field(realX: Int, realY: Int, fieldX: Int, fieldY: Int) = Field(realX, realY, fieldX, fieldY).addTo(this)

class Field(var realX: Int, var realY: Int, var fieldX: Int, var fieldY: Int) : Container() {
    var occupied = false

    init {
        roundRect(Size(cs, cs), RectCorners(5f), fill = Colors["#35385c"])
        this.position(realX, realY)
    }


}

