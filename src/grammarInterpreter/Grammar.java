package grammarInterpreter;

import java.util.*;

/**
 *
 * @author jeslev
 */
public class Grammar {
    //Clase que maneja reglas (simbolo -> Elementos[])
    public static class Rule {

        private Symbol lhs;
        private Element[] rhs;

        public Rule(Symbol lhs, Element[] rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public Symbol getLeftSide() {
            return lhs;
        }

        public Element[] getRightSide() {
            return rhs;
        }

        public String toString() {
            StringBuffer ret = new StringBuffer();
            ret.append(lhs + " ->");
            for (int i = 0; i < rhs.length; i++) {
                ret.append(" " + rhs[i]);
            }
            return ret.toString();
        }

    }

    private Symbol root = null;
    private HashSet symbols = new HashSet();
    private HashMap atoms = new HashMap();
    private HashSet rules = new HashSet();
    private HashMap rule_map = new HashMap();

    public Grammar() {
    }

    public Atom getAtom(String name) {
        return (Atom) atoms.get(name);
    }

    public void setRoot(Symbol root) {
        this.root = root;
        symbols.add(root);
    }

    public void addRule(Symbol lhs, Element[] rhs) {
        add(new Rule(lhs, rhs));
    }

    public void add(Rule rule) {
        Collection c = getRules(rule.lhs);
        if (c == null) {
            c = new ArrayList();
            rule_map.put(rule.lhs, c);
        }
        c.add(rule);
        rules.add(rule);

        symbols.add(rule.lhs);
        for (int i = 0; i < rule.rhs.length; i++) {
            Element e = rule.rhs[i];
            if (e instanceof Symbol) {
                symbols.add(e);
            } else {
                atoms.put(e.getName(), e);
            }
        }
    }

    public Symbol getRoot() {
        return root;
    }

    public Collection getRules(Symbol lhs) {
        return (Collection) rule_map.get(lhs);
    }

    public Collection getRules() {
        return rules;
    }

    public Collection getSymbols() {
        return symbols;
    }

    public Collection getAtoms() {
        return atoms.values();
    }

    public Collection getLeftSideSymbols() {
        return rule_map.keySet();
    }

}
