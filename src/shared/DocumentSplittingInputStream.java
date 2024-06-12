package shared;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DocumentSplittingInputStream extends BufferedInputStream {
    private static final int BUF_SIZE = 8192;
    private boolean isStreamClosed = false;

    public DocumentSplittingInputStream(InputStream in) {
        super(in);
    }

    public ByteArrayInputStream readUntilStanzaReceivedOrStreamClosed() throws IOException, XMLStreamException {
        byte[] readBuf = new byte[BUF_SIZE];
        int bytesRead = 0;
        int totalBytesRead = 0;
        while ((bytesRead = read(readBuf, totalBytesRead, BUF_SIZE - totalBytesRead)) > 0) {
            totalBytesRead += bytesRead;
            if (closeTagInBuf(readBuf)) {
                break;
            }
        }
        byte[] trimmed = trimTrailingZeros(readBuf);
        return new ByteArrayInputStream(trimmed);
    }

    private byte[] trimTrailingZeros(byte[] buf) {
        int zeroCount = 0;
        int i = buf.length;
        while (buf[i - 1] == 0) {
            zeroCount++;
            i--;
        }
        byte[] trimmedBytes = new byte[buf.length - zeroCount];
        System.arraycopy(buf, 0, trimmedBytes, 0, buf.length - zeroCount);
        return trimmedBytes;
    }

    private boolean closeTagInBuf(byte[] buf) {
        String data = new String(buf, StandardCharsets.UTF_8);
        if (data.contains("</stream>")) {
            isStreamClosed = true;
        }
        return data.contains("</stream>")
                || data.contains("</message>") || data.contains("</presence>")
                || data.contains("<stream");
    }

    public boolean isStreamClosed() {
        return isStreamClosed;
    }

}
