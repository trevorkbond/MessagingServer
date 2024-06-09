package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        private Socket clientSocket = null;
        public ClientHandler(Socket socket) throws IOException {
            clientSocket = socket;
        }
        @Override
        public void run() {
            try (
                    PrintWriter out =
                            new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in =
                            new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    )
            {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Heard: " + inputLine);
                    out.println(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
