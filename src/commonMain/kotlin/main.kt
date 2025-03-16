import korlibs.image.color.*
import korlibs.image.font.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import kotlin.Number
import kotlin.math.*
import kotlin.properties.*

var font: BitmapFont by Delegates.notNull()
var backgroundField: RoundRect? = null
val blocks = mutableMapOf<Int, Block>()
var fields = mutableListOf<Field>()
var fieldSize = Size(560, 560)
var cs = fieldSize.height / 8
var leftOccupied = false
var middleOccupied = false
var rightOccupied = false
suspend fun main() = Korge(
    virtualSize = Size(480, 853),

    title = "Block Smash",
    bgcolor = Colors["#5a78c5"],
    /**
    `gameId` is associated with the location of storage, which contains `history` and `best`.
    see [Views.realSettingsFolder]
     */
    gameId = "io.github.sauronbach.blockSmash",
    forceRenderEveryFrame = false, // Optimization to reduce battery usage!
) {
    font = resourcesVfs["clear_sans.fnt"].readBitmapFont()


    backgroundField = roundRect(fieldSize, RectCorners(5f), Colors["#202443"])
    backgroundField!!.centerOnStage()
    backgroundField!!.y -= 70
    convertToRealX(5)
    populateField(this)

    //val testBlock = block(BlockColors.Red, BlockType.TWObyTWO, StartPosition.LEFT)
    createPieces(this)

}


fun populateField(container: Container) {
    for (x in 0..7) {
        for (y in 0..7) {
            val f = container.field(convertToRealX(x).toInt(), convertToRealY(y).toInt(), x, y)

            fields.add(f)
            f.onClick {
                println("YOU CLICKED ON ${f.fieldX} ${f.fieldY}, with the real coords: ${f.realX} ${f.realY}")
                println("Converted to realX: ${convertToCoordX(f.fieldX)}, realY: ${convertToCoordY(f.fieldY)}")
            }
        }
    }
}


fun convertToRealX(fieldCoordinate: Int): Number {
    return backgroundField!!.x + cs * fieldCoordinate
}

fun convertToRealY(fieldCoordinate: Int): Number {
    return backgroundField!!.y + cs * fieldCoordinate
}

fun convertToCoordX(realX: Int): Number {
    return round((realX - backgroundField!!.x) / cs)
}

fun convertToCoordY(realY: Int): Number {
    return round((realY - backgroundField!!.y) / cs)
}

fun createPieces(container: Container) {
    var location: StartPosition? = null
    for (i in 0..2) {

        when {
            !leftOccupied -> {
                location = StartPosition.LEFT
                leftOccupied = true
            }
            !middleOccupied -> {
                location = StartPosition.MIDDLE
                middleOccupied = true
            }
            !rightOccupied -> {
                location = StartPosition.RIGHT
                rightOccupied = true
            }
        }
        val color = BlockColors.getRandomColor()
        container.block(color, BlockType.TWObyTWO, location!!)

    }
}
