package com.chancho0518log.api.service;

import com.chancho0518log.api.domain.Post;
import com.chancho0518log.api.repository.PostRepository;
import com.chancho0518log.api.request.PostCreate;
import com.chancho0518log.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성 Service")
    void PostServiceTest() {

        // given
        PostCreate postCreate = PostCreate.builder()
                .title("글 제목입니다.")
                .content("글 내용입니다.")
                .build();

        // when
        postService.postContent(postCreate);

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("글 제목입니다.", post.getTitle());
        assertEquals("글 내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 단건 조회")
    void getOnePost() {

        // given
        Post requestPost = Post.builder()
                .title("글 제목입니다.")
                .content("글 내용입니다.")
                .build();

        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("글 제목입니다.", response.getTitle());
        assertEquals("글 내용입니다.", response.getContent());
    }

    @Test
    @DisplayName("글 리스트 조회")
    void getList() {

        // given
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("글 제목입니다.111")
                        .content("글 내용입니다.111")
                        .build(),
                Post.builder()
                        .title("글 제목입니다.222")
                        .content("글 내용입니다.222")
                        .build()
        ));

        // when
        List<PostResponse> postsResponse = postService.getList();

        // then
        assertEquals(2L, postsResponse.size());
    }
}