package util;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A (partially) synchronized version of a PrintStream. The HotSpot JVM implementation
 * does this by default, but for safety across systems we must add extra synchronization.
 */
public class SyncronizedPrintStream extends PrintStream {

    /**
     * Creates a synchronized wrapper print stream around the given output stream
     * @param out the stream
     */
    public SyncronizedPrintStream(OutputStream out) {
        super(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void println(String x) {
        super.println(x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void print(String s) {
        super.print(s);
    }

    @Override
    public synchronized PrintStream printf(String format, Object... args) {
        return super.printf(format, args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized PrintStream append(CharSequence csq, int start, int end) {
        return super.append(csq, start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void flush() {
        super.flush();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void close() {
        super.close();
    }
}
