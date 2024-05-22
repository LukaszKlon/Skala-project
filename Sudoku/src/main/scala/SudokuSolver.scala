import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


class SudokuSolver {
    private val sudokuLoader = new SudokuLoader()
    private val simulatedAnneling = new SimulatedAnneling(1.2,0.999,0.01)

    def solve(filename:String,useActors:Boolean): (Array[Array[Int]],Array[Array[Int]]) = {
        var solved = false
        val startSudoku = sudokuLoader.sudokuLoad(filename)
        while (!solved){
            if (!useActors){
                simulatedAnneling.setSudoku(startSudoku)
                for (i <- 1 to 10) {
                    solved = simulatedAnneling.simulatedAnneling()
                    if (solved) return (startSudoku, simulatedAnneling.sudokuMatrix)

                }
            }else{
//                implicit val timeout: Timeout = Timeout(5.seconds)
//                val system = ActorSystem("SudokuSolverSystem")
//                val actor = system.actorOf(ActorSolve.props, "sudokuSolver")
//
//                val futureResult: Future[(Boolean, Array[Array[Int]])] = (actor ? startSudoku).mapTo[(Boolean, Array[Array[Int]])]
//                val (solved, solvedSudoku) = Await.result(futureResult, timeout.duration)
//
//                system.terminate()
//                if (solved) return (startSudoku, solvedSudoku)

                implicit val timeout: Timeout = Timeout(30.seconds)
                val system = ActorSystem("SudokuSolverSystem")
                val actorPool = system.actorOf(RoundRobinPool(10).props(ActorSolve.props), "sudokuSolverPool")

                val promise = Promise[(Boolean, Array[Array[Int]])]()

                def submitTask(): Unit = {
                    val futureResult: Future[(Boolean, Array[Array[Int]])] = (actorPool ? startSudoku).mapTo[(Boolean, Array[Array[Int]])]
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
                    return (startSudoku, solvedResult._2)
                }
            }
        }
        return (startSudoku,simulatedAnneling.sudokuMatrix)
    }
}
