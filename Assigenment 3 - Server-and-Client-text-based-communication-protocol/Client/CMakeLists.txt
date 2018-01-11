cmake_minimum_required(VERSION 3.8)
project(Boost_Echo_Client)

set(CMAKE_CXX_STANDARD 11)
set(SOURCE_FILES
        include/ConnectionHandler.h
        src/ConnectionHandler.cpp
        src/echoClient.cpp )
#src/BBclient.cpp include/BBclient.h
# see https://cmake.org/cmake/help/latest/module/FindBoost.html
find_package(Boost)

include_directories(../../../../../../boost_1_66_0/boost_1_66_0)
# Note: a target should be already defined using 'add_executable' or 'add_library'
link_libraries(../../../../../../boost_1_66_0/boost_1_66_0/stage/lib)


add_executable(Boost_Echo_Client ${SOURCE_FILES}
        src
        include)
