typealias Board = Array<IntArray>
typealias blockBlueprint = Array<Array<Int>>

class BlockPoolValidator {
    private var gameField: Board = Array(8) { IntArray(8) { 0 } }

    init {
        printBoard()
    }

    private fun printBoard(board: Board = gameField) {
        println("Aktueller Spielstand:\n")
        for (row in board) {
            println(row.joinToString(" ") { cell ->
                if (cell == 0) "·" else "■"
            })
        }
    }

    private fun printBlock(block: blockBlueprint) {
        for (row in block) {
            println(row.joinToString(" ") { cell ->
                if (cell == 0) "·" else "■"
            })
        }
    }

    private fun copyBoard(src: Board): Board = Array(src.size) { row -> src[row].copyOf() }

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

    fun checkPool(pool: MutableList<blockBlueprint>): Boolean {
        updateArray()
        val blocks = mutableListOf<blockBlueprint>()
        for (block in pool) {
            blocks.add(block)

        }

        var directFitCounter = 0
        val directBlocks = mutableListOf<blockBlueprint>()
        val directBlockPL = mutableListOf<List<Pair<Int, Int>>>()
        val problemBlocks = mutableListOf<blockBlueprint>()
        for (block in blocks) {
            val pLList = checkPossibleLocations(block)
            if (pLList.isNotEmpty()) {
                directFitCounter++
                directBlocks.add(block)
                directBlockPL.add(pLList)
            } else {
                problemBlocks.add(block)
            }

        }

        println("Amount of blocks that fit directly: $directFitCounter")
        if (directFitCounter < 3) {
            var validPool = true
            var currentBlock = 0
            for (block in directBlocks) {
                val combinationList = mutableListOf<blockBlueprint>()
                directBlocks.forEach {
                    combinationList.add(it)
                }
                // Check individual blocks if placing them anywhere on the field can result in the problem blocks being placeable
                for (positionPair in directBlockPL[currentBlock]) {
                    val field = copyBoard(gameField)
                    println("PLACING NEW:")

                    placeBlock(block, positionPair, field)
                    combinationList.remove(block)
                    println("Versuche Block-Platzierung bei row=${positionPair.first}, col=${positionPair.second}")
                    printBoard(field)
                    // A BLOCK HAS BEEN PLACED. CHECK IF THE PROBLEM BLOCKS CAN BE PLACED AFTER CHECKING FOR A BLAST!!
                    if (checkForBlast(field)) {
                        //check if placement is possible now
                        for (problem in problemBlocks) {
                            if (checkPossibleLocations(problem).isNotEmpty()) {
                                println("A PROBLEM CAN BE RESOLVED. NO FURTHER TESTING IS NEEDED.")
                                problemBlocks.remove(problem)
                            }
                        }
                        if (problemBlocks.isNotEmpty()) {
                            if (combinationList.isNotEmpty()) {
                                for (comb in combinationList) {
                                    var locs = checkPossibleLocations(comb)
                                    if (locs.isNotEmpty()) {
                                        var integer = 0
                                        for (positionPair in locs) {
                                            placeBlock(block, positionPair, field)
                                            println("Versuche Block-Platzierung bei row=${positionPair.first}, col=${positionPair.second}")
                                            printBoard(field)
                                            if (checkForBlast(field)) {
                                                //check if placement is possible now
                                                for (problem in problemBlocks) {
                                                    if (checkPossibleLocations(problem).isNotEmpty()) {
                                                        println("THERE IS A POSSIBLE COMBINATION.")
                                                        validPool = true
                                                        problemBlocks.remove(problem)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }


                    } else {

                        validPool = false
                    }


                }
                currentBlock++
            }
            /*If all problems have been resolved set the pool as valid!*/
            if (problemBlocks.isEmpty()) validPool = true
            return validPool

        } else return true
    }

    private fun checkPossibleLocations(block: blockBlueprint): MutableList<Pair<Int, Int>> {
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
        val pLList = mutableListOf<Pair<Int, Int>>() /*List of possible locations*/

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
                    pLList.add(Pair(row, col))
                    //println("POSSIBLE ROW/COL: $row, $col")
                }
            }
        }



        println("possibleLocations: $pLAmount")




        return pLList
    }

    private fun checkForBlast(field: Board): Boolean {

        for (row in 0..7) {
            val ocpFields = mutableListOf<Pair<Int, Int>>()
            for (col in 0..7) {
                if (field[row][col] == 1) {
                    ocpFields.add(Pair(row, col))
                }
            }
            if (ocpFields.count() == 8) {
                for ((r, c) in ocpFields) {
                    field[r][c] = 0

                }
                return true
            }
        }
        return false
    }

    private fun placeBlock(block: blockBlueprint, positionPair: Pair<Int, Int>, field: Board) {
        printBlock(block)
        printBoard(field)
        for (blockRow in 0..2) {
            for (blockCol in 0..2) {
                if (block[blockRow][blockCol] == 1) {
                    val targetRow = positionPair.first + blockRow
                    val targetCol = positionPair.second + blockCol


                    field[targetRow][targetCol] = 1


                }


            }
        }
    }
}

