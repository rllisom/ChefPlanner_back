package com.salesianostriana.chefplanner.files.storage;

import com.salesianostriana.chefplanner.files.shared.model.FileMetadata;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();
    FileMetadata store(MultipartFile file);
    Resource loadAsResource(String id);
    void deleteFile(String filename);
}
