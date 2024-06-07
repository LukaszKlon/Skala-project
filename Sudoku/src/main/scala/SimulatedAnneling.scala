import scala.util.Random

class SimulatedAnneling(val startTemperature: Double, val lossTemperature: Double, val epsilon: Double) {

    private val sudokuFiller = new SudokuFiller()
    var sudokuMatrix: Sudoku = new Sudoku()
    private var guessFields: Array[Array[(Int, Int)]] = Array.empty

    private def checkRowIncorrect(rowNumber: Int): Int = {
        val existInRow: Array[Boolean] = Array.fill(9)(false)
        var score: Int = 0

        for (i <- 0 until 9) {
            val index = sudokuMatrix(rowNumber, i) - 1
            if (existInRow(index)) {
                score += 1
            }
            existInRow(index) = true
        }
        score
    }

    private def checkColIncorrect(colNumber: Int): Int = {
        val existInCol: Array[Boolean] = Array.fill(9)(false)
        var score: Int = 0

        for (i <- 0 until 9) {
            val index = sudokuMatrix(i, colNumber) - 1
            if (existInCol(index)) {
                score += 1
            }
            existInCol(index) = true
        }
        score
    }

    private def calculateScore(): Int = {
        var score: Int = 0
        for (i <- 0 until 9) {
            score = score + checkRowIncorrect(i)
            score = score + checkColIncorrect(i)
        }
        score
    }

    def simulatedAnneling(): Boolean = {

        if (sudokuMatrix.grid.isEmpty) {
            println("You don't set value")
            return false
        }
        var currentTemperature = startTemperature
        var randomSudokuSquare: Array[(Int, Int)] = null
        var randomIndex1: Int = 0
        var randomIndex2: Int = 0
        var buffer = 0
        var score = calculateScore()

        while (currentTemperature > epsilon) {

            randomSudokuSquare = guessFields(Random.nextInt(guessFields.length))
            randomIndex1 = Random.nextInt(randomSudokuSquare.length)
            randomIndex2 = Random.nextInt(randomSudokuSquare.length)
            val (x1, y1) = randomSudokuSquare(randomIndex1)
            val (x2, y2) = randomSudokuSquare(randomIndex2)
            buffer = sudokuMatrix(x1, y1)
            sudokuMatrix(x1, y1) = sudokuMatrix(x2, y2)
            sudokuMatrix(x2, y2) = buffer
            val temporaryScore = calculateScore()

            if (temporaryScore == 0) return true

            val difference = temporaryScore - score

            if (difference * Random.nextDouble() < 0.01 * currentTemperature) {
                score = temporaryScore
            } else {
                buffer = sudokuMatrix(x1, y1)
                sudokuMatrix(x1, y1) = sudokuMatrix(x2, y2)
                sudokuMatrix(x2, y2) = buffer
            }

            currentTemperature = currentTemperature * lossTemperature
        }

        false
    }

    def setSudoku(sudokuMatrix: Sudoku): Unit = {
        this.sudokuMatrix = sudokuMatrix.cloneSudoku
        this.guessFields = sudokuFiller.fillSudoku(this.sudokuMatrix)
    }
}
