import java.util.ArrayList;
import java.util.HashMap;

/**
 * Trie Class to find roads based on search
 */
public class Trie {
    public ArrayList<Segement> segements;
    HashMap<Character,Trie> children;

    public Trie(ArrayList<Segement>s, HashMap<Character,Trie> child) {
        segements = s;
        children = child;
    }
}
