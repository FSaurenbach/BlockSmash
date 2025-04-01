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
import kotlin.math.*
import kotlin.properties.*

val rows = mutableListOf<MutableList<MutableList<Field>>>()
var font: BitmapFont by Delegates.notNull()
var backgroundField: RoundRect? = null
var fields = mutableListOf<Field>()
var fieldSize = Size(560, 560)
var cs = fieldSize.height / 8
var leftOccupied = false
var middleOccupied = false
var rightOccupied = false
var sContainer: Container? = null
val occupiedFields = mutableListOf<Field>()
val allblocks = mutableListOf<Block>()
var placedBlocks = mutableListOf<PlacedBlock>()
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
    val background = LinearGradientPaint(
        0, 0, 0, 853, cycle = CycleMethod.NO_CYCLE
    ) {
        // Subtle blue gradient, slight change from light to slightly darker blue
        addColorStop(0.0, RGBA(95, 146, 255)) // Lighter blue at the top
        addColorStop(1.0, RGBA(65, 102, 163)) // Slightly darker blue at the bottom
    }
    roundRect(Size(1920, 1080), RectCorners(5f), background).centerOnStage()
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

    for (field in fields) {
        // Ensure that the rows list has enough sublists to accommodate all Y values
        while (rows.size <= field.fieldY) {
            rows.add(mutableListOf())
        }

        // Ensure that each row has enough sublists to accommodate all X values
        while (rows[field.fieldY].size <= field.fieldX) {
            rows[field.fieldY].add(mutableListOf())
        }

        rows[field.fieldY][field.fieldX].add(field)
    }
    println(rows)

}


fun convertToRealX(fieldCoordinate: Int): Number {
    return backgroundField!!.x + cs * fieldCoordinate
}

fun convertToRealY(fieldCoordinate: Int): Number {
    return backgroundField!!.y + cs * fieldCoordinate
}

fun convertToCoordX(realX: Int): Int {
    return round((realX - backgroundField!!.x) / cs).toInt()
}

fun convertToCoordY(realY: Int): Int {
    return round((realY - backgroundField!!.y) / cs).toInt()
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
        var c = container.block(color, BlockTypeHelper.getRandomBlockType(), location!!)
        allblocks.add(c)

    }
}

fun addNewPieces() {
    if (!leftOccupied && !middleOccupied && !rightOccupied) createPieces(sContainer!!)
}


fun checkForBlast() {
    for (rowY in 0 until 8) {
        var counter = 0
        val checkedBlocks = mutableListOf<PlacedBlock>()
        for (placedBlock in placedBlocks) {
            if (placedBlock.fieldY == rowY) {
                checkedBlocks.add(placedBlock)
                counter++
            }
        }
        println("Row: $rowY, counter: $counter")
        if (counter == 8) {
            for (block in checkedBlocks) {
                var occupiedField = fields.find { it.pos == block.pos }
                //println("Removing occupied field:$occupiedField")
                block.removeFromParent()
                occupiedFields.remove(occupiedField)
                placedBlocks.remove(block)
                occupiedField?.occupied = false

            }

        }
        //println("Current row: $rowY, counter:$counter ${checkedBlocks.count()}")
        checkedBlocks.clear()
    }

}






