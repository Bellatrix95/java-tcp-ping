package main.java.com.company.utils;

import org.apache.commons.cli.*;

/**
 * Helper class for defining arguments passed to the application.
 *
 * @author Ivana Salmanić
 */
public class ArgumentParser {

    private static Options setOptionsForArgs() {
        Option pitcher = Option.builder("p")
                .required(false)
                .desc("Run as Pitcher.")
                .longOpt("pitcher")
                .build();
        Option catcher = Option.builder("c")
                .required(false)
                .desc("Run as Catcher.")
                .longOpt("catcher")
                .build();
        Option bind = Option.builder("b")
                .required(false)
                .desc("Catcher's property, socket server IP address.")
                .longOpt("bind")
                .hasArg()
                .build();
        Option hostname = Option.builder("h")
                .required(false)
                .desc("Pitcher's property, socket server hostname.")
                .longOpt("hostname")
                .hasArg()
                .build();
        Option port = Option.builder("port")
                .required(true)
                .desc("Port")
                .longOpt("port")
                .hasArg()
                .build();
        Option messagesPerSecond = Option.builder("mps")
                .required(false)
                .desc("The number of messages per second. Default is set to 1 MPs")
                .longOpt("mps")
                .hasArg()
                .build();
        Option messageSize = Option.builder("size")
                .required(false)
                .desc("Message size in byte in range (50,3000). The default is set to 300 bytes.")
                .longOpt("size")
                .hasArg()
                .build();

        Options options = new Options();
        options.addOption(pitcher);
        options.addOption(catcher);
        options.addOption(bind);
        options.addOption(hostname);
        options.addOption(port);
        options.addOption(messageSize);
        options.addOption(messagesPerSecond);

        return options;
    }

    /**
     * Checks if command-line arguments are properly set.
     * @param args application arguments
     * @return command-line handler object
     */
    public static CommandLine checkCommandLineParams(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(setOptionsForArgs(), args);

        if(!commandLine.hasOption("port")) throw new ParseException("Port not set!");

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
            throw new ParseException("The application will exit, Pitcher and Catcher options aren't set! ");
        }
        return commandLine;
    }
}
