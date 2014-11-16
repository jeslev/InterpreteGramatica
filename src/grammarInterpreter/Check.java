package grammarInterpreter;


import java.util.StringTokenizer;

/**
 *
 * @author jeslev
 */
public class Check {
    
    private Grammar grammar,g;
    private Parser parser = null;
    private String texto,area;
    
    public Check(String text, String area){
        this.texto = text;
        this.area = area;
        grammar = null;
    }
    
    public boolean init(){
        g =  getGrammar(); // se procesa la gramatica
        if(g == null) return false;
        return true;
    }
    
    public Tree check(){
        StringTokenizer toks = new StringTokenizer(texto);
        Atom[] text = new Atom[toks.countTokens()];
	boolean text_ok = true;
        for(int i = 0; toks.hasMoreTokens(); i++) { //se procesa la cadena
            text[i] = g.getAtom(toks.nextToken());
            if(text[i] == null) text_ok = false;
	}
        
        Tree t = null;
        if(text_ok) {
            if(parser == null) parser = new Parser(g);
            t = parser.parse(text);
            parser.printTree(t, 1);
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
