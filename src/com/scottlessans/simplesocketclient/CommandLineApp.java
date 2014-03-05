package com.scottlessans.simplesocketclient;

import com.scottlessans.simplesocketclient.command.HelpCommand;
import com.scottlessans.simplesocketclient.command.ListCommand;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: slessans
 * Date: 3/3/14
 * Time: 5:36 PM
 */
public class CommandLineApp implements App {

    protected Map<String, AppCommand> appCommands;

    protected PrintStream outputStream;
    protected BufferedReader inputReader;
    protected BufferedReader socketInputReader;
    protected BufferedWriter socketOutputWriter;

    public CommandLineApp() {
        this.appCommands = new HashMap<>();
    }

    public void registerCommand(AppCommand appCommand) {
        appCommand.setApp(this);
        this.appCommands.put(appCommand.getCommandName(), appCommand);
    }

    public void registerCommands(AppCommand [] appCommandsList) {
        for(AppCommand appCommand : appCommandsList) {
            this.registerCommand(appCommand);
        }
    }

    @Override
    public PrintStream getOutputStream() {
        return this.outputStream;
    }

    @Override
    public BufferedReader getInputReader() {
        return this.inputReader;
    }

    @Override
    public BufferedReader getSocketInputReader() {
        return this.socketInputReader;
    }

    @Override
    public BufferedWriter getSocketOutputWriter() {
        return this.socketOutputWriter;
    }

    public void run() throws Exception {

        this.registerCommand(new ListCommand());
        this.registerCommand(new HelpCommand());

        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
        this.outputStream = System.out;

        System.out.println("Gathering connection info...");

        System.out.print("Enter host: ");
        final String host = inputReader.readLine();

        System.out.print("Enter port: ");
        final int port = Integer.parseInt(inputReader.readLine());

        System.out.println("Attempting to connect to '" + host + "' over port " + port);

        try (
                Socket socket = new Socket(host, port);
                BufferedWriter out =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream()));
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()))
        )
        {

            this.socketOutputWriter = out;
            this.socketInputReader = in;

            this.getOutputStream().println("Connected.");
            this.runCommand("help", null);

            while(true) {
                System.out.print("% ");
                String input = inputReader.readLine();

                String [] parts = input.split("\\s");

                String command = parts[0].trim();
                String commandInput = input.substring(command.length());

                // left trim
                commandInput = commandInput.replaceAll("^\\s+","");

                if ("exit".equals(command)) {
                    break;
                }
                try {
                    this.runCommand(command, commandInput);
                } catch (InvalidCommandUsageException exc) {
                    this.getOutputStream().println("Invalid usage of command '" + command + "':");
                    this.getOutputStream().println(exc.getMessage());
                }
            }
        }

    }

    protected void runCommand(String name, String input) throws Exception {
        if (input == null) input = "";
        AppCommand command = this.appCommands.get(name);
        if ( command == null ) {
            this.getOutputStream().println("No command with name " + name);
            return;
        }
        command.run(input);
    }

    @Override
    public Set<String> getCommandNames() {
        return this.appCommands.keySet();
    }

    @Override
    public AppCommand getAppCommand(String commandName) {
        return this.appCommands.get(commandName);
    }
}
