package shared;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class XMPPReader {

    private DocumentSplittingInputStream inputStream;
    private boolean isClientReader;


    public XMPPReader(DocumentSplittingInputStream inputStream, boolean isClient) {
        this.inputStream = inputStream;

        //FIXME consider better way to handle the difference between a client streamer and server streamer, possibly make abstract class
        isClientReader = isClient;
    }

    //TODO: error handle XMLStreamException and IOException
    public void read() throws XMLStreamException, IOException {
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

            //FIXME: this is temporarily here to test if the client can an "open xmpp stream" message from the server and if a server can write back
            if (receivedStanza == StanzaType.STREAM_OPEN) {
                return;
            }
            System.out.println(receivedStanza);
        }
    }
}
