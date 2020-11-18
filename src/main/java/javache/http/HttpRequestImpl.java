package javache.http;

import javache.helpers.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class HttpRequestImpl implements HttpRequest {

    private List<String> requestList;
    private String method;
    private String requestUrl;
    private Map<String, String> headers;
    List<Pair<String, String>> bodyParameters;

    public HttpRequestImpl(String request) {
        this.setRequestList(request);
        this.setMethod(request);
        this.setHeaders();
        this.setRequestUrl(request);
        this.setBodyParameters();
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    private void setHeaders() {
        this.headers = new LinkedHashMap<>();
        this.requestList.stream()
                .filter(h -> h.contains(": "))
                .map(h -> h.split(": "))
                .forEach(pair -> this.headers.put(pair[0], pair[1]));
    }

    @Override
    public List<Pair<String, String>> getBodyParameters() {
        return this.bodyParameters;
    }

    private void setBodyParameters() {
        this.bodyParameters =  new ArrayList<>();

        String endLine = this.requestList.get(this.requestList.size() - 1);

        if (!endLine.equals("crlf")) {
            Arrays.stream(endLine
                    .replace("crlf", "")
                    .split("&"))
                    .map(param -> param.split("="))
                    .map(pair -> Pair.createPair(pair[0], pair[1]))
                    .forEach(bodyParameters::add);
        }
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(String request) {
        this.method = this.requestList.get(0).split("\\s+")[0];
    }

    @Override
    public String getRequestUrl() {
        return this.requestUrl;
    }

    @Override
    public void setRequestUrl(String input) {
        this.requestUrl = this.requestList.get(0).split("\\s+")[1];
    }

    @Override
    public void addHeader(String header, String value) {
        this.headers.putIfAbsent(header, value);
    }

    @Override
    public void addBodyParameter(String parameter, String value) {
        this.bodyParameters.add(new Pair<>(parameter, value));
    }

    @Override
    public boolean isResource() {
        return this.requestUrl.contains(".");
    }

    private void setRequestList(String input) {
        this.requestList = Arrays.stream(input.split("\r\n"))
                .collect(Collectors.toList());
    }
}
