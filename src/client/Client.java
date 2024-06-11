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
                            <stream>
                            <BookCatalogue xmlns="http://www.publishing.org">
                            <Book>
                                <Title>Yogasana Vijnana: the Science of Yoga</Title>
                                <Author>Dhirendra Brahmachari</Author>
                                <Date>1966</Date>
                                <ISBN>81-40-34319-4</ISBN>
                                <Publisher>Dhirendra Yoga Publications</Publisher>
                                <Cost currency="INR">11.50</Cost>
                            </Book>
                            <Book>
                                <Title>The First and Last Freedom</Title>
                                <Author>J. Krishnamurti</Author>
                                <Date>1954</Date>
                                <ISBN>0-06-064831-7</ISBN>
                                <Publisher>Harper &amp; Row</Publisher>
                                <Cost currency="USD">2.95</Cost>
                            </Book>
                            </BookCatalogue>
                            </stream>""").getBytes();
            out.write(buf);
            out.flush();
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.write(userInput.getBytes());
            }
        }
    }
}
