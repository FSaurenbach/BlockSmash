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

        var directFitCounter = 0
        val directBlocks = mutableListOf<block>()
        val problemBlocks = mutableListOf<block>()
        for (block in blocks) {
            if (checkPossibleLocations(block) != 0) {
                directFitCounter++
                directBlocks.add(block)
            } else {
                problemBlocks.add(block)
            }

        }

        println("Amount of blocks that fit directly: $directFitCounter")
        if (directFitCounter < 3) {

            TODO("check if problem")

        }

    }

    private fun checkPossibleLocations(block: block): Int {
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
        var pLAmount = 0 /*Amount of possible locations*/
        var pLList = mutableListOf<block>() /*List of possible locations*/

        for (row in 0..7) {
            for (col in 0..7) {
                var amount = 0
                var amountToMatch = 0
                var valid = true

                for (blockRow in 0..2) {
                    for (blockCol in 0..2) {
                        if (block[blockRow][blockCol] == 1) {
                            amountToMatch++
                            val targetRow = row + blockRow
                            val targetCol = col + blockCol

                            if (targetRow > 7 || targetCol > 7) {
                                valid = false
                                break
                            }

                            if (gameField[targetRow][targetCol] == 0) {
                                amount++
                            } else {
                                valid = false
                                break
                            }
                        }
                    }
                    if (!valid) break
                }

                if (valid && amount == amountToMatch) {
                    pLAmount++
                    println("POSSIBLE ROW/COL: $row, $col")
                }
            }
        }



        println("possibleLocations: $pLAmount")




        return pLAmount
    }

}

