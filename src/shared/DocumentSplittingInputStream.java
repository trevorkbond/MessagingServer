package shared;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

public class DocumentSplittingInputStream extends BufferedInputStream {
    private static final int BUF_SIZE = 8192;
    private final byte[] streamCloseTag = HexFormat.ofDelimiter(":").parseHex("3c:2f:73:74:72:65:61:6d:3e");

    public DocumentSplittingInputStream(InputStream in) {
        super(in);
    }

    public ByteArrayInputStream readUntilStreamClosed() throws IOException, XMLStreamException {
        byte[] readBuf = new byte[BUF_SIZE];
        int bytesRead = 0;
        int totalBytesRead = 0;
        System.out.println("before while");
        while ((bytesRead = read(readBuf, totalBytesRead, BUF_SIZE - totalBytesRead)) != -1) {
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
        System.out.println(Arrays.toString(buf));
        System.out.println(Arrays.toString(trimmedBytes));
        return trimmedBytes;
    }

    private boolean closeTagInBuf(byte[] buf) {
        String data = new String(buf, StandardCharsets.UTF_8);
        return data.contains("</stream>");
    }

}
