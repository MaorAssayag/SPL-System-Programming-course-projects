#ifndef FILESYSTEM_H_
#define FILESYSTEM_H_

#include "Files.h"
#include "GlobalVariables.h"

class FileSystem {
private:
	Directory* rootDirectory;
	Directory* workingDirectory;


public:
    void clean();
	virtual ~FileSystem(); // destructor
    FileSystem(const FileSystem &other); // copy constructor
    FileSystem(FileSystem &&other); // move constructor
	FileSystem& operator=(const FileSystem &other); // copy assignment
	FileSystem& operator=(FileSystem &&other); // move assignment

	FileSystem();
	Directory& getRootDirectory() const; // Return reference to the root directory
	Directory& getWorkingDirectory() const; // Return reference to the working directory
	void setWorkingDirectory(Directory *newWorkingDirectory); // Change the working directory of the file system
};

#endif
