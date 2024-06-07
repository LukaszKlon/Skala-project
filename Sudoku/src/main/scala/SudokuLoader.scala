import scala.io.Source

class SudokuLoader {
    def sudokuLoad(filename: String): Sudoku = {
        val file = Source.fromFile(filename)
        val sudoku = file.getLines().map { line =>
            line.map {
                case c if '0' to '9' contains c => c.toInt - 48
                case _ => 0
            }.toArray
        }.toArray
        file.close()
        new Sudoku(sudoku)
    }
}

object SudokuApp {
    def main(args: Array[String]): Unit = {

        val sudokuSolver = new SudokuSolver()
        val recursiveSudoku = new RecursiveSudokuSolver()
        val (sudoku,solve) = sudokuSolver.solve("C:\\Users\\mateu\\Desktop\\stuff\\programowanie\\Skala-project\\Sudoku\\src\\main\\scala\\text",useActors = true)
        val recursiveSolvedSudoku = recursiveSudoku.solve(sudoku)
        println("Loaded Sudoku")
        println(sudoku)
        println("Solved Sudoku")
        println(solve)
        println("Recursive Solved Sudoku")
        println(recursiveSolvedSudoku)

    }
}