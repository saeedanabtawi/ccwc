import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

public class App {
    public static void main(String[] args) throws Exception {
        String filename = null;
        String option = null;
        
        // Handle different argument patterns
        if (args.length == 0) {
            // Read from stdin
            processStdin("");
            return;
        } else if (args.length == 1) {
            if (args[0].startsWith("-")) {
                // Option only - read from stdin
                processStdin(args[0]);
                return;
            } else {
                // Filename only - default behavior
                filename = args[0];
            }
        } else {
            option = args[0];
            filename = args[1];
        }

        // Read file content
        String content = Files.readString(Path.of(filename));
        
        // Process based on option
        if (option == null) {
            // Default behavior: equivalent to -c, -l and -w
            long lines = countLines(content);
            long words = countWords(content);
            long bytes = Files.size(Path.of(filename));
            System.out.printf("%8d %8d %8d %s%n", lines, words, bytes, filename);
        } else {
            switch (option) {
                case "-c":
                    System.out.printf("%8d %s%n", Files.size(Path.of(filename)), filename);
                    break;
                case "-l":
                    System.out.printf("%8d %s%n", countLines(content), filename);
                    break;
                case "-w":
                    System.out.printf("%8d %s%n", countWords(content), filename);
                    break;
                case "-m":
                    System.out.printf("%8d %s%n", content.length(), filename);
                    break;
                default:
                    System.err.println("Invalid option. Usage: ccwc [-c|-l|-w|-m] <filename>");
                    System.exit(1);
            }
        }
    }

    private static void processStdin(String option) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        
        String finalContent = content.toString();
        if (option.isEmpty()) {
            // Default behavior for stdin
            System.out.printf("%8d %8d %8d%n", 
                countLines(finalContent),
                countWords(finalContent),
                finalContent.getBytes().length);
        } else {
            switch (option) {
                case "-c":
                    System.out.printf("%8d%n", finalContent.getBytes().length);
                    break;
                case "-l":
                    System.out.printf("%8d%n", countLines(finalContent));
                    break;
                case "-w":
                    System.out.printf("%8d%n", countWords(finalContent));
                    break;
                case "-m":
                    System.out.printf("%8d%n", finalContent.length());
                    break;
                default:
                    System.err.println("Invalid option. Usage: ccwc [-c|-l|-w|-m] <filename>");
                    System.exit(1);
            }
        }
    }

    private static long countLines(String content) {
        return content.lines().count();
    }

    private static long countWords(String content) {
        StringTokenizer tokenizer = new StringTokenizer(content);
        return tokenizer.countTokens();
    }
}
