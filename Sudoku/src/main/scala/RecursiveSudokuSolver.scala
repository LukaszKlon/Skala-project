class RecursiveSudokuSolver {

    def solve(sudoku: Sudoku): Sudoku = {
        val copySudoku = sudoku.cloneSudoku
        if (solveHelper(copySudoku)) copySudoku else null
    }

    private def solveHelper(sudoku: Sudoku): Boolean = {
        for (row <- 0 until 9) {
            for (col <- 0 until 9) {
                if (sudoku(row, col) == 0) {
                    for (num <- 1 to 9) {
                        if (isValid(sudoku, row, col, num)) {
                            sudoku(row, col) = num
                            if (solveHelper(sudoku)) {
                                return true
                            } else {
                                sudoku(row, col) = 0
                            }
                        }
                    }
                    return false
                }
            }
        }
        true
    }

    private def isValid(sudoku: Sudoku, row: Int, col: Int, num: Int): Boolean = {
        for (r <- 0 until 9) {
            if (sudoku(r, col) == num) {
                return false
            }
        }

        for (c <- 0 until 9) {
            if (sudoku(row, c) == num) {
                return false
            }
        }

        val startRow = row - row % 3
        val startCol = col - col % 3
        for (r <- 0 until 3) {
            for (c <- 0 until 3) {
                if (sudoku(r + startRow, c + startCol) == num) {
                    return false
                }
            }
        }
        true
    }
}
