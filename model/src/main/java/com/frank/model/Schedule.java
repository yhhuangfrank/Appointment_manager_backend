package com.frank.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("Schedule")
public class Schedule {

    @Indexed
    private String hosCode;

    @Indexed
    private String hosScheduleId;

    private String docName;

    private Integer maxCount;

    private Integer availableCount;

    private String workDate;

    private String workTime;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;

    private Integer status;
}
