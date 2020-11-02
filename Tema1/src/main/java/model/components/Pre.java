package model.components;

public class Pre extends Link {
    private String startPlaceName;
    private String destinationTransitionName;


    public Pre(String startPlaceName, String destinationTransitionName, Integer cost) {
        super(cost);
        this.startPlaceName = startPlaceName;
        this.destinationTransitionName = destinationTransitionName;
    }

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public void setStartPlaceName(String startPlaceName) {
        this.startPlaceName = startPlaceName;
    }

    public String getDestinationTransitionName() {
        return destinationTransitionName;
    }

    public void setDestinationTransitionName(String destinationTransitionName) {
        this.destinationTransitionName = destinationTransitionName;
    }

    @Override
    public String toString() {
        return "From " + startPlaceName + " to " + destinationTransitionName + " with cost " + getCost();
    }
}
