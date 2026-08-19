package mainPackage;
import java.io.FileInputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import ast.SimpLanVisitorImpl;
import ast.Node;

import parser.SimpLanLexer ;
import parser.SimpLanParser ;
import parser.SVMLexer ;
import parser.SVMParser ;


public class TestAST {
    public static void main(String[] args) throws Exception {

        String fileName = "prova.simplan";

        FileInputStream is = new FileInputStream(fileName);
        ANTLRInputStream input = new ANTLRInputStream(is);
        SimpLanLexer lexer = new SimpLanLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SimpLanParser parser = new SimpLanParser(tokens);
        SimpLanVisitorImpl visitor = new SimpLanVisitorImpl();
        Node ast = visitor.visit(parser.prog()); //generazione AST

        if (lexer.lexicalErrors > 0){
            System.out.println("The program was not in the right format. Exiting the compilation process now");
        } else {
            System.out.println("Visualizing AST...");
            System.out.println(ast.toPrint(""));
        }

    }
}
