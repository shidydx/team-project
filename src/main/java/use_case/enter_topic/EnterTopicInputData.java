package use_case.enter_topic;

public class EnterTopicInputData {
    private final String topic;

    public EnterTopicInputData(String topic){
        this.topic = topic;
    }
    public String getTopic(){
        return topic;
    }
}
