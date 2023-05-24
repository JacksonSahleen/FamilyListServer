package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.time.ZonedDateTime;

/**
 * Base class for all the WebAPI handlers
 */
public class Handler {

    /**
     * Gson object for handling JSON serialization and deserialization, includes a TypeAdapter for ZonedDateTime
     */
    protected final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime .class, new TypeAdapter<ZonedDateTime>() {
                @Override
                public void write(JsonWriter out, ZonedDateTime value) throws IOException {
                    out.value(value.toString());
                }

                @Override
                public ZonedDateTime read(JsonReader in) throws IOException {
                    return ZonedDateTime.parse(in.nextString());
                }
            })
            .enableComplexMapKeySerialization()
            .create();

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
