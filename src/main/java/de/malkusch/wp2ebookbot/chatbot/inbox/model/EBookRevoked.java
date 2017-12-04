package de.malkusch.wp2ebookbot.chatbot.inbox.model;

import de.malkusch.wp2ebookbot.shared.infrastructure.event.Event;

public final class EBookRevoked implements Event {

    public final String topCommentId;

    EBookRevoked(Revocation revocation) {
        topCommentId = revocation.topCommentId.toString();
    }

}
