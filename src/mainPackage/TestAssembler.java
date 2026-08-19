package mainPackage;
import java.io.FileInputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import evaluator.ExecuteVM;
import ast.SVMVisitorImpl;

import parser.SimpLanLexer ;
import parser.SimpLanParser ;
import parser.SVMLexer ;
import parser.SVMParser ;

public class TestAssembler {
	public static void main(String[] args) throws Exception {

		String fileName = "prova.simplan.asm";


				FileInputStream isASM = new FileInputStream(fileName);
				ANTLRInputStream inputASM = new ANTLRInputStream(isASM);
				SVMLexer lexerASM = new SVMLexer(inputASM);
				CommonTokenStream tokensASM = new CommonTokenStream(lexerASM);
				SVMParser parserASM = new SVMParser(tokensASM);

				//parserASM.assembly();

				SVMVisitorImpl visitorSVM = new SVMVisitorImpl();
				visitorSVM.visit(parserASM.assembly()); 

				//System.out.println("You had: "+lexerASM.lexicalErrors+" lexical errors and "+parserASM.getNumberOfSyntaxErrors()+" syntax errors.");
				//if (lexerASM.lexicalErrors>0 || parserASM.getNumberOfSyntaxErrors()>0) System.exit(1);

				System.out.println("Starting Virtual Machine...");
				ExecuteVM vm = new ExecuteVM(visitorSVM.code);
				vm.cpu();
	}
}
