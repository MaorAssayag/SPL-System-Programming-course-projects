CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system

all: BBclient
	g++ -o bin/bb bin/connectionHandler.o bin/BBclient.o $(LDFLAGS)

BBclient: bin/connectionHandler.o bin/BBclient.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/BBclient.o: src/BBclient.cpp
	g++ $(CFLAGS) -o bin/BBclient.o src/BBclient.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
