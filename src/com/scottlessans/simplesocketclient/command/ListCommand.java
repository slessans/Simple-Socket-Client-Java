package com.scottlessans.simplesocketclient.command;

/**
 * User: slessans
 * Date: 3/3/14
 * Time: 5:47 PM
 */
public class ListCommand extends AppCommandAbstract {


    @Override
    protected String getCommandHelpString() {
        return "Prints list of available commands";
    }


    @Override
    public String getCommandName() {
        return "list";
    }

    @Override
    public void run(String input) {
        this.getApp().getOutputStream().println("Available commands:");
        for(String commandName : this.getApp().getCommandNames()) {
            this.getApp().getOutputStream().println(" - " + commandName);
        }
    }
}
