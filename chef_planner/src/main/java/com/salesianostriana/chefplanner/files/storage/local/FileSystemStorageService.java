package com.salesianostriana.chefplanner.files.storage.local;

import com.salesianostriana.chefplanner.files.shared.exception.StorageException;
import com.salesianostriana.chefplanner.files.shared.model.FileMetadata;
import com.salesianostriana.chefplanner.files.storage.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@ConditionalOnProperty(name = "storage.type",havingValue = "local", matchIfMissing = true)
public class FileSystemStorageService implements StorageService {
    @Value("${storage.location}")
    private String storageLocation;

    private Path rootLocation;

    @PostConstruct
    @Override
    public void init() {
        rootLocation = Paths.get(storageLocation);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("No se ha podido inicializar la localización de ficheros", e);
        }
    }

    @Override
    public FileMetadata store(MultipartFile file) {
        return null;
    }

    private Path load(String filename){
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String id) {
        try{
            Path file = load(id);
            UrlResource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()){
              return resource;
            }else{
                throw new StorageException("No se puede leer el fichero: " + id);
            }
        }catch (MalformedURLException ex){
            throw new StorageException("No se puede leer el fichero: " + id);
        }
    }

    @Override
    public void deleteFile(String filename) {

    }
}
