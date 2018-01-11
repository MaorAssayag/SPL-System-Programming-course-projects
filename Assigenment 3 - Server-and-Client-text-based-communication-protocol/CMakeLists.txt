cmake_minimum_required(VERSION 3.8)
project(Assigenment_3)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES
        Client/.idea/Client.iml
        Client/.idea/misc.xml
        Client/.idea/modules.xml
        Client/.idea/workspace.xml
        Client/cmake-build-debug/Client.cbp
        Client/cmake-build-debug/cmake_install.cmake
        Client/cmake-build-debug/CMakeCache.txt
        Client/cmake-build-debug/Makefile
        Client/include/connectionHandler.h
        Client/src/connectionHandler.cpp
        Client/src/echoClient.cpp
        Client/CMakeLists.txt
        Client/dsadasdd.txt
        Client/makefile)

add_executable(Assigenment_3 ${SOURCE_FILES})