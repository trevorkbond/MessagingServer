package server;

import shared.DocumentSplittingInputStream;
import shared.XMPPReader;
import shared.XMPPWriter;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static volatile boolean isStreamOpen;

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
                XMPPReader reader = new XMPPReader(in, false);
                reader.read();
                XMPPWriter writer = new XMPPWriter(out);
                writer.writeBytes("<stream>");
                reader.read();
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
