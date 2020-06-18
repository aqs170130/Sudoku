// Author: Andy Shao
// Course: CS1337.0U1
// Date: 6/27/2018
// Assignment: Lecture Homework #5 Exercise #1
// Compiler: Eclipse Oxygen
// Description:
// This program finds the mode of a
// data set.
#include <iostream>
#include <fstream>
#include <iomanip>
using namespace std;
struct FileData
{
	int * dataArray; // A pointer to the dynamic data array
	int count; // The total number of elements in the file.
	int maxElem; // The largest element in the file.
};//end struct FileData
struct ModeData
{
	int mode; // The modal element
	int frequency; // The frequency of the modal element
};//end struct ModeData
FileData readDataFromFile (ifstream & inFile, int size);
ModeData findMode (FileData fileData);
/**
 * This is the main function and it uses
 * findMode() and readDataFromFile()
 */
int main()
{
	ifstream inputFile;// defining input File
	FileData filedata = readDataFromFile(inputFile,700);//read data fron file
	ModeData data = findMode(filedata);// calculate mode data from filedata
	delete [] filedata.dataArray;//deallocate dataArray
	filedata.dataArray = nullptr;//set dataArray equal to nullptr
	//output results to console
	if(data.frequency==1)
	{
		cout << "There is no real mode" << endl;
	}//end if(data.frequency==1)
	else
	{
		cout << "The Mode is: " << data.mode << endl;
		cout << "And its frequency is: " << data.frequency << endl;
	}//end else
	return 0;
}
/**
 * This function reads data from file
 * and returns the results in a FileData
 * structure.
 */
FileData readDataFromFile (ifstream & inFile, int size)
{
	int * arrptr = new int[size];//allocate an array
	int count = 0;
	inFile.open("RandomModeOneMode.txt");
	int max, reader;
	//calculate the max and read the values into arrptr
	while(count < size && inFile >> reader)
	{
		arrptr[count] = reader;
		count++;
		if(count == 1)
		{
			max = reader;
		}//end if(count == 1)
		if(reader > max)
		{
			max = reader;
		}//end if(reader > max)
	}//end while(count < size && inFile >> reader)
	FileData temp;
	temp.dataArray = arrptr;
	temp.count = count;
	temp.maxElem = max;
	return temp;
}//end FileData readDataFromFile (ifstream & inFile, int size)
/**
 * Finds the mode given a FileData structure
 */
ModeData findMode (FileData fileData)
{
	// initialize an dynamic array to count the number of instances each  number appears
	int * arrcntr = new int[fileData.maxElem+1];
	// intialize all values in arrcntr to zero
	for(int i = 0; i < fileData.maxElem+1; i++)
	{
		arrcntr[i]=0;
	}//end for(int i = 0; i < fileData.maxElem+1; i++)
	// go through each element fileData.dataArray
	// and count each value in arrcntr
	for(int i = 0; i < fileData.count;i++)
	{
		int value = fileData.dataArray[i];
		arrcntr[value]++;
	}//end for(int i = 0; i < fileData.count;i++)
	//find the max value and its index in arrcntr
	int mode, modecount;
	for(int i = 0; i < fileData.maxElem+1; i++)
	{
		if(i==0)
		{
			mode = i;
			modecount = arrcntr[i];
		}//end if(i==0)
		if(arrcntr[i] > modecount)
		{
			mode = i;
			modecount = arrcntr[i];
		}//end if(arrcntr[i] > modecount)
	}//end for(int i = 0; i < fileData.maxElem+1; i++)
	//create a structure and fill it with the data
	ModeData temp;
	temp.mode = mode;
	temp.frequency = modecount;
	//deallocate arrcntr
	delete [] arrcntr;
	arrcntr = nullptr;
	return temp;
}//end ModeData findMode (FileData fileData)
