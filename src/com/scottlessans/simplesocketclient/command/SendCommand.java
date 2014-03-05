package com.scottlessans.simplesocketclient.command;

import com.scottlessans.simplesocketclient.InvalidCommandUsageException;

import java.io.*;

/**
 * User: slessans
 * Date: 3/5/14
 * Time: 12:58 PM
 */
public class SendCommand extends AppCommandAbstract {


    @Override
    protected String getCommandHelpString() {
        return "Sends data to socket. Use --data=<string> to send a string (will read until " +
                "end of line). Use --file=<path> to read data from file. Path can be absolute " +
                "or relative.";
    }

    @Override
    public String getCommandName() {
        return "send";
    }

    @Override
    public void run(String input) throws Exception {

        if (input.startsWith("--data=")) {
            String data = input.substring("--data=".length());

            if (data.length() <= 0) {
                throw new InvalidCommandUsageException(
                        "data param must be string with length > 0"
                );
            }

            this.sendDataFromStream(new InputStreamReader(new ByteArrayInputStream(data.getBytes())));

        } else if (input.startsWith("--file=")) {

            String filePath = input.substring("--data=".length());

            if (filePath.length() <= 0) {
                throw new InvalidCommandUsageException(
                        "file param must be string with length > 0"
                );
            }

            if (filePath.startsWith("~/")) {
                String oldPath = filePath;
                String userHome = System.getProperty("user.home");
                if (!userHome.endsWith("/")) {
                    userHome += "/";
                }
                filePath = userHome + filePath.substring(2);
                this.getApp().getOutputStream().println(
                        "Expanded relative path '" + oldPath + "' to path '" + filePath + "'"
                );
            }


            try (
                    FileReader fileReader = new FileReader(filePath);
                    BufferedReader bufferedReader = new BufferedReader(fileReader)
            ) {
                this.sendDataFromStream(bufferedReader);
            }
        }

    }

    protected void sendDataFromStream(final Reader reader) throws IOException {

        char [] buffer = new char[512];

        int totalSent = 0;

        while (true) {
            int readLen;
            try {
                readLen = reader.read(buffer);
                totalSent += readLen;
            } catch (IOException exc) {
                // catch this exception, so as to now bubble up the
                // IOException and close the socket
                this.getApp().getOutputStream().println(
                        "Error reading data " + exc.getLocalizedMessage()
                );
                return;
            }

            this.getApp().getSocketOutputWriter().write(buffer, 0, readLen);
            this.getApp().getSocketOutputWriter().flush();

            if (readLen < buffer.length) {
                // eof
                this.getApp().getOutputStream().println(
                        "Finished sending data. " +
                                totalSent + " byte" + (totalSent == 1 ? "" : "s") + " sent."
                );
                return;
            }
        }

    }
}
