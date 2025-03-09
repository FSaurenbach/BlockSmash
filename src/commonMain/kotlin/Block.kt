import Number.*
import korlibs.korge.view.*
import korlibs.image.color.*
import korlibs.korge.view.align.centerBetween
import korlibs.math.geom.RectCorners
import korlibs.math.geom.Size

fun Container.block(number: Number) = Block(number).addTo(this)

class Block(val number: Number) : Container() {

    init {
        roundRect(Size(cellSize, cellSize), RectCorners(5f), fill = number.color)
        val textColor = when (number) {
            ZERO, ONE -> Colors.BLACK
            else -> Colors.WHITE
        }
        text(number.value.toString(), textSizeFor(number), textColor, font).apply {
            centerBetween(0f, 0f, cellSize, cellSize)
        }
    }
}

private fun textSizeFor(number: Number): Float = when (number) {
    ZERO, ONE, TWO, THREE, FOUR, FIVE -> (cellSize / 2).toFloat()
    SIX, SEVEN, EIGHT -> (cellSize * 4 / 9).toFloat()
    NINE, TEN, ELEVEN, TWELVE -> (cellSize * 2 / 5).toFloat()
    THIRTEEN, FOURTEEN, FIFTEEN -> (cellSize * 7 / 20).toFloat()
    SIXTEEN -> (cellSize * 3 / 10).toFloat()
}
