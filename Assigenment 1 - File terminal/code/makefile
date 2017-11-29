#All Targets
all: fs

# Tool invocations
fs: bin/Files.o bin/FileSystem.o bin/Commands.o bin/Environment.o bin/Main.o bin/tools.o
	@echo 'Building target: fs'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/fs bin/Files.o bin/FileSystem.o bin/Commands.o bin/Environment.o bin/Main.o bin/tools.o
	@echo 'Finished building target: fs'
	@echo ' '
	
# Depends on the source and header files
bin/Files.o: src/Files.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Files.o src/Files.cpp
	
# Depends on the source and header files
bin/FileSystem.o: src/FileSystem.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/FileSystem.o src/FileSystem.cpp
	
# Depends on the source and header files
bin/Commands.o: src/Commands.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Commands.o src/Commands.cpp

# Depends on the source and header files
bin/Environment.o: src/Environment.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Environment.o src/Environment.cpp
	
# Depends on the source and header files
bin/Main.o: src/Main.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/Main.o src/Main.cpp

# Depends on the source and header files
bin/tools.o: src/tools.cpp
	g++ -g -Wall -Weffc++ -std=c++11 -c -Iinclude -o bin/tools.o src/tools.cpp

#Clean the build directory
clean:
	rm -f bin/*