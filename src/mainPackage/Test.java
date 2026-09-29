package mainPackage;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.LexerNoViableAltException;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;
import ast.HPCLanVisitorImpl;
import evaluator.ExecuteVM;
import ast.ErrorType;
import ast.Node;
import ast.SVMVisitorImpl;

import parser.HPCLanLexer;
import parser.HPCLanParser;
import parser.SVMLexer;
import parser.SVMParser;

import parser.HPCLanErrorListener;

public class Test {

	public static void main(String[] args) throws Exception {

		String fileName = "Samples.hpc";

		FileInputStream is = new FileInputStream(fileName);
		ANTLRInputStream input = new ANTLRInputStream(is);
		HPCLanLexer lexer = new HPCLanLexer(input);

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		HPCLanParser parser = new HPCLanParser(tokens);

		parser.removeErrorListeners();
		parser.addErrorListener(new HPCLanErrorListener());

		HPCLanVisitorImpl visitor = new HPCLanVisitorImpl();
		Node ast = visitor.visit(parser.prog()); // generazione AST

		SymbolTable ST = new SymbolTable();
		ArrayList<SemanticError> errors = ast.checkSemantics(ST, 0);
		if (errors.size() > 0) {
			System.out.println("You had: " + errors.size() + " errors:");
			for (SemanticError e : errors)
				System.out.println("\t" + e);
		} else {
			System.out.println("Visualizing AST...");
			System.out.println(ast.toPrint(""));

			Node type = ast.typeCheck(); // type-checking bottom-up
			if (type instanceof ErrorType)
				System.out.println("Type checking is WRONG!");
			else
				System.out.println(type.toPrint("Type checking ok! Type of the program is: "));

			// CODE GENERATION
			String code = ast.codeGeneration();
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName + ".asm"));
			out.write(code);
			out.close();
			System.out.println("Code generated! Assembling and running generated code.");

			FileInputStream isASM = new FileInputStream(fileName + ".asm");
			ANTLRInputStream inputASM = new ANTLRInputStream(isASM);
			SVMLexer lexerASM = new SVMLexer(inputASM);
			CommonTokenStream tokensASM = new CommonTokenStream(lexerASM);
			SVMParser parserASM = new SVMParser(tokensASM);

			// parserASM.assembly();

			SVMVisitorImpl visitorSVM = new SVMVisitorImpl();
			visitorSVM.visit(parserASM.assembly());

			// System.out.println("You had: "+lexerASM.lexicalErrors+" lexical errors and
			// "+parserASM.getNumberOfSyntaxErrors()+" syntax errors.");
			// if (lexerASM.lexicalErrors>0 || parserASM.getNumberOfSyntaxErrors()>0)
			// System.exit(1);
			/*
			 * System.out.println("Trovati " + lexErrors.getNumberOfErrors() +
			 * " errori lessicali:");
			 * if(lexErrors.getNumberOfErrors()!=0){
			 * for (String err : lexErrors.getErrors())
			 * System.out.println("\t" + err);
			 * System.out.println("Compilazione interrotta.");
			 * System.exit(1);
			 * }
			 */
			System.out.println("Starting Virtual Machine...");
			ExecuteVM vm = new ExecuteVM(visitorSVM.code);
			vm.cpu();
		}
	}

}
