package com.frank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("Schedule")
public class Schedule {
    @Id
    private String id;

    @Indexed
    private String hosCode;

    @Indexed
    private String hosScheduleId;

    private String docName;

    private Integer maxCount;

    private Integer availableCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;

    private Integer workTime;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;

    private Integer status;
}
