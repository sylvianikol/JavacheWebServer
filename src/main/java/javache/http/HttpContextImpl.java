package javache.http;

import javache.enums.Folder;
import javache.enums.HttpStatus;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;

import static javache.constants.WebConstants.*;

public class HttpContextImpl implements HttpContext {

    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private UriComponents uriBuilder;

    public HttpContextImpl(HttpRequest httpRequest) throws IOException {
        this.httpRequest = httpRequest;
        this.createHttpResponse();
    }

    @Override
    public HttpRequest getHttpRequest() {
        return this.httpRequest;
    }

    @Override
    public HttpResponse getHttpResponse() {
        return this.httpResponse;
    }

    private void createHttpResponse() throws IOException {
        this.httpResponse = new HttpResponseImpl();

        String url = this.httpRequest.getRequestUrl().endsWith("/")
                ? INDEX
                : this.httpRequest.getRequestUrl();

        Folder folder = this.httpRequest.isResource()
                ? Folder.ASSETS
                : Folder.PAGES;

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(folder + url);

        if (is == null) {
            url = NOT_FOUND;

            this.httpResponse.setStatusCode(HttpStatus.NOT_FOUND);
            this.httpResponse.addHeader("Content-type", "text/html");
            String template = PATH_TEMPLATE + folder.getFolder() + url;
            uriBuilder = UriComponentsBuilder.fromUriString(template).build();

            byte[] htmlBytes = Files.readAllBytes(Path.of(uriBuilder.getPath()));

            this.httpResponse.setContent(htmlBytes);

        } else {
            String resourcePath = Arrays.toString(is.readAllBytes());

            if (resourcePath.contains(Folder.ASSETS.toString())) {
                this.httpResponse.setStatusCode(HttpStatus.OK);
                this.httpResponse.addHeader("Content-Type", "image/*");

                String template = PATH_TEMPLATE + folder.getFolder() + url;
                uriBuilder = UriComponentsBuilder.fromUriString(template).build();

                byte[] imageBytes = Files.readAllBytes(Path.of(uriBuilder.getPath()));

                this.httpResponse.setContent(imageBytes);

            } else if (resourcePath.contains(Folder.PAGES.toString())) {
                this.httpResponse.setStatusCode(HttpStatus.OK);

                String template = PATH_TEMPLATE + folder.getFolder() + url;
                uriBuilder = UriComponentsBuilder.fromUriString(template).build();

                byte[] htmlBytes = Files.readAllBytes(Path.of(uriBuilder.getPath()));

                this.httpResponse.setContent(htmlBytes);
            }
        }

        this.httpResponse.addHeader("Date", LocalDateTime.now().toString());
        this.httpResponse.addHeader("Server", SERVER_NAME + "/" + SERVER_VERSION);
        this.httpResponse.addHeader("Content-Length", String.valueOf(this.httpResponse.getContent().length));

    }

}
