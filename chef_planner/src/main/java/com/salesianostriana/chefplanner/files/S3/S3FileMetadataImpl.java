package com.salesianostriana.chefplanner.files.S3;


import com.salesianostriana.chefplanner.files.shared.model.AbstractFileMetadata;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@SuperBuilder
public class S3FileMetadataImpl extends AbstractFileMetadata {
    public static S3FileMetadataImpl of(String key, String filename, String url) {
        return S3FileMetadataImpl.builder()
                .id(key)
                .filename(filename)
                .URL(url)
                .build();
    }
}