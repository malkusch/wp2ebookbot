package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import de.malkusch.wp2ebookbot.shared.infrastructure.event.Event;

public final class PublicationPermitted implements Event {

    public final String articleId;
    public final String topCommentId;
    public final String permissionId;

    PublicationPermitted(Permission answer) {
        articleId = answer.topCommentId.articleId.toString();
        topCommentId = answer.topCommentId.toString();
        permissionId = answer.permissionId.toString();
    }

}
