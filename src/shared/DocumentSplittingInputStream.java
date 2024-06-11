package shared;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HexFormat;

public class DocumentSplittingInputStream extends BufferedInputStream {
    private final byte[] streamCloseTag = HexFormat.ofDelimiter(":").parseHex("3c:2f:73:74:72:65:61:6d:3e");
    private int readCount = 0;
    private boolean underlyingInputStreamNotFinished = true;

    public DocumentSplittingInputStream(InputStream in) {
        super(in);
    }

    public void readUntilStreamClosed() throws IOException {
        while (read() != -1) {
            if (closeTagInBuf()) {
                underlyingInputStreamNotFinished = false;
            }
        }
    }

    private boolean closeTagInBuf() {
        for (byte b : buf) {
            boolean found = true;
            for (byte value : streamCloseTag) {
                if (b != value) {
                    found = false;
                    break;
                }
            }
            if (found) return found;
        }
        return false;
    }

    public boolean isUnderlyingInputStreamNotFinished() {
        return underlyingInputStreamNotFinished;
    }
}
