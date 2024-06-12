package shared;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class XMPPStreamer {

    private DocumentSplittingInputStream inputStream;
    private BufferedOutputStream outputStream;
    private boolean isClientStreamer;


    public XMPPStreamer(DocumentSplittingInputStream inputStream, BufferedOutputStream outputStream, boolean isClient) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        //FIXME consider better way to handle the difference between a client streamer and server streamer, possibly make abstract class
        isClientStreamer = isClient;
    }

    //TODO: error handle XMLStreamException and IOException
    public void stream() throws XMLStreamException, IOException {
        while (!inputStream.isStreamClosed()) {
            ByteArrayInputStream xmlToProcess = inputStream.readUntilStanzaReceivedOrStreamClosed();
            XMLParser parser = new XMLParser(xmlToProcess);

            // Adding this extra check because XMLStreamReader throws an exception when the XML received is just
            // "</stream>" i.e. when the message being sent is simply a stream close message. My opinion is there
            // is no reason to parse this XML if we already know it is a stream close message, which DSIS knows
            // and indicates by setting isStreamClosed
            if (inputStream.isStreamClosed()) {
                return;
            }
            StanzaType receivedStanza = parser.parse();
            System.out.println(receivedStanza);
//            if (receivedStanza == StanzaType.STREAM_OPEN && !isClientStreamer) {
//                writeBytes("<stream>");
//            } else {
//                break;
//            }
        }
    }

//    private void writeBytes(String message) throws IOException {
//        byte[] buf = (message).getBytes();
//        outputStream.write(buf);
//        outputStream.flush();
//    }
}
