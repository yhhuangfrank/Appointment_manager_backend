package com.frank.hosp.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleQuery {
    private int page;

    private int limit;

    private String hosCode;

    private String hosScheduleId;

    private String docName;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;

    private Integer status;
}
