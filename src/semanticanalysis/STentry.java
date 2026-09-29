package semanticanalysis;

import ast.Type;

public class STentry {

    private final Type type;
    private final int dim;
    private final int offset;
    private final int nesting;
    private final String label;
    private final Integer value; // added to store constant value

    public STentry(Type _type, int _offset, String _label, int _dim, Integer _value, int _nesting) {
        type = _type;
        offset = _offset;
        label = _label;
        dim = _dim;
        value = _value;
        nesting = _nesting;
    }

    public Type gettype() {
        return type;
    }

    public int getoffset() {
        return offset;
    }

    public int getdim() {
        return dim;
    }

    public String getlabel() {
        return label;
    }

    public Integer getvalue() {
        return value;
    }

    public int getnesting() {
        return nesting;
    }

}
