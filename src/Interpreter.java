import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.List;

public class Interpreter {
    // Charset supported by the interpreter
    // ...
    // "Support" is a very loose word, in this case
    public static final Charset CHARSET = Charset.forName("UTF-8");

    // Environment for the commands to execute on
    private Environment env;

    public Interpreter(DataBase db) {
        env = new Environment(db);
    }

    // Entry point into the interpreter
    public void runOn(InputStream in, boolean verbosity) {
        InputStreamReader reader = new InputStreamReader(in, CHARSET);
        env.setVerbosity(verbosity);

        Tokenizer tokenizer = new Tokenizer(reader);
        Parser parser = new Parser(tokenizer.getTokens());
        for (Command command : parser.parse()) {
            try {
                command.execute(env);
            } catch (QueryException e) {
                System.err.println(e.getMessage());
            }
        }

        List<ParseException> errors = parser.getErrors();

        if (!errors.isEmpty()) {
            System.err.println("\nSyntax Errors:");

            for (ParseException e : errors) {
                System.err.println(e.getMessage());
            }
        }
    }

    // Runs a REPL on an InputStream
    public void runRepl(InputStream in) {
        System.out.println("Welcome to the SURLY REPL");
        System.out.println(
                "Each line must be a full command and end with a ';'");
        System.out.println("Press <C-c> or <C-d> to exit the REPL\n");

        Scanner scanner = new Scanner(in);

        ByteArrayInputStream stream;

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            stream = new ByteArrayInputStream(line.getBytes(CHARSET));
            runOn(stream, true);
        }
    }
}

