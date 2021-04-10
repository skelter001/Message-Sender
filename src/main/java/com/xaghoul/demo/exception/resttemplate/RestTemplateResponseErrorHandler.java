package com.xaghoul.demo.exception.resttemplate;

import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == CLIENT_ERROR
                || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @SneakyThrows
    @Override
    public void handleError(ClientHttpResponse httpResponse) {
        switch (httpResponse.getStatusCode().series()) {
            case SERVER_ERROR:
                throw new ServerException();
            case CLIENT_ERROR:
                throw new ClientException();
            default:
                throw new NotFoundException("Trouble finding this site");
        }
    }
}