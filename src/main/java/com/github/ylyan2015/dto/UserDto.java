package com.github.ylyan2015.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private Integer age;
    private LocalDateTime createTime;
}
