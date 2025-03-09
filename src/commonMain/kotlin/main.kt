import korlibs.image.color.*
import korlibs.image.font.*
import korlibs.image.text.*
import korlibs.io.async.ObservableProperty
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.service.storage.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import kotlin.properties.*

var cellSize: Float = 0f
var fieldSize: Float = 0f
var leftIndent: Float = 0f
var topIndent: Float = 0f
var font: BitmapFont by Delegates.notNull()

fun columnX(number: Int) = leftIndent + 10 + (cellSize + 10) * number
fun rowY(number: Int) = topIndent + 10 + (cellSize + 10) * number

var map = PositionMap()
val blocks = mutableMapOf<Int, Block>()
var history: History by Delegates.notNull()

fun numberFor(blockId: Int) = blocks[blockId]!!.number
fun deleteBlock(blockId: Int) = blocks.remove(blockId)!!.removeFromParent()

val score = ObservableProperty(0)
val best = ObservableProperty(0)

var freeId = 0
var isAnimationRunning = false
var isGameOver = false

suspend fun main() = Korge(
    virtualSize = Size(480, 853),

    title = "Block Smash",
    bgcolor = RGBA(253, 247, 240),
    /**
    `gameId` is associated with the location of storage, which contains `history` and `best`.
    see [Views.realSettingsFolder]
     */
    gameId = "io.github.sauronbach.blockSmash",
    forceRenderEveryFrame = false, // Optimization to reduce battery usage!
) {
    font = resourcesVfs["clear_sans.fnt"].readBitmapFont()

    val storage = views.storage
    history = History(storage.getOrNull("history")) {
        storage["history"] = it.toString()
    }
    best.update(storage.getOrNull("best")?.toInt() ?: 0)

    score.observe {
        if (it > best.value) best.update(it)
    }
    best.observe {
        storage["best"] = it.toString()
    }

    var field = field()


    val bgBest = roundRect(Size(cellSize * 1.5, cellSize * 0.8), RectCorners(5f), fill = Colors["#bbae9e"]) {
        centerXOnStage()
    }
    text("BEST", cellSize * 0.25f, RGBA(239, 226, 210), font) {
        centerXOn(bgBest)
        alignTopToTopOf(bgBest, 5.0)
    }
    text(best.value.toString(), cellSize * 0.5f, Colors.WHITE, font) {
        setTextBounds(Rectangle(0f, 0f, bgBest.width, cellSize - 24f))
        alignment = TextAlignment.MIDDLE_CENTER
        alignTopToTopOf(bgBest, 12.0)
        centerXOn(bgBest)
        best.observe {
            text = it.toString()
        }
    }

    val bgScore = roundRect(Size(cellSize * 1.5f, cellSize * 0.8f), RectCorners(5.0f), fill = Colors["#bbae9e"]) {
        alignRightToLeftOf(bgBest, 24.0)
        alignTopToTopOf(bgBest)
    }
    text("SCORE", cellSize * 0.25f, RGBA(239, 226, 210), font) {
        centerXOn(bgScore)
        alignTopToTopOf(bgScore, 5.0)
    }
    text(score.value.toString(), cellSize * 0.5f, Colors.WHITE, font) {
        setTextBounds(Rectangle(0f, 0f, bgScore.width, cellSize - 24f))
        alignment = TextAlignment.MIDDLE_CENTER
        centerXOn(bgScore)
        alignTopToTopOf(bgScore, 12.0)
        score.observe {
            text = it.toString()
        }
    }


}

