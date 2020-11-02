package model.components;

public class Post extends Link {
    private String destinationPlaceName;
    private String startTransitionName;

    public Post(String destinationPlaceName, String startTransitionName, Integer cost) {
        super(cost);
        this.destinationPlaceName = destinationPlaceName;
        this.startTransitionName = startTransitionName;
    }

    public String getDestinationPlaceName() {
        return destinationPlaceName;
    }

    public void setDestinationPlaceName(String destinationPlaceName) {
        this.destinationPlaceName = destinationPlaceName;
    }

    public String getStartTransitionName() {
        return startTransitionName;
    }

    public void setStartTransitionName(String startTransitionName) {
        this.startTransitionName = startTransitionName;
    }
}
