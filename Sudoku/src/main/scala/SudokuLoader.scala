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
