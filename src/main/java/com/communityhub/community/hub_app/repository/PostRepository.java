package com.communityhub.community.hub_app.repository;

import com.communityhub.community.hub_app.model.Post;
import com.communityhub.community.hub_app.model.Subreddit;
import com.communityhub.community.hub_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}
