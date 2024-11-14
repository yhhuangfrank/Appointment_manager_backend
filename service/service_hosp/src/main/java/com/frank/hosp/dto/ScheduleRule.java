package com.frank.hosp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ScheduleRule {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;

    private String dayOfWeek;

    private Integer docCount;

    private Integer maxCount;

    private Integer availableCount;

    private Integer status;
}
