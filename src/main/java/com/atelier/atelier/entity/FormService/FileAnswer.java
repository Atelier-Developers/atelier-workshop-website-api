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
    private byte[] data;

}
