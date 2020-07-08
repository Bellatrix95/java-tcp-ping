package main.java.com.company.arguments;

import org.apache.commons.cli.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Helper class for checking arguments passed to the application.
 *
 * @author Ivana Salmanić
 */
public class ArgumentChecker implements IArgumentChecker {
    private static CommandLineParser parser = new DefaultParser();

    /**
     * Checks if command-line arguments are set properly
     * @param args application arguments
     * @return properties map with checked options
     */
    public Map<String, String> checkArgs(String[] args) throws ParseException {
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

        return parseCommandLineToProperties(commandLine);
    }

    /**
     * Parses commandLine object to map so it can be returned
     * @param commandLine application arguments
     * @return properties map with checked options
     */
    private Map<String, String> parseCommandLineToProperties(CommandLine commandLine) {
        Iterator argsIterator = commandLine.iterator();
        Map<String, String> properties = new HashMap<>();
        while(argsIterator.hasNext()) {
            Option argument = (Option) argsIterator.next();

            if(argument.getOpt().equals("p") || argument.getOpt().equals("c") ) {
                properties.put(argument.getOpt(), "clientFlag");
            } else {
                properties.put(argument.getOpt(), commandLine.getOptionValue(argument.getOpt()));
            }
        }
        return properties;
    }
}
