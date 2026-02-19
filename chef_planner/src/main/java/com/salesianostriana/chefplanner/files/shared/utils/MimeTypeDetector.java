package com.salesianostriana.chefplanner.files.shared.utils;

import org.springframework.core.io.Resource;

public interface MimeTypeDetector {
    String getMimeType(Resource resource);
}
