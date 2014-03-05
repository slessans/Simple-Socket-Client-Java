package com.scottlessans.simplesocketclient.command;

import com.scottlessans.simplesocketclient.InvalidCommandUsageException;

import java.io.IOException;

/**
 * User: slessans
 * Date: 3/3/14
 * Time: 6:08 PM
 */
public class ReadCommand extends AppCommandAbstract {


    @Override
    protected String getCommandHelpString() {
        return "Reads from socket. Use --length=<int> to read a certain number " +
                "of bytes from socket. Use --delim=<string> to read until certain " +
                "delimiter is hit.";
    }

    @Override
    public String getCommandName() {
        return "read";
    }

    @Override
    public void run(String input) throws Exception {

        if (input.startsWith("--length=")) {

            int length = 0;

            try {
                length = Integer.parseInt(input.substring("--length=".length()));
            } catch(NumberFormatException exc) {
                throw new InvalidCommandUsageException(
                        "Invalid value for length: " + exc.getLocalizedMessage(),
                        exc
                );
            }

            if (length <= 0) {
                throw new InvalidCommandUsageException(
                        "Invalid value for length: must be more than 0"
                );
            }

            this.readUntilLength(length);

        } else if (input.startsWith("--delim=")) {
            String delim = input.substring("--delim=".length());

            if (delim.length() <= 0) {
                throw new InvalidCommandUsageException(
                        "Invalid value for delim, must be string with length > 0"
                );
            }

            this.readUntilDelimiter(delim);
        }

    }

    protected void readUntilDelimiter(String delim) throws IOException {

        if (delim.length() == 0) {
            return;
        }

        char [] delim_chars = delim.toCharArray();

        int delim_match_position = 0;

        StringBuilder stringBuilder = new StringBuilder();

        boolean encounteredEOF = false;
        char c;
        while(delim_match_position < delim_chars.length)
        {
            c = (char)this.getApp().getSocketInputReader().read();

            if (c < 0) {
                encounteredEOF = true;
                break;
            }

            stringBuilder.append(c);

            if (delim_chars[delim_match_position] == c) {
                // we are matching
                delim_match_position++;
            }
        }

        if (encounteredEOF) {
            this.getApp().getOutputStream().println(
                    "Could not read until delim '" + delim + "\' "
                    + "encountered, end of file. Read partial data:"
            );
            this.getApp().getOutputStream().println("<" + stringBuilder.toString() + ">");
        } else {
            // we have matched, remove delimiter from end
            String output = stringBuilder.toString();

            if (!output.endsWith(delim)) {
                throw new RuntimeException(
                        "output '" + output + "' does not end with delim '" + delim + "'"
                );
            }

            this.getApp().getOutputStream().println("Read until delmiter '" + delim + "':");
            this.getApp().getOutputStream().println(
                    "<" + output.substring(0, output.length() - delim.length()) + ">"
            );
        }
    }

    protected void readUntilLength(final int length) throws IOException {

        if (length <= 0) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        boolean reachedEOF = false;

        for (int currentLength = 0; currentLength < length; currentLength++) {
            char c = (char)this.getApp().getSocketInputReader().read();
            if (c < 0) {
                reachedEOF = true;
                break;
            }

            stringBuilder.append(c);
        }

        if (reachedEOF) {
            this.getApp().getOutputStream().println(
                    "Could not tead until length '" + length + "', reached EOF. " +
                    "Read partial data:"
            );
            this.getApp().getOutputStream().println("<" + stringBuilder.toString()  + ">");
        } else {
            this.getApp().getOutputStream().println("Read until length '" + length + "':");
            this.getApp().getOutputStream().println("<" + stringBuilder.toString()  + ">");
        }

    }


}
