package com.scottlessans.simplesocketclient.command;

import com.scottlessans.simplesocketclient.App;
import com.scottlessans.simplesocketclient.AppCommand;

/**
 * User: slessans
 * Date: 3/3/14
 * Time: 5:48 PM
 */
public abstract class AppCommandAbstract implements AppCommand {

    private App app;

    public App getApp() {
        return this.app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void printCommandHelp()
    {
        this.getApp().getOutputStream().println(this.getCommandHelpString());
    }

    abstract protected String getCommandHelpString();


}
