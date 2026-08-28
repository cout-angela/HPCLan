package semanticanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import ast.BoolType;
import ast.IntType;
import ast.Type;

public class SymbolTable {

    private final ArrayList<HashMap<String, STentry>> symbol_table;
    private final ArrayList<Integer> offset;

    public SymbolTable() {
        symbol_table = new ArrayList<HashMap<String, STentry>>();
        offset = new ArrayList<Integer>();
    }

    public Integer nesting() {
        return symbol_table.size() - 1;
    }

    public STentry lookup(String id) {
        int n = symbol_table.size() - 1;
        boolean found = false;
        STentry T = null;
        while ((n >= 0) && !found) {
            HashMap<String, STentry> H = symbol_table.get(n);
            T = H.get(id);
            if (T != null) {
                found = true; 
            }else {
                n = n - 1;
            }
        }
        return T;
    }

    public Integer nslookup(String id) {
        int n = symbol_table.size() - 1;
        boolean found = false;
        while ((n >= 0) && !found) {
            HashMap<String, STentry> H = symbol_table.get(n);
            if (H.get(id) != null) {
                found = true; 
            }else {
                n = n - 1;
            }
        }
        return n;
    }

    public void add(HashMap<String, STentry> H) {
        symbol_table.add(H);
        offset.add(1);		// si inizia da 2 perche` prima ci sonop FP e AL
    }

    public void remove() {
        int x = symbol_table.size();
        symbol_table.remove(x - 1);
        offset.remove(x - 1);
    }

    public boolean top_lookup(String id) {
        int n = symbol_table.size() - 1;
        STentry T = null;
        HashMap<String, STentry> H = symbol_table.get(n);
        T = H.get(id);
        return (T != null);
    }

    public void insert(String id, Type type, String _label, int _dim, Integer _value, int _nesting) {
        int n = symbol_table.size() - 1;
        HashMap<String, STentry> H = symbol_table.get(n);
        symbol_table.remove(n);
        int offs = offset.get(n);
        offset.remove(n);
        STentry idtype = new STentry(type, offs, _label, _dim, _value, _nesting);
        H.put(id, idtype);
        symbol_table.add(H);
        offset.add(offs + _dim);
    }

    public void increaseoffset() {
        int n = offset.size() - 1;
        int offs = offset.get(n);
        offset.remove(n);
        offs = offs + 1;
        offset.add(offs);
    }

}
