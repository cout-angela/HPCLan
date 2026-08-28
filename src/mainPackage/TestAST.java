package mainPackage;
import java.io.FileInputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import ast.HPCLanVisitorImpl;
import ast.Node;

import parser.HPCLanLexer ;
import parser.HPCLanParser ;
import parser.SVMLexer ;
import parser.SVMParser ;


public class TestAST {
    public static void main(String[] args) throws Exception {

        String fileName = "prova.simplan";

        FileInputStream is = new FileInputStream(fileName);
        ANTLRInputStream input = new ANTLRInputStream(is);
        HPCLanLexer lexer = new HPCLanLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        HPCLanParser parser = new HPCLanParser(tokens);
        HPCLanVisitorImpl visitor = new HPCLanVisitorImpl();
        Node ast = visitor.visit(parser.prog()); //generazione AST

        if (lexer.lexicalErrors > 0){
            System.out.println("The program was not in the right format. Exiting the compilation process now");
        } else {
            System.out.println("Visualizing AST...");
            System.out.println(ast.toPrint(""));
        }

    }
}
