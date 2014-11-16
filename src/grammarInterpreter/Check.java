

package grammarInterpreter;


import java.util.StringTokenizer;

/**
 *
 * @author jeslev
 */
public class Check {
    
    private Grammar grammar;
    private Parser parser = null;
    private String text,area;
    
    public Check(String text, String area){
        this.text = text;
        this.area = area;
        grammar = null;
    }
    
    public Tree init(){
        Grammar g =  getGrammar();
        if(g == null) return null;
        
        StringTokenizer toks = new StringTokenizer(text);
        Atom[] text = new Atom[toks.countTokens()];
	boolean text_ok = true;
        for(int i = 0; toks.hasMoreTokens(); i++) {
            text[i] = g.getAtom(toks.nextToken());
            if(text[i] == null) text_ok = false;
	}
        
        Tree t = null;
        if(text_ok) {
            if(parser == null) parser = new Parser(g);
            t = parser.parse(text);
        }
        return t;
    }
    
    private Grammar getGrammar() {
        try {
            if(grammar == null) grammar = GrammarParser.parse(area);
            return grammar;
        } catch(GrammarParser.ParseException e) {
            System.err.println("Error en getGrammar: "+e.toString());
            return null;
        }
    }
    
    
}
