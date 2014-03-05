package com.scottlessans.simplesocketclient;


import com.scottlessans.simplesocketclient.command.ReadCommand;
import com.scottlessans.simplesocketclient.command.SendCommand;

/**
 * User: slessans
 * Date: 3/3/14
 * Time: 5:20 PM
 */
public class Main {

    public static void main(String [] args) throws Exception {

        CommandLineApp commandLineApp = new CommandLineApp();
        commandLineApp.registerCommands(new AppCommand[]{
                new ReadCommand(),
                new SendCommand()
        });
        commandLineApp.run();

    }

}
