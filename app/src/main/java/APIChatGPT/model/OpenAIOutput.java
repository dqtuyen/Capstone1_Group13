package APIChatGPT.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OpenAIOutput  implements Serializable {
    private String id;
    private String object;
    private long created;
    private String model;
    private ArrayList<MessageData> choices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ArrayList<MessageData> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<MessageData> choices) {
        this.choices = choices;
    }
}