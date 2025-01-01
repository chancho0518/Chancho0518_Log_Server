package com.chancho0518log.api.service;

import com.chancho0518log.api.domain.Post;
import com.chancho0518log.api.repository.PostRepository;
import com.chancho0518log.api.request.PostCreate;
import com.chancho0518log.api.request.PostEdit;
import com.chancho0518log.api.request.PostSearch;
import com.chancho0518log.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    void getOnePostTest() {

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
    void getListTest() {

        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                        .mapToObj(i -> Post.builder()
                                .title(i + "번 글 제목입니다.")
                                .content(i + "번 글 내용입니다.")
                                .build()).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        // when
        List<PostResponse> postsResponse = postService.getList(postSearch);

        // then
        assertEquals(10L, postsResponse.size());
        assertEquals("30번 글 제목입니다.", postsResponse.get(0).getTitle());
        assertEquals("26번 글 제목입니다.", postsResponse.get(4).getTitle());
        assertEquals("30번 글 내용입니다.", postsResponse.get(0).getContent());
        assertEquals("26번 글 내용입니다.", postsResponse.get(4).getContent());
    }

    @Test
    @DisplayName("글 제목 edit")
    void titleEditTest() {

        // given
        Post post = Post.builder()
                .title("글 제목 입니다.")
                .content("글 내용 입니다.")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("수정된 글 제목")
                .content("글 내용 입니다.")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post editedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. postId = " + post.getId()));

        assertEquals("수정된 글 제목", editedPost.getTitle());
    }

    @Test
    @DisplayName("글 내용 edit")
    void contentEditTest() {

        // given
        Post post = Post.builder()
                .title("글 제목 입니다.")
                .content("글 내용 입니다.")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("수정된 글 내용")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post editedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. postId = " + post.getId()));

        assertEquals("수정된 글 내용", editedPost.getContent());
    }
}