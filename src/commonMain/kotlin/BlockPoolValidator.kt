typealias Board = Array<IntArray>
typealias BlockBlueprint = Array<Array<Int>>

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

    private fun printBlock(block: BlockBlueprint) {
        for (row in block) {
            println(row.joinToString(" ") { cell ->
                if (cell == 0) "·" else "■"
            })
        }
        println()
    }

    private fun copyBoard(src: Board): Board = Array(src.size) { row -> src[row].copyOf() }

    fun updateArray(): Board? {
        this.reset()
        for (field in occupiedFields) {
            gameField[field.fieldY][field.fieldX] = 1
        }
        //printBoard()
        return null
    }

    private fun reset() {
        gameField = Array(8) { IntArray(8) { 0 } }
    }

    fun checkPool(pool: MutableList<BlockBlueprint>): Boolean {
        updateArray()
        printBoard(gameField)

        val startingBlocks = mutableListOf<BlockBlueprint>()
        val startingBlocksLocations = mutableListOf<List<Pair<Int, Int>>>()
        val problemBlocks = mutableListOf<BlockBlueprint>()
        for (block in pool) {
            val pLList = checkPossibleLocations(block, gameField)
            if (pLList.isNotEmpty()) {
                startingBlocks.add(block)
                startingBlocksLocations.add(pLList)
            } else {
                problemBlocks.add(block)
            }

        }

        println("Amount of valid starting blocks: ${startingBlocks.count()}")
        var validPool = true
        var currentBlock = 0

        // Generate permutations(all possible placement combinations of the pieces)
        val permutationsList = mutableListOf<List<BlockBlueprint>>()
        for (i in 0..2) {

            val blockPool = mutableListOf<BlockBlueprint>();for (b in pool) blockPool.add(b)
            val permutation = mutableListOf<BlockBlueprint>()
            val permutationCopy = mutableListOf<BlockBlueprint>()
            permutation.add(blockPool[i])
            permutationCopy.add(blockPool[i])
            blockPool.removeAt(i)

            for (integer in 0..1) {
                if (integer == 0) {
                    permutation.add(blockPool[0])
                    permutation.add(blockPool[1])
                } else {
                    permutationCopy.add(blockPool[1])
                    permutationCopy.add(blockPool[0])
                }
            }

            permutationsList.add(permutation)
            permutationsList.add(permutationCopy)
        }
        // Filter out all bad permutations!
        val badCombinations = mutableListOf<List<BlockBlueprint>>()
        for (combination in permutationsList) {
            problemBlocks.forEach {
                if (it.contentDeepEquals(combination[0])) {
                    println("At least one permutation is bad.")
                    badCombinations.add(combination)
                }
            }
        }
        permutationsList.removeAll(badCombinations)
        for (combination in permutationsList) {
            val possibleLocations0 = checkPossibleLocations(combination[0], gameField)
            for (positionPair in possibleLocations0) {
                val field = copyBoard(gameField)
                placeBlock(combination[0], positionPair, field)
                checkForBlast(field)
                val possibleLocations1 = checkPossibleLocations(combination[1], gameField)
                if (possibleLocations1.isNotEmpty()) {
                    for (positionPair in possibleLocations1) {
                        placeBlock(combination[1], positionPair, field)
                        checkForBlast(field)
                        val possibleLocations2 = checkPossibleLocations(combination[2], gameField)
                        if (possibleLocations2.isNotEmpty()) {
                            for (positionPair in possibleLocations2) {
                                placeBlock(combination[2], positionPair, field)
                                checkForBlast()
                                println("VALID POOL!!")
                                return true
                            }
                        }
                    }
                }
            }

        }
        println("INVALID POOL!!")
        return false



















        if (0 == 0) return validPool

        // old check (bad)
        for (block in startingBlocks) {
            val combinationList = mutableListOf<BlockBlueprint>()
            startingBlocks.forEach {
                combinationList.add(it)
            }
            // Check individual blocks if placing them anywhere on the field can result in the problem blocks being placeable
            for (positionPair in startingBlocksLocations[currentBlock]) {
                val field = copyBoard(gameField)
                //println("PLACING NEW:")

                placeBlock(block, positionPair, field)
                combinationList.remove(block)
                //println("Versuche Block-Platzierung bei row=${positionPair.first}, col=${positionPair.second}")
                //printBoard(field)
                // A BLOCK HAS BEEN PLACED. CHECK IF THE PROBLEM BLOCKS CAN BE PLACED AFTER CHECKING FOR A BLAST!!
                if (checkForBlast(field)) {
                    //check if placement is possible now
                    val toRemove = mutableListOf<BlockBlueprint>()
                    for (problem in problemBlocks) {
                        if (checkPossibleLocations(problem, field).isNotEmpty()) {
                            println("A PROBLEM CAN BE RESOLVED. NO FURTHER TESTING IS NEEDED.")
                            toRemove.add(problem)
                        }
                    }
                    problemBlocks.removeAll(toRemove)
                    if (problemBlocks.isNotEmpty()) {
                        if (combinationList.isNotEmpty()) {
                            for (comb in combinationList) {
                                val locs = checkPossibleLocations(comb, field)
                                if (locs.isNotEmpty()) {
                                    var integer = 0
                                    for (positionPair in locs) {
                                        placeBlock(comb, positionPair, field)
                                        //println("Versuche Block-Platzierung bei row=${positionPair.first}, col=${positionPair.second}")
                                        //printBoard(field)
                                        if (checkForBlast(field)) {
                                            //check if placement is possible now
                                            val toRemove = mutableListOf<BlockBlueprint>()
                                            for (problem in problemBlocks) {
                                                if (checkPossibleLocations(problem, field).isNotEmpty()) {
                                                    println("THERE IS A POSSIBLE COMBINATION.")
                                                    validPool = true

                                                }
                                            }
                                            problemBlocks.removeAll(toRemove)
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
        }/*If all problems have been resolved set the pool as valid!*/
        if (problemBlocks.isEmpty()) validPool = true
        return validPool


    }

    private fun checkPossibleLocations(block: BlockBlueprint, field: Board): MutableList<Pair<Int, Int>> {

        /*for (row in block) {
            println(row.joinToString(" ") { cell ->
                if (cell == 0) "·" else "■"
            })
        }*/
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

                            if (field[targetRow][targetCol] == 0) {
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



        //println("possibleLocations: $pLAmount")




        return pLList
    }

    private fun checkForBlast(field: Board): Boolean {
        var blast = false
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
                blast = true
            }
        }
        for (col in 0..7) {
            val ocpFields = mutableListOf<Pair<Int, Int>>()
            for (row in 0..7) {
                if (field[row][col] == 1) {
                    ocpFields.add(Pair(row, col))
                }
            }
            if (ocpFields.count() == 8) {
                for ((r, c) in ocpFields) {
                    field[r][c] = 0

                }
                blast = true
            }
        }
        return blast
    }

    private fun placeBlock(block: BlockBlueprint, positionPair: Pair<Int, Int>, field: Board) {
        //printBlock(block)
        //printBoard(field)
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

