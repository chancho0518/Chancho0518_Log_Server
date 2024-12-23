package com.chancho0518log.api.controller;

import com.chancho0518log.api.request.PostCreate;
import com.chancho0518log.api.response.PostResponse;
import com.chancho0518log.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Map post(@RequestBody @Valid PostCreate request) {

        Long postId = postService.postContent(request);

        return Map.of("postId", postId);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {

        return postService.get(postId);
    }
}
