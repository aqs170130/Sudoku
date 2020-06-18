
// Author: Andy Shao
// Course: CS1337.0U1
// Date: 5/22/2018
// Assignment: Lecture Homework #1 Exercise #1
// Compiler: Eclipse Oxygen
// Description:
// This program prints statistics
// about random numbers to the console.

#include <iostream>
#include <fstream>
#include <iomanip>
using namespace std;
int main()
{
	int totalnumberofnumbers=0; // total number of numbers
	int total = 0; // total sum of all the numbers
	double average; //average of the numbers
	int numbereven = 0; // number of even numbers
	int numberodd = 0; //number of odd numbers
	int numberbetween = 0; //number of numbers strictly greater than 384 but less than or equal to 605
	int max; //maximum number in the file
	int min; //minimum number in the file
	int currentNumber;//current number examined
	ifstream inputFile; //declare inputFile variable
	inputFile.open("Random.txt");

	while(inputFile >> currentNumber)
	{
		totalnumberofnumbers++;
		if(totalnumberofnumbers == 1)
		{
			max = currentNumber;
			min = currentNumber;
		}//end if
		if(currentNumber > max)
		{
			max = currentNumber;
		}// end if
		if(currentNumber < min)
		{
			min = currentNumber;
		}// end if
		total += currentNumber;
		if(currentNumber % 2 == 1)
		{
			numberodd++;
		}//end if
		else
		{
			numbereven++;
		}// end else
		if(currentNumber > 384 && currentNumber <= 605)
		{
			numberbetween++;
		}// end if
	}//end while loop
	average = total / totalnumberofnumbers;
	cout << "Total number of numbers: " << totalnumberofnumbers << endl;
	cout << "Average of the numbers: " << fixed << setprecision(1) <<average << endl;
	cout << "Number of even numbers: " << numbereven << endl;
	cout << "Number of odd numbers: " << numberodd << endl;
	cout << "Number of numbers strictly greater than 384 but less than or equal to 605: " << numberbetween << endl;
	cout << "Maximum: " << max << endl;
	cout << "Minimum: " << min << endl;
}//End main

