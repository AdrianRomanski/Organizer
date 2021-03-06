package adrianromanski.datamodel;

import java.time.LocalDate;

public class Item {

    private String shortDescription;
    private String details;
    private LocalDate deadline;
    private String isComplete;


    public Item(String shortDescription, String details, LocalDate deadline) {
        this.shortDescription = shortDescription;
        this.details = details;
        this.deadline = deadline;
    }

    public Item(String shortDescription, String details, LocalDate deadline, String isComplete) {
        this.shortDescription = shortDescription;
        this.details = details;
        this.deadline = deadline;
        this.isComplete = isComplete;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(String isComplete) {
        this.isComplete = isComplete;
    }
}

