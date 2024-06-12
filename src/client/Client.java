package client;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        try (
            Socket socket = new Socket("localhost", 4444);
            BufferedOutputStream out =
                    new BufferedOutputStream(socket.getOutputStream());
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
        ) {
            sendMessage(out, "<stream>");
            sendMessage(out, "<message></message>");
            sendMessage(out, "<presence></presence>");
            sendMessage(out, "</stream>");
        }
    }

    private static void sendMessage(BufferedOutputStream out, String
            message) throws IOException, InterruptedException {
        byte[] buf = (message).getBytes();
        out.write(buf);
        out.flush();
        Thread.sleep(3000);
    }
}
