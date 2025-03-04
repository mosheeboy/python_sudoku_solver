import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// SudokuSolver class to read the input file, solve each puzzle and display the solutions
public class sudokuSolver {
    public static void main(String[] args) {
        try {
            SudokuPuzzle[] puzzles = readPuzzlesFromFile("p096_sudoku.txt");
            int totalSum = 0; // Variable to keep track of the total sum
    
            for (int i = 0; i < puzzles.length; i++) {
                System.out.println("Grid " + (i + 1));
                puzzles[i].solve();
                int sum = puzzles[i].getTopLeftSum();
                totalSum += sum; // Add the sum of the current puzzle to the total sum
                System.out.println("Sum of 3 digits in top-left corner: " + sum);
            }
    
            // Print the total sum
            System.out.println("Total sum of the 3 digits in top-left corner of all puzzles: " + totalSum);
    
        } catch (IOException e) {
            System.out.println("Error reading the input file.");
            e.printStackTrace();
        }
    }
    

    // Method to read the input file and return a list of puzzles
    public static SudokuPuzzle[] readPuzzlesFromFile(String filename) throws IOException {

        // Use Java Scanner class to read the input file line by line
        Scanner scanner = new Scanner(new File(filename));

        // Create a list of puzzles to return and SudokuPuzzle object to store the current puzzle
        List<SudokuPuzzle> puzzles = new ArrayList<>();
        SudokuPuzzle currentPuzzle = null;

        // Loop through the 50 puzzles in the input file
        for(int i = 0; i < 50; i++){
            // Skip the first line of each puzzle (Grid XX)
            scanner.nextLine();
            // Create a new SudokuPuzzle object and initialize grid to store the current puzzle
            currentPuzzle = new SudokuPuzzle();
            currentPuzzle.grid = new int[9][9];

            // Loop through the 9 lines of each puzzle representing each sub-grid
            for(int j = 0; j < 9; j++){
                // Read each line of the sub-grid and store into character array
                char [] line = scanner.nextLine().toCharArray();
                // Loop through the 9 characters in the line and store into the grid
                for(int k = 0; k < 9; k++){
                    currentPuzzle.grid[j][k] = Character.getNumericValue(line[k]);
                }
            }
            // Add the current puzzle to the list of puzzles
            puzzles.add(currentPuzzle);
        }
        // Return the list of puzzles as an array
        return puzzles.toArray(new SudokuPuzzle[puzzles.size()]);
    }
}

class SudokuPuzzle {
    public int[][] grid;

    public SudokuPuzzle() {
    } 

    public void solve() {
        System.out.println("Unsolved");
        displayGrid();
        List<int[]> blankCells = new ArrayList<>();
        getBlankCells(grid, blankCells);
        if(solveSudoku(grid, blankCells, 0)){
            System.out.println("Solved");
            displayGrid();
            getTopLeftSum();
        } else {
            System.out.println("Not solved");
        }
    }

    private boolean solveSudoku(int[][] board, List<int[]> emptyCells, int index) {
        if (index == emptyCells.size()) {
            return true; // Puzzle is solved
        }

        int[] cell = emptyCells.get(index);
        int row = cell[0];
        int col = cell[1];

        for (int num = 1; num <= 9; num++) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;

                if (solveSudoku(board, emptyCells, index + 1)) {
                    return true; // Move to the next empty cell
                }

                board[row][col] = 0; // Backtrack
            }
        }

        return false; // No valid value found, need to backtrack
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        // Check row and column
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false; // Number already exists in row or column
            }
        }

        // Check subgrid (3x3)
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return false; // Number already exists in subgrid
                }
            }
        }

        return true; // Number can be placed in this cell
    }   

    private void getBlankCells(int[][] board, List<int[]> blankCells) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    blankCells.add(new int[]{i, j});
                }
            }
        }
    }

    public int getTopLeftSum() {
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += grid[0][i];
        }
        return sum;
    }


    public void displayGrid() {
        for (int i = 0; i < 9; i++) {

        // Print horizontal border for the entire grid at the top and after every 3 rows
            if (i == 0 || i % 3 == 0) {
                System.out.println("+-------+-------+-------+");
            }

        for (int j = 0; j < 9; j++) {
            // Print vertical border for the entire grid at the start and after every 3 columns
            if (j == 0 || j % 3 == 0) {
                System.out.print("| ");
            }
            System.out.print(grid[i][j] + " ");
            if (j == 8) {
                System.out.print("|");  // Print vertical border for the entire grid at the end
            }
        }
        System.out.println();
        }
        System.out.println("+-------+-------+-------+");  // Print the horizontal border at the bottom
    }   



}
