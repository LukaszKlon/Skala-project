object SudokuApp {
    def main(args: Array[String]): Unit = {
        val sudokuLoader = new SudokuLoader()
        val sudokuSolver = new SudokuSolver()
        val recursiveSudoku = new RecursiveSudokuSolver()
        val startSudoku = sudokuLoader.sudokuLoad("C:\\Users\\mateu\\Desktop\\stuff\\programowanie\\scala-projekt\\sudoku\\src\\main\\scala\\text")
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
