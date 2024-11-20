package com.frank.hosp.dto;

import lombok.Data;

import java.util.Date;

@Data
public class HospitalQuery {
    private String hosName;

    private String hosCode;

    private Long dictCode;

    private String address;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;
}
