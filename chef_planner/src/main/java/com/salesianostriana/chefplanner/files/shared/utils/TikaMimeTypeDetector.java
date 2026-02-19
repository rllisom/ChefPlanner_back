package com.salesianostriana.chefplanner.files.shared.utils;

import com.salesianostriana.chefplanner.files.shared.exception.StorageException;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLConnection;

@Component
public class TikaMimeTypeDetector implements MimeTypeDetector{

    private Tika tika;

    public TikaMimeTypeDetector(){
        tika = new Tika();
    }

    public String getMimeType(Resource resource) {
        try {
            if (resource.isFile()) {
                return tika.detect(resource.getFile());
            }

            String filename = resource.getFilename();
            if (StringUtils.hasText(filename)) {
                String guessed = URLConnection.guessContentTypeFromName(filename);
                if (StringUtils.hasText(guessed)) return guessed;
            }

            return "application/octet-stream";
        }catch (IOException ex) {
            throw new StorageException("Error trying to get the MIME type", ex);
        }
    }
}
