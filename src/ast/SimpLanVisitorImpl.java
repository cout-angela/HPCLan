package ast;

import java.util.ArrayList;
import parser.HPCLanBaseVisitor;

import parser.HPCLanParser.BaseExpContext;
import parser.HPCLanParser.BoolValContext;
import parser.HPCLanParser.DecContext;
import parser.HPCLanParser.ExpContext;
import parser.HPCLanParser.FunDecContext;
import parser.HPCLanParser.FunExpContext;
import parser.HPCLanParser.IdInitContext;
import parser.HPCLanParser.IfExpContext;
import parser.HPCLanParser.IntValContext;
import parser.HPCLanParser.LetInExpContext;
import parser.HPCLanParser.ParamContext;
import parser.HPCLanParser.SingleExpContext;
import parser.HPCLanParser.TypeContext;
import parser.HPCLanParser.VarExpContext;
import parser.HPCLanParser.SignedValContext;

public class SimpLanVisitorImpl extends HPCLanBaseVisitor<Node> {

	public Node visitLetInExp(LetInExpContext ctx) {
		ArrayList<Node> declarations = new ArrayList<Node>();

		// visit all nodes corresponding to declarations inside the let context and store them in
		// declarations notice that the ctx.let().dec() returns a list because of the use of * or +
		// in the grammar
		for (DecContext dc : ctx.let().dec())
			declarations.add( visit(dc) );

		Node exp = visit( ctx.exp() );

		return new ProgLetInNode(declarations, exp) ;
	}

	public Node visitSingleExp(SingleExpContext ctx) {
		//simply return the result of the visit to the inner exp
		return new ProgNode(visit(ctx.exp()));
	}

	public Node visitIdInit(IdInitContext ctx) {
		Node typeNode = visit(ctx.type()); //visit the type
		Node expNode = visit(ctx.exp()); //visit the exp
		return new DecNode(ctx.ID().getText(), typeNode, expNode); //build and return the varNode
	}

	public Node visitFunDec(FunDecContext ctx) {
		ArrayList<ParNode> _param = new ArrayList<ParNode>() ;
		for (ParamContext vc : ctx.param()) // build the list of parameters with the types
			_param.add( new ParNode(vc.ID().getText(), (Type) visit( vc.type() )) );

		ArrayList<Node> innerDec = new ArrayList<Node>(); // this is for the declarations in the body
		if(ctx.let() != null){
			//if there are visit each dec and add it to the innerDec list
			for(DecContext dc : ctx.let().dec())
				innerDec.add(visit(dc));
		}
		Node exp = visit(ctx.exp()); // visit the body

		return new FunNode(ctx.ID().getText(), (Type) visit(ctx.type()), _param, innerDec, exp);
	}

	public Node visitType(TypeContext ctx) {
		if(ctx.getText().equals("int"))
			return new IntType();
		else return new BoolType();
	}

	public Node visitExp(ExpContext ctx) {
		if (ctx.op == null) { return visit(ctx.value());
		} else {
			Node left = visit(ctx.left);
			Node right = visit(ctx.right);
			switch (ctx.op.getText()) {
				case "*": return new MultNode(left, right);
				case "/": return new DivNode(left, right);
				case "+": return new PlusNode(left, right);
				case "-": return new MinusNode(left, right);
				case "==": return new EqualNode(left, right);
				case "!=": return new UnEqualNode(left, right);
				case ">=": return new GeqNode(left, right);
				case "<=": return new LeqNode(left, right);
				case "<": return new LtNode(left, right);
				case ">": return new GtNode(left, right);
				case "&&": return new AndNode(left, right);
				case "||": return new OrNode(left, right);
				default: throw new IllegalStateException("Unknown operator: " + ctx.op.getText());
			}
		}
	}

	public Node visitBaseExp(BaseExpContext ctx) {
		// expression in parentheses — production named #baseExp: remove parentheses
		return visit (ctx.exp());
	}

	public Node visitIfExp(IfExpContext ctx) {
		// it is a conditional — production named #ifExp: built the abstract trees for
		// the guard and the branches; store the pointers
		Node condExp = visit(ctx.cond);
		Node thenExp = visit(ctx.thenBranch);
		Node elseExp = visit(ctx.elseBranch);
		return new IfNode(condExp, thenExp, elseExp);
	}

	public Node visitFunExp(FunExpContext ctx) {
		// it is a function invocation — production named #funExp: build the subtree of
		// the arguments, store the name of the function declare the result
		ArrayList<Node> args = new ArrayList<Node>();

		for (ExpContext exp : ctx.exp())
			args.add(visit(exp));

		return new CallNode(ctx.ID().getText(), args);
	}

	public Node visitSignedVal(SignedValContext ctx) {
		String operator = ctx.op.getText();
		if (operator.equals("-")) return new UMinusNode(visit(ctx.value())) ;
		else if (operator.equals("!")) return new NotNode(visit(ctx.value())) ;
		else return visit(ctx.value()) ; // operator.equals("+")
	}

	@Override
	public Node visitIntVal(IntValContext ctx) {
		return new IntNode(Integer.parseInt(ctx.INT().getText()));
	}

	@Override
	public Node visitBoolVal(BoolValContext ctx) {
		return new BoolNode(Boolean.parseBoolean(ctx.BOOL().getText()));
	}

	@Override
	public Node visitVarExp(VarExpContext ctx) {
		return new IdNode(ctx.getText());
	}

}
