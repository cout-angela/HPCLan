// Generated from src/parser/HPCLan.g4 by ANTLR 4.13.1
package parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link HPCLanParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface HPCLanVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link HPCLanParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(HPCLanParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idDec}
	 * labeled alternative in {@link HPCLanParser#simpledec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdDec(HPCLanParser.IdDecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayDec}
	 * labeled alternative in {@link HPCLanParser#simpledec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayDec(HPCLanParser.ArrayDecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code simpleDec}
	 * labeled alternative in {@link HPCLanParser#dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleDec(HPCLanParser.SimpleDecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funDec}
	 * labeled alternative in {@link HPCLanParser#dec}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunDec(HPCLanParser.FunDecContext ctx);
	/**
	 * Visit a parse tree produced by {@link HPCLanParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(HPCLanParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link HPCLanParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(HPCLanParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link HPCLanParser#stm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStm(HPCLanParser.StmContext ctx);
	/**
	 * Visit a parse tree produced by {@link HPCLanParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExp(HPCLanParser.ExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code signedVal}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignedVal(HPCLanParser.SignedValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code baseExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseExp(HPCLanParser.BaseExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExp(HPCLanParser.IfExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code funExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunExp(HPCLanParser.FunExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code arrayExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayExp(HPCLanParser.ArrayExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varExp}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarExp(HPCLanParser.VarExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intVal}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntVal(HPCLanParser.IntValContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolVal}
	 * labeled alternative in {@link HPCLanParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolVal(HPCLanParser.BoolValContext ctx);
}