package javache.http;

import javache.enums.HttpStatus;

import java.util.Map;

public interface HttpResponse {

    HttpStatus getStatusCode();

    void setStatusCode(HttpStatus statusCode);

    Map<String, String> getHeaders();

    void addHeader(String header, String value);

    byte[] getContent();

    void setContent(byte[] content);

    byte[] getBytes();
}
