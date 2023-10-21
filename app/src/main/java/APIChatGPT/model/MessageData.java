package APIChatGPT.model;

public class MessageData {
    private String text;
    private int index;
    private String logprobs;
    private String finish_reason;

    private TypeTalking typeTalking;
    private long created;
    private String userName;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLogprobs() {
        return logprobs;
    }

    public void setLogprobs(String logprobs) {
        this.logprobs = logprobs;
    }

    public String getFinish_reason() {
        return finish_reason;
    }

    public void setFinish_reason(String finish_reason) {
        this.finish_reason = finish_reason;
    }

    public TypeTalking getTypeTalking() {
        return typeTalking;
    }

    public void setTypeTalking(TypeTalking typeTalking) {
        this.typeTalking = typeTalking;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}