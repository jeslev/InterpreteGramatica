
package grammarInterpreter;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author jeslev
 */
public class GrammarInterpreter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GUI g = new GUI();
        g.setVisible(true);
        g.setTitle("Interprete de Gramatica Libre de Contexto");
        g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
}
