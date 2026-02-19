package com.salesianostriana.chefplanner.files.shared.utils;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public interface MimeTypeDetector {
    String getMimeType(Resource resource);
}
