package com.kuku9.goods.domain.event.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Value
public class EventUpdateRequest {

    @NotNull(message = "이벤트를 등록하시려면 제목을 입력하세요.")
    private String title;

    @NotNull(message = "이벤트를 등록하시려면 내용을 입력하세요.")
    private String content;

    @NotNull(message = "이벤트를 등록하시려면 오픈일자를 입력하세요.")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime openAt;

}
