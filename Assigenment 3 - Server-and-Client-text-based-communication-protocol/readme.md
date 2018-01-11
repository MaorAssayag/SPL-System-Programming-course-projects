#  Server&Client text based communication protocol

* In this assignment we will implement an online movie rental service server and client. The communication between the server and the client(s) will be performed using a text based communication protocol, which will support renting, listing and returning of movies.

* The implementation of the server will be based on the Thread-Per-Client (TPC) and Reactor servers taught in class. The servers, as seen in class, do not support bi-directional message passing. Any time the server receives a message from a client it can reply back to that specific client itself, but what if we want to send messages between clients, or broadcast an announcment to all clients? The first part of the assignment will be to replace some of the current interfaces with new interfaces that will allow such a case. 

* We will implement the movie rental service over the User service text-based protocol. The User Service Text-based protocol is the base protocol which will define the message structure and base command. Given an implementation of the protocol we can implement many user service applications. The service you will build is a movie rental service. Since the service requires data to be saved about each user and available movies for rental, we will implement a simple JSON text database which will be read when the server starts and updated each time a change is made.

* we will also implement a simple terminal-like client in C++. To simplify matters, commands will be written by keyboard and sent “as is” to the server.

* for  <a href="https://github.com/MaorAssayag/SPL-System-Programming-course-projects/blob/master/Assigenment%203%20-%20Server-and-Client-text-based-communication-protocol/assignment3.pdf">full descreption</a>.

