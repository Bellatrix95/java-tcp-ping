package main.java.com.company.arguments;

import org.apache.commons.cli.ParseException;

import java.util.Map;

public interface IArgumentChecker {

    Map<String, String> checkArgs(String[] args) throws ParseException;

}
