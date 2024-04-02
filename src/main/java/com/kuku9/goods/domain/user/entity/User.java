package com.kuku9.goods.domain.user.entity;

import com.kuku9.goods.domain.user.dto.request.UserSignupRequest;
import com.kuku9.goods.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;


@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user SET deleted_at=CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment("사용자가 로그인할때 쓸 id명, 이메일")
    @Email
    private String username;

    @Column(nullable = false)
    @Comment("사용자 실명")
    private String realName;

    @Column(nullable = false)
    @Comment("비밀번호")
    private String password;


    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    @Comment("유저 권한")
    private UserRoleEnum role = UserRoleEnum.USER;


    public User(
        UserSignupRequest request,
        String encodedPassword
    ) {
        this.username = request.getUsername();
        this.realName = request.getRealName();
        this.password = encodedPassword;

    }

    public static User from(
        UserSignupRequest request,
        String encodedPassword

    ) {
        return new User(request, encodedPassword);
    }

    public void modifyPassword(String encodedNewPassword) {
        this.password = encodedNewPassword;
    }

    public void updateRole(UserRoleEnum userRoleEnum) {
        this.role = userRoleEnum;
    }
}
