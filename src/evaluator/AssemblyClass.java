package evaluator;

public class AssemblyClass {
    private int code;
    private String arg1;
    private final String arg2;
    private String arg3;

    public AssemblyClass(int _code, String _arg1, String _arg2, String _arg3) {
        code = _code;
        arg1 = _arg1;
        arg2 = _arg2;
        arg3 = _arg3;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int _code) {
        code = _code;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String _arg1) {
        arg1 = _arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public String getArg3() {
        return arg3;
    }

    public void setArg3(String _arg3) {
        arg3 = _arg3;
    }
}