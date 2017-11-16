package de.malkusch.wp2ebookbot.chatbot.model;

import de.malkusch.wp2ebookbot.shared.infrastructure.event.Event;

public final class PublicationPermitted implements Event {

    public final String topCommentId;
    public final String permissionId;

    PublicationPermitted(Permission answer) {
        topCommentId = answer.topCommentId.toString();
        permissionId = answer.permissionId.toString();
    }

}
