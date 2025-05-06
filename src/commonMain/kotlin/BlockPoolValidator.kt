import kotlin.properties.*

typealias Board = Array<IntArray>
typealias block = Array<Array<Int>>

class BlockPoolValidator {
    private var gameField: Board = Array(8) { IntArray(8) { 0 } }

    init {
        printBoard()
    }

    fun printBoard() {
        println("Aktueller Spielstand:\n")
        for (row in gameField) {
            println(row.joinToString(" ") { cell ->
                if (cell == 0) "·" else "■"
            })
        }
    }

    fun updateArray(): Board? {
        this.reset()
        println("OCCUPIED FIELDS")
        for (field in occupiedFields) {
            gameField[field.fieldY][field.fieldX] = 1
        }
        printBoard()
        return null
    }

    private fun reset() {
        gameField = Array(8) { IntArray(8) { 0 } }
    }

    fun checkPool(pool: MutableList<block>) {
        val blocks = mutableListOf<block>()
        for (block in pool) {
            blocks.add(block)

        }

        var directFitCouter = 0
        for (block in blocks) {
            if (checkDirectFit(block)) directFitCouter++

        }
        println("Amount of blocks that fit directly: $directFitCouter")
        var orders = 6

    }

    private fun checkDirectFit(block: block): Boolean {
        var verticalLength = 0
        var horizontalLength = 0
        if (block[0][0] == 1 || block[1][0] == 1 || block[2][0] == 1) {
            verticalLength++
        }
        if (block[0][1] == 1 || block[1][1] == 1 || block[2][1] == 1) {
            verticalLength++
        }
        if (block[0][2] == 1 || block[1][2] == 1 || block[2][2] == 1) {
            verticalLength++
        }
        // Horizontal Length
        if (block[0][0] == 1 || block[0][1] == 1 || block[0][2] == 1) {
            horizontalLength++
        }
        if (block[1][0] == 1 || block[1][1] == 1 || block[1][2] == 1) {
            horizontalLength++
        }
        if (block[2][0] == 1 || block[2][1] == 1 || block[2][2] == 1) {
            horizontalLength++
        }
/*
        println("VERTICAL LENGTH: $verticalLength")
        println("HORIZONTAL LENGTH: $horizontalLength")
        for (row in block) {
            println(row.joinToString(" ") { cell ->
                if (cell == 0) "·" else "■"
            })
        }*/
        var possibleLocations = 0
        for (row in 0..7) {
            for (col in 0..7) {
                // Check if the give block could be placed at these coordinates
                var valid = true
                var counter = 0
                for (blockRow in 0..2) {
                    for (blockCol in 0..2) {
                        val r = row + blockRow
                        val c = col + blockCol

                        if (r > 7 || c > 7) {
                            valid = false
                            break
                        }
                    }
                }
                if (valid) {
                    for (blockRow in 0..2) {
                        for (blockCol in 0..2) {
                            if (row + blockRow <= 7 && col + blockCol <= 7) {


                                if (block[blockRow][blockCol] == 0) counter++
                                if (block[blockRow][blockCol] == 1) {

                                    if (gameField[row + blockRow][col + blockCol] == 0) counter++

                                }
                            }
                        }
                    }
                }
                if (counter == 9) {
                    possibleLocations++
                    /*println("POSSIBLE ROW/COL: $row, $col")*/
                }
            }
        }

        println("possibleLocations: $possibleLocations")




        return if(possibleLocations !=0) true else false
    }

}

