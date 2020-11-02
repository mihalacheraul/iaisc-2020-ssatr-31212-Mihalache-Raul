package loader;

import com.google.gson.Gson;
import model.PetriNet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;

public class PetriNetLoader {
    private static Gson gson = new Gson();
    private static Reader reader;

    /**
     * Load the Petri Net from /in/loadPetriNet.txt
     *
     * @return the loaded Petri Net
     */
    public static PetriNet loadPetriNet() {
        try {
            URL url = PetriNetLoader.class.getResource("/in/loadPetriNet.txt");
            reader = new FileReader(new File(url.toURI()));
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
        return gson.fromJson(reader, PetriNet.class);
    }
}
