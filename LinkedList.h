// Author: Andy Shao
// Course: CS1337.0U1
// Date: 7/13/2018
// Assignment: Lecture Homework #7 Exercise #2
// Compiler: Eclipse Oxygen
// Description:
// This program works with linked
// list classes

#ifndef LINKEDLIST_H_
#define LINKEDLIST_H_
struct Node{
	int number;
	Node * nextNode;
	Node * prevNode;
};

class LinkedList{
private:
	Node headnode;
	Node tailnode;
	int numitems;
public:
	LinkedList();
	~LinkedList();
	Node * addNodeToEnd (int value);
	Node * addNodeToBeginning (int value);
	void printListForward ();
	void printListBackward ();
	void sort ();
};




#endif /* LINKEDLIST_H_ */
