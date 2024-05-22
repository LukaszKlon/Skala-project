import scala.io.Source

class SudokuLoader {
    def sudokuLoad(filename: String): Array[Array[Int]] = {
        val file = Source.fromFile(filename)
        val sudoku = file.getLines().map { line =>
            line.map {
                case c if '0' to '9' contains c => c.toInt - 48
                case _ => 0
            }.toArray
        }.toArray
        file.close()
        sudoku
    }
}

object SudokuApp {
    def main(args: Array[String]): Unit = {

        val sudokuSolver = new SudokuSolver()
        val (sudoku,solve) = sudokuSolver.solve("C:\\Users\\klonl\\OneDrive\\Pulpit\\STUDIA\\semestr4\\Scala\\Skala-project\\Sudoku\\src\\main\\scala\\text",useActors = true)
        println("Loaded Sudoku")
        sudoku.foreach(row => println(row.mkString(" ")))
        println("Solved Sudoku")
        solve.foreach(row => println(row.mkString(" ")))

    }
}