#!/usr/bin/env python
#coding:utf-8

"""
Each sudoku board is represented as a dictionary with string keys and
int values.
e.g. my_board['A1'] = 8
"""
import sys

ROW = "ABCDEFGHI"
COL = "123456789"


def print_board(board):
    """Helper function to print board in a square."""
    print("-----------------")
    for i in ROW:
        row = ''
        for j in COL:
            row += (str(board[i + j]) + " ")
        print(row)


def board_to_string(board):
    """Helper function to convert board dictionary to string for writing."""
    ordered_vals = []
    for r in ROW:
        for c in COL:
            ordered_vals.append(str(board[r + c]))
    return ''.join(ordered_vals)


def forward_check(board, row, col):
    """ checks rows, columns, and squares and discards invalid domain values """

    candidates = set(map(str, range(1,10))) # initial Sudoku possible domains 
    
    for i in ROW: 
        candidates.discard(str(board[i + col])) # discard invalid values

    for j in COL:
        candidates.discard(str(board[row + j]))

    startRow, startCol = ROW.index(row) // 3 * 3, COL.index(col) // 3 * 3
    for i in range(3):
        for j in range(3):
            candidates.discard(str(board[ROW[startRow + i] + COL[startCol + j]]))
    
    return candidates


def mrv_cell(board):
    """ finds the mrv to fill """
    mrv = None
    min_candidates = 10
    
    for r in ROW:
        for c in COL:
            if board[r + c] == 0: # checks empty cell
                candidates = forward_check(board, r, c) # if current cell is empty, call forward_check function
                if len(candidates) < min_candidates: # if this is less than current mrv, set mrv to this new value
                    min_candidates = len(candidates)
                    mrv = r, c

    return mrv


def backtracking(board):
    """Takes a board and returns solved board."""
    # TODO: implement this

    empty_cell = mrv_cell(board) # starts by finding empty cell
    if not empty_cell:
        return board
    
    row, col = empty_cell # coordinates of empty cell

    for value in forward_check(board, row, col):
        board[row + col] = int(value)
        if backtracking(board) is not None:
            return board
        board[row + col] = 0 # backtracking step
    
    return None


if __name__ == '__main__':
    if len(sys.argv) > 1:

        # Running sudoku solver with one board $python3 sudoku.py <input_string>.
        print(sys.argv[1])
        # Parse boards to dict representation, scanning board L to R, Up to Down
        board = { ROW[r] + COL[c]: int(sys.argv[1][9*r+c]) for r in range(9) for c in range(9)}
    
        solved_board = backtracking(board)
    
        # Write board to file
        out_filename = 'output.txt'
        outfile = open(out_filename, "w")
        outfile.write(board_to_string(solved_board))
        outfile.write('\n')
    
    else:
        # Running sudoku solver for boards in sudokus_start.txt $python3 sudoku.py
        # Read boards from source.
        src_filename = 'sudokus_start.txt'
        try:
            srcfile = open(src_filename, "r")
            sudoku_list = srcfile.read()
        except:
            print("Error reading the sudoku file %s" % src_filename)
            exit()
        # Setup output file
        out_filename = 'output.txt'
        outfile = open(out_filename, "w")
        
        # Solve each board using backtracking
        for line in sudoku_list.split("\n"):
    
            if len(line) < 9:
                continue
    
            # Parse boards to dict representation, scanning board L to R, Up to Down
            board = { ROW[r] + COL[c]: int(line[9*r+c]) for r in range(9) for c in range(9)}
    
            # Print starting board. TODO: Comment this out when timing runs.
            # print_board(board)
    
            # Solve with backtracking
            solved_board = backtracking(board)
    
            # Print solved board. TODO: Comment this out when timing runs.
            # print_board(solved_board)
    
            # Write board to file
            outfile.write(board_to_string(solved_board))
            outfile.write('\n')
    
        print("Finishing all boards in file.")