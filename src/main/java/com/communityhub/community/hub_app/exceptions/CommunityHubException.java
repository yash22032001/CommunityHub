package com.communityhub.community.hub_app.exceptions;

public class CommunityHubException extends RuntimeException {
    public CommunityHubException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public CommunityHubException(String exMessage) {
        super(exMessage);
    }
}
