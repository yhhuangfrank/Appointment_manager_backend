package com.frank.hosp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ScheduleDetail {

    private String hosName;

    private String docName;

    private Integer maxCount;

    private Integer availableCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;

    private String dayOfWeek;
}
