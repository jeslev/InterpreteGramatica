package grammarInterpreter;

import java.util.*;
/**
 *
 * @author jeslev
 */
class Generator {

    private static final int MAX_DEPTH = 50;
    private static final int MAX_TRIES = 50;
    private static Random rand = new Random();

    public static class GenerateException extends Exception {

        public GenerateException(String msg) {
            super(msg);
        }
    }

    public static List generate(Grammar g) throws GenerateException {
        
        int try_count = 0;
        boolean verified = false;
        Symbol root = g.getRoot();
        
        while (try_count < MAX_TRIES) {
            ++try_count;
            try {
                ArrayList log = new ArrayList();
                generateSub(g.getRoot(), 0, g, log);
                return log;
            } catch (GenerateException e) {
                //Encontraoms un error, o agotamos los niveles del arbol
                if (!verified) { //verificamos si es una gramatica recursiva infinita
                    if (!terminates(g)) {
                        throw new GenerateException("No finalizal a gramatica");
                    }
                    verified = true;
                }
            }
        }
        throw new GenerateException("No se encontro palabras");
    }

    private static void generateSub(Symbol root, int depth, Grammar g, ArrayList log) 
            throws GenerateException {
        
        if (depth > MAX_DEPTH) {
            throw new GenerateException("Gramatica Recursiva Infinita");
        }
        
        Collection rules = g.getRules(root);

        int which = rand.nextInt(rules.size()); // random entre las reglas de la raiz de la gramatica
        Iterator it = rules.iterator();
        while (it.hasNext()) { //se busca la posicion del random en la lista
            Grammar.Rule rule = (Grammar.Rule) it.next();
            if (which == 0) {
                Element[] rhs = rule.getRightSide(); //lado derecho de la regla
                for (int i = 0; i < rhs.length; i++) {
                    Element e = rhs[i];
                    if (e instanceof Symbol) { //Si es un simbolo evalua el sgte nivel del arbol
                        generateSub((Symbol) e, depth + 1, g, log);
                    } else { //si es letra o numero lo agrega a la cadena final
                        log.add(e);
                    }
                }
                break;
            }
            --which;
        }
    }

    private static boolean terminates(Grammar g) {
        // determina que simbolos terminan en alguna letra final
        HashSet terminating = new HashSet();
        boolean changed = true;
        while (changed) {
            changed = false;
            Iterator it = g.getRules().iterator();
            while (it.hasNext()) {
                Grammar.Rule rule = (Grammar.Rule) it.next();
                Symbol lhs = rule.getLeftSide();
                if (terminating.contains(lhs)) {
                    continue;
                }
                if (allSymsInSet(rule.getRightSide(), terminating)) {
                    terminating.add(lhs);
                    changed = true;
                }
            }
        }
        return terminating.contains(g.getRoot());
    }

    private static boolean allSymsInSet(Element[] rhs, Set q) {
        for (int i = 0; i < rhs.length; i++) {
            if (rhs[i] instanceof Symbol && !q.contains(rhs[i])) {
                return false;
            }
        }
        return true;
    }
}
