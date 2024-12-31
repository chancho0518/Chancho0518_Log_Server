package com.chancho0518log.api.repository;

import com.chancho0518log.api.domain.Post;
import com.chancho0518log.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
