typealias Board = Array<IntArray>
typealias block = Array<IntArray>
class BlockPoolValidator {
    var gameField:Board = Array(8) { IntArray(8) { 0 } }
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
    fun updateArray():Board?{
        this.reset()
        println("OCCUPIED FIELDS")
        for (field in occupiedFields){
            gameField[field.fieldY][field.fieldX] = 1
        }
        printBoard()
        return null
    }
    fun reset(){
        gameField = Array(8) { IntArray(8) { 0 } }
    }
}

