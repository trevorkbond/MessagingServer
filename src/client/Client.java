package client;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws IOException {
        try (
            Socket socket = new Socket("localhost", 4444);
            BufferedOutputStream out =
                    new BufferedOutputStream(socket.getOutputStream());
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
        ) {
            byte[] buf = (
                    """
                            <?xml version="1.0" encoding="UTF-8"?>
                            <stream>""").getBytes();
            out.write(buf);
            out.flush();
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.write(userInput.getBytes());
            }
        }
    }
}
