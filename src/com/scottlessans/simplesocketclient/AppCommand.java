package com.scottlessans.simplesocketclient;

/**
 * User: slessans
 * Date: 3/3/14
 * Time: 5:34 PM
 */
public interface AppCommand {

    public App getApp();
    public void setApp(App app);

    public String getCommandName();
    public void printCommandHelp();
    public void run(String input) throws Exception;

}
