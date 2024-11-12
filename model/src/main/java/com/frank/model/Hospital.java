package com.frank.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Document("Hospital")
public class Hospital implements Serializable {
    @Id
    private String id;

    @Indexed
    private String hosName;

    @Indexed(unique = true)
    private String hosCode;

    private Long dictCode;

    private String level;

    private String address;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;
}
