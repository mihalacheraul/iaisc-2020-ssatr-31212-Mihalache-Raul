package model;

import model.components.Place;
import model.components.Post;
import model.components.Pre;
import model.components.Transition;

import java.util.Set;

public class PetriNet {
    private Set<Place> places;
    private Set<Transition> transitions;
    private Set<Pre> pres;
    private Set<Post> posts;

    public PetriNet(Set<Place> strings, Set<Transition> transitions, Set<Pre> pres, Set<Post> posts) {
        this.places = strings;
        this.transitions = transitions;
        this.pres = pres;
        this.posts = posts;
    }

    public PetriNet() {
    }

    public Set<Place> getPlaces() {
        return places;
    }

    public void setPlaces(Set<Place> strings) {
        this.places = strings;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(Set<Transition> transitions) {
        this.transitions = transitions;
    }

    public Set<Pre> getPres() {
        return pres;
    }

    public void setPres(Set<Pre> pres) {
        this.pres = pres;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
