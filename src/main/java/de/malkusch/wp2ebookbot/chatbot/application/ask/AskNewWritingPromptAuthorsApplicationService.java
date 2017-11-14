package de.malkusch.wp2ebookbot.chatbot.application.ask;

import java.io.IOException;

import de.malkusch.wp2ebookbot.chatbot.model.AskNewWritingPromptsService;

public final class AskNewWritingPromptAuthorsApplicationService {

    private final AskNewWritingPromptsService askService;

    AskNewWritingPromptAuthorsApplicationService(AskNewWritingPromptsService findNewWritingPromptsService) {
        this.askService = findNewWritingPromptsService;
    }

    public void askNewWritingPromptAuthorsForPermission() throws IOException {
        askService.askNewWritingPromptsAuthorsForPermission();
    }

}
