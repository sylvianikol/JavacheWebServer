package javache.http;

import javache.helpers.Pair;

import java.util.List;
import java.util.Map;

public interface HttpRequest {

    Map<String, String> getHeaders();

    List<Pair<String, String>> getBodyParameters();

    String getMethod();

    void setMethod(String method);

    String getRequestUrl();

    void setRequestUrl(String request);

    void addHeader(String header, String value);

    void addBodyParameter(String parameter, String value);

    boolean isResource();
}
