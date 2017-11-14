package de.malkusch.wp2ebookbot.chatbot.model;

import de.malkusch.wp2ebookbot.shared.infrastructure.event.Event;

public final class PublicationPermitted implements Event {

    public final String commentId;

    PublicationPermitted(CommentId commentId) {
        this.commentId = commentId.toString();
    }

}
