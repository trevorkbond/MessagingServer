package shared;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
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

    public StanzaType parse() throws XMLStreamException {
        StanzaType receivedStanza = null;
        while (reader.hasNext()) {
            printEventType();
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT)
                if (reader.getName().toString().equals("stream")) {
                    return StanzaType.STREAM_OPEN;
                } else {
                    receivedStanza = getStanzaType();
                }
            reader.next();
        }
        return receivedStanza;
    }

    private StanzaType getStanzaType() {
        return switch (reader.getName().toString()) {
            case "message" -> StanzaType.MESSAGE;
            case "presence" -> StanzaType.PRESENCE;
            case "stream" -> StanzaType.STREAM_CLOSE;
            default -> StanzaType.UNKNOWN;
        };
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
