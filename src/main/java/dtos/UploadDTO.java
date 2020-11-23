package dtos;

import java.io.Serializable;

public class UploadDTO implements Serializable {
    private long id;
    private String filepath;
    private String filename;

    public UploadDTO(long id, String filepath, String filename) {
        this.id = id;
        this.filepath = filepath;
        this.filename = filename;
    }

    public UploadDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
