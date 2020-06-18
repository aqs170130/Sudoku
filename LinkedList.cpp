// Author: Andy Shao
// Course: CS1337.0U1
// Date: 7/13/2018
// Assignment: Lecture Homework #7 Exercise #2
// Compiler: Eclipse Oxygen
// Description:
// This program works with linked
// list classes#include <iostream>
#include <iomanip>
#include <iostream>
#include <fstream>
#include "LinkedList.h"
using namespace std;
/**
 * constructor for the linked list class
 */
LinkedList::LinkedList()
{
	numitems = 0;
	headnode.nextNode = &tailnode;
	headnode.prevNode = nullptr;
	tailnode.nextNode = nullptr;
	tailnode.prevNode = &headnode;
}//end LinkedList::LinkedList()
/**
 * destructor for the linked list
 */
LinkedList::~LinkedList()
{
	Node * currNode = headnode.nextNode;
	for(int i = 0; i < numitems;i++){
		Node * next = currNode->nextNode;
		delete currNode;
		currNode = next;
	}
}//end LinkedList::~LinkedList()
/**
 * adds a node to the end of the list
 */
Node * LinkedList::addNodeToEnd (int value)
{
	Node * node = new Node;
	node->number = value;
	node->prevNode = tailnode.prevNode;
	tailnode.prevNode = node;
	node->prevNode->nextNode = node;
	numitems++;
	return node;
}//end Node * LinkedList::addNodeToEnd (int value)
/**
 * adds a node to the beginning of the list
 */
Node * LinkedList::addNodeToBeginning (int value)
{
	Node * node = new Node;
	node->number = value;
	node->nextNode = headnode.nextNode;
	headnode.nextNode = node;
	node->nextNode->prevNode = node;
	numitems++;
	return node;
}//end Node * LinkedList::addNodeToBeginning (int value)
/**
 * This function prints the list in forwards order
 */
void LinkedList::printListForward ()
{
	Node * currNode = headnode.nextNode;
	int tencount = 0;
	for(int i = 0; i < numitems; i++)
	{
		cout << setw(5) <<currNode->number;
		tencount++;
		if(tencount == 10)
		{
			cout << endl;
			tencount = 0;
		}//end if(tencount == 10)
		currNode = currNode->nextNode;
	}//end for(int i = 0; i < numitems; i++)
}//end void LinkedList::printListForward ()
/**
 * This function prints the list in backwards order
 */
void LinkedList::printListBackward ()
{
	Node * currNode = tailnode.prevNode;
	int tencount = 0;
	for(int i = 0; i < numitems; i++)
	{
		cout << setw(5) <<currNode->number;
		tencount++;
		if(tencount == 10)
		{
			cout << endl;
			tencount = 0;
		}//end if(tencount == 10)
		currNode = currNode->prevNode;
	}//end for(int i = 0; i < numitems; i++)
}//end void LinkedList::printListBackward ()
/**
 * This fucntion sorts the list using bubble sort
 */
void LinkedList::sort ()
{
	bool swapper;
	do{
		swapper = false;
		Node * currNode = headnode.nextNode;
		for(int count = 0; count < numitems-1; count++)
		{
			if(currNode->number > currNode->nextNode->number)
			{
				int temp = currNode->number;
				currNode->number = currNode->nextNode->number;
				currNode->nextNode->number = temp;
				swapper = true;
			}// end if(currNode->number > currNode->nextNode->number)
			currNode = currNode->nextNode;
		}// end for(int count = 0; count < numitems-1; count++)
	}while(swapper);
}//end void LinkedList::sort ()




