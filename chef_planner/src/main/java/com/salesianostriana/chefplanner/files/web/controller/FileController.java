package com.salesianostriana.chefplanner.files.web.controller;

import com.salesianostriana.chefplanner.files.shared.utils.MimeTypeDetector;
import com.salesianostriana.chefplanner.files.storage.StorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Endpoints que maneja la subida y muestra de ficheros")
public class FileController {

    private final StorageService storageService;
    private final MimeTypeDetector mimeTypeDetector;


    @GetMapping("/files/{id:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Resource resource = storageService.loadAsResource(filename);
        String mimeType = mimeTypeDetector.getMimeType(resource);

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType);

        try{
            long len = resource.contentLength();
            if (len >= 0) {
                builder.header(HttpHeaders.CONTENT_LENGTH, Long.toString(len));
            }
        }catch (Exception e){
            throw new IOException(e.toString());
        }

        return builder.body(resource);
    }
}
