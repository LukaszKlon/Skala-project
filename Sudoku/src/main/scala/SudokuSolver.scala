import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class SudokuSolver extends solver {
    private val simulatedAnneling = new SimulatedAnneling(1.2, 0.999, 0.01)

    def solve(startSudoku: Sudoku, useActors: Boolean): Sudoku = {
        if (useActors) {
            solveWithActors(startSudoku)
        } else {
            solveWithoutActors(startSudoku)
        }
    }

    private def solveWithActors(startSudoku: Sudoku): Sudoku = {
        implicit val timeout: Timeout = Timeout(30.seconds)
        val system = ActorSystem("SudokuSolverSystem")
        val actorPool = system.actorOf(RoundRobinPool(10).props(ActorSolve.props), "sudokuSolverPool")

        val promise = Promise[(Boolean, Sudoku)]()

        def submitTask(): Unit = {
            val futureResult: Future[(Boolean, Sudoku)] = (actorPool ? startSudoku).mapTo[(Boolean, Sudoku)]
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
            return solvedResult._2
        }
        simulatedAnneling.sudokuMatrix
    }

    private def solveWithoutActors(startSudoku: Sudoku): Sudoku = {
        var solved = false
        while (!solved) {
            simulatedAnneling.setSudoku(startSudoku)
            for (i <- 1 to 10) {
                solved = simulatedAnneling.simulatedAnneling()
                if (solved) return simulatedAnneling.sudokuMatrix
            }
        }
        simulatedAnneling.sudokuMatrix
    }
}