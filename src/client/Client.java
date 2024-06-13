package client;

import shared.DocumentSplittingInputStream;
import shared.XMPPReader;
import shared.XMPPWriter;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.Socket;

public class Client {
    private static volatile boolean isStreamOpen = false;
    public static void main(String[] args) throws IOException, InterruptedException, XMLStreamException {
        try (
            Socket socket = new Socket("localhost", 4444);
            BufferedOutputStream out =
                    new BufferedOutputStream(socket.getOutputStream());
            DocumentSplittingInputStream in =
                    new DocumentSplittingInputStream(socket.getInputStream());
        ) {
            ClientReader reader = new ClientReader(in);
            new Thread(reader).start();
            XMPPWriter writer = new XMPPWriter(out);
            writer.writeBytes("<stream>");
            while (!isStreamOpen) {
                Thread.sleep(1000);
            }
            Thread.sleep(2000);
            writer.writeBytes("<message></message>");
            Thread.sleep(2000);
            writer.writeBytes("<presence></presence>");
            Thread.sleep(2000);
            writer.writeBytes("</stream>");
        }
    }

    private static class ClientReader implements Runnable {

        private DocumentSplittingInputStream inputStream;

        public ClientReader(DocumentSplittingInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            XMPPReader reader = new XMPPReader(inputStream, true);
            try {
                reader.read();
                isStreamOpen = true;
            } catch (XMLStreamException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
