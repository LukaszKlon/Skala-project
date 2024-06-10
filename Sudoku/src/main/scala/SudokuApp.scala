object SudokuApp {
    def main(args: Array[String]): Unit = {
        val sudokuLoader = new SudokuLoader()
        val sudokuSolver = new SudokuSolver()
        val recursiveSudoku = new RecursiveSudokuSolver()
        val startSudoku = sudokuLoader.sudokuLoad("C:\\Users\\klonl\\OneDrive\\Pulpit\\STUDIA\\semestr4\\Scala\\Skala-project\\Sudoku\\src\\main\\scala\\sudoku\\s1.txt")
        val solvedSudoku = sudokuSolver.solve(startSudoku, useActors = true)
        val recursiveSolvedSudoku = recursiveSudoku.solve(startSudoku, useActors = true)

        println("Loaded Sudoku")
        println(startSudoku)
        println("Solved Sudoku")
        println(solvedSudoku)
        println("Recursive Solved Sudoku")
        println(recursiveSolvedSudoku)
    }
}
