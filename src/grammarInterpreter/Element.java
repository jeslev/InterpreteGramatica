
package grammarInterpreter;

/**
 *
 * @author jeslev
 */
public class Element {
    private static int last_alloc = 0;
    
    private String name;

    public Element() {
        this.name = "temp" + last_alloc;
        last_alloc++;
    }
    
    public Element(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public String toString() { return name; }
}
