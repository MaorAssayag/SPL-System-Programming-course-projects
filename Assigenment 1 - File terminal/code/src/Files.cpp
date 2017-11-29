#include "../include/Files.h"

using namespace std;


/****  Class : BaseFile  ****/
/*****************************/
BaseFile::BaseFile(string name) :name(name){}
BaseFile::BaseFile(const BaseFile &aBaseFile):name(""){ //copy Constructor - in use
    name = aBaseFile.getName();
    if (verbose == 1 || verbose == 3) {
        cout << "BaseFile::BaseFile(const BaseFile &aBaseFile)" << endl;
    }
}
string BaseFile::getName() const{
	return name;
}
void BaseFile::setName(string newName) {
    name = newName;
}
BaseFile::~BaseFile() { // destructor in use
    if (verbose == 1 || verbose == 3) {
        cout << "BaseFile::~BaseFile()" << endl;
    }
}


/****  Class : File  ****/
/*****************************/
File::File(string name, int size) :BaseFile(name),size(size){}
File::File(const File &aFile):BaseFile(aFile),size(){//copy Constructor in use
    this->size = aFile.size;
    if (verbose == 1 || verbose == 3) {
        cout << "File::File(const File &aFile)" << endl;
    }
}
File::~File(){
    if (verbose == 1 || verbose == 3) {
        cout << "File::~File()" << endl;
    }
}
int File::getSize() {
	return size;
}
string File::Type() {
    return "FILE";
}


/****  Class : Directory  ****/
/*****************************/
Directory::Directory(string name, Directory *parent) : BaseFile(name),children(),parent(parent){};
Directory::Directory(const Directory &aDirectory):BaseFile(aDirectory),children(),parent(){
    children = move(const_cast<Directory&>(aDirectory).CopyChildern());
    parent = aDirectory.parent;
    SetParentIt();
}
Directory::Directory(Directory&& other):BaseFile(other),children(),parent(other.getParent()){// Move Constructor
    children = move(other.children);
    other.children.erase(other.children.begin(),other.children.end()); // vector is an object, must give him a NULL (not nullptr)
    other.clearDir();
    if (verbose == 1 || verbose == 3) {
        cout << "Directory::Directory(Directory&& other)" << endl;
    }
}
Directory& Directory::operator=(const Directory &aDirectory){
    if(this == &aDirectory)
        return *this;
    clearDir();
    setName( aDirectory.getName());
    setParent(aDirectory.getParent());
    if(parent == this)
        parent = nullptr;
    children = move(const_cast<Directory&>(aDirectory).CopyChildern());
    SetParentIt();
    if (verbose == 1 || verbose == 3) {
        cout << "Directory& Directory::operator=(const Directory &aDirectory)" << endl;
    }
    return *this;
}
Directory& Directory::operator=(Directory &&other){
    if (this != &other) {
        clearDir();
        parent = other.parent;
        if(parent == this)
            parent = nullptr;
        children = other.children;
        other.children.clear();
    }
    if (verbose == 1 || verbose == 3) {
        cout << "Directory& Directory::operator=(Directory &&other)" << endl;
    }
    return *this;
}
Directory::~Directory() {
    clearDir();
    if (verbose == 1 || verbose == 3) {
        cout << "Directory::~Directory()" << endl;
    }
}
Directory* Directory::getParent() const {
	return parent;
}
void Directory::setParent(Directory *newParent) {
	parent = newParent;
}
void Directory::addFile(BaseFile* file) {
	children.push_back(file);
    if (file->Type() == "DIR"){
        dynamic_cast<Directory*> (file) -> setParent(this);
    }
}
void Directory::removeFile(string name) {
	bool found = false;
	std::vector<BaseFile*>::iterator it;
	it = children.begin();
	while (it != children.end()&&!found)
	{
		found = (*it)->getName().compare(name)==0;
        if(!found)
		    it++;
	}
	if (found) {
        delete (*it);
        children.erase(it);
    }
}
void Directory::removeFile(BaseFile* file) {
	removeFile(file -> getName());
}
void Directory::sorting(int option) {
	if (option == 1) {
		int c, d;
		BaseFile* swap;
		int n = children.size();
		for (c = 0; c < (n - 1); c++) {
            for (d = 0; d < n - c - 1; d++) {
                if (children[d]->getName().compare(children[d + 1]->getName())>0) {
                    swap = children[d];
                    children[d] = children[d + 1];
                    children[d + 1] = swap;
                }
            }
        }
	}
	else if(option == 0) {
		int c, d;
		BaseFile* swap;
		int n = children.size();
		for (c = 0; c < (n - 1); c++)
		{
			for (d = 0; d < n - c - 1; d++)
			{
				if (children[d]->getSize() > children[d + 1]->getSize()) 
				{
					swap = children[d];
					children[d] = children[d + 1];
					children[d + 1] = swap;
				}
			}
		}
	}
}
void Directory::sortByName() {
	sorting(1);
}

void Directory::sortBySize() {
	sorting(0);
}

void Directory::clearDir(){
    for (std::vector< BaseFile* >::iterator it = children.begin() ; it != children.end(); ++it)
    {
        delete (*it);
    }
    children.clear();
    parent = nullptr;
}

vector<BaseFile*> Directory::getChildren() {
    return children;
}

int Directory::getSize() {
    int size = 0;
    std::vector<BaseFile*>::iterator it;//iterator on children vector
    it = children.begin();
    while (it != children.end()){
        size += (*it)->getSize();
        it++;
    }
    return size;
}

string Directory::getAbsolutePath(){
   if(parent == nullptr){
       return "/";
   }
    if(parent->parent == nullptr){
        return  "/" +  this->getName();
    }
    return  parent->getAbsolutePath() + "/" + this->getName();
}

vector <BaseFile*> Directory::CopyChildern(){
    vector<BaseFile*> copychildren;
    for (std::vector< BaseFile* >::iterator it = children.begin() ;children.size() > 0 && it != children.end(); ++it)
    {
        if((*it)->Type().compare("DIR") == 0){
            Directory* a = new Directory((*it)->getName(),this);
            a->children = std::move(dynamic_cast<Directory*>(*it)->CopyChildern());//recurseve call
            copychildren.push_back(reinterpret_cast<BaseFile *&&>(a));
        }else{
            copychildren.push_back(new File((*it)->getName(),(*it)->getSize()));
        }
    }
    return copychildren;
}
string Directory::Type() {
    return "DIR";
}
void Directory::SetParentIt() {
    vector<BaseFile*> childrenset;
    for (std::vector< BaseFile* >::iterator it = children.begin() ; it != children.end(); ++it)
    {
        if((*it)->Type().compare("DIR") == 0){
            dynamic_cast<Directory*>(*it)->setParent(this);
        }
    }
}

//-----------EOF-----------//