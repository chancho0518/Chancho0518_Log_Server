package com.chancho0518log.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostEdit {

    @NotBlank(message = "제목을 입력하세요.")
    private final String title;

    @NotBlank(message = "내용을 입력하세요.")
    private final String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
