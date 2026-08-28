package mainPackage;

import ast.ErrorType;
import ast.Node;
import ast.SVMVisitorImpl;
import ast.HPCLanVisitorImpl;
import evaluator.ExecuteVM;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.SVMLexer;
import parser.SVMParser;
import parser.HPCLanLexer;
import parser.HPCLanParser;
import semanticanalysis.SemanticError;
import semanticanalysis.SymbolTable;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class PrintASTandSemAn {

    public static void main(String[] args) throws Exception {

        String fileName = "prova.simplan";

        FileInputStream is = new FileInputStream(fileName);
        ANTLRInputStream input = new ANTLRInputStream(is);
        HPCLanLexer lexer = new HPCLanLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        HPCLanParser parser = new HPCLanParser(tokens);
        HPCLanVisitorImpl visitor = new HPCLanVisitorImpl();
        Node ast = visitor.visit(parser.prog()); //generazione AST

        //SIMPLE CHECK FOR LEXER ERRORS
        if (lexer.lexicalErrors > 0){
            System.out.println("The program was not in the right format. Exiting the compilation process now") ;
        } else {
            System.out.println("Visualizing AST...");
            System.out.println(ast.toPrint(""));

            SymbolTable ST = new SymbolTable();
            ArrayList<SemanticError> errors = ast.checkSemantics(ST, 0);
            if (errors.size() > 0) {
                System.out.println("You had: " + errors.size() + " errors:");
                for (SemanticError e : errors)
                    System.out.println("\t" + e);
            } else {
                Node type = ast.typeCheck(); //type-checking bottom-up
                if (type instanceof ErrorType)
                    System.out.println("Type checking is ERROR!");
                else
                    System.out.println(type.toPrint("Type checking ok! Type of the program is: "));
            }
        }
    }
}
