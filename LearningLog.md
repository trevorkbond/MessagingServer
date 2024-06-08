# Learning Log

### June 7, 2024

It has been a while since I've done any sort of network programming in Java. As such, I started with the basics and used the Javadoc as my reference. I learned the following:

- Java separates TCP/UDP socket implementations at the class level. I will be working with TCP, so using ServerSocket and Socket will work great.
- A ServerSocket is Java's implementation of a listening TCP socket. *Interestingly* and slightly confusingly, the name for the client socket returned when that listening socket accepts a connection is simply named Socket. On the client end of the connection, the Socket class is also used.

TODO from today:

- [ ] Review the "try with resources" Java syntax and why it is used.
  - All class instances created using try-with-resources that implement the AutoCloseable interface will be closed regardless of whether the try block runs normally or is interrupted by an exception. In this case, the ServerSocket and Socket will close even if an exception occurs.
- [ ] Look into threading implementation in Java, as I plan on having the server start up a new thread to handle each returned client connection.