//
// Created by refael on 05/01/2018.
//

#ifndef CLIENT_BBCLIENT_H
#define CLIENT_BBCLIENT_H

class SocketRead{
private:
    ConnectionHandler _connectionHandler;
public:
    SocketRead (ConnectionHandler connectionHandler);
    void run();
};

class keyboardRead{
private:
    ConnectionHandler _connectionHandler;
public:
    keyboardRead (ConnectionHandler connectionHandler);
    void run();
};

#endif //CLIENT_BBCLIENT_H
