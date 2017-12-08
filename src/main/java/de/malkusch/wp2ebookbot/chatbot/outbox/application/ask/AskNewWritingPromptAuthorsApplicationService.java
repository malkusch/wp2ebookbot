package de.malkusch.wp2ebookbot.chatbot.outbox.application.ask;

import java.io.IOException;

import org.springframework.stereotype.Service;

import de.malkusch.wp2ebookbot.chatbot.outbox.model.AskPermissionService;

@Service
public final class AskNewWritingPromptAuthorsApplicationService {

    private final AskPermissionService askService;

    AskNewWritingPromptAuthorsApplicationService(AskPermissionService findNewWritingPromptsService) {
        this.askService = findNewWritingPromptsService;
    }

    public void askNewWritingPromptAuthorsForPermission() throws IOException {
        askService.askNewTopCommentsAuthorForPermission();
    }

}
