package com.scottlessans.simplesocketclient.command;

import com.scottlessans.simplesocketclient.AppCommand;

/**
 * User: slessans
 * Date: 3/3/14
 * Time: 6:00 PM
 */
public class HelpCommand extends AppCommandAbstract {


    @Override
    protected String getCommandHelpString() {
        return "Prints app help message.";
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public void run(String input) {
        if (input == null || input.length() == 0) {
            this.getApp().getOutputStream().println(
                    "Enter exit to exit. Enter a command, or enter the command 'list' " +
                            "to get a list of available commands. Enter help <commandName> for help " +
                            "with a specific command."
            );
        } else {
            AppCommand command = this.getApp().getAppCommand(input);
            if ( command == null ) {
                this.getApp().getOutputStream().println("No command with name " + input);
                return;
            }
            command.printCommandHelp();
        }
    }
}
