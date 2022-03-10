package json;

public class Proc {

    private String command;
    private String initiator;
    private int participators;

    public Proc(String command, String initiator, int participators) {
        this.command = command;
        this.initiator = initiator;
        this.participators = participators;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public int getParticipators() {
        return participators;
    }

    public void setParticipators(int participators) {
        this.participators = participators;
    }
}
