import akka.actor.{Actor, Props}

class ActorRecursiveSolve extends Actor{
    def receive: Receive = {
        case sudoku: Sudoku =>
            val solver = new RecursiveSudokuSolver
            val solved = solver.solve(sudoku, useActors = false) // Use non-actor-based solving within the actor
            sender() ! (solved != null, solved)
        case _ => println("Unknown message")
    }
}

object ActorRecursiveSolve {
    def props: Props = Props[ActorRecursiveSolve]()
}