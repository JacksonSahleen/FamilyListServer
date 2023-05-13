package handler;

import java.io.*;

/**
 * Base class for all the WebAPI handlers
 */
public class Handler {

    /**
     * Reads a string from an InputStream
     *
     * @param is the InputStream to read from
     * @return the string read from the InputStream
     * @throws IOException if an I/O error occurs
     */
    protected String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /**
     * Writes a string to an OutputStream
     *
     * @param str the string to write
     * @param os the OutputStream to write to
     * @throws IOException if an I/O error occurs
     */
    protected void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
