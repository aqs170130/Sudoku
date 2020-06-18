/*
 * assignment1.cpp
 * This program solves a crossword puzzle
 * author: Andy Shao
 *
 */


#include <iostream>
#include <string>
#include <fstream>
#include <cstring>
#include <algorithm>
#include <cctype>

using namespace std;

enum direction {LEFT_RIGHT,
RIGHT_LEFT,
DOWN,
UP,
RIGHT_DOWN,
RIGHT_UP,
LEFT_DOWN,
LEFT_UP};

const int MAX = 50;

struct wordGame
{
	int version;
	int numberRows;
	int numberColumns;
	char puzzle[MAX][MAX];
};

struct wordFind
{
	string word;
	bool found;
	int row;
	int column;
	direction where;
};

// function prototypes go here
void findWord(wordGame &game, wordFind &theFind);
bool readPuzzle(wordGame &game, string inputFileName);
void displayPuzzle(wordGame &game);
void displayFindWord(wordFind &theFind);


// this is the main function. It calls all the other
// functions.
int main()
{
	wordGame game;//declare a game
	string puzzlefile = "puzzle2.txt";
	cin >> puzzlefile;// input the puzzle file
	bool worked = readPuzzle(game, puzzlefile);//read the puzzle with puzzlefile name
	//if the initial read failed
	if (worked == false) {
		cout << "The puzzle file \"" << puzzlefile << "\" could not be opened or is invalid" << endl;
		return -1;
	}

	// if the read worked
	cout << "The puzzle from file \"" << puzzlefile << "\"" << endl;
	displayPuzzle(game); //display the puzzle

	string wordfile = "words2.txt";
	cin >> wordfile;//input a word file
	ifstream inputFile; // create an ifstream object
	inputFile.open(wordfile); //open the ifstream object wordfile name
	// Failed to read input file.
	if (!inputFile) {
		cout << "The puzzle file \"" << wordfile << "\" could not be opened or is invalid" << endl;
		return -1;
	}

	//file read worked
	string currentword;
	while (inputFile >> currentword) { //read in each word in the file
		wordFind theFind; // create a wordFind struct
		transform(currentword.begin(), currentword.end(), currentword.begin(), ::toupper); // Bonus: fold to upper case
		theFind.word = currentword; //set word attribute to current word
		findWord(game, theFind); //call findWord function on game and theFind struct
		displayFindWord(theFind); // display find info about the word
	}

	return 0;
}

// Find if word is in the givn line
void findWord(wordGame &game, wordFind &theFind){
	//go line by line do a right to left search
	for(int i = 0; i < game.numberRows; i++){
		string linesearch = "";
		for(int j = game.numberColumns-1; j >=0;j--){
			linesearch += game.puzzle[i][j];
		}
		int indexcol = linesearch.find(theFind.word);//find the word
		if(indexcol != string::npos){//if the word was found
			theFind.column = game.numberColumns- 1 -indexcol;//set the col
			theFind.row = i;// set the row
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = RIGHT_LEFT;//set the direction
			theFind.found = true;//set found = true
			return;
		}

	}

	//go line by line do a left to right search
	for(int i = 0; i < game.numberRows; i++){
		string linesearch = ""; //all the characters in the line from left to right
		for(int j = 0; j < game.numberColumns;j++){
			linesearch += game.puzzle[i][j];
		}
		int indexcol = linesearch.find(theFind.word);//find the word
		if(indexcol != string::npos){//if the word was found
			theFind.column = indexcol;//set the col
			theFind.row = i;// set the row
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = LEFT_RIGHT;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}

	//go column by column and do a DOWN search
	for(int j = 0; j < game.numberColumns;j++){
		string linesearch = "";
		for(int i = 0; i < game.numberRows; i++){
			linesearch += game.puzzle[i][j];
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = j;//set the col
			theFind.row = index;// set the row
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = DOWN;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	//go column by column and do a DOWN search
	for(int j = 0; j < game.numberColumns;j++){
		string linesearch = "";
		for(int i = game.numberRows - 1; i >=0 ; i--){
			linesearch += game.puzzle[i][j];
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = j;//set the col
			theFind.row = game.numberRows - 1 - index;// set the row
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = UP;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	//search the diagonal RIGHT_DOWN in the next two for loops
	for(int i = 0; i < game.numberRows; i++){
		int col = 0;// start searching from column 0 and row i
		int row = i;
		string linesearch = "";
		while(row < game.numberRows && col < game.numberColumns){//while we are still in the puzzle
			linesearch += game.puzzle[row][col];// add each character in diagonal to linesearch
			row++;//increment row and column
			col++;
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = index + 0;//set the col equal to starting col=0 + index
			theFind.row = i + index;// set the row equal to starting row=i + index
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = RIGHT_DOWN;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	for(int j = 1; j < game.numberRows; j++){
		int col = j;// start searching from column j and row 0
		int row = 0;
		string linesearch = "";
		while(row < game.numberRows && col < game.numberColumns){//while we are still in the puzzle
			linesearch += game.puzzle[row][col];// add each character in diagonal to linesearch
			row++;//increment row and column
			col++;
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = index + j;//set the col equal to starting col=j + index
			theFind.row = 0 + index;// set the row equal to starting row=0 + index
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = RIGHT_DOWN;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	//search the diagonal LEFT_UP in the next two for loops
	for(int i = 0; i < game.numberRows; i++){
		int col = game.numberColumns-1;// start searching from column game.numberColumns-1 and row i
		int row = i;
		string linesearch = "";
		while(row >= 0 && col >= 0){//while we are still in the puzzle
			linesearch += game.puzzle[row][col];// add each character in diagonal to linesearch
			row--;//increment row and column
			col--;
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = game.numberColumns - 1 -index;//set the col equal to starting col - index
			theFind.row = i - index;// set the row equal to starting row=i + index
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = LEFT_UP;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	for(int j = 0; j < game.numberColumns-1; j++){
		int col = j;// start searching from column game.numberColumns-1 and row i
		int row = game.numberRows-1;
		string linesearch = "";
		while(row >= 0 && col >= 0){//while we are still in the puzzle
			linesearch += game.puzzle[row][col];// add each character in diagonal to linesearch
			row--;//increment row and column
			col--;
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = j -index;//set the col equal to starting col - index
			theFind.row = game.numberRows - 1 - index;// set the row equal to starting row - index
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = LEFT_UP;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	// search the diagonal LEFT_DOWN
	for(int j = 0; j < game.numberColumns; j++){
		int col = j;// start searching from column game.numberColumns-1 and row i
		int row = 0;
		string linesearch = "";
		while(row < game.numberRows && col >= 0){//while we are still in the puzzle
			linesearch += game.puzzle[row][col];// add each character in diagonal to linesearch
			row++;//increment row and column
			col--;
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = j - index;//set the col equal to starting col - index
			theFind.row = 0 + index;// set the row equal to starting row=i + index
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = LEFT_DOWN;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	for(int i = 1; i < game.numberRows; i++){
		int col = game.numberColumns-1;// start searching from column game.numberColumns-1 and row i
		int row = i;
		string linesearch = "";
		while(row < game.numberRows && col >= 0){//while we are still in the puzzle
			linesearch += game.puzzle[row][col];// add each character in diagonal to linesearch
			row++;//increment row and column
			col--;
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = game.numberColumns - 1 - index;//set the col equal to starting col - index
			theFind.row = i + index;// set the row equal to starting row=i + index
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = LEFT_DOWN;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	// search the diagonal RIGHT_UP
	for(int i = 0; i < game.numberRows; i++){
		int col = 0;// start searching from column game.numberColumns-1 and row i
		int row = i;
		string linesearch = "";
		while(row >=0 && col < game.numberColumns){//while we are still in the puzzle
			linesearch += game.puzzle[row][col];// add each character in diagonal to linesearch
			row--;//increment row and column
			col++;
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = index;//set the col equal to starting col + index
			theFind.row = i - index;// set the row equal to starting row - index
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = RIGHT_UP;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	for(int j = 1; j < game.numberColumns; j++){
		int col = j;// start searching from column game.numberColumns-1 and row i
		int row = game.numberRows-1;
		string linesearch = "";
		while(row >=0 && col < game.numberColumns){//while we are still in the puzzle
			linesearch += game.puzzle[row][col];// add each character in diagonal to linesearch
			row--;//increment row and column
			col++;
		}
		int index = linesearch.find(theFind.word);//find the word
		if(index != string::npos){//if the word was found
			theFind.column = j + index;//set the col equal to starting col + index
			theFind.row = game.numberRows - 1 - index;// set the row equal to starting row - index
			theFind.column++; // update column to a different system of numbering
			theFind.row++; // update row to a different system of numbering
			theFind.where = RIGHT_UP;//set the direction
			theFind.found = true;//set found = true
			return;
		}
	}
	theFind.found = false;
}



// Read the puzzle with puzzlefile name
bool readPuzzle(wordGame &game, string inputFileName){
	ifstream inputFile; //open an input stream for input File
	inputFile.open(inputFileName); // open the inputFile
	game.version = 2; // set game version to 2
	if(inputFile){//if inputFile is valid
		int rows, cols;
		inputFile >> rows >> cols;//read in the rows and cols
		// Check if row and column are in range 1-50(Bonus)
		if (rows < 1 || rows > MAX || cols < 1 || cols > MAX) {
			// Out of range: close file and return false
			inputFile.close(); // close the input file
			return false;
		}

		game.numberRows = rows;// set numberRows in game
		game.numberColumns = cols;// set numberColumns in game

		char current;
		int rowRead;
		for (int i = 0; i < game.numberRows;i++) {
			// read the new line in
			if (inputFile.get(current).eof()) {
				// If it is end of file, not enough line read.
				inputFile.close(); // close the input file
				return false;
			}
			for(int j = 0; j < game.numberColumns; j++) {
				if (inputFile.get(current).eof()) {
					// If it is end of file, not enough line read.
					inputFile.close(); // close the input file
					return false;
				}
				if (current == '\r' || current == '\n') {
					// Skip new line
					j--;
					continue;
				}
				rowRead = i;
				game.puzzle[i][j] = toupper(current);// add it to game puzzle; Bonus: fold to upper
			}
		}
		inputFile.close(); // close the input file
		return true;
	}
	else{
		return false;
	}
}

// Display the puzzle
void displayPuzzle(wordGame &game){
	for (int i = 0; i < game.numberRows;i++){//loop through all the rows
		for(int j = 0; j < game.numberColumns; j++){//loop through all the columns
			cout << game.puzzle[i][j]; // print out each character
		}
		cout << endl;
	}
}

// Display the word
void displayFindWord(wordFind &theFind) {
	cout << "The word " << theFind.word;
	if(theFind.found) { //if the word was found
		cout << " was found at (" << theFind.row << ", " << theFind.column << ") - ";
		//output the direction based on theFind.where
		switch (theFind.where) {
		case LEFT_RIGHT:
			cout << "right";
			break;
		case RIGHT_LEFT:
			cout << "left";
			break;
		case DOWN:
			cout << "down";
			break;
		case UP:
			cout << "up";
			break;
		case RIGHT_DOWN:
			cout << "right/down";
			break;
		case RIGHT_UP:
			cout << "right/up";
			break;
		case LEFT_DOWN:
			cout << "left/down";
			break;
		case LEFT_UP:
			cout << "left/up";
			break;
		default:
			cout << "invalid";
		}
		cout << endl;
	}
	else{//if the word was not found
		cout << " was not found" << endl;
	}
}




