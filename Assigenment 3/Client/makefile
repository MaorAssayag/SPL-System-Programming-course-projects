CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread

#All Targets
all: BBclient
	@echo 'Building target: BBclient'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/BBclient bin/connectionHandler.o bin/BBclient.o $(LDFLAGS)
	@echo 'Finished building target: BBclient'
	@echo ' '
BBclient: bin/connectionHandler.o bin/BBclient.o

# Depends on the source and header files
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

# Depends on the source and header files
bin/BBclient.o: src/BBclient.cpp
	g++ $(CFLAGS) -o bin/BBclient.o src/BBclient.cpp

#Clean the build directory
.PHONY: clean
clean:
	rm -f bin/*