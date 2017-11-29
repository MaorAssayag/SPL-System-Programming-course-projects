
#include "../include/Commands.h"
#include <regex>


/****  Class : BaseCommand  ****/
/*****************************/
BaseCommand::BaseCommand(string args) : args(args) {}

string BaseCommand::getArgs() {
    return args;
}

BaseCommand::~BaseCommand(){}

BaseCommand *BaseCommand::copyCommand() {
    return nullptr;
}



/****  Class : PwdCommand  ****/
/*****************************/
PwdCommand::PwdCommand(string args) : BaseCommand(args) {}

void PwdCommand::execute(FileSystem &fs) {
    string print = fs.getWorkingDirectory().getAbsolutePath();
    cout << print << endl;
}

string PwdCommand::toString() {
    return "pwd";
}

PwdCommand::~PwdCommand() {

}

BaseCommand *PwdCommand::copyCommand() {
    return new PwdCommand(getArgs());
}


/****  Class : CdCommand  ****/
/*****************************/
CdCommand::CdCommand(string args) : BaseCommand(args) {}

string CdCommand::toString() {
    return "cd";
}
CdCommand::~CdCommand() {}

void CdCommand::execute(FileSystem &fs) {
    Directory *temp = &fs.getWorkingDirectory();
    string tempargs = getArgs();
    bool thisRoot = true;
    while (tempargs.size()>0 && (tempargs[0] == '.' || tempargs[1] == '.')) {//check if this Cd ..
        if (fs.getWorkingDirectory().getParent() != nullptr) {
            thisRoot = false;
            fs.setWorkingDirectory(fs.getWorkingDirectory().getParent());
            temp = &fs.getWorkingDirectory();
            tempargs = tempargs.substr(2);
            if(tempargs.size()>0)
                tempargs = tempargs.substr(1);
        } else {
            cout << "The system cannot find the path specified" << endl;
            return;
        }
    }
    if (tempargs[0] == '/' && thisRoot)
        temp = &fs.getRootDirectory();
    vector<string> path = MasterTools::ArrayString(tempargs);
    std::vector<string>::iterator it;
    it = path.begin();
    bool AllGood = MasterTools::AllGoog(*temp,path);//check if the path is exist and the end of the path is Directory
    if (AllGood) {
        Directory *a = dynamic_cast<Directory *>(MasterTools::BringtheBasefile(*temp, path));
        fs.setWorkingDirectory(a);//set new working Directory
    } else {
        cout << "The system cannot find the path specified" << endl;
    }
}

BaseCommand *CdCommand::copyCommand() {
    return new CdCommand(getArgs());
}



/****  Class : LsCommand  ****/
/*****************************/
LsCommand::LsCommand(string args) : BaseCommand(args) {}

string LsCommand::toString() {
    return "ls";
}

LsCommand::~LsCommand() {}

void LsCommand::execute(FileSystem &fs) {
    unsigned int temp = getArgs().find_first_of("-s");//check if need to sort by size
    bool SortBySize = false;
    string args = getArgs();
    if (temp < getArgs().size()) {
        SortBySize = true;
        if (args.find_first_of(" ")< args.size())
            args = args.substr(args.find_first_of(" ") + 1);//remove the -s from the args
        else{ args = args.substr(2); }
    }
    Directory *a = &fs.getWorkingDirectory();
    bool tempbool = true;
    vector<string> path;
    if (args.size() > 0) {
        if (args[0] == '/') {//check if we need the Root Directory OR the Working Directory
            a = &fs.getRootDirectory();
        }
        path = MasterTools::ArrayString(args);//make Vector of String
        tempbool = MasterTools::AllGoog(*a, path);//check if the path exist
        if (tempbool && path.size() != 0)
            a = dynamic_cast<Directory *>(MasterTools::BringtheBasefile(*a, path));
    }
    if (tempbool) {
        a->sortByName();
        if (SortBySize)
            a->sortBySize();//sort by size
        vector<BaseFile *> children = a->getChildren();
        std::vector<BaseFile *>::iterator it;
        it = children.begin();
        while (it != children.end()) {//print the sorting list
            cout << (*it)->Type() << '\t' << (*it)->getName() << '\t' << (*it)->getSize() << endl;
            it++;
        }
    } else {
        cout << "The system cannot find the path specified" << endl;
    }
}

BaseCommand *LsCommand::copyCommand() {
    return new LsCommand(getArgs());
}


/****  Class : MkdirCommand  ****/
/*****************************/
MkdirCommand::MkdirCommand(string args) : BaseCommand(args) {}

string MkdirCommand::toString() {
    return "mkdir";
}

MkdirCommand::~MkdirCommand() {}

void MkdirCommand::execute(FileSystem &fs) {
    Directory *current = &(fs.getWorkingDirectory());
    if (getArgs()[0] == '/') {
        current = &fs.getRootDirectory();
    }
    vector<string> path = MasterTools::ArrayString(getArgs());
    std::vector<string>::iterator it;
    bool exist = MasterTools::AllGoog(*current, path);
    if (exist || (MasterTools::BringtheBasefile(*current,path)!=nullptr)) {
        cout << "The directory already exists" << endl;
    } else {
        //check for invalid names in the path/dir
        it = path.begin();
        bool contains_invalid_chars = false;
        while (it != path.end()){
            contains_invalid_chars = (contains_invalid_chars || !std::regex_match((*it), std::regex("^[A-Za-z0-9]+$")));
            it++;
        }
        if (contains_invalid_chars) {
            return;
        }

        BaseFile *currentCheck;
        it = path.begin();
        while (it != path.end()) {
            currentCheck = MasterTools::CheckIfExist(current,current->getChildren(), (*it));
            if (currentCheck == nullptr) {
                current = new Directory((*it), current);
                current->getParent()->addFile(current);
            } else {
                current = dynamic_cast<Directory *>(currentCheck);
            }
            it++;
        }
    }

}

BaseCommand *MkdirCommand::copyCommand() {
    return new MkdirCommand(getArgs());
}

/****  Class : MkfileCommand  ****/
/*****************************/
MkfileCommand::MkfileCommand(string args) : BaseCommand(args) {}

string MkfileCommand::toString() {
    return "mkfile";
}

MkfileCommand::~MkfileCommand() {}

void MkfileCommand::execute(FileSystem &fs) {
    Directory *current = &fs.getWorkingDirectory();
    if (getArgs()[0] == '/') {
        current = &fs.getRootDirectory();
    }
    vector<string> path = MasterTools::ArrayString(getArgs());
    string name = path[path.size() - 1];//get the name of the file and size
    path.pop_back();//erase them from the vector
    if (MasterTools::AllGoog(*current, path)) {
        current = dynamic_cast<Directory *>(MasterTools::BringtheBasefile(*current, path));
        string size = name.substr(name.find_first_of(" ") + 1);
        name = name.substr(0, name.find_first_of(" "));
        // did the name of the file is valid ?
        bool contains_invalid_chars = !std::regex_match(name, std::regex("^[A-Za-z0-9]+$"));
        if (contains_invalid_chars) {
            return;
        }

        if (MasterTools::CheckIfExist(current,current->getChildren(), name) != nullptr) {
            cout << "File already exists" << endl;
        } else {
            int temp;
            istringstream buffer(size);
            buffer >> temp;
            current->addFile(new File(name, temp));
        }
    } else {
        cout << "The system cannot find the path specified" << endl;
    }
}

BaseCommand *MkfileCommand::copyCommand() {
    return new MkfileCommand(getArgs());
}


/****  Class : CpCommand  ****/
/*****************************/
CpCommand::CpCommand(string args) : BaseCommand(args) {}

string CpCommand::toString() {
    return "cp";
}

CpCommand::~CpCommand() {}

void CpCommand::execute(FileSystem &fs) {
    string source = getArgs().substr(0, getArgs().find_first_of(" "));//separated the source from the destination
    Directory *currentsrc = &fs.getWorkingDirectory();
    if (source[0] == '/') {
        currentsrc = &fs.getRootDirectory();
    }
    string destination = getArgs().substr(getArgs().find_first_of(" ") + 1);
    Directory *currentdes = &fs.getWorkingDirectory();
    if (destination[0] == '/') {
        currentdes = &fs.getRootDirectory();
    }
    vector<string> sourcepath = MasterTools::ArrayString(source);
    vector<string> destinationpath = MasterTools::ArrayString(destination);
    source = sourcepath[sourcepath.size() - 1];//take the what we want to copy
    sourcepath.pop_back();
    if (MasterTools::AllGoog(*currentsrc, sourcepath) &&
        MasterTools::AllGoog(*currentdes,destinationpath)) {//check if the source path and destination path are exist
        currentsrc = dynamic_cast<Directory *>(MasterTools::BringtheBasefile(*currentsrc, sourcepath));
        BaseFile *a = MasterTools::CheckIfExist(currentsrc,currentsrc->getChildren(), source);
        currentdes = dynamic_cast<Directory *>(MasterTools::BringtheBasefile(*currentdes, destinationpath));
        if (a != nullptr) {
            if (MasterTools::CheckIfExist(currentdes,currentdes->getChildren(), a->getName()) == nullptr) {
                if (a->Type() == "DIR") {
                    currentdes->addFile(new Directory(dynamic_cast<Directory &>(*a)));
                } else {
                    currentdes->addFile(new File(dynamic_cast<File &>(*a)));
                }
            }
        } else {
            cout << "No such file or directory" << endl;
        }
    } else {
        cout << "No such file or directory" << endl;
    }
}

BaseCommand *CpCommand::copyCommand() {
    return new CpCommand(getArgs());
}


/****  Class : MvCommand  ****/
/*****************************/
MvCommand::MvCommand(string
                     args) : BaseCommand(args) {}

string MvCommand::toString() {
    return "mv";
}

MvCommand::~MvCommand() {}

void MvCommand::execute(FileSystem &fs) {
    string source = getArgs().substr(0, getArgs().find_first_of(" "));//separated the source from the destination
    Directory *currentsrc = &fs.getWorkingDirectory();
    if (source[0] == '/') {
        currentsrc = &fs.getRootDirectory();
    }
    string destination = getArgs().substr(getArgs().find_first_of(" ") + 1);
    Directory *currentdes = &fs.getWorkingDirectory();
    if (destination[0] == '/') {
        currentdes = &fs.getRootDirectory();
    }
    vector<string> sourcepath = MasterTools::ArrayString(source);
    vector<string> destinationpath = MasterTools::ArrayString(destination);
    vector<string> sourcepath2 = MasterTools::ArrayString(source);
    string sourceforrm = source;
    source = sourcepath[sourcepath.size() - 1];//take the what we want to copy
    sourcepath.pop_back();
    if (MasterTools::AllGoog(*currentsrc, sourcepath)) {
        BaseFile *NeedToCheck = MasterTools::BringtheBasefile(*currentsrc, sourcepath2);
        if (!MasterTools::CheckAncestorsbBefDel(fs.getWorkingDirectory(), *NeedToCheck)) {
            cout << "Can't move directory" << endl;
        } else if (NeedToCheck != nullptr && MasterTools::AllGoog(*currentdes, destinationpath)) {
            NeedToCheck = MasterTools::BringtheBasefile(*currentdes, destinationpath);
            NeedToCheck = MasterTools::CheckIfExist(dynamic_cast<Directory *>(NeedToCheck),dynamic_cast<Directory *>(NeedToCheck)->getChildren(),
                                                    source);
            if (NeedToCheck == nullptr) {
                BaseCommand* temp1 = new CpCommand(getArgs());
                temp1 -> execute(fs);
                delete temp1;
                temp1 = new RmCommand(sourceforrm);
                temp1 -> execute(fs);
                delete temp1;
            } else {
                cout << "File or directory already exists" << endl;
            }

        } else {
            cout << "No such file or directory" << endl;
        }
    } else
        cout << "No such file or directory" << endl;

}

BaseCommand *MvCommand::copyCommand() {
    return new MvCommand(getArgs());
}


/****  Class : RenameCommand  ****/
/*****************************/
RenameCommand::RenameCommand(string
                             args) : BaseCommand(args) {}

string RenameCommand::toString() {
    return "rename";
}

RenameCommand::~RenameCommand() {}

void RenameCommand::execute(FileSystem &fs) {
    string oldname = getArgs().substr(0, getArgs().find_first_of(" "));//separated the source from the new name
    Directory *currentsrc = &fs.getWorkingDirectory();
    if (oldname[0] == '/') {
        currentsrc = &fs.getRootDirectory();
    }
    if (fs.getWorkingDirectory().getName().compare(oldname) == 0) {
        cout << "Can't rename the working directory" << endl;
    } else {
        string newname = getArgs().substr(getArgs().find_first_of(" ") + 1);
        vector<string> sourcepath = MasterTools::ArrayString(oldname);
        oldname = sourcepath[sourcepath.size() - 1];//take the old name
        sourcepath.pop_back();
        if (fs.getWorkingDirectory().getName().compare(oldname) == 0) {
            cout << "Can't rename the working directory" << endl;
        } else {
            Directory *temp = dynamic_cast<Directory *>(MasterTools::BringtheBasefile(*currentsrc, sourcepath));
            if (!MasterTools::AllGoog(*currentsrc, sourcepath)) {
                cout << "No such file or directory" << endl;
            } else if (MasterTools::CheckIfExist(temp,temp->getChildren(), oldname) == nullptr) {
                cout << "No such file or directory" << endl;
            } else {
                if(MasterTools::CheckIfExist(temp,temp->getChildren(), newname)== nullptr){
                    sourcepath.push_back(oldname);
                    MasterTools::BringtheBasefile(*currentsrc, sourcepath)->setName(newname);
                }
            }
        }
    }
}

BaseCommand *RenameCommand::copyCommand() {
    return new RenameCommand(getArgs());
}


/****  Class : HistoryCommand  ****/
/*****************************/
HistoryCommand::HistoryCommand(string args, const vector<BaseCommand *> &history) : BaseCommand(args), history(history){}

string HistoryCommand::toString() {
    return "history";
}

HistoryCommand::~HistoryCommand() {}

void HistoryCommand::execute(FileSystem &fs) {
    std::vector<BaseCommand *>::const_iterator it = history.begin();
    int i = 0;
    while (it != history.end()) {
        if ((*it)->toString() != "error"){
            cout << i << '\t' << (*it)->toString() << " " << (*it)->getArgs() << endl;
        }else{//its an error command - echo the input
            cout << i << '\t' << (*it)->getArgs() << endl;
        }
        it++;
        i++;
    }
}

/****  Class : RmCommand  ****/
/*****************************/
RmCommand::RmCommand(string
                     args) : BaseCommand(args) {}

string RmCommand::toString() {
    return "rm";
}

RmCommand::~RmCommand() {}

void RmCommand::execute(FileSystem &fs) {
    string source = getArgs();
    Directory *currentsrc = &fs.getWorkingDirectory();
    if (source[0] == '/') {
        currentsrc = &fs.getRootDirectory();
    }
    vector<string> sourcepath2 = MasterTools::ArrayString(source);
    vector<string> sourcepath = MasterTools::ArrayString(source);
    string needtodelete;
    if (sourcepath.size() != 0){//to get the name of the Basefile need to delete
        needtodelete = sourcepath[sourcepath.size() - 1];
        sourcepath.pop_back();//to get the parent path
    }
    if (MasterTools::AllGoog(*currentsrc, sourcepath)) {
        BaseFile *NeedToCheck = MasterTools::BringtheBasefile(*currentsrc, sourcepath2);
        Directory *temp = dynamic_cast<Directory *>(MasterTools::BringtheBasefile(*currentsrc, sourcepath));
        if (!MasterTools::CheckAncestorsbBefDel(fs.getWorkingDirectory(), *NeedToCheck)) {
            cout << "Can't remove directory" << endl;
        } else {
            if (MasterTools::CheckIfExist(temp,temp->getChildren(), needtodelete) != nullptr) {
                temp->removeFile(needtodelete);//Delete the BaseFile by name
            } else {
                cout << "No such file or directory" << endl;
            }
        }
    } else {
        cout << "No such file or directory" << endl;
    }
}

BaseCommand *RmCommand::copyCommand() {
    return new RmCommand(getArgs());
}


/****  Class : VerboseCommand  ****/
/*****************************/
VerboseCommand::VerboseCommand(string args):BaseCommand(args){}
string VerboseCommand::toString() {
    return "verbose";
}

VerboseCommand::~VerboseCommand() {}

void VerboseCommand::execute(FileSystem &fs) {
    bool contains_invalid_chars = !std::regex_match(getArgs(), std::regex("^[0123]+$"));
    if (contains_invalid_chars){
        cout << "Wrong verbose input" << endl; // print error message
        return;
    }
    istringstream buffer(getArgs());
    buffer >> verbose; // verbose = int value of getArgs()
}

BaseCommand *VerboseCommand::copyCommand() {
    return new VerboseCommand(getArgs());
}


/****  Class : ErrorCommand  ****/
/*****************************/
ErrorCommand::ErrorCommand(string args):BaseCommand(args){}
string ErrorCommand::toString() {
    return "error";
}

ErrorCommand::~ErrorCommand() {}

void ErrorCommand::execute(FileSystem &fs) {
    string args = getArgs();
    size_t i = args.find_first_of(" ");
    string currentCommandRequested;
    if (i != std::string::npos) {
        currentCommandRequested = args.substr(0, i); // extract the command, first word
    } else { currentCommandRequested = args; }
    cout << currentCommandRequested << ": Unknown command"  << endl; // print error message
}

BaseCommand *ErrorCommand::copyCommand() {
    return new ErrorCommand(getArgs());
}


/****  Class : ExecCommand  ****/
/*****************************/
ExecCommand::ExecCommand(string args, const vector<BaseCommand *> &history):BaseCommand(args),history(history){}
string ExecCommand::toString() {
    return "exec";
}

ExecCommand::~ExecCommand() {}

void ExecCommand::execute(FileSystem &fs) {
    bool contains_invalid_chars = !std::regex_match(getArgs(), std::regex("^[0-9]+$"));
    if (contains_invalid_chars){
        cout << "Command not found" << endl; // print error message
        return;
    }
    unsigned int temp;
    istringstream buffer(getArgs());
    buffer >> temp;
    if (temp >= 0 && temp < history.size()){
        history.at(temp)->execute(fs);
    }
    else {
        cout << "Command not found" << endl;
    }
}


/****  Class : MasterTools  ****/
/*****************************/
vector<string> MasterTools::ArrayString(string path) {
    vector<string> ans;
    unsigned int j = 0;
    unsigned int k = 0;
    while (j < path.size()) {
        j = k;
        k++;
        while (k < path.size() && path[k] != '/') {
            k++;
        }
        if (j < path.size()) {
            if (j != 0 || path[j] == '/')
                j++;
            if (k != j)
                ans.insert(ans.end(), path.substr(j, k - j));
        }
    }
    return ans;
}//make vector from part of the string path
BaseFile *MasterTools::CheckIfExist(Directory *a,vector<BaseFile *> children, string Name) {
    bool found = false;
    std::vector<BaseFile *>::iterator it;
    it = children.begin();
    if (Name == "..")
        return a->getParent();
    while (it != children.end() && !found) {
        found = ((*it)->getName() == Name);
        if (!found)
            it++;
    }
    if (found)
        return (*it);
    else
        return nullptr;

}//check if the name of the file/Directory exist
bool MasterTools::AllGoog(Directory &temp, vector<string> path) {
    bool AllGood = (&temp != nullptr);
    Directory *temp2 = &temp;
    std::vector<string>::iterator it = path.begin();
    while (path.size()>0 && it != path.end() && AllGood && (*it).size() > 0) {
        BaseFile *tampBaseFile = MasterTools::CheckIfExist(temp2,temp2->getChildren(), (*it));
        if (tampBaseFile != NULL && tampBaseFile->Type().compare("DIR") == 0) {
            temp2 = dynamic_cast<Directory *>(tampBaseFile);
        } else
            AllGood = false;
        it++;
    }
    return AllGood;

}

BaseFile *MasterTools::BringtheBasefile(Directory &current, vector<string> path) {
    std::vector<string>::iterator it = path.begin();
    Directory *cur = &current;
    while (it != path.end() && &current != nullptr && path[0].compare("") != 0) {
        if (MasterTools::CheckIfExist(&current,cur->getChildren(), (*it)) != nullptr &&
            (MasterTools::CheckIfExist(&current,cur->getChildren(), (*it)))->Type().compare("DIR") == 0)
            cur = (dynamic_cast<Directory *>((MasterTools::CheckIfExist(&current,cur->getChildren(), (*it)))));
        else
            return (MasterTools::CheckIfExist(&current,cur->getChildren(), (*it)));
        it++;
    }
    return cur;
}

bool MasterTools::CheckAncestorsbBefDel(Directory &WorkDir, BaseFile &Willbydelete) {
    Directory *check = &WorkDir;
    bool ans = true ;
    for (; ans && &Willbydelete != nullptr && Willbydelete.Type().compare("DIR") == 0
           && check != nullptr; check = check->getParent()) {
        ans = (check != &Willbydelete);
    }
    return ans;
}

//-----------EOF-----------//