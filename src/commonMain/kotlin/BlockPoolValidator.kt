import kotlin.properties.*

typealias Board = Array<IntArray>
typealias block = Array<Array<Int>>

class BlockPoolValidator {
    private var gameField: Board = Array(8) { IntArray(8) { 0 } }

    init {
        printBoard()
    }

    private fun printBoard() {
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
        updateArray()
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

        println("VERTICAL LENGTH: $verticalLength")
        println("HORIZONTAL LENGTH: $horizontalLength")
        for (row in block) {
            println(row.joinToString(" ") { cell ->
                if (cell == 0) "·" else "■"
            })
        }
        var possibleLocations = 0
        var possibleLocations2 = 0

        for (row in 0..7) {
            for (col in 0..7) {
                var counter = 0
                var amountToMatch = 0
                var possible = true

                for (blockRow in 0..2) {
                    for (blockCol in 0..2) {
                        if (block[blockRow][blockCol] == 1) {
                            amountToMatch++
                            val targetRow = row + blockRow
                            val targetCol = col + blockCol

                            if (targetRow > 7 || targetCol > 7) {
                                possible = false
                                break
                            }

                            if (gameField[targetRow][targetCol] == 0) {
                                counter++
                            } else {
                                possible = false
                                break
                            }
                        }
                    }
                    if (!possible) break
                }

                if (possible && counter == amountToMatch) {
                    possibleLocations++
                    println("POSSIBLE ROW/COL: $row, $col")
                }
            }
        }



        println("possibleLocations: $possibleLocations")
        println("possible if field was empty: $possibleLocations2")




        return possibleLocations != 0
    }

}

