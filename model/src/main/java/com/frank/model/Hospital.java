package com.frank.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Hospital {

    @Indexed
    private String hosName;

    @Indexed(unique = true)
    private String hosCode;

    private String hosType;

    private String address;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;
}
