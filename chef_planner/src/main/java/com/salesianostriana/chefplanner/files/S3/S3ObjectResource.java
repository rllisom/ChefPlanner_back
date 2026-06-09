package com.salesianostriana.chefplanner.files.S3;

import org.springframework.core.io.AbstractResource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Supplier;

public class S3ObjectResource extends AbstractResource {

    private final Supplier<InputStream> inputStreamSupplier;
    private final String filename;
    private final long contentLength;
    private final String contentType;

    public S3ObjectResource(
            Supplier<InputStream> inputStreamSupplier,
            String filename,
            long contentLength,
            String contentType
    ) {
        this.inputStreamSupplier = Objects.requireNonNull(inputStreamSupplier);
        this.filename = filename;
        this.contentLength = contentLength;
        this.contentType = contentType;
    }

    @Override
    public String getDescription() {
        return "S3ObjectResource(filename=" + filename + ")";
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inputStreamSupplier.get();
    }

    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public long contentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }
}