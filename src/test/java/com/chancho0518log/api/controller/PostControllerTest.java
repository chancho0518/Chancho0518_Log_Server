package com.chancho0518log.api.controller;

import com.chancho0518log.api.domain.Post;
import com.chancho0518log.api.repository.PostRepository;
import com.chancho0518log.api.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void initialization() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 테스트")
    void postsTest() throws Exception {

        // given
        PostCreate request = PostCreate.builder()
                .title("글제목 호호")
                .content("글 내용입니다. 이히히히히")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 title validation")
    void titleTest() throws Exception {

        // given
        PostCreate request = PostCreate.builder()
                .content("글 내용입니다. 이히히히히")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("제목을 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장")
    void saveTest() throws Exception {

        // given
        PostCreate request = PostCreate.builder()
                .title("글 제목입니다.")
                .content("글 내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("글 제목입니다.", post.getTitle());
        assertEquals("글 내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 단건 조회")
    void getOnePost() throws Exception {

        // given
        Post post = Post.builder()
                .title("1번 글 제목")
                .content("1번 글 내용입니다.")
                .build();

        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1번 글 제목"))
                .andExpect(jsonPath("$.content").value("1번 글 내용입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("Service 정책에 따른 글 단건 조회")
    void servicePolicy() throws Exception {

        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("1번 글 내용입니다.")
                .build();

        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("1번 글 내용입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 리스트 1page 조회 / 페이징 처리")
    void getList() throws Exception {

        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title(i + "번 글 제목입니다.")
                        .content(i + "번 글 내용입니다.")
                        .build()).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("30번 글 제목입니다."))
                .andExpect(jsonPath("$[0].content").value("30번 글 내용입니다."))
                .andExpect(jsonPath("$[4].id").value(26))
                .andExpect(jsonPath("$[4].title").value("26번 글 제목입니다."))
                .andExpect(jsonPath("$[4].content").value("26번 글 내용입니다."))
                .andDo(print());
    }
}