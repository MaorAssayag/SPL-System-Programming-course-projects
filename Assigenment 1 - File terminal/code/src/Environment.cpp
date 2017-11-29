#include "../include/Environment.h"
#include "../include/tools.h"

/****  Class : Environment  ****/
/*****************************/
Environment::Environment():commandsHistory(),fs(){} // Default constructor

void Environment::start() {
    string commands[12] = {"pwd", "cd", "ls", "mkdir", "mkfile", "cp", "mv", "rename", "rm", "history", "verbose",
                           "exec"};
    string currentLine;
    string currentCommandRequested;
    string lineContent;
    size_t i;
    Tools T;
    while (true) {
        cout << fs.getWorkingDirectory().getAbsolutePath() << ">"; // print current path
        getline(cin, currentLine);  //user input to currentLine
        if (currentLine == "exit") break;

        if (verbose == 2 || verbose == 3) {
            cout << currentLine << endl;
        }

        if (currentLine.empty()) continue; // skip the remaining code and continue to the next iteration loop;

        currentLine = T.deleteSpaces(currentLine);  //delete spaces before command and at the end

        i = currentLine.find_first_of(" ");
        if (i != std::string::npos) {
            currentCommandRequested = currentLine.substr(0, i); // extract the command, first word
            lineContent = currentLine.substr(i + 1, currentLine.length() - i); // get the rest of the input
        } else {
            currentCommandRequested = currentLine;
            lineContent = "";
        }

        BaseCommand *currentCommand = this->getCommand(commands, currentCommandRequested, lineContent);
        currentCommand->execute(fs);
        addToHistory(currentCommand);
    }
}

BaseCommand *Environment::getCommand(string commands[], string currentCommand, string lineContent) {
//getCommand will return the correct BaseCommand according to the user input.
    BaseCommand *ans = nullptr;
    int j = 0;
    for (j = 0; j < 12; j++) {
        if (commands[j] == (currentCommand)) {
            break;
        }
    }
    switch (j) {
        case 0:// call pwd
            ans = new PwdCommand(lineContent);
            break;
        case 1:// call cd
            ans = new CdCommand(lineContent);
            break;
        case 2:// call ls
            ans = new LsCommand(lineContent);
            break;
        case 3:// call mkdir
            ans = new MkdirCommand(lineContent);
            break;
        case 4:// call mkfile
            ans = new MkfileCommand(lineContent);
            break;
        case 5:// call cp
            ans = new CpCommand(lineContent);
            break;
        case 6:// call mv
            ans = new MvCommand(lineContent);
            break;
        case 7:// call rename
            ans = new RenameCommand(lineContent);
            break;
        case 8:// call rm
            ans = new RmCommand(lineContent);
            break;
        case 9:// call history
            ans = new HistoryCommand(lineContent, this->commandsHistory);  // need to pass also the history vector ?
            break;
        case 10:// call verbose
            ans = new VerboseCommand(lineContent);
            break;
        case 11:// call exec
            ans = new ExecCommand(lineContent, this->commandsHistory);
            break;
        default:
            if (!lineContent.empty()){ans = new ErrorCommand(currentCommand+" "+lineContent);}//invalid command - ErrorCommand
            else {ans = new ErrorCommand(currentCommand);}//invalid command - ErrorCommand
            break;
    }
    return ans;
}


FileSystem &Environment::getFileSystem() { // Get a reference to the file system
    return fs;
}

void Environment::addToHistory(BaseCommand *command) { // Add a new command to the history
    commandsHistory.push_back(command);
}

const vector<BaseCommand *> &Environment::getHistory() const { // Return a reference to the history of commands
    return commandsHistory;
}


//****************** //
//     Rule_of_5     //
//******************//
Environment::~Environment() {// destructor
    BaseCommand *temp = nullptr;
    vector<BaseCommand *> tempVector = getHistory();
    if (!tempVector.empty()) {
        for (unsigned int j = 0; j < tempVector.size(); j++) {
            temp = tempVector[j];
            delete temp;
        }
    }
    if (verbose == 1 || verbose == 3) {
        cout << "Environment:: ~Environment()" << endl;
    }
}

Environment::Environment(const Environment &other):commandsHistory(),fs() {  // copy constructor
    for (vector<BaseCommand*>::const_iterator it = other.getHistory().begin() ; it != other.getHistory().end(); ++it)
    {
        if ((*it)->toString()=="history"){
            addToHistory(new HistoryCommand((*it)->getArgs(), commandsHistory)); // By instruction from the forum
        }
        if ((*it)->toString()=="exec"){
            addToHistory(new ExecCommand((*it)->getArgs(), commandsHistory)); // By instruction from the forum
        }
        else{
            addToHistory((*it)->copyCommand());  //will return a NEW copy of the command
        }
    }
    fs = const_cast<Environment &>(other).getFileSystem(); // copy assignment of FileSystem
    if (verbose == 1 || verbose == 3) {
        cout << "Environment::Environment(const Environment &other)" << endl;
    }
}

Environment::Environment(Environment &&other):commandsHistory(std::move(other.commandsHistory)),fs(std::move(other.fs)) {  // move constructor
    if (verbose == 1 || verbose == 3) {
        cout << "Environment::Environment(Environment &&other)" << endl;
    }
}

Environment &Environment::operator=(const Environment &other) {     // copy assignment
    if (this!= &other){
        for (vector<BaseCommand*>::const_iterator it = commandsHistory.begin() ; it != commandsHistory.end(); ++it)
        { delete (*it); } // delete my old sources
        commandsHistory.clear();
        fs.clean();
        for (vector<BaseCommand*>::const_iterator it = other.getHistory().begin() ; it != other.getHistory().end(); ++it)
        {
            if ((*it)->toString()=="history"){
                addToHistory(new HistoryCommand((*it)->getArgs(), commandsHistory)); // By instruction from the forum
            }
            if ((*it)->toString()=="exec"){
                addToHistory(new ExecCommand((*it)->getArgs(), commandsHistory)); // By instruction from the forum
            }
            else{
                addToHistory((*it)->copyCommand());  //will return a NEW copy of the command
            }
        }
        fs = const_cast<Environment &>(other).getFileSystem(); // copy assignment of FileSystem
    }
    if (verbose == 1 || verbose == 3) {
        cout << "Environment& Environment::operator=(const Environment &other)" << endl;
    }
    return (*this);
}

Environment &Environment::operator=(Environment &&other) {         // move assignment
    if (this != &other) // prevent self-move
    {
        BaseCommand *temp = nullptr; //remove my sources
        vector<BaseCommand *> tempVector = getHistory();
        if (!tempVector.empty()) {
            for (unsigned int j = 0; j < tempVector.size(); j++) {
                temp = tempVector[j];
                delete temp;
            }
        }
        delete &(fs.getRootDirectory());
        fs.clean();
        commandsHistory = std::move(other.commandsHistory); // get the other sources
        fs = std::move(other.getFileSystem());// move&remove other sources before exiting the function
    }
    if (verbose == 1 || verbose == 3) {
        cout << "Environment& Environment:: operator=(Environment &&other)" << endl;
    }
    return *this;
}
//---------EOF----------//