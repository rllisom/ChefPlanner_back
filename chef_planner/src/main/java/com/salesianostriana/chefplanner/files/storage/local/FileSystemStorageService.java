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
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "local", matchIfMissing = true)
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
        try {
            String filename = store(file.getBytes(), file.getOriginalFilename(), file.getContentType());
            return LocalFileMetadataImpl.of(filename);
        } catch (Exception ex) {
            throw new StorageException("Error al almacenar el fichero: " + file.getOriginalFilename(), ex);
        }
    }

    @Override
    public FileMetadata store(MultipartFile file, String folder) {
        return null;
    }

    private String store(byte[] file, String filename, String contentType) throws Exception {
        String newFilename = StringUtils.cleanPath(filename);

        if (file.length == 0)
            throw new StorageException("El fichero está vacío");

        newFilename = calculateNewFilename(newFilename);

        try (InputStream inputStream = new ByteArrayInputStream(file)) {
            Files.copy(inputStream, rootLocation.resolve(newFilename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException ex) {
            throw new StorageException("Error al almacenar el fichero: " + newFilename, ex);
        }
        return newFilename;
    }

    private String calculateNewFilename(String filename) {
        String newFilename = filename;
        while(Files.exists(rootLocation.resolve(newFilename))) {
            String extension = StringUtils.getFilenameExtension(newFilename);
            String name = newFilename.replace("." + extension, "");
            String suffix = Long.toString(System.currentTimeMillis());
            suffix = suffix.substring(suffix.length()-6);
            newFilename = name + "_" + suffix + "." + extension;
        }
        return newFilename;
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String id) {
        try {
            Path file = load(id);
            UrlResource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("No se puede leer el fichero: " + id);
            }
        } catch (MalformedURLException ex) {
            throw new StorageException("No se puede leer el fichero: " + id, ex);
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            Files.delete(load(filename));
        } catch (IOException e) {
            throw new StorageException("No se pudo eliminar el fichero: " + filename, e);
        }
    }

    // Método para limpiar todo el almacenamiento (útil en tests)
    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(rootLocation);
        } catch (IOException e) {
            throw new StorageException("No se han podido borrar todos los archivos", e);
        }
    }
}