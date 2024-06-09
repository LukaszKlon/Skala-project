class Sudoku(val grid: Array[Array[Int]]) {
    def this() = this(Array.ofDim[Int](9, 9))

    def apply(row: Int, col: Int): Int = grid(row)(col)

    def update(row: Int, col: Int, value: Int): Unit = grid(row)(col) = value

    def cloneSudoku: Sudoku = {
        new Sudoku(grid.map(_.clone()))
    }

    override def toString: String = {
        grid.map(_.mkString(" ")).mkString("\n")
    }
}
