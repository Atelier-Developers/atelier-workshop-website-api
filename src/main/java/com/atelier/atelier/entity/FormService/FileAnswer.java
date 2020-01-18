package com.atelier.atelier.entity.FormService;

import javax.persistence.*;
import java.io.File;

@Entity
@Table
public class FileAnswer implements AnswerData{

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String fileName;

    @Column
    private String fileType;

    @Lob
    private byte[] data;

    public FileAnswer() {
    }

    public FileAnswer(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
