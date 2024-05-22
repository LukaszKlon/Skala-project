import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class SudokuFiller {

    def fillSudoku(sudokuMatrix: Array[Array[Int]]): Array[Array[(Int,Int)]] = {
        val result = ListBuffer[Array[(Int,Int)]]()
        for (row <- 0 to 2) {
            for (col <- 0 to 2) {
                val numberInSquare = Array.fill(9)(true)
                val emptyCells = ListBuffer[(Int, Int)]()

                for (index <- 0 to 8) {
                    val firstIndex = 3 * row + index / 3
                    val secondIndex = 3 * col + index % 3
                    if (sudokuMatrix(firstIndex)(secondIndex) == 0){
                        emptyCells.append((firstIndex,secondIndex))
                    }
                    else{
                        val existingValue = sudokuMatrix(firstIndex)(secondIndex) - 1
                        numberInSquare(existingValue) = false
                    }
                }

                for ((firstIndex,secondIndex) <- emptyCells) {
                    var index = 0
                    while (!numberInSquare(index)) {
                        index += 1
                    }
                    numberInSquare(index) = false
                    sudokuMatrix(firstIndex)(secondIndex) =  index + 1
                }

                if (emptyCells.length >= 2) result.append(emptyCells.toArray)
            }
        }
        result.toArray
    }
}
