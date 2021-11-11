package io.temp.file.storage.store.model;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class LoadFile {
    private String fileName;
    private String fileType;
    private String fileSize;
    private int downloadCount = 0;
    private Date expirationDate;
    private byte[] file;
}
