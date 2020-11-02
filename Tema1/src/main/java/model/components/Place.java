package model.components;

public class Place {
    java.lang.String name;
    Integer nrOfTokens;

    public Place() {
    }

    public Place(java.lang.String name, Integer nrOfTokens) {
        this.name = name;
        this.nrOfTokens = nrOfTokens;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public Integer getNrOfTokens() {
        return nrOfTokens;
    }

    public void setNrOfTokens(Integer nrOfTokens) {
        this.nrOfTokens = nrOfTokens;
    }
}
