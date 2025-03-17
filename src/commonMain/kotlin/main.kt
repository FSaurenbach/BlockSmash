import korlibs.image.color.*
import korlibs.image.font.*
import korlibs.image.paint.*
import korlibs.image.vector.*
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
var sContainer: Container? = null
suspend fun main() = Korge(
    virtualSize = Size(480, 853),

    title = "Block Smash",
    bgcolor = Colors["#4c65a4"],
    /**
    `gameId` is associated with the location of storage, which contains `history` and `best`.
    see [Views.realSettingsFolder]
     */
    gameId = "io.github.sauronbach.blockSmash",
    forceRenderEveryFrame = false, // Optimization to reduce battery usage!
) {
    var background = LinearGradientPaint(
        0, 0, // x0, y0: Start at the top-left corner
        0, 853, // x1, y1: End at the bottom-left corner (vertical gradient)
        cycle = CycleMethod.NO_CYCLE
    ) {
        // Subtle blue gradient, slight change from light to slightly darker blue
        addColorStop(0.0, RGBA(95, 146, 255)) // Lighter blue at the top
        addColorStop(1.0, RGBA(65, 102, 163)) // Slightly darker blue at the bottom
    }
    roundRect(Size(1920,1080), RectCorners(5f), background).centerOnStage()
    font = resourcesVfs["clear_sans.fnt"].readBitmapFont()

    sContainer = this

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
        container.block(color, BlockTypeHelper.getRandomBlockType(), location!!)

    }
}
fun checker(){
    if (!leftOccupied && !middleOccupied && !rightOccupied) createPieces(sContainer!!)
}
