package com.salesianostriana.chefplanner.files.S3;

import com.salesianostriana.chefplanner.files.shared.exception.StorageException;
import com.salesianostriana.chefplanner.files.shared.model.FileMetadata;
import com.salesianostriana.chefplanner.files.storage.StorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import com.salesianostriana.chefplanner.files.S3.S3ObjectResource;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

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
            throw new StorageException("No se pudo inicializar el bucket de S3: " + bucket, e);
        }
    }

    @Override
    public FileMetadata store(MultipartFile file) {
        return store(file, ""); // Por defecto a la raíz
    }

    @Override
    public FileMetadata store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("El fichero está vacío");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String key = buildObjectKey(originalFilename, folder);

        try {
            String contentType = file.getContentType();
            if (!StringUtils.hasText(contentType)) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            // Lógica de subida real a S3/MinIO
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType)
                    .metadata(Map.of("original-filename", originalFilename))
                    .build();

            s3.putObject(putReq, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return S3FileMetadataImpl.of(key, originalFilename, null);

        } catch (IOException | S3Exception e) {
            throw new StorageException("Error al guardar en S3: " + originalFilename, e);
        }
    }

    @Override
    public Resource loadAsResource(String id) {
        try {
            var head = s3.headObject(HeadObjectRequest.builder().bucket(bucket).key(id).build());
            Supplier supplier = () -> s3.getObject(GetObjectRequest.builder().bucket(bucket).key(id).build());
            return new S3ObjectResource(supplier, id, head.contentLength(), head.contentType());
        } catch (Exception e) {
            throw new StorageException("No se pudo leer el fichero de S3: " + id, e);
        }
    }

    @Override
    public void deleteFile(String key) {
        try {
            s3.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
        } catch (S3Exception e) {
            throw new StorageException("No se pudo eliminar el fichero de S3: " + key, e);
        }
    }

    private String buildObjectKey(String originalFilename, String folder) {
        String ext = StringUtils.getFilenameExtension(originalFilename);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String filename = (StringUtils.hasText(ext)) ? (uuid + "." + ext) : uuid;

        return (StringUtils.hasText(folder)) ? (folder + "/" + filename) : filename;
    }
}