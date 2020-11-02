package model.components;

public class Transition {
    private String name;
    private Integer duration;
    private Integer minDuration;
    private Integer maxDuration;
    private boolean timed;


    public Transition() {
    }

    public Transition(String name, Integer duration, boolean timed) {
        this.name = name;
        this.duration = duration;
        this.timed = timed;
    }

    public Transition(String name, Integer minDuration, Integer maxDuration, boolean timed) {
        this.name = name;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.timed = timed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public boolean isTimed() {
        return timed;
    }

    public void setTimed(boolean timed) {
        this.timed = timed;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    @Override
    public String toString() {
        return duration != null ? "Transition " + name + " with duration " + duration : "Transition " + name + " with duration between " + minDuration + " and " + maxDuration;
    }
}
