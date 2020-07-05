package main.java.com.company.arguments;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

class ArgumentOption {

    private static Option createPitcherOption() {
        return Option.builder("p")
                .required(false)
                .desc("Run as Pitcher.")
                .longOpt("pitcher")
                .build();
    }

    private static Option createCatcherOption() {
        return Option.builder("c")
                .required(false)
                .desc("Run as Catcher.")
                .longOpt("catcher")
                .build();
    }

    private static Option createBindAddressOption() {
        return Option.builder("b")
                .required(false)
                .desc("Catcher's property, socket server IP address.")
                .longOpt("bind")
                .hasArg()
                .build();
    }

    private static Option createHostnameOption() {
        return Option.builder("h")
                .required(false)
                .desc("Pitcher's property, socket server hostname.")
                .longOpt("hostname")
                .hasArg()
                .build();
    }

    private static Option createPortOption() {
        return Option.builder("port")
                .required(true)
                .desc("Port")
                .longOpt("port")
                .hasArg()
                .build();
    }

    private static Option createMPSOption() {
        return Option.builder("mps")
                .required(false)
                .desc("The number of messages per second. Default is set to 1 MPs.")
                .longOpt("mps")
                .hasArg()
                .build();
    }
    private static Option createMessageSizeOption() {
        return Option.builder("size")
                .required(false)
                .desc("Message size in byte in range (50, 3000). The default is set to 300 bytes.")
                .longOpt("size")
                .hasArg()
                .build();
    }

    /**
     * Creates supported argument options object
     * @return supported argument options
     */
    static Options setArgsOptions() {
        Options options = new Options();
        options.addOption(createPitcherOption());
        options.addOption(createCatcherOption());
        options.addOption(createBindAddressOption());
        options.addOption(createHostnameOption());
        options.addOption(createPortOption());
        options.addOption(createMessageSizeOption());
        options.addOption(createMPSOption());

        return options;
    }
}
