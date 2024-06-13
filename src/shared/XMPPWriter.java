package shared;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class XMPPWriter {

    private BufferedOutputStream outputStream;

    public XMPPWriter(BufferedOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeBytes(String message) throws IOException {
        byte[] buf = (message).getBytes();
        outputStream.write(buf);
        outputStream.flush();
    }
}
