import akka.actor.{Actor, Props}

class ActorSolve extends Actor {
    private val simulatedAnneling = new SimulatedAnneling(1.2, 0.999, 0.01)

    def receive: Receive = {
        case startSudoku: Sudoku =>
            val (solved, sudokuMatrix) = solve(startSudoku)
            sender() ! (solved, sudokuMatrix)
        case _ => println("Unknown message")
    }

    def solve(startSudoku: Sudoku): (Boolean, Sudoku) = {
        var solved = false
        simulatedAnneling.setSudoku(startSudoku)
        for (i <- 1 to 10) {
            solved = simulatedAnneling.simulatedAnneling()
            if (solved) {
                return (true, simulatedAnneling.sudokuMatrix)
            }
        }
        (false, simulatedAnneling.sudokuMatrix)
    }
}

object ActorSolve {
    def props: Props = Props[ActorSolve]()
}
