// Generated from /Users/laneve/Documents/dev/CLP_2025-26/HPCLan/src/parser/HPCLan.g4 by ANTLR 4.13.1
package parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link HPCLanParser}.
 */
public interface HPCLanListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link HPCLanParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(HPCLanParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link HPCLanParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(HPCLanParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idDec}
	 * labeled alternative in {@link HPCLanParser#simpledec}.
	 * @param ctx the parse tree
	 */
	void enterIdDec(HPCLanParser.IdDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idDec}
	 * labeled alternative in {@link HPCLanParser#simpledec}.
	 * @param ctx the parse tree
	 */
	void exitIdDec(HPCLanParser.IdDecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayDec}
	 * labeled alternative in {@link HPCLanParser#simpledec}.
	 * @param ctx the parse tree
	 */
	void enterArrayDec(HPCLanParser.ArrayDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayDec}
	 * labeled alternative in {@link HPCLanParser#simpledec}.
	 * @param ctx the parse tree
	 */
	void exitArrayDec(HPCLanParser.ArrayDecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code simpleDec}
	 * labeled alternative in {@link HPCLanParser#dec}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDec(HPCLanParser.SimpleDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code simpleDec}
	 * labeled alternative in {@link HPCLanParser#dec}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDec(HPCLanParser.SimpleDecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code funDec}
	 * labeled alternative in {@link HPCLanParser#dec}.
	 * @param ctx the parse tree
	 */
	void enterFunDec(HPCLanParser.FunDecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code funDec}
	 * labeled alternative in {@link HPCLanParser#dec}.
	 * @param ctx the parse tree
	 */
	void exitFunDec(HPCLanParser.FunDecContext ctx);
	/**
	 * Enter a parse tree produced by {@link HPCLanParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(HPCLanParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link HPCLanParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(HPCLanParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link HPCLanParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(HPCLanParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link HPCLanParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(HPCLanParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link HPCLanParser#stm}.
	 * @param ctx the parse tree
	 */
	void enterStm(HPCLanParser.StmContext ctx);
	/**
	 * Exit a parse tree produced by {@link HPCLanParser#stm}.
	 * @param ctx the parse tree
	 */
	void exitStm(HPCLanParser.StmContext ctx);
	/**
	 * Enter a parse tree produced by {@link HPCLanParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp(HPCLanParser.ExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link HPCLanParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp(HPCLanParser.ExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code signedVal}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void enterSignedVal(HPCLanParser.SignedValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code signedVal}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void exitSignedVal(HPCLanParser.SignedValContext ctx);
	/**
	 * Enter a parse tree produced by the {@code baseExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void enterBaseExp(HPCLanParser.BaseExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code baseExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void exitBaseExp(HPCLanParser.BaseExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void enterIfExp(HPCLanParser.IfExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void exitIfExp(HPCLanParser.IfExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code funExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void enterFunExp(HPCLanParser.FunExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code funExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void exitFunExp(HPCLanParser.FunExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void enterArrayExp(HPCLanParser.ArrayExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void exitArrayExp(HPCLanParser.ArrayExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void enterVarExp(HPCLanParser.VarExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void exitVarExp(HPCLanParser.VarExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intVal}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void enterIntVal(HPCLanParser.IntValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intVal}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void exitIntVal(HPCLanParser.IntValContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolVal}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void enterBoolVal(HPCLanParser.BoolValContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolVal}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 */
	void exitBoolVal(HPCLanParser.BoolValContext ctx);
}