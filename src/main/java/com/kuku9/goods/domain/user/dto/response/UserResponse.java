package com.kuku9.goods.domain.user.dto.response;

import com.kuku9.goods.domain.user.entity.User;
import com.kuku9.goods.domain.user.entity.UserRoleEnum;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.kuku9.goods.domain.user.entity.User}
 */
public record UserResponse(String realName, UserRoleEnum role, LocalDateTime createdAt) {
    public static UserResponse from(User findUser) {
        return new UserResponse(findUser.getRealName(), findUser.getRole(), findUser.getCreatedAt());
    }
}