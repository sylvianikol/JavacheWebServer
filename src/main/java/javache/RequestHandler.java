package javache;

import javache.enums.HttpStatus;
import javache.http.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import static javache.constants.WebConstants.*;

public class RequestHandler {

    private HttpContext httpContext;

    public RequestHandler() {
    }

    public byte[] handleRequest(String requestContent) throws IOException, URISyntaxException {

        HttpRequest httpRequest = new HttpRequestImpl(requestContent);

        this.httpContext = new HttpContextImpl(httpRequest);

        return this.httpContext.getHttpResponse().getBytes();
    }

    private byte[] generateDemoResponse() {

        this.httpContext.getHttpResponse().setStatusCode(HttpStatus.OK);

        byte[] content = ("Hello from " + SERVER_NAME + " v. " + SERVER_VERSION).getBytes();

        this.httpContext.getHttpResponse().addHeader("Date", LocalDateTime.now().toString());
        this.httpContext.getHttpResponse().addHeader("Server", SERVER_NAME + "/" + SERVER_VERSION);
        this.httpContext.getHttpResponse().addHeader("Content-Length", String.valueOf(content.length));
        this.httpContext.getHttpResponse().addHeader("Content-Type", "text/html");

        this.httpContext.getHttpResponse().setContent(("Hello from " + SERVER_NAME + " v. " + SERVER_VERSION).getBytes());

        return this.httpContext.getHttpResponse().getBytes();
    }
}
