package APIChatGPT.model;

import java.io.Serializable;

public class OpenAIInput implements Serializable {
    private String prompt;

    public OpenAIInput() {
    }

    public OpenAIInput(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}