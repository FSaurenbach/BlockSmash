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
        var block1: block? = null
        var block2: block? = null
        var block3: block? = null

        for (block in pool) {
            if (block1 == null) block1 = block
            if (block2 == null) block2 = block
            if (block3 == null) block3 = block
            println("NEW BLOCK")
            for (row in block) {
                println(row.joinToString(" ") { cell ->
                    if (cell == 0) "·" else "■"
                })
            }
        }
        var blocks = listOf(block1!!, block2!!, block3!!)
        var directFitCouter = 0
        for (block in blocks){
            if (checkDirectFit(block)) directFitCouter++

        }
        println("Amount of blocks that fit directly: $directFitCouter")
        var orders = 6

    }

    fun checkDirectFit(block: block): Boolean {
        for (r in 0..7) {
            for (c in 0..7) {
                // Check if the block can fit at this position
                var canFit = true
                for (dr in block.indices) {
                    for (dc in block[dr].indices) {
                        if (block[dr][dc] == 1) {
                            if (r + dr !in 0..7 || c + dc !in 0..7 || gameField[r + dr][c + dc] == 1) {
                                // If the block doesn't fit or collides with an existing block
                                canFit = false
                                break
                            }
                        }
                    }
                    if (!canFit) break
                }

                // If the block fits, place it
                if (canFit) {
                    return true
                    /*for (dr in block.indices) {
                        for (dc in block[dr].indices) {
                            if (block[dr][dc] == 1) {
                                gameField[r + dr][c + dc] = 1
                            }
                        }
                    }*/
                }
            }
        }
        return false
    }

}

