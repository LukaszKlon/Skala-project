

object SudokuApp {

    def single_test(Solver: solver, actors: Boolean,sudoku: Sudoku,i: Int): Unit = {
        val startTime = System.nanoTime()
        val solvedSudoku = Solver.solve(sudoku, useActors = actors)
        val finishTime = System.nanoTime()
        println(s"Test s$i Pass Time: ${(finishTime - startTime) / 1e9} seconds")
    }

    def test(recursive: Boolean, useActors: Boolean): Unit = {
        val sudokuLoader = new SudokuLoader()
        var Solver: solver = new SudokuSolver
        if (recursive) {
            Solver = new RecursiveSudokuSolver
        }
        val startTime = System.nanoTime()
        for (i <- 1 to 50) {
            val sudoku = sudokuLoader.sudokuLoad(s"./src/main/scala/sudoku/s$i.txt")
            single_test(Solver,useActors,sudoku, i)
        }
        val finishTime = System.nanoTime()
        Thread.sleep(2000)
        println(s"Test Passed Time: ${(finishTime - startTime) / 1e9} seconds")
    }

    def main(args: Array[String]): Unit = {

        test(false,false)
//        val sudokuLoader = new SudokuLoader
//        val sudokuSolver = new SudokuSolver
//        val recursiveSudoku = new RecursiveSudokuSolver
//        val startSudoku = sudokuLoader.sudokuLoad("C:\\Users\\klonl\\OneDrive\\Pulpit\\STUDIA\\semestr4\\Scala\\Skala-project\\Sudoku\\src\\main\\scala\\sudoku\\s1.txt")
//        val solvedSudoku = sudokuSolver.solve(startSudoku, useActors = true)
//        val recursiveSolvedSudoku = recursiveSudoku.solve(startSudoku, useActors = true)
//
//        println("Loaded Sudoku")
//        println(startSudoku)
//        println("Solved Sudoku")
//        println(solvedSudoku)
//        println("Recursive Solved Sudoku")
//        println(recursiveSolvedSudoku)
    }
}
