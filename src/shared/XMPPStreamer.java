package shared;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class XMPPStreamer {

    private DocumentSplittingInputStream inputStream;


    public XMPPStreamer(DocumentSplittingInputStream inputStream) {
        this.inputStream = inputStream;
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
        }
    }
}
