package simulator;

import loader.PetriNetLoader;
import model.PetriNet;
import model.components.Place;
import model.components.Post;
import model.components.Pre;
import model.components.Transition;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class PetriNetSimulator {
    private static Map<String, Integer> initialMark = new HashMap<>();
    private static Map<String, Integer> currentMark;
    private static PetriNet initialPetriNet = preparePetriNet(PetriNetLoader.loadPetriNet());
    private static File file;

    static {
        try {
            file = new File(PetriNetSimulator.class.getResource("/out/out.txt").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PetriNet petriNet;
        petriNet = preparePetriNet(PetriNetLoader.loadPetriNet());
        for (Place place : petriNet.getPlaces()) {
            initialMark.put(place.getName(), place.getNrOfTokens());
        }
        System.out.println(initialMark.toString());
        try {
            Files.write(Paths.get(file.getCanonicalPath()), ("Initial mark: " + initialMark.toString() + "\n").getBytes());
        } catch (IOException e) {
            System.out.println("Exception Occurred" + e);
        }
        currentMark = initialMark;
        while (true) {
            Transition transitionToBeExecuted = getTransitionToBeExecuted(getExecutableTransitions(petriNet));
            if (transitionToBeExecuted == null) {
                if (petriNet.getTransitions().stream().allMatch(transition -> 0 == transition.getDuration())) {
                    //deadLock
                    System.out.println("Deadlock");
                    appendStrToFile("Deadlock" + "\n");
                    break;
                } else {
                    petriNet.getTransitions().stream().filter(transition -> 0 < transition.getDuration()).forEach(transition -> transition.setDuration(transition.getDuration() - 1));
                }
            } else {
                appendStrToFile("Execute transition: " + transitionToBeExecuted.getName() + "\n");
                executeTransition(transitionToBeExecuted, petriNet);
                System.out.println(currentMark.toString());
                appendStrToFile(currentMark.toString() + "\n");
                if (checkReturnToInitialMark(initialMark, currentMark)) {
                    System.out.println("Initial mark reached");
                    appendStrToFile("Initial mark reached" + "\n");
                    break;
                }
            }
        }
    }

    /**
     * Method which get all transitions that can be executed (that have enough tokens in previous place)
     *
     * @param petriNet The Petri Net where to check for executable transitions
     * @return the list of executable transitions
     */
    private static List<Transition> getExecutableTransitions(PetriNet petriNet) {
        List<Transition> executableTransitions = new ArrayList<>();
        for (Transition t : petriNet.getTransitions()) {
            appendStrToFile(t.toString() + "\n");
            List<Pre> transitionPres = petriNet.getPres().stream().filter(pre -> pre.getDestinationTransitionName().equals(t.getName())).collect(Collectors.toList());
            if (transitionPres.stream().allMatch(pre -> pre.getCost() <= getPlaceByPre(pre, petriNet).getNrOfTokens())) {
                executableTransitions.add(t);
            }
        }
        System.out.println("Executable transitions: ");
        if (executableTransitions.size() == 0) {
            System.out.println("NONE");
        } else {
            executableTransitions.forEach(transition -> System.out.println(transition.toString()));
        }
        return executableTransitions;
    }

    /**
     * Remove possible null Places, Transitions, Pres, Posts and generate the first random duration for
     * Transitions that have random duration
     *
     * @param petriNet the Petri Net that have to be prepared
     * @return prepared Petri Net
     */
    private static PetriNet preparePetriNet(PetriNet petriNet) {
        petriNet.setPlaces(petriNet.getPlaces().stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        petriNet.setTransitions(petriNet.getTransitions().stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        petriNet.setPres(petriNet.getPres().stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        petriNet.setPosts(petriNet.getPosts().stream().filter(Objects::nonNull).collect(Collectors.toSet()));
        // create random Duration for transitions with random duration
        petriNet.getTransitions().forEach(transition -> {
            if (transition.getDuration() == null) {
                transition.setDuration((int) ((Math.random() * ((transition.getMaxDuration() - transition.getMinDuration()) + 1)) + transition.getMinDuration()));
            }
        });
        return petriNet;
    }

    /**
     * Get a Place from a Petri Net and a specified Pre
     *
     * @param pre      The specified pre
     * @param petriNet The Petri Net to get Place from
     * @return the required Place
     */
    private static Place getPlaceByPre(Pre pre, PetriNet petriNet) {
        return petriNet.getPlaces().stream().filter(place -> place.getName().equals(pre.getStartPlaceName())).findFirst().orElse(null);
    }

    /**
     * Get the transition to be executed. Which mean the only transition with 0 duration
     * or a random one if there are more transitions to be executed with 0 duration
     *
     * @param transitions The list of Transitions to chose from
     * @return the Transition to be executed
     */
    private static Transition getTransitionToBeExecuted(List<Transition> transitions) {
        List<Transition> transitionList = transitions.stream().filter(transition -> 0 == transition.getDuration()).collect(Collectors.toList());
        if (transitionList.size() == 0) {
            return null;
        }
        return transitionList.stream().findAny().orElse(null);
    }

    /**
     * Execute a transition in a PetriNet. Move the tokens according with the costs from Pre and Post
     * and reset the duration of executed transition
     *
     * @param transitionToBeExecuted The transition which have to be executed
     * @param petriNet               The Petri net where to execute the transition
     */
    private static void executeTransition(Transition transitionToBeExecuted, PetriNet petriNet) {
        System.out.println("Execute transition: " + transitionToBeExecuted.getName());
        List<Pre> transitionPres = petriNet.getPres().stream().filter(pre -> pre.getDestinationTransitionName().equals(transitionToBeExecuted.getName())).collect(Collectors.toList());
        List<Post> transitionPosts = petriNet.getPosts().stream().filter(post -> post.getStartTransitionName().equals(transitionToBeExecuted.getName())).collect(Collectors.toList());

        transitionPres.forEach(pre -> petriNet.getPlaces().stream().filter(place -> place.getName().equals(pre.getStartPlaceName())).forEach(place -> place.setNrOfTokens(place.getNrOfTokens() - pre.getCost())));
        transitionPosts.forEach(post -> petriNet.getPlaces().stream().filter(place -> place.getName().equals(post.getDestinationPlaceName())).forEach(place -> place.setNrOfTokens(place.getNrOfTokens() + post.getCost())));
        petriNet.getTransitions().forEach(transition -> transition.setDuration(transition.getDuration() == 0 ? 0 : transition.getDuration() - 1));
        currentMark = new HashMap<>();
        petriNet.getPlaces().forEach(place -> currentMark.put(place.getName(), place.getNrOfTokens()));
        if (transitionToBeExecuted.isTimed() && transitionToBeExecuted.getMinDuration() != null) {
            petriNet.getTransitions().stream().filter(transition -> transition.getName().equals(transitionToBeExecuted.getName())).forEach(transition -> transition.setDuration((int) ((Math.random() * ((transition.getMaxDuration() - transition.getMinDuration()) + 1)) + transition.getMinDuration())));
        }
        if (transitionToBeExecuted.isTimed() && transitionToBeExecuted.getMinDuration() == null) {
            //noinspection OptionalGetWithoutIsPresent
            petriNet.getTransitions().stream().filter(transition -> transition.getName().equals(transitionToBeExecuted.getName())).forEach(transition -> transition.setDuration(initialPetriNet.getTransitions().stream().filter(transition1 -> transition1.getName().equals(transitionToBeExecuted.getName())).findFirst().get().getDuration()));
        }
    }

    /**
     * Check if the current mark is the same as initial mark
     *
     * @param initialMark first mark
     * @param currentMark second mark
     * @return true if are equal of false otherwise
     */
    private static boolean checkReturnToInitialMark(Map<String, Integer> initialMark, Map<String, Integer> currentMark) {
        return initialMark.equals(currentMark);
    }

    /**
     * Append a String to a file... out.txt
     *
     * @param str the String to be appended
     */
    private static void appendStrToFile(String str) {
        try {
            Files.write(Paths.get(file.getCanonicalPath()), str.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }
}
