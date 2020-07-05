package main.java.com.company.arguments;

import org.apache.commons.cli.*;

/**
 * Helper class for checking arguments passed to the application.
 *
 * @author Ivana Salmanić
 */
public class ArgumentChecker {
    private static CommandLineParser parser = new DefaultParser();

    /**
     * Checks if command-line arguments are set properly
     * @param args application arguments
     * @return command-line handler with checked options
     */
    public static CommandLine checkCommandLineParams(String[] args) throws ParseException {
        CommandLine commandLine = parser.parse(ArgumentOption.setArgsOptions(), args);

        if (commandLine.hasOption("p")) {

            if (!commandLine.hasOption("h")) throw new ParseException("Hostname not set!");

            if (commandLine.hasOption("size")) {
                int messageSize = Integer.parseInt(commandLine.getOptionValue("size"));
                if (messageSize < 50 || messageSize > 3000)
                    throw new ParseException("Message size can be in the range of (50,3000)!");
            }
        } else if (commandLine.hasOption("c")) {

            if (!commandLine.hasOption("b")) throw new ParseException("Bind address not set!");
        } else {
            throw new ParseException("The application will exit, Pitcher or Catcher flag isn't set! ");
        }

        return commandLine;
    }
}
