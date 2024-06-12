package client;

import shared.DocumentSplittingInputStream;
import shared.XMPPStreamer;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException, XMLStreamException {
        try (
            Socket socket = new Socket("localhost", 4444);
            BufferedOutputStream out =
                    new BufferedOutputStream(socket.getOutputStream());
            DocumentSplittingInputStream in =
                    new DocumentSplittingInputStream(socket.getInputStream());
        ) {
            XMPPStreamer streamer = new XMPPStreamer(in, out, true);
            sendMessage(out, "<stream>");
//            streamer.stream();
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
        Thread.sleep(4000);
    }
}
