package de.malkusch.wp2ebookbot.chatbot.application.ask;

import java.io.IOException;

import de.malkusch.wp2ebookbot.chatbot.model.AskPermissionService;

public final class AskNewWritingPromptAuthorsApplicationService {

    private final AskPermissionService askService;

    AskNewWritingPromptAuthorsApplicationService(AskPermissionService findNewWritingPromptsService) {
        this.askService = findNewWritingPromptsService;
    }

    public void askNewWritingPromptAuthorsForPermission() throws IOException {
        askService.askNewTopCommentsAuthorForPermission();
    }

}
