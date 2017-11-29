#ifndef COMMANDS_H_
#define COMMANDS_H_

#include <string>
#include <iostream>
#include "FileSystem.h"
#include "GlobalVariables.h"
#include "../include/Files.h"

class BaseCommand {
private:
	string args;

public:
	BaseCommand(string args);
	string getArgs();
	virtual void execute(FileSystem & fs) = 0;
	virtual string toString() = 0;
	virtual ~BaseCommand();
    virtual BaseCommand * copyCommand();
};

class PwdCommand : public BaseCommand {
private:
public:
	PwdCommand(string args);
	void execute(FileSystem & fs); // Every derived class should implement this function according to the document (pdf)
	virtual string toString();
    ~PwdCommand();
    BaseCommand * copyCommand();
};

class CdCommand : public BaseCommand {
private:
public:
	CdCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~CdCommand();
    BaseCommand * copyCommand();
};

class LsCommand : public BaseCommand {
private:
public:
	LsCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~LsCommand();
    BaseCommand * copyCommand();
};

class MkdirCommand : public BaseCommand {
private:
public:
	MkdirCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~MkdirCommand();
    BaseCommand * copyCommand();
};

class MkfileCommand : public BaseCommand {
private:
public:
	MkfileCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~MkfileCommand();
    BaseCommand * copyCommand();
};

class CpCommand : public BaseCommand {
private:
public:
	CpCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~CpCommand();
    BaseCommand * copyCommand();
};

class MvCommand : public BaseCommand {
private:
public:
	MvCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~MvCommand();
    BaseCommand * copyCommand();
};

class RenameCommand : public BaseCommand {
private:
public:
	RenameCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~RenameCommand();
    BaseCommand * copyCommand();
};

class RmCommand : public BaseCommand {
private:
public:
	RmCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~RmCommand();
    BaseCommand * copyCommand();
};

class HistoryCommand : public BaseCommand {
private:
	const vector<BaseCommand *> & history;
public:
	HistoryCommand(string args, const vector<BaseCommand *> & history);
	void execute(FileSystem & fs);
	string toString();
    ~HistoryCommand();
};


class VerboseCommand : public BaseCommand {
private:
public:
	VerboseCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~VerboseCommand();
    BaseCommand * copyCommand();
};

class ErrorCommand : public BaseCommand {
private:
public:
	ErrorCommand(string args);
	void execute(FileSystem & fs);
	string toString();
    ~ErrorCommand();
    BaseCommand * copyCommand();
};

class ExecCommand : public BaseCommand {
private:
	const vector<BaseCommand *> & history;
public:
	ExecCommand(string args, const vector<BaseCommand *> & history);
	void execute(FileSystem & fs);
	string toString();
    ~ExecCommand();
};

class MasterTools { // Class for aid functions
private:
public:
    static vector <string> ArrayString(string path);//return the string path in order
    static BaseFile* CheckIfExist(Directory *a,vector<BaseFile *> children, string Name);//check if the name of file/dir exist
    static bool AllGoog (Directory &temp,vector<string> path);
    static BaseFile* BringtheBasefile (Directory &current,vector <string> path);
    static bool CheckAncestorsbBefDel(Directory &WorkDir,BaseFile &Willbydelete);
};
#endif
