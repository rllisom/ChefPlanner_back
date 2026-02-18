package com.salesianostriana.chefplanner.files.shared.model;

public interface FileMetadata {

    String getFilename();
    String getURL();
    String getDeleteId();
    String getDeleteURL();
    String getId();
    void setFilename(String filename);
    void setURL(String url);
    void setDeleteId(String deleteId);
    void setDeleteURL(String deleteURL);
    void setId(String id);


}
