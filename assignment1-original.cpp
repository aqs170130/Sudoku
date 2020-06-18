/*
 * assignment1.cpp
 *
 * <add comments to describe the purpose of this application>
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

int main()
{
	wordGame game;//declare a game
	string puzzlefile;
	cin >> puzzlefile;// input the puzzle file
	bool worked = readPuzzle(game, puzzlefile);//read the puzzle with puzzlefile name
	if(worked){// if the read worked
		cout << "The puzzle from file \"" << puzzlefile << "\"" << endl;
		displayPuzzle(game);//display the puzzle
		string wordfile;
		cin >> wordfile;//input a word file
		ifstream inputFile;// create an ifstream object
		inputFile.open(wordfile);//open the ifstream object wordfile name
		if(inputFile){//file read worked
			string currentword;
			while(inputFile >> currentword){//read in each word in the file
				wordFind theFind;// create a wordFind struct
				theFind.word = currentword;//set word attribute to currentword
				findWord(game,theFind);//call findWord function on game and theFind structs
				cout << "The word " << currentword;
				if(theFind.found){//if the word was found
					cout << " was found at (" << theFind.row << ", " << theFind.column << ") - ";
					//output the direction based on theFind.where
					if(theFind.where == 0){cout << "right";}
					else if(theFind.where == 1){cout << "left";}
					else if(theFind.where == 2){cout << "down";}
					else if(theFind.where == 3){cout << "up";}
					else if(theFind.where == 4){cout << "right/down";}
					else if(theFind.where == 5){cout << "right/up";}
					else if(theFind.where == 6){cout << "left/down";}
					else if(theFind.where == 7){cout << "left/up";}
					cout << endl;
				}
				else{//if the word was not found
					cout << " was not found" << endl;
				}
			}
		}
		else{//if input file is invalid
			cout << "The puzzle file \"" << wordfile << "\" could not be opened or is invalid" << endl;
		}
	}
	else{//if the intiial read failed
		cout << "The puzzle file \"" << puzzlefile << "\" could not be opened or is invalid" << endl;
	}
	/*
	wordFind theFind;
	theFind.word = "MERCURY";
	findWord(game,theFind);
	cout << theFind.found << endl;
	cout << theFind.row << endl;
	cout << theFind.column << endl;
	cout << theFind.where << endl;
	displayPuzzle(game);
	*/
	return 0;
}
void findWord(wordGame &game, wordFind &theFind){
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
bool readPuzzle(wordGame &game, string inputFileName){
	ifstream inputFile; //open an input stream for input File
	inputFile.open(inputFileName); // open the inputFile
	game.version = 1; // set game version to 1
	if(inputFile){//if inputFile is valid
		int rows, cols;
		inputFile >> rows >> cols;//read in the rows and cols
		game.numberRows = rows;// set numberRows in game
		game.numberColumns = cols;// set numberColumns in game
		char newline;
		inputFile.get(newline);// read the new line in

		char current;
		for (int i = 0; i < game.numberRows;i++){
			for(int j = 0; j < game.numberColumns; j++){
				inputFile.get(current);//read the current character
				game.puzzle[i][j] = current;// add it to game puzzle
			}
			inputFile.get(current);// read the new line in
		}
		inputFile.close(); // close the input file
		return true;

	}
	else{
		return false;
	}
}
void displayPuzzle(wordGame &game){
	for (int i = 0; i < game.numberRows;i++){//loop through all the rows
		for(int j = 0; j < game.numberColumns; j++){//loop through all the columns
			cout << game.puzzle[i][j]; // print out each character
		}
		cout << endl;
	}
}




