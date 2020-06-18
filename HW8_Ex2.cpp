// Author: Andy Shao
// Course: CS1337.0U1
// Date: 7/13/2018
// Assignment: Lecture Homework #7 Exercise #2
// Compiler: Eclipse Oxygen
// Description:
// This program works with linked
// list classes

#include <iostream>
#include <iomanip>
#include <fstream>
#include "LinkedList.h"
using namespace std;



/**
 * read numbers from an input file.
 * print the list forward,
 * sort it, and print the list again
 */
int main(){
	LinkedList list;
	ifstream inputFile;
	inputFile.open("Perm50.txt");
	int read;
	while(inputFile >> read)
	{
		list.addNodeToEnd(read);
	}//end while(inputFile >> read)
	list.printListForward();
	list.sort();
	list.printListForward();
	list.printListBackward();
	return 0;
}//end main
