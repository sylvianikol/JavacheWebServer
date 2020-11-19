package javache.http;

import javache.enums.Folder;
import javache.enums.HttpStatus;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import javax.activation.MimetypesFileTypeMap;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static javache.constants.WebConstants.*;

public class HttpContextImpl implements HttpContext {

    private HttpRequest httpRequest;
    private HttpResponse httpResponse;
    private UriComponents uriBuilder;

    public HttpContextImpl(HttpRequest httpRequest) throws IOException, URISyntaxException {
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

        String url = this.httpRequest.getRequestUrl().equals("/")
                ? INDEX
                : this.httpRequest.getRequestUrl();

        Folder folder = this.httpRequest.isResource() && !url.endsWith(".html")
                ? Folder.ASSETS
                : Folder.PAGES;

        String filePath = url.contains(folder.getFolder())
                ? RESOURCES_PATH + url
                : RESOURCES_PATH + folder.getFolder() + url;

        File file = Paths.get(filePath).toFile();

        if (!file.exists()) {
            filePath = RESOURCES_PATH + Folder.PAGES.getFolder() + NOT_FOUND;
            this.httpResponse.setStatusCode(HttpStatus.NOT_FOUND);
            file = Paths.get(filePath).toFile();
        } else {
            this.httpResponse.setStatusCode(HttpStatus.OK);

        }

        String mimetype = new MimetypesFileTypeMap().getContentType(file);
        uriBuilder = UriComponentsBuilder.fromUriString(filePath).build();

        byte[] contentBytes = Files.readAllBytes(Path.of(uriBuilder.getPath()));

        this.httpResponse.addHeader("Date", LocalDateTime.now().toString());
        this.httpResponse.addHeader("Server", SERVER_NAME + "/" + SERVER_VERSION);
        this.httpResponse.addHeader("Content-Length", String.valueOf(contentBytes.length));
        this.httpResponse.addHeader("Content-Type", mimetype);

        this.httpResponse.setContent(contentBytes);
    }

}
