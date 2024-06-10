import akka.actor.{Actor, Props, ActorSystem}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class RecursiveSudokuSolver extends solver {
    def solve(sudoku: Sudoku, useActors: Boolean): Sudoku = {
        if (useActors) {
            solveWithActors(sudoku)
        } else {
            val copySudoku = sudoku.cloneSudoku
            if (solveHelper(copySudoku)) copySudoku else null
        }
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

    private def solveWithActors(sudoku: Sudoku): Sudoku = {
        implicit val timeout: Timeout = Timeout(30.seconds)
        val system = ActorSystem("RecursiveSudokuSolverSystem")
        val actorPool = system.actorOf(RoundRobinPool(10).props(ActorRecursiveSolve.props), "recursiveSudokuSolverPool")

        val promise = Promise[(Boolean, Sudoku)]()

        def submitTask(): Unit = {
            val futureResult: Future[(Boolean, Sudoku)] = (actorPool ? sudoku.cloneSudoku).mapTo[(Boolean, Sudoku)]
            futureResult.onComplete {
                case scala.util.Success((true, solvedSudoku)) =>
                    promise.trySuccess((true, solvedSudoku))
                case scala.util.Success((false, _)) =>
                    submitTask() // Retry if not solved
                case scala.util.Failure(_) =>
                    submitTask() // Retry on failure
            }
        }

        // Submit initial batch of tasks
        for (_ <- 1 to 10) {
            submitTask()
        }

        val solvedResult = Await.result(promise.future, timeout.duration)
        system.terminate()

        if (solvedResult._1) {
            solvedResult._2
        } else {
            null
        }
    }
}