package com.communityhub.community.hub_app.service;

import com.communityhub.community.hub_app.dto.SubredditDto;
import com.communityhub.community.hub_app.mapper.SubredditMapper;
import com.communityhub.community.hub_app.model.Subreddit;
import com.communityhub.community.hub_app.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    private final AuthService authService;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        log.info("Subreddit Request "+subredditDto);
        log.info("Created on "+ Instant.now());
        Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto,authService.getCurrentUser()));
        log.info("Subreddit saved "+ save);
        return subredditMapper.mapSubredditToDto(save);

    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subreddit not found with id: " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
    }
}
