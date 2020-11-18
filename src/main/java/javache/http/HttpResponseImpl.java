package javache.http;

import javache.enums.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseImpl implements HttpResponse {

    private HttpStatus statusCode;
    private Map<String, String> headers;
    private byte[] content;

    public HttpResponseImpl() {
        this.headers = new HashMap<>();
        this.content = new byte[0];
    }

    @Override
    public HttpStatus getStatusCode() {
        return this.statusCode;
    }

    @Override
    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    @Override
    public void addHeader(String header, String value) {
        this.headers.putIfAbsent(header, value);
    }

    @Override
    public byte[] getContent() {
        return this.content;
    }

    @Override
    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public byte[] getBytes() {
        byte[] headersAsBytes = this.getHeadersAsBytes();
        byte[] contentAsBytes = this.getContent();

        byte[] response = new byte[headersAsBytes.length + contentAsBytes.length];

        System.arraycopy(headersAsBytes, 0, response, 0, headersAsBytes.length);
        System.arraycopy(contentAsBytes, 0, response, headersAsBytes.length, contentAsBytes.length);

        return response;
    }

    private byte[] getHeadersAsBytes() {
        StringBuilder result = new StringBuilder();

        result.append(this.statusCode.getStatusPhrase())
                .append(System.lineSeparator());

        this.headers.forEach((key, value) -> result
                .append(key)
                .append(": ")
                .append(value)
                .append(System.lineSeparator()));

        result.append(System.lineSeparator());

        return result.toString().getBytes();
    }
}
