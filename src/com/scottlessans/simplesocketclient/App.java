package com.scottlessans.simplesocketclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintStream;
import java.util.Set;

/**
 * User: slessans
 * Date: 3/3/14
 * Time: 5:38 PM
 */
public interface App {

    // to user
    public PrintStream getOutputStream();
    public BufferedReader getInputReader();

    // to socket
    public BufferedReader getSocketInputReader();
    public BufferedWriter getSocketOutputWriter();

    // app doesn't own any streams from get. use them in run
    // and if exception is thrown, app will handle cleanup of
    // thsoe resources
    public void run() throws Exception;

    public Set<String> getCommandNames();
    public AppCommand getAppCommand(String commandName);


}
