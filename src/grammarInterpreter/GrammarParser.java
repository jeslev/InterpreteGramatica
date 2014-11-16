
package grammarInterpreter;
import java.util.*;
/**
 *
 * @author jeslev
 */
public class GrammarParser {
    
    public static class ParseException extends Exception {
        private int line_num;
        private ParseException(String message) { this(-1, message); }
        private ParseException(int line_num, String message) {
            super(message);
            this.line_num = line_num;
        }
        public String toString() {
            if(line_num < 0) {
                return getMessage();
            } else {
                return "linea " + line_num+ ": " + getMessage();
            }
        }
    }
    
    public static Grammar parse(String text) throws ParseException {
        Grammar ret = new Grammar();

        StringTokenizer lines = new StringTokenizer(text, "\n");
        Symbol cur_sym = null;
        Symbol eps = new Symbol("-");
        HashMap map = new HashMap(); // names mapped to Elements
        map.put("-", eps);
        HashMap usage = new HashMap(); // Elements mapped to line #'s
        ArrayList sequence = new ArrayList();
        int line_num = 0;
        while(lines.hasMoreTokens()) {
            ++line_num;
            String line = lines.nextToken();
            int comment = line.indexOf('#');
            if(comment >= 0) line = line.substring(0, comment);

            StringTokenizer toks = new StringTokenizer(line);
            while(toks.hasMoreTokens()) {
                String tok = toks.nextToken();
                if(tok.equals("->")) {
                    if(sequence.size() == 0) {
                        throw new ParseException(line_num, "Falta simbolo a la izq de la flecha");
                    }
                    Object last_obj = sequence.remove(sequence.size() - 1);
                    if(!(last_obj instanceof Symbol)) {
                        throw new ParseException(line_num, "La izq de la flecha debe ser un simbolo");
                    }
                    Symbol last = (Symbol) last_obj;
                    if(cur_sym == null) {
                        if(sequence.size() > 0) {
                            throw new ParseException(line_num, "Solo puede haber un simbolo a la izq");
                        }
                        ret.setRoot(last);
                    } else {
                        addRule(ret, cur_sym, sequence, line_num, eps);
                        sequence.clear();
                    }
                    cur_sym = last;
                } else if(tok.equals("|")) {
                    if(cur_sym == null) {
                        throw new ParseException(line_num, "Mala posicion del pipe |");
                    }
                    addRule(ret, cur_sym, sequence, line_num, eps);
                    sequence.clear();
                } else {
                    Element e = (Element) map.get(tok);
                    if(e == null) {
                        // verify token is valid
                        boolean ok = true;
                        boolean is_sym = true;
                        for(int i = 0; ok && i < tok.length(); i++) {
                            char c = tok.charAt(i);
                            if(!Character.isDigit(c) && !Character.isLetter(c)) {
                                ok = false;
                            }
                            if(!Character.isUpperCase(c)) {
                                is_sym = false;
                            }
                        }

                        if(!ok) {
                            throw new ParseException(line_num,"No se reconoce: " + tok);
                        }

                        if(is_sym) e = new Symbol(tok);
                        else       e = new Atom(tok);
                        map.put(tok, e);
                        usage.put(e, new Integer(line_num));
                    }
                    sequence.add(e);
                }
            }
        }
        addRule(ret, cur_sym, sequence, line_num, eps);

        // confirm that all symbols used are defined
        if(!ret.getLeftSideSymbols().containsAll(ret.getSymbols())) {
                Iterator it = ret.getSymbols().iterator();
                while(it.hasNext()) {
                    Symbol sym = (  Symbol) it.next();
                    if(ret.getRules(sym) == null) {
                        Integer line = (Integer) usage.get(sym);
                        throw new ParseException(line == null ? -1 : line.intValue(),
                            "El simbolo no contiene reglas: " + sym);
                    }
                }
        }
        if(ret.getRoot() == null) {
            throw new ParseException("No hay simbolo incial");
        }
        Collection root_rules = ret.getRules(ret.getRoot());
        if(root_rules == null || root_rules.size() == 0) {
            throw new ParseException("Inicio nulo");
        }
        return ret;
    }


    private static void addRule(Grammar ret, Symbol lhs, ArrayList rhs, int line_num, Symbol eps)
                    throws ParseException {
        if(rhs.contains(eps)) {
            // if epsilon is in sequence, make sure it is
            // alone, and remove it.
            if(rhs.size() > 1) {
                throw new ParseException(line_num, "Produccion vacia no puede extender mas");
            }
                ret.addRule(lhs, new Element[] { });
        } else {
                ret.addRule(lhs, toElements(rhs));
        }
    }
    
    private static Element[] toElements(ArrayList l) {
        Element[] ret = new Element[l.size()];
        int i = 0;
        Iterator it = l.iterator();
        while(it.hasNext()) {
            ret[i] = (Element) it.next();
            i++;
        }
        return ret;
    }
}
