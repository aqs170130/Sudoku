import java.util.ArrayList;
import java.util.Stack;

public class SudokuSolver {
	int[][][] possibilities; //a 3-d array of size 9x9x10 (10th for storing number of 1's in other columns) [0,8] in this array corresponds to [1,9] on actual puzzle
	// 9x9 2-d array with an array of size 11 in each slot. First 9 slots represents possible values
	// a 1 or 0 indicates whether this value is possible
	final int[][] originalPuzzle; // a 2-d array of size 9x9 with original sudoku values or a -1 if empty square
	int[][] solution;
	//2-d array follows format originalPuzzle[row][column]
	DiGraph[] constraints;//Holds the bipartite digraph that represent contraints of each square
	AllDifferent[] different; //the AllDifferent object built from constraints
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int[][] puz = {{5,3,-1,-1,7,-1,-1,-1,-1},
				{6,-1,-1,1,9,5,-1,-1,-1},
				{-1,9,8,-1,-1,-1,-1,6,-1},
				{8,-1,-1,-1,6,-1,-1,-1,3},
				{4,-1,-1,8,-1,3,-1,-1,1},
				{7,-1,-1,-1,2,-1,-1,-1,6},
				{-1,6,-1,-1,-1,-1,2,8,-1},
				{-1,-1,-1,4,1,9,-1,-1,5},
				{-1,-1,-1,-1,8,-1,-1,7,9}};
		int[][] puz1 = {
				{-1,-1,-1,-1,-1,-1,2,-1,-1},
				{-1,8,-1,-1,-1,7,-1,9,-1},
				{6,-1,2,-1,-1,-1,5,-1,-1},
				{-1,7,-1,-1,6, -1,-1,-1,-1},
				{-1,-1,-1,9,-1,1,-1,-1,-1},
				{-1,-1,-1,-1,2,-1,-1,4,-1},
				{-1,-1,5,-1,-1,-1,6,-1,3},
				{-1,9,-1,4,-1,-1,-1,7,-1},
				{-1,-1,6,-1,-1,-1,-1,-1,-1}};
		int[][] puz2 = {
				{-1,6,-1,3,-1,-1,8,-1,4},
				{5,3,7,-1,9,-1,-1,-1,-1},
				{-1,4,-1,-1,-1,6,3,-1,7},
				{-1,9,-1,-1,5,1,2,3,8},
				{-1,-1,-1,-1,-1,-1,-1,-1,-1},
				{7,1,3,6,2,-1,-1,4,-1},
				{3,-1,6,4,-1,-1,-1,1,-1},
				{-1,-1,-1,-1,6,-1,5,2,3},
				{1,-1,2,-1,-1,9,-1,8,-1}
		};
		/*		
		int[][] puz = new int[9][9];
		for(int i = 0; i < puz.length; i++) {
			for(int j = 0; j < puz[i].length; j++) {
				puz[i][j] = -1;
			}
		}
		puz[0][0] = 1;
		//puz[0][1] = 2;
		//puz[0][2] = 3;
		puz[1][0] = 4;
		puz[1][1] = 5;
		puz[1][2] = 6;
		puz[2][0] = 7;
		puz[2][1] = 8;
		puz[2][2] = 9;
		*/
		SudokuSolver sud = new SudokuSolver(puz);
		//sud.initialPossibilities();
		//sud.buildAllDifferent(18);
		//DiGraph thegraph = sud.constraints[18];
		//AllDifferent dif = sud.different[10];
		//System.out.println(dif.getDiGraph().printEdges());
		
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {				
				System.out.print(sud.solution[i][j] + " ");
			}
			System.out.println();
		}
		
		/*
		for(int i = 0; i < 2; i++) {
			sud.different[i].getDiGraph().printEdges();
		}
		*/
		
	}
	/**
	 * The constructor creates the array of possibilities, which initially includes all
	 * possible options as possibilities. It calls initialPossibilities method to set up
	 * the array based on input puzzle. The buildAllDifferent function creates the graphs
	 * based on the possibilities array and stores the graphs in an array. Set up AllDifferent
	 * array using the array of graphs and remember to find a max matching for each. Finally,
	 * solve it and process the solution.
	 * @param originalPuzzle a double array of integer of size 9x9 representing sudoku puzzle
	 */
	public SudokuSolver(final int[][] originalPuzzle) {
		if(originalPuzzle.length == 9 && originalPuzzle[0].length == 9) {
			this.originalPuzzle = originalPuzzle;
			possibilities = new int[9][9][10];
			//Intialize array with all 1's (except in last column it would be a 9)
			//This indicates that the array has all possibilities availabe for the square in sudoku
			//puzzle intially.
			for(int i = 0; i < possibilities.length; i++) {
				for(int j = 0; j < possibilities[i].length; j++) {
					for(int k = 0; k < possibilities[i][j].length - 1; k++) {
						possibilities[i][j][k] = 1;
					}
					//The 9 indicates how many 1's are in the array
					possibilities[i][j][possibilities[i][j].length - 1] = 9;
				}
			}
			//Set up possibilities double array based on sudoku (see method)
			initialPossibilities();
			//constraints = new AllDifferent[27];
			constraints = new DiGraph[27];
			
			for(int i = 0; i < 27; i++) {//call buildAlldifferent on all possible values
				buildAllDifferent(i);
			}
			different = new AllDifferent[27];
			for(int i = 0; i < 27; i++) {//build AllDifferent class for all possible values from DiGraph
				different[i] = new AllDifferent(constraints[i], 8);
				different[i].findMaxMatching();//find max matching of alldifferents
			}
			
			solve();
			processSolution();
		}
		else {
			this.originalPuzzle = originalPuzzle;
			System.out.println("Error in Constructor: array must be of size 9x9");
		}
	}
	private void solve() {
		Stack<Integer> stack = new Stack<>();//stack represents the work that still needs to be done
		boolean[] onstack = new boolean[27];//array of booleans to keep track of whether it's on stack -> set to false now

		for(int i = 0; i < 27; i++) {//push 0 through 26 onto stack
			stack.push(i);
			
		}
		for(int i = 0; i < onstack.length; i++) { //all AllDifferents are on the stack
			onstack[i] = true;
		}
		while(!stack.isEmpty()) {//iterate through this while stack is not empty
			//pop from stack
			int i = stack.pop();
			//System.out.println(i);
			//array of booleans to keep track of whether it's on stack -> set to false now
			onstack[i] = false;
			//if needs max matching, call it

			//call prune: returns edges
			int[][] edgestoprune = different[i].prune(); //edgestoprune is a 18 x 18 two dim-array
			//iterate through each valid edge from domain to range
			for(int sourcevertex = different[i].getBipartiteseparator() + 1; sourcevertex < edgestoprune.length; sourcevertex++) { //for all sourcevertices from [bipartiteseparator + 1, end]
				for(int sinkvertex = 0; sinkvertex <= different[i].getBipartiteseparator(); sinkvertex++) { //for all sinkvertices from [0, bipartiteseparator]
					if(edgestoprune[sourcevertex][sinkvertex] == 1) {	//if a vertex needs to be eliminated
						int[] result = calculateSudokuSquare(i, sourcevertex); //calculate exact square in sudoku (row, col) 0<= row(or col) <= 8
						//remove from row AllDifferent, AllDifferent is represented by result[0] or row in sudoku
						if(!different[result[0]].prunenumber(result[1] + 9, sinkvertex)) {//column + 9 brings the domain into [9,17], sinkvertex is [0,8] representing [1,9] in puzzle
							//if prune failed
							int firstparm = result[1] + 9;
							int secparm = sinkvertex;
							System.out.println("Prune fails at i: " + i + " for row AllDifferent removal at square " 
							+ result[0]+ ", " + result[1] + " of " + firstparm + " -> " + secparm + " with source vertex " 
									+ sourcevertex);
							System.out.println("AllDifferent Index " + result[0] + " graph");
							System.out.println(different[result[0]].printGraph());
						}
						/*
						else {
							int firstparm = result[1] + 9;
							int secparm = sinkvertex;
							System.out.println("Prune succeeds at i: " + i + " for row AllDifferent removal at square " 
							+ result[0]+ ", " + result[1] + " of " + firstparm + " -> " + secparm + " with source vertex " 
									+ sourcevertex);							
						}
						*/
						if(!onstack[result[0]] && result[0] != i) {//if this AllDifferent is not stack, push it on
							stack.push(result[0]);
						}
						//remove from column AllDifferent AllDifferent is represented by result[1] + 9 or column in sudoku plus 9
						//AllDifferent index 9-17 are the columns
						if(!different[result[1] + 9].prunenumber(result[0] + 9, sinkvertex)) {//row + 9 brings the domain into [9,17], sinkvertex is [0,8] representing [1,9] in puzzle
							int firstparm = result[0] + 9;
							int secparm = sinkvertex;
							System.out.println("Prune fails at i: " + i + " for col AllDifferent removal at square " 
							+ result[0]+ ", " + result[1] + " of " + firstparm + " -> " + secparm + " with source vertex " 
									+ sourcevertex);
							int index = result[1] + 9;
							System.out.println("AllDifferent Index " + index + " graph");
							System.out.println(different[result[1] + 9].printGraph());
							
						}
						/*
						else {
							int firstparm = result[0] + 9;
							int secparm = sinkvertex;
							System.out.println("Prune succeeds at i: " + i + " for col AllDifferent removal at square " 
							+ result[0]+ ", " + result[1] + " of " + firstparm + " -> " + secparm + " with source vertex " 
									+ sourcevertex);							
						}
						*/
						if(!onstack[result[1] + 9] && result[1] + 9 != i) {//if this AllDifferent is not stack, push it on
							stack.push(result[1] + 9);
						}
						//calculate which box and which square (labeled from 0 to 8) in sudoku
						int boxX = result[0] /3; //integer division calculates box's x-coordinate
						int boxY = result[1] /3;//integer division calculates box's y-coordinate
						int squarex = result[0] % 3; //calculate square's x coordinate and y-coordinate [0,2]
						int squareY = result[1] % 3;
						int boxnum = boxX * 3 + boxY;//calculate boxnum with row and column values (3 boxes per row)
						int squarenum = squarex * 3 + squareY;//calculate squarenum with row and column values (3 boxes per row)
						if(!different[boxnum + 18].prunenumber(squarenum + 9, sinkvertex)) { //squarenum + 9 brings the brings the domain into [9,17], sinkvertex is [0,8] representing [1,9] in puzzle
							int firstparm = squarenum + 9;
							int secparm = sinkvertex;
							int actualindex = boxnum + 18;//Forgot to add 18 for different is cause of bug
							System.out.println("Prune fails at i: " + i + " for box AllDifferent removal at square " 
							+ result[0]+ ", " + result[1] + " of " + firstparm + " -> " + secparm + " with source vertex " 
									+ sourcevertex);
							System.out.println("AllDifferent Index " + actualindex + " graph");
							System.out.println(different[actualindex].printGraph());
						}
						/*
						else {
							int firstparm = squarenum + 9;
							int secparm = sinkvertex;
							System.out.println("Prune succeeds at i: " + i + " for box AllDifferent removal at square " 
							+ result[0]+ ", " + result[1] + " of " + firstparm + " -> " + secparm + " with source vertex " 
									+ sourcevertex);
						}
						*/
						if(!onstack[boxnum] && boxnum + 18 != i) {//if this AllDifferent is not stack, push it on
							stack.push(boxnum);
						}
					}				
				}
			}
				//current edge maps to a square in sudoku
				//neighbors include row, column, and box (remove one of these based on i)
				//call prunenumber and calculate appropriate vertex value (different than current AllDifferent) based on square in sudoku
				//push valid rows, columns, and indices onto stack if they are not there already
		}

	}
	/**
	 * method processes solution from AllDifferent row column
	 */
	private void processSolution() {
		solution = new int[9][9];
		for(int i = 0; i < 9; i++) {//iterate through each row
			final DiGraph g = different[i].getDiGraph();
			for(int j = 0; j <= different[i].getBipartiteseparator(); j++) {//for each node in second half of array
				ArrayList<Integer> valueList = g.adj(j);
				int value = -1;
				if(valueList.size() != 0) {
					//System.out.println("No value here " + i + " " + j);
					value = valueList.get(0);
				}
				solution[i][value - (different[i].getBipartiteseparator() + 1)] = j+1;
				//solution[i][j - (different[i].getBipartiteseparator() + 1)] = value + 1; //input value at (row, col)
				//add 1 to value to get [0,8] to [1,9]
			}
		}
	}
	/**
	 * method calculates precise square based on two parameters
	 * @param allDifferentValue represents which allDifferent index we are analyzing (mapping in comments)
	 * @param sourcevertex represents the sourcevertex of the edge we are analyzing
	 * @return an integer array of size two with format (row, col)
	 */
	private int[] calculateSudokuSquare(int allDifferentValue, int sourcevertex) {
		//(index 0-8 represents rows, 9-17 columns, 18-26 squares)
		//use if, else if statement to map to a row, column, or square
		int[] result = new int[2]; //returns an integer array of size two with coordinates
		if(allDifferentValue >= 0 && allDifferentValue <= 8) {//row
			//value of allDifferentValue represents which row
			result[0] = allDifferentValue;
			//sourcevertex range from [9,17] and represents which column
			result[1] = sourcevertex - 9;
		}
		else if(allDifferentValue >= 9 && allDifferentValue <= 17) {
			//value of allDifferentValue represents which column
			result[1] = allDifferentValue - 9;
			//sourcevertex range from [9,17] and represents which row
			result[0] = sourcevertex - 9;
		}
		else if(allDifferentValue >= 18 && allDifferentValue <= 26) {
			//value of allDifferentValue represents which box
			//calculate which box
			int boxnumber = allDifferentValue - 18;
			//map box from 0 <= x(row) <= 2 and 0 <= y(col) <= 2
			int x = boxnumber / 3;
			int y = boxnumber % 3;
			//sourcevertex represents which exact square in the box from 0 <= x(row) <= 2 and 0 <= y(col) <= 2
			int squarenumber = sourcevertex - 9;//sourcevertex range from [9,17]
			int sourcex = squarenumber / 3;
			int sourcey = squarenumber % 3;
			//calculate actual coordinates
			result[0] = x * 3 + sourcex; //x moves in multiple of 3's: sourcex moves one by one
			result[1] = y * 3 + sourcey; //y moves in multiple of 3's: sourcey moves one by one
		}
		else {
			System.out.println("error in calculateSudokuSquare");
		}
		return result;
	}
	/*
	 * Sets up all the intial possibilities in 3-d array possibilities
	 * For every given value in the sudoku puzzle, the current square can only
	 * take on the given value. No other square on the same row, column, or box
	 * can take this value.
	 * Calls removePossibilitySquare, removePossibilityRow, removePossibilityCol,
	 * and removePossibilityBox to do the separate functionality.
	 */
	private void initialPossibilities() {
		for(int i = 0; i < originalPuzzle.length; i++) {//rows 0-8
			for(int j = 0; j < originalPuzzle[i].length; j++) {//columns 0-8
				if(originalPuzzle[i][j] != -1) {//if there is a value in square
					removePossibilitySquare(i, j, originalPuzzle[i][j] - 1);
					removePossibilityRow(i, j, originalPuzzle[i][j] - 1);
					removePossibilityCol(i, j, originalPuzzle[i][j] - 1);
					removePossibilityBox(i, j, originalPuzzle[i][j] - 1);
				}
			}
		}
	}
	/**
	 * If we know a specific square in a sudoku has a given value, then remove all other possibilities
	 * in this given square from 3-d possibilities.
	 * @param row the row of the square (0-8)
	 * @param col the column of the square (0-8)
	 * @param possibility the value we are analyzing
	 */
	private void removePossibilitySquare(int row, int col, int possibility) {
		for(int k = 0; k < possibilities[0][0].length - 1; k++) {
			if(k != possibility) { //change all values from 0-8 (corresponding to 1-9) to 0 except for possibility (stays 1)
				possibilities[row][col][k] = 0; 
			}
		}
		possibilities[row][col][possibilities[0][0].length - 1] = 1; //only one 1 on this square now
	}
	/**
	 * If we know a specific square in a sudoku has a given value, then remove all other possibilities
	 * in this given row from 3-d possibilities. This method excludes removal of possibilities from the
	 * specific square because this function is already achieved in removePossibilitySquare method
	 * @param row the row of the square (0-8)
	 * @param col the column of the square (0-8)
	 * @param possibility the value we are analyzing
	 */
	private void removePossibilityRow(int row, int col, int possibility) {
		for(int j = 0; j < possibilities[0].length; j++) {//iterate through each column in specified row
			if(j != col) {
				if(possibilities[row][j][possibility] == 1) {//assuming this possibility hasn't already been eliminated from this square
					possibilities[row][j][possibility] = 0; 
					// indicate that this other square on the row can't take on value of possibility
					possibilities[row][j][possibilities[0][0].length - 1]--;//decrement number of 1's
				}
			}
		}
	}
	/**
	 * If we know a specific square in a sudoku has a given value, then remove all other possibilities
	 * in this given column from 3-d possibilities. This method excludes removal of possibilities from the
	 * specific square because this function is already achieved in removePossibilitySquare method
	 * @param row the row of the square (0-8)
	 * @param col the column of the square (0-8)
	 * @param possibility the value we are analyzing
	 */
	private void removePossibilityCol(int row, int col, int possibility) {
		for(int i = 0; i < possibilities.length; i++) {//iterate through each row in specifed column
			if(i != row) {
				if(possibilities[i][col][possibility] == 1) {//assuming this possibility hasn't already been eliminated from this square
					possibilities[i][col][possibility] = 0; 
					// indicate that this other square on the column can't take on value of possibility
					possibilities[i][col][possibilities[0][0].length - 1]--;//decrement number of 1's
				}
			}
		}
	}
	/**
	 * If we know a specific square in a sudoku has a given value, then remove all other possibilities
	 * in this given Box from 3-d possibilities. This method excludes removal of possibilities from the
	 * specific square because this function is already achieved in removePossibilitySquare method.
	 * The sudoku has 9 boxes labeled (x,y) where 0 <= x <= 2 and 0 <= y <= 2.
	 * @param row the row of the square (0-8)
	 * @param col the column of the square (0-8)
	 * @param possibility the value we are analyzing
	 */
	private void removePossibilityBox(int row, int col, int possibility) {
		//Calculate which Box this square is in
		//The sudoku has 9 boxes labeled (x,y) where 0 <= x <= 2 and 0 <= y <= 2
		int x = row/3; //do integer division
		int y = col/3;
		for(int i = 3 * x; i < 3 * x + 3; i++) {//iterate through each square in the box
			for(int j = 3 * y; j < 3 * y + 3; j++) {//i is rows, j is columns 
				if(i != row && j != col) {//if value is different from square
					if(possibilities[i][j][possibility] == 1) {//assuming this possibility hasn't already been eliminated from this square
						possibilities[i][j][possibility] = 0; 
						// indicate that this other square on the column can't take on value of possibility
						possibilities[i][col][possibilities[0][0].length - 1]--;//decrement number of 1's
					}
				}
			}
		}
	}
	//(index 0-8 represents rows, 9-17 columns, 18-26 squares) 
	/**
	 * builds all 27 graphs based on possibilities with 9 rows, 9 columns, and 9 boxes.
	 * Checks a particular row, column, or box to see which numbers are possible for each
	 * square in that particular row, column, or box.
	 * @param index indicates which graph to build
	 */
	private void buildAllDifferent(int index) {
		DiGraph g = new DiGraph(18); 
		if(index >= 0 && index <= 8) {//here index = row number
			for(int i = 0; i < possibilities[0].length; i++) { //i represents each column in the row [0, 9-1]
				for(int j = 0; j < possibilities[0][0].length - 1; j++) { //j represents the possible values the squares can take (see line below) 
					//exclude last entry in possibilities b/c it contains # of 1's
					if(possibilities[index][i][j] == 1) {// j's [0,8] maps to [1,9] (values the square can take on)
						g.addEdge(i + 9, j); 
						//add 9 to domain because domain is always behind range in bipartite graphs in AllDifferent class (see class comments)
						// [0,8] is the range of [9, 17] (the domain)
					}
				}
			}		
		}
		else if(index >= 9 && index <= 17) {//here index - 9 = current column
			for(int i = 0; i < possibilities[0].length; i++) { //i represents each row in the column [0, 9-1]
				for(int j = 0; j < possibilities[0][0].length - 1; j++) { //j represents the possible values the squares can take (see line below)
					//exclude last entry in possibilities b/c it contains # of 1's
					if(possibilities[i][index-9][j] == 1) {// j's [0,8] maps to [1,9] (values the square can take on)
						g.addEdge(i + 9, j); 
						//add 9 to domain because domain is always behind range in bipartite graphs in AllDifferent class (see class comments)
						// [0,8] is the range of [9, 17] (the domain)
					}
				}
			}
		}
		else if(index >= 18 && index <= 26) {//here index - 18 = box number
			int boxnumber = index - 18;
			//The sudoku has 9 boxes labeled (x,y) where 0 <= x <= 2 and 0 <= y <= 2
			//x is rows, y is columns 
			int x = boxnumber / 3;
			int y = boxnumber % 3;
			for(int i = 3 * x; i < 3 * x + 3; i++) {//iterate through each square in the box
				for(int j = 3 * y; j < 3 * y + 3; j++) {//i is rows, j is columns 
					for(int k = 0; k < possibilities[0][0].length - 1; k++) { //k represents the possible values the squares can take (see line below)
						if(possibilities[i][j][k] == 1) {// k's [0,8] maps to [1,9] (values the square can take on) (remember to take out last square(# of 1's)
							//calculate domain according to scheme described below
							int domain = (i % 3) * 3 + (j % 3); //first term takes on values of 0,3,6. second term takes on values of 0,1,2
							g.addEdge(domain + 9, k);
							//Domain of box is labeled with this scheme: 0 1 2
							//      										3 4 5 
							//											6 7 8 
							//add 9 to domain because domain is always behind range in bipartite graphs in AllDifferent class (see class comments)
							// [0,8] is the range of [9, 17] (the domain)
						}
					}
				}
			}
		}
		//AllDifferent alldifferent = new AllDifferent(g, 8); //after graph is created, intialize the AllDifferent class object
		//constraints[index] = alldifferent; //add constraints array with AllDifferent object
		constraints[index] = g;
	}

}
