#ifndef ENVIRONMENT_H_
#define ENVIRONMENT_H_

#include "Files.h"
#include "Commands.h"

#include <string>
#include <vector>

using namespace std;

class Environment {
private:
	vector<BaseCommand*> commandsHistory;
	FileSystem fs;

public:
	Environment();

    virtual ~Environment(); // destructor
    Environment(const Environment &other); // copy constructor
    Environment(Environment &&other); // move constructor
    Environment& operator=(const Environment &other); // copy assignment
    Environment& operator=(Environment &&other); // move assignment

	void start();
    FileSystem& getFileSystem(); // Get a reference to the file system
	void addToHistory(BaseCommand *command); // Add a new command to the history
	const vector<BaseCommand*>& getHistory() const; // Return a reference to the history of commands
	BaseCommand* getCommand(string commands[],string currentCommand,string lineContent) ; // Return a index to the string in the commands array
};

#endif