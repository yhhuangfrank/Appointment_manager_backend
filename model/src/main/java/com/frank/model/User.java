package com.frank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_info", schema = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 20)
    @Column(name = "nick_name", length = 20)
    private String nickName;

    @Size(max = 20)
    @Column(name = "name", length = 20)
    private String name;

    @Size(max = 100)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 100)
    @Column(name = "auth_no", length = 100)
    private String authNo;

    @Column(name = "auth_status")
    private Integer authStatus;

    @Column(name = "create_time", nullable = false, insertable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant createTime;

    @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant updateTime;

}