package com.frank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "hospital_setting", schema = "mytest")
public class HospitalSetting implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "hos_name", length = 100)
    private String hosName;

    @Size(max = 30)
    @Column(name = "hos_code", length = 30)
    private String hosCode;

    @NotNull
    @Column(name = "status", nullable = false, insertable = false)
    private Integer status;

    @NotNull
    @Column(name = "create_time", nullable = false, insertable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @NotNull
    @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @NotNull
    @Column(name = "is_deleted", nullable = false, insertable = false)
    private Integer isDeleted;

}