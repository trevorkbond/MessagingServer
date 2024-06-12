package server;

import shared.DocumentSplittingInputStream;
import shared.XMLParser;
import shared.XMPPStreamer;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        int port = 4444;
        try (
                ServerSocket serverSocket = new ServerSocket(port);
        ) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf("New connection from %s\n" +
                        "There are now %d threads running in this process\n",
                        clientSocket.getInetAddress(), Thread.activeCount());
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start();
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        public ClientHandler(Socket socket) throws IOException {
            clientSocket = socket;
        }
        @Override
        public void run() {
            try (
                    BufferedOutputStream out =
                            new BufferedOutputStream(clientSocket.getOutputStream());
                    DocumentSplittingInputStream in =
                            new DocumentSplittingInputStream(clientSocket.getInputStream());
                    )
            {
                //TODO: Make this process repetitive - XML must be processed in XMPP stanzas while the "document" is being received
                XMPPStreamer streamer = new XMPPStreamer(in, out, false);
                streamer.stream();
            } catch (IOException | XMLStreamException e) {
                e.printStackTrace();
            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                        System.out.println("Closed client connection");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
