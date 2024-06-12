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
            parser.parse();
        }
    }
}
