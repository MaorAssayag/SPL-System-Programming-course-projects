#include "../include/FileSystem.h"
#include "../include/Commands.h"

FileSystem::FileSystem():rootDirectory(new Directory("Root", nullptr)),workingDirectory(nullptr){
    workingDirectory = this -> rootDirectory;
}

Directory& FileSystem::getRootDirectory() const {
    return *rootDirectory;
}

Directory& FileSystem::getWorkingDirectory() const {
    return *workingDirectory;
}

void FileSystem::setWorkingDirectory(Directory *newWorkingDirectory) {
    workingDirectory = newWorkingDirectory;
}

//****************** //
//     Rule_of_5     //
//******************//
FileSystem:: ~FileSystem(){
    delete rootDirectory;
    rootDirectory = nullptr;
    if (verbose == 1 || verbose == 3){
        cout << "FileSystem:: ~FileSystem()" << endl;
    }
}

FileSystem::FileSystem(const FileSystem &other):rootDirectory(),workingDirectory(){ // copy constructor
    delete rootDirectory; // in the creation of fs(at env copy constructor) we allocate a new dir - we choose to not to intialize fs with this function. so we need to delete the first allocation.
    rootDirectory = new Directory(*&other.getRootDirectory());

    vector<string> path = MasterTools::ArrayString(other.getWorkingDirectory().getAbsolutePath());
    Directory *a = dynamic_cast<Directory *>(MasterTools::BringtheBasefile(*rootDirectory, path));
    this->setWorkingDirectory(a);//set new working Directory

    if (verbose == 1 || verbose == 3){
        cout << "FileSystem::FileSystem(const FileSystem &other)" << endl;
    }
}

FileSystem::FileSystem(FileSystem &&other):rootDirectory(&other.getRootDirectory()),workingDirectory(&other.getWorkingDirectory()){               // move constructor
    other.rootDirectory = nullptr;
    other.workingDirectory = nullptr;
    if (verbose == 1 || verbose == 3){
        cout << "FileSystem::FileSystem(FileSystem &&other)" << endl;
    }
}

FileSystem& FileSystem::operator=(const FileSystem &other) {     // copy assignment
    if (this != &other){
        delete rootDirectory;
        rootDirectory = new Directory(*&other.getRootDirectory());

        workingDirectory = nullptr;
        vector<string> path = MasterTools::ArrayString(other.getWorkingDirectory().getAbsolutePath());
        Directory *a = dynamic_cast<Directory *>(MasterTools::BringtheBasefile(*rootDirectory, path));
        this->setWorkingDirectory(a);//set new working Directory
    }
    if (verbose == 1 || verbose == 3){
        cout << "FileSystem& FileSystem::operator=(const FileSystem &other)" << endl;
    }
    return (*this);
}

FileSystem& FileSystem:: operator=(FileSystem &&other) {         // move assignment
    if(this != &other) // prevent self-move
    {
        rootDirectory = &other.getRootDirectory();
        workingDirectory = &other.getWorkingDirectory();
        other.rootDirectory = nullptr;
        other.workingDirectory = nullptr;
    }
    if (verbose == 1 || verbose == 3){
        cout << "FileSystem& FileSystem:: operator=(FileSystem &&other)" << endl;
    }
    return *this;
}

void FileSystem:: clean(){
    rootDirectory = nullptr;
    workingDirectory = nullptr;
}
//---------EOF----------//