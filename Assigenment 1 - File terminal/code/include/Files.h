#ifndef FILES_H_
#define FILES_H_

#include <string>
#include <vector>
#include <iostream>
#include "GlobalVariables.h"

using namespace std;

//**        BaseFile       **//
class BaseFile {
private:
	string name;

public:
	BaseFile(string name);// Constructor
	BaseFile(const BaseFile &aBaseFile);//copy Constructor
	string getName() const;
	void setName(string newName);
	virtual ~BaseFile();//destructor
	virtual int getSize() = 0;
	virtual string Type() = 0;
};



//**        File          **//
class File : public BaseFile {
private:
	int size;
		
public:
	File(string name, int size); // Constructor
	File(const File &aFile);//copy Constructor
	virtual ~File();//dis
	int getSize(); // Return the size of the file
	string Type();
};



//**        Directory     **//
class Directory : public BaseFile {
private:
	vector<BaseFile*> children;
	Directory *parent;
	void sorting(int option);//sorting algo
	void clearDir();//clear the victor of children
	vector<BaseFile*> CopyChildern();//copy childern

public:
	Directory(string name, Directory *parent); // Constructor
	Directory(const Directory &aDirectory);//copy Constructor
	Directory(Directory&& other);// Move Constructor
	Directory &operator=(const Directory &aDirectory);
	Directory& operator=(Directory &&other);// Move Assignment
	virtual ~Directory();
	Directory *getParent() const; // Return a pointer to the parent of this directory
	void setParent(Directory *newParent); // Change the parent of this directory
	void addFile(BaseFile* file); // Add the file to children
	void removeFile(string name); // Remove the file with the specified name from children
	void removeFile(BaseFile* file); // Remove the file from children
	void sortByName(); // Sort children by name alphabetically (not recursively)
	void sortBySize(); // Sort children by size (not recursively)
	vector<BaseFile*> getChildren(); // Return children
	int getSize(); // Return the size of the directory (recursively)
	string getAbsolutePath();  //Return the path from the root to this
	string Type();
	void SetParentIt();
	
};

#endif