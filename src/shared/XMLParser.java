package shared;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;

public class XMLParser {
    private final XMLStreamReader reader;

    public XMLParser(ByteArrayInputStream in) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        reader = factory.createXMLStreamReader(in);
    }

    public void parse() throws XMLStreamException {
        while (reader.hasNext()) {
            printEventType();
            reader.next();
        }
    }

    private void printEventType() {
        System.out.println(getEventTypeString(reader.getEventType()));
    }

    public static String getEventTypeString(int eventType) {
        return switch (eventType) {
            case XMLEvent.START_ELEMENT -> "START_ELEMENT";
            case XMLEvent.END_ELEMENT -> "END_ELEMENT";
            case XMLEvent.PROCESSING_INSTRUCTION -> "PROCESSING_INSTRUCTION";
            case XMLEvent.CHARACTERS -> "CHARACTERS";
            case XMLEvent.COMMENT -> "COMMENT";
            case XMLEvent.START_DOCUMENT -> "START_DOCUMENT";
            case XMLEvent.END_DOCUMENT -> "END_DOCUMENT";
            case XMLEvent.ENTITY_REFERENCE -> "ENTITY_REFERENCE";
            case XMLEvent.ATTRIBUTE -> "ATTRIBUTE";
            case XMLEvent.DTD -> "DTD";
            case XMLEvent.CDATA -> "CDATA";
            case XMLEvent.SPACE -> "SPACE";
            default -> "UNKNOWN_EVENT_TYPE , " + eventType;
        };
    }

}
