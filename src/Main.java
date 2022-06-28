
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import AST.ASTNode;
import AST.Environment;
import compiler.Address;
import compiler.CodeBlock;
import compiler.ICodeBlock;
import exceptions.DivisionByZeroException;
import exceptions.IdentifierAlreadyExistsException;
import exceptions.IncorrectNumberOfArgumentsException;
import exceptions.NullIdentifierException;
import exceptions.TypeException;
import exceptions.ValueException;
import parser.ParseException;
import parser.Parser;
import parser.TokenMgrError;
import types.IType;
import values.IValue;

public class Main {

	public static void main(String[] args) {
		if (args.length > 0) {
			fileMode(args[0]);
		} else {
			consoleMode();
		}
	}
	
	@SuppressWarnings("static-access")
	private static void consoleMode() {
		// Evaluate from console
		System.out.println("[ CONSOLE MODE ]\n");
		System.out.print("> ");
		Parser parser = new Parser(System.in);
		runParser(parser, false);
		while (true) {
			System.out.print("> ");
			parser.ReInit(System.in);
			runParser(parser, false);
		}
	}
	
	private static void fileMode(String filename) {
		try {
			// Evaluate from file
			System.out.println("[ FILE MODE ]\n");
			System.out.println("> Evaluating \""+filename+"\"");
			FileInputStream file = new FileInputStream(filename);
			Parser parser = new Parser(file);
			runParser(parser, true);
		} catch (FileNotFoundException e) {
			// Failed to open file, switch to console mode
			System.out.println("Failed to open \""+filename+"\". Starting console...\n");
			consoleMode();
		}
	}
	
	@SuppressWarnings("static-access")
	private static void runParser(Parser parser, boolean multiplePrograms) {
		try {
			if (multiplePrograms) {
				List<ASTNode> programs = parser.MultipleStart();
				System.out.println("> Detected "+programs.size()+" programs");
				int i = 0;
				for (ASTNode program : programs) {
					System.out.println("Program: "+program.toString());
					System.out.println("Type: "+program.typecheck(new Environment<IType>()).toString());
					System.out.println("Value: "+program.eval(new Environment<IValue>()).toString());
					System.out.print("Compile: ");
					ICodeBlock code = new CodeBlock();
					program.compile(code, new Environment<Address>());
					code.dump("Compila"+i);
					System.out.println("Compiled!");
					System.out.println();
				}
				System.out.println();
			} else {
				ASTNode program = parser.Start();
				System.out.println("Program: "+program.toString());
				System.out.println("Type: "+program.typecheck(new Environment<IType>()).toString());
				System.out.println("Value: "+program.eval(new Environment<IValue>()).toString()+"\n");
				ICodeBlock code = new CodeBlock();
				program.compile(code, new Environment<Address>());
				code.dump("Compila");
			}
		} catch (TypeException e) {
			System.out.println("TypeError: "+e.getMessage()+"\n");
		} catch (ValueException e) {
			System.out.println("ValueError: "+e.getMessage()+"\n");
		} catch (NullIdentifierException e) {
			System.out.println("NullIdentifierError: "+e.getMessage()+"\n");
		} catch (IdentifierAlreadyExistsException e) {
			System.out.println("IdentifierAlreadyExistsError: "+e.getMessage()+"\n");
		} catch (IncorrectNumberOfArgumentsException e) {
			System.out.println("IncorrectNumberOfArgumentsError: "+e.getMessage()+"\n");
		} catch (ParseException e) {
			System.out.println("ParseError: "+e.getMessage()+"\n");
		} catch (TokenMgrError e) {
			System.out.println("TokenMgrError: "+e.getMessage()+"\n");
		} catch (DivisionByZeroException e) {
			System.out.println("DivisionByZeroError: "+e.getMessage()+"\n");
		} catch (Exception e) {
			System.out.println("Ok, something went terribly wrong, so let's just forget any of this happened and move along, shall we? Here's the stacktrace tho:\n");
			e.printStackTrace();
		} catch (Throwable e) {
			System.out.println("Ok, something went terribly wrong, so let's just forget any of this happened and move along, shall we? Here's the stacktrace tho:\n");
			e.printStackTrace();
		}
	}
}
