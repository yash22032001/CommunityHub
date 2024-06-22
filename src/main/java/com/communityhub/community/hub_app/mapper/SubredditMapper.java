package com.communityhub.community.hub_app.mapper;

import com.communityhub.community.hub_app.dto.SubredditDto;
import com.communityhub.community.hub_app.model.Post;
import com.communityhub.community.hub_app.model.Subreddit;
import com.communityhub.community.hub_app.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    @Mapping(target = "createdOn",source = "subreddit.createdDate")
    @Mapping(target = "userId",source = "subreddit.user.userId")
    SubredditDto mapSubredditToDto(Subreddit subreddit);


    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts != null ? numberOfPosts.size() : 0;
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(source = "user", target = "user")
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto,User user);
}
