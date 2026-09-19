package ast;

import java.util.ArrayList;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import parser.HPCLanBaseVisitor;

import parser.HPCLanParser.ProgContext;
import parser.HPCLanParser.SimpledecContext;
import parser.HPCLanParser.IdDecContext;
import parser.HPCLanParser.ArrayDecContext;
import parser.HPCLanParser.DecContext;
import parser.HPCLanParser.SimpleDeclContext;
import parser.HPCLanParser.FunDecContext;
import parser.HPCLanParser.ParamContext;
import parser.HPCLanParser.TypeContext;
import parser.HPCLanParser.StmContext;
import parser.HPCLanParser.ExpContext;
import parser.HPCLanParser.ValueContext;
import parser.HPCLanParser.BaseExpContext;
import parser.HPCLanParser.VarExpContext;
import parser.HPCLanParser.IntValContext;
import parser.HPCLanParser.IfExpContext;
import parser.HPCLanParser.SignedValContext;
import parser.HPCLanParser.ArrayExpContext;
import parser.HPCLanParser.FunExpContext;
import parser.HPCLanParser.BoolValContext;
import parser.HPCLanParser.AsgStmContext;
import parser.HPCLanParser.WhileStmContext;
import parser.HPCLanParser.IfStmContext;
import parser.HPCLanParser.MapredStmContext;
import parser.HPCLanParser.ArrayStmContext;



public class HPCLanVisitorImpl extends HPCLanBaseVisitor<Node> {

    public Node visitProg(ProgContext ctx) {
        ArrayList<Node> declarations = new ArrayList<Node>();
        ArrayList<Node> statements = new ArrayList<Node>();

        // visit all nodes corresponding to declarations inside the let context and store them in
        // declarations notice that the ctx.let().dec() returns a list because of the use of * or +
        // in the grammar
        
        for (DecContext dc : ctx.dec()) {
            declarations.add(visit(dc));
        }
        for (StmContext sc : ctx.stm()) {
            statements.add(visit(sc));
        }

        Node exp = visit(ctx.exp());

        return new ProgNode(declarations, statements, exp);
    }

    public Node visitIdDec(IdDecContext ctx) {
        Node typeNode = visit(ctx.type()); //visit the type
        Node expNode = visit(ctx.exp()); //visit the exp
        if (ctx.c == null) {
            return new DecNode(ctx.ID().getText(), typeNode, expNode); //build and return the varNode
        }

        return new ConstDecNode(ctx.ID().getText(), typeNode, expNode); //build and return the varNode
    }

   

    public Node visitArrayDec(ArrayDecContext ctx) {
        Node typeNode = visit(ctx.type());
        String name = ctx.ID(0).getText();

        if (ctx.INT() != null) {
            return new ArrayDecNode(name, typeNode, new IntNode(Integer.parseInt(ctx.INT().getText())));
        }

        return new ArrayDecNode(name, typeNode, new IdNode(ctx.ID(1).getText()));
    }

    public Node visitSimpleDecl(SimpleDeclContext ctx) {
        Node typeNode = visit(ctx.simpledec());
        
        return typeNode;
    }
    
    public Node visitFunDec(FunDecContext ctx) {
        ArrayList<ParNode> _param = new ArrayList<ParNode>();
        for (ParamContext vc : ctx.param()) // build the list of parameters with the types
        {
            _param.add(new ParNode(vc.ID().getText(), (Type) visit(vc.type())));
        }

        ArrayList<Node> innerDec = new ArrayList<Node>(); // this is for the declarations in the body
        
        for (SimpledecContext dc : ctx.simpledec()) {
            innerDec.add(visit(dc));
        }

        ArrayList<Node> stms = new ArrayList<Node>(); // this is for the declarations in the body
        
        for (StmContext sc : ctx.stm()) {
            stms.add(visit(sc));
        }

        Node exp = visit(ctx.exp()); // visit the body

        return new FunNode(ctx.ID().getText(), (Type) visit(ctx.type()), _param, innerDec, stms, exp);
    }

    public Node visitParam(ParamContext ctx) {
        return new ParNode(ctx.ID().getText(), (Type) visit(ctx.type()));
    }

    public Node visitType(TypeContext ctx) {
        if (ctx.getText().equals("int")) {
            return new IntType(); 
        }else {
            return new BoolType();
        }
    }

    public Node visitAsgStm(AsgStmContext ctx) {
        Node expNode = visit(ctx.exp()); //visit the exp
        return new AsgNode(ctx.ID().getText(), expNode);
    }

    public Node visitArrayStm(ArrayStmContext ctx) {
        Node index = visit(ctx.exp(0));
        Node exp = visit(ctx.exp(1));
        return new ArrayStmNode(ctx.ID().getText(), exp, index);
    }

     public Node visitWhileStm(WhileStmContext ctx) {
        Node condExp = visit(ctx.exp());
        ArrayList<Node> stms = new ArrayList<Node>();
        for (StmContext sc : ctx.stm()) {
            stms.add(visit(sc));
        }
        return new WhileStmNode(condExp, stms);
    } 

/*     public Node visitIfStm(IfStmContext ctx) {
        Node condExp = visit(ctx.cond);
        ArrayList<Node> thenStms = new ArrayList<Node>();
        for (StmContext sc : ctx.thenBranch) {
            thenStms.add(visit(sc));
        }
        ArrayList<Node> elseStms = new ArrayList<Node>();
        for (StmContext sc : ctx.elseBranch) {
            elseStms.add(visit(sc));
        }
        return new IfStmNode(condExp, thenStms, elseStms);
    } */

     public Node visitMapredStm(MapredStmContext ctx) {
        //Node index = ctx.ID(0);
        Node n;
        String arrayId;
        Node arrayIdx = visit(ctx.exp(0));
        Node exp = visit(ctx.exp(1));
        System.out.println("ctx.INT: " + ctx.INT());
        System.out.println("ctx.ID(1).getText(): " + ctx.ID(1));

        if(ctx.INT() != null){
            n = new IntNode(Integer.parseInt(ctx.INT().getText()));
            arrayId = ctx.ID(1).getText();
        }else{
            n = new IdNode(ctx.ID(1).getText());
            System.out.println("n: " + n);

            arrayId = ctx.ID(2).getText();
        }
        
        return new MapredStmNode(ctx.ID(0).getText(), n, new ArrayStmNode(arrayId, exp, arrayIdx));
    }

    
    public Node visitExp(ExpContext ctx) {
        if (ctx.op == null) {
            return visit(ctx.value());
        } else {
            Node left = visit(ctx.left);
            Node right = visit(ctx.right);
            switch (ctx.op.getText()) {
                case "*":
                    return new MultNode(left, right);
                case "/":
                    return new DivNode(left, right);
                case "+":
                    return new PlusNode(left, right);
                case "-":
                    return new MinusNode(left, right);
                case "==":
                    return new EqualNode(left, right);
                case "!=":
                    return new UnEqualNode(left, right);
                case ">=":
                    return new GeqNode(left, right);
                case "<=":
                    return new LeqNode(left, right);
                case "<":
                    return new LtNode(left, right);
                case ">":
                    return new GtNode(left, right);
                case "&&":
                    return new AndNode(left, right);
                case "||":
                    return new OrNode(left, right);
                default:
                    throw new IllegalStateException("Unknown operator: " + ctx.op.getText());
            }
        }
    }

    public Node visitSignedVal(SignedValContext ctx) {
        String operator = ctx.op.getText();
        if (operator.equals("-")) {
            return new UMinusNode(visit(ctx.value())); 
        } else if (operator.equals("!")) {
            return new NotNode(visit(ctx.value())); 
        } else {
            return visit(ctx.value()); // operator.equals("+")
        }
    }

    public Node visitBaseExp(BaseExpContext ctx) {
        return visit(ctx.exp());
    }

    public Node visitIfExp(IfExpContext ctx) {
        // it is a conditional — production named #ifExp: built the abstract trees for
        // the guard and the branches; store the pointers

        ArrayList<Node> thenStm = new ArrayList<Node>();
        ArrayList<Node> elseStm = new ArrayList<Node>();

        // visit all nodes corresponding to declarations inside the let context and store them in
        // declarations notice that the ctx.let().dec() returns a list because of the use of * or +
        // in the grammar
        
        for (StmContext ts : ctx.thenBranch) {
            thenStm.add(visit(ts));
        }
        for (StmContext es : ctx.elseBranch) {
            elseStm.add(visit(es));
        }
        Node condExp = visit(ctx.cond);
       
        Node thenExp = visit(ctx.exp(1));
        Node elseExp = visit(ctx.exp(2));
        
        return new IfExpNode(condExp, thenStm, elseStm, thenExp, elseExp);
    }

    public Node visitFunExp(FunExpContext ctx) {
        // it is a function invocation — production named #funExp: build the subtree of
        // the arguments, store the name of the function declare the result
        ArrayList<Node> args = new ArrayList<Node>();

        for (ExpContext exp : ctx.exp()) {
            args.add(visit(exp));
        }

        return new CallNode(ctx.ID().getText(), args);
    }
    
    public Node visitArrayExp(ArrayExpContext ctx) {
        Node index = visit(ctx.exp());
        return new ArrayNode(ctx.ID().getText(), index);
    }

    public Node visitVarExp(VarExpContext ctx) {
        return new IdNode(ctx.ID().getText());
    }

    @Override
    public Node visitIntVal(IntValContext ctx) {
        return new IntNode(Integer.parseInt(ctx.INT().getText()));
    }

    @Override
    public Node visitBoolVal(BoolValContext ctx) {
        return new BoolNode(Boolean.parseBoolean(ctx.BOOL().getText()));
    }

}
