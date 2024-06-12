# Learning Log

### June 7, 2024

It has been a while since I've done any sort of network programming in Java. As such, I started with the basics and used the Javadoc as my reference. I learned the following:

- Java separates TCP/UDP socket implementations at the class level. I will be working with TCP, so using ServerSocket and Socket will work great.
- A ServerSocket is Java's implementation of a listening TCP socket. *Interestingly* and slightly confusingly, the name for the client socket returned when that listening socket accepts a connection is simply named Socket. On the client end of the connection, the Socket class is also used.

TODO from today:

- [x] Review the "try with resources" Java syntax and why it is used.
  - All class instances created using try-with-resources that implement the AutoCloseable interface will be closed regardless of whether the try block runs normally or is interrupted by an exception. In this case, the ServerSocket and Socket will close even if an exception occurs.
- [x] Look into threading implementation in Java, as I plan on having the server start up a new thread to handle each returned client connection.

### June 8, 2024

I converted the simple server to a (still simple) echo server. I then learned about threading in Java and made it a multi-threaded echo server. I learned the following:

- A past class I took (CS 240) has an excellent rundown of multi-threaded programming in Java that I reviewed [here](https://github.com/softwareconstruction240/softwareconstruction/blob/main/instruction/concurrency/concurrency.md).
- Java has an ExecutorService class with different methods for creating a single thread or different types of threadpools.

TODO from today:
- [ ] Implement ExecutorService and use a cached threadpool so the service doesn't waste resources with creating a new thread every time a new client connects.
- [x] Learn more about the Extensible Messaging and Presence Protocol (XMPP) and how I will implement it in this client/server model. Learning about and implementing this will likely take a while, but it looks to be the standard messaging protocol. 

### June 10, 2024

I found several sources to help me learn about XMPP. One of which is RFC 6120, a technical description of the latest XMPP standards. I also researched XML parsing over a TCP connection. Here's what I found:

- Java has a helpful tutorial on a [streaming API for XML](https://docs.oracle.com/javase/tutorial/jaxp/stax/index.html). I plan to reference this heavily and learn about their different implementations.
- XMPP connections are opened by an initiator (usually a client). The signal that the client is opening an XMPP stream is an opening <stream> tag. The server will then respond with an opening <stream> tag.
- The terminology for a message sent across an XMPP stream is an XMPP stanza.

TODO from today:

- [x] Implement the XMLParser class with what I learn from the Java XML streaming tutorial.
- [x] Get the server and client speaking XMPP at a basic level by having the client initiate an XMPP stream and having the server respond appropriately. 

### June 11, 2024

I used the Java XML streaming tutorial found yesterday to learn how I can parse XML. The main issue I encountered (that is not yet fully resolved) is that I need to be processing XMPP stanzas as they come in, not just after the entire XML "document" is sent. I am currently remedying this with an extension of the InputStream class that, as of now, stops reading from the socket when it sees a closing "</stream>" tag, the signal of the end of an XMPP stream. 

What I learned:

- Starting simple is extremely helpful, especially when working with new APIs. I ended up simplifying my target achievement for today significantly.
- Java's terminology for IO that uses characters and Strings is Reader/Writer. InputStream/OutputStream is used for IO dealing directly with bytes.

TODO from today:

- [x] Figure out the relationship between XMLParser and DocumentSplittingInputStream. Most importantly, figure out how to get XMLParser to parse data received from the stream when the stream signals it to do so upon receipt of an XMPP stanza.

### June 12, 2024

I expanded on yesterday's work to add additional parsing capabilities and have the XMLParser recognize the types of XMPP stanzas it is receiving.

TODO from today: 

- [ ] Make XMPPStreamer an abstract class with different implementations for the client and server side. Both the client and server will have to process XMPP stanzas, but certain of their behaviors will be different, such as a client initiating an XMPP stream and the server responding appropriately.
- [ ] Figure out if IO multiplexing is needed and if so what the Java implementation looks like. I think this will be useful for the client side especially because I client could receive a message from the server at any time that they need to read. Non-blocking, edge triggered monitoring for IO (like epoll in C) would be useful in this case.