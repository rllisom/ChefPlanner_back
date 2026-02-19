package com.salesianostriana.chefplanner.files.storage.local;

import com.salesianostriana.chefplanner.files.shared.model.AbstractFileMetadata;
import com.salesianostriana.chefplanner.files.shared.model.FileMetadata;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class LocalFileMetadataImpl extends AbstractFileMetadata {

    public static FileMetadata of(String filename) {
        return LocalFileMetadataImpl.builder()
                .id(filename)
                .filename(filename)
                .build();
    }

}
