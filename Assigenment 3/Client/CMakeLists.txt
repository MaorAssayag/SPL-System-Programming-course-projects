cmake_minimum_required(VERSION 3.8)
project(Client)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES
        include/ConnectionHandler.h
        src/ConnectionHandler.cpp
        src/echoClient.cpp src/BBclient.cpp include/SocketThread.h include/KeyboardThreade.h)

add_executable(Client ${SOURCE_FILES}
        src
        include)
