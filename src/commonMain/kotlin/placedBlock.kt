import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.math.geom.*

fun Container.placedBlock(color: RGBA, fieldX: Int, fieldY: Int) =
    PlacedBlock(color, fieldX, fieldY).addTo(this)


class PlacedBlock(color: RGBA, var fieldX: Int,var fieldY: Int) : Container() {
    init {
        roundRect(Size(cs, cs), RectCorners(5f), fill = color)
        val realX = convertToRealX(fieldX).toInt()
        val realY = convertToRealY(fieldY).toInt()
        this.position(realX, realY)
        placedBlocks.add(this)
        //println("RealX: $realX, RealY: $realY, Field: $fieldX, $fieldY")
    }

}
