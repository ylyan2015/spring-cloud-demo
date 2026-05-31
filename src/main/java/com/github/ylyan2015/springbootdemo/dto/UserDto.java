package com.github.ylyan2015.springbootdemo.dto;

import com.github.ylyan2015.springbootdemo.common.GenderEnum;
import com.github.ylyan2015.springbootdemo.common.UserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "用户DTO")
public class UserDto {

    @Schema(description = "用户ID")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    @Schema(description = "用户名", required = true, example = "admin")
    private String username;

    @Size(min = 6, max = 100, message = "密码长度必须在6-100之间")
    @Schema(description = "密码")
    private String password;

    @Size(max = 50, message = "昵称长度不能超过50")
    @Schema(description = "昵称", example = "管理员")
    private String nickname;

    @Size(max = 20, message = "手机号长度不能超过20")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100")
    @Schema(description = "邮箱", example = "admin@example.com")
    private String email;

    @Schema(description = "性别", example = "MALE")
    private GenderEnum gender;

    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    @Schema(description = "年龄", example = "30")
    private Integer age;

    @Size(max = 255, message = "头像URL长度不能超过255")
    @Schema(description = "头像URL")
    private String avatar;

    @Size(max = 20, message = "身份证号长度不能超过20")
    @Schema(description = "身份证号")
    private String idCard;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态", required = true, example = "ENABLED")
    private UserStatusEnum status;

    @Size(max = 500, message = "地址长度不能超过500")
    @Schema(description = "地址")
    private String address;

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
