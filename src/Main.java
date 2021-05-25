import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        DataBase db = new DataBase();
        Interpreter interpreter = new Interpreter(db);

        if (args.length > 0) {
            boolean repl = false;
            boolean verbose = true;

            // Parse args
            for (String arg : args) {
                // Start the repl after files are interpreted
                if (arg.equals("--repl")) {
                    repl = true;
                }
                // Run the next file as silent
                else if (arg.equals("-s") || arg.equals("--silent")) {
                    verbose = false;
                }
                // Run a file through the interpreter
                else {
                    try {
                        interpreter.runOn(new FileInputStream(arg), verbose);
                        System.out.println();
                    } catch (FileNotFoundException e) {
                        System.out.println("File " + arg + " not found");
                    }
                    // Default to verbose mode
                    verbose = true;
                }
            }

            if (repl) {
                interpreter.runRepl(System.in);
            }
        } else {
            interpreter.runOn(System.in, true);
        }
    }
}
