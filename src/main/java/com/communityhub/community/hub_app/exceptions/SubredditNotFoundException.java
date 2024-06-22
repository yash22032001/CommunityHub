package com.communityhub.community.hub_app.exceptions;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubredditNotFoundException extends RuntimeException {
    public SubredditNotFoundException(String message) {
        super(message);
        log.info("SubredditNotFoundException "+message);
    }
}