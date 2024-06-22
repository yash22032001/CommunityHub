package com.communityhub.community.hub_app.mapper;

import com.communityhub.community.hub_app.dto.PostRequest;
import com.communityhub.community.hub_app.dto.PostResponse;
import com.communityhub.community.hub_app.model.Post;
import com.communityhub.community.hub_app.model.Subreddit;
import com.communityhub.community.hub_app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "description", source = "postRequest.description")
    Post map(PostRequest postRequest, Subreddit subreddit, User user);//Unknown

    @Mapping(target = "subredditId", source = "subreddit.id")
//    @Mapping(target = "postName", source = "postName")//
//    @Mapping(target = "description", source = "description")//
//    @Mapping(target = "url", source = "url")//
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    PostResponse mapToDto(Post post);
}
