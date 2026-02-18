package com.salesianostriana.chefplanner.files.shared.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractFileMetadata implements FileMetadata {

    protected String id;
    protected String filename;
    protected String URL;
    protected String deleteId;
    protected String deleteURL;

}
