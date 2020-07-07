package test;


import main.java.com.company.arguments.ArgumentChecker;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * ArgumentChecker class - testing. Different arguments are passed to checkCommandLineParams function.
 *
 * @author Ivana Salmanić
 */
public class ArgumentCheckerTest {
    private static String[] argsWithoutFlag = new String[]{"-port", "3000", "-b", "localhost"};
    private static String[] argsForPitcher = new String[]{"-p", "-port", "3000", "-h", "localhost", "-mps", "30", "-size", "50"};
    private static String[] argsForCatcher = new String[]{"-c", "-port", "3000", "-b", "localhost"};
    private static String[] argsPitcherWrongMessageSize = new String[]{"-p", "-port", "3000", "-h", "localhost", "-mps", "30", "-size", "5000"};

    @Test
    public void givenArgsWithoutFlag_whenChecked_shouldThrowParseException() {
        String expectedMessage = "The application will exit, Pitcher or Catcher flag isn't set! ";
        try {
            ArgumentChecker.checkCommandLineParams(argsWithoutFlag);
            fail();
        } catch (ParseException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void givenArgsForPitcher_whenChecked_shouldPassWithoutAnError() {
        try {
            ArgumentChecker.checkCommandLineParams(argsForPitcher);
            assertTrue(true);
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void givenArgsForCatcher_whenChecked_shouldPassWithoutAnError() {
        try {
            ArgumentChecker.checkCommandLineParams(argsForCatcher);
            assertTrue(true);
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void givenArgsPitcherWrongMessageSize_whenChecked_shouldThrowParseException() {
        String expectedMessage = "Message size can be in the range of (50,3000)!";
        try {
            ArgumentChecker.checkCommandLineParams(argsPitcherWrongMessageSize);
            fail();
        } catch (ParseException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }

}
