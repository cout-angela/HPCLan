package mainPackage;

import ast.ErrorType;
import ast.Node;
import ast.SVMVisitorImpl;
import ast.SimpLanVisitorImpl;
import evaluator.ExecuteVM;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.SVMLexer;
import parser.SVMParser;
import parser.SimpLanLexer;
import parser.SimpLanParser;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class PrintAST {

    public static void main(String[] args) throws Exception {

        String fileName = "prova.simplan";

        FileInputStream is = new FileInputStream(fileName);
        ANTLRInputStream input = new ANTLRInputStream(is);
        SimpLanLexer lexer = new SimpLanLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SimpLanParser parser = new SimpLanParser(tokens);
        SimpLanVisitorImpl visitor = new SimpLanVisitorImpl();
        Node ast = visitor.visit(parser.prog()); //generazione AST

        //SIMPLE CHECK FOR LEXER ERRORS
        if (lexer.lexicalErrors > 0){
            System.out.println("The program was not in the right format. Exiting the compilation process now") ;
        } else {
            System.out.println("Visualizing AST...");
            System.out.println(ast.toPrint(""));
        }
    }
}
