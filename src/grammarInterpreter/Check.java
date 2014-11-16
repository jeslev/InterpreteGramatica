package grammarInterpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author jeslev
 */
public class Check {

    private Grammar g;
    private String area;

    public Check(String area) {
        this.area = area;
    }

    public boolean init() { //inicializa la gramatica, verifica que sea correcta
        try {
            if (g == null) {
                g = GrammarParser.parse(area);
            }
            return true;
            
        } catch (GrammarParser.ParseException e) {
            System.err.println("Error en getGrammar: " + e.toString());
            return false;
        }

    }

    public String random() {
        List sentence;
        try {
            sentence = Generator.generate(g);
        } catch (Generator.GenerateException e) {
            System.err.println(e);
            return "ERROR";
        }
        StringBuffer buf = new StringBuffer();
        Iterator it = sentence.iterator();
        while (it.hasNext()) {
            if (buf.length() > 0) {
                buf.append(" ");
            }
            buf.append(it.next().toString());
        }
        return buf.toString();

    }

}
