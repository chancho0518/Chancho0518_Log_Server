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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
    @DisplayName("글 리스트 1page 조회 / 페이징 처리")
    void getList() {

        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                        .mapToObj(i -> Post.builder()
                                .title(i + "번 글 제목입니다.")
                                .content(i + "번 글 내용입니다.")
                                .build()).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0, 5, DESC, "id");

        // when
        List<PostResponse> postsResponse = postService.getList(pageable);

        // then
        assertEquals(5L, postsResponse.size());
        assertEquals("30번 글 제목입니다.", postsResponse.get(0).getTitle());
        assertEquals("26번 글 제목입니다.", postsResponse.get(4).getTitle());
        assertEquals("30번 글 내용입니다.", postsResponse.get(0).getContent());
        assertEquals("26번 글 내용입니다.", postsResponse.get(4).getContent());
    }
}