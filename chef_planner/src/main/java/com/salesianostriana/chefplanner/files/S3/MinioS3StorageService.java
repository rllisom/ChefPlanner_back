package com.salesianostriana.chefplanner.files.S3;

import com.salesianostriana.chefplanner.files.shared.exception.StorageException;
import com.salesianostriana.chefplanner.files.shared.model.FileMetadata;
import com.salesianostriana.chefplanner.files.storage.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
public class MinioS3StorageService implements StorageService {

    private final S3Client s3;

    @Value("${storage.s3.bucket}")
    private String bucket;

    public MinioS3StorageService(S3Client s3) {
        this.s3 = s3;
    }

    @PostConstruct
    @Override
    public void init() {
        try {
            boolean exists = s3.listBuckets().buckets().stream()
                    .anyMatch(b -> b.name().equals(bucket));
            if (!exists) {
                s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            }
        } catch (S3Exception e) {
            throw new StorageException("No se pudo inicializar el bucket de MinIO: " + bucket, e);
        }
    }

    @Override
    public FileMetadata store(MultipartFile file) {
        // Implementación de subida a S3...
        return null; // Aquí iría tu lógica de S3FileMetadataImpl
    }

    @Override
    public Resource loadAsResource(String id) {
        // Implementación de carga desde S3...
        return null;
    }

    @Override
    public void deleteFile(String filename) {
        // Implementación de borrado en S3...
    }
}