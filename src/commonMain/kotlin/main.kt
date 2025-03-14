import korlibs.image.color.*
import korlibs.image.font.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import kotlin.Number
import kotlin.properties.*

var cellSize: Float = 0f
var font: BitmapFont by Delegates.notNull()

var backgroundField: RoundRect? = null
val blocks = mutableMapOf<Int, Block>()

var fieldSize = Size(560, 560)
var cs = fieldSize.height / 8
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
    convertRealX(5)
    populateField(this)

    var testBlock = block(Colors["#ff0008"]).centerXOnStage()
    testBlock.onClick {
        println("af")
    }

}

fun populateField(container: Container) {
    for (x in 0..7) {
        for (y in 0..7) {
            val f = container.field(convertRealX(x).toInt(), convertRealY(y).toInt(), x, y)
            f.onClick {
                println("YOU CLICKED ON ${f.fieldX} ${f.fieldY}, with the real coords: ${f.realX} ${f.realY}")
            }
        }
    }
}


fun convertRealX(fieldCoordinate: Int): Number {
    return backgroundField!!.x + cs * fieldCoordinate
}

fun convertRealY(fieldCoordinate: Int): Number {
    return backgroundField!!.y + cs * fieldCoordinate
}

fun convertXPosition(p:Double):Number{
    return
}
