package org.poo.cb;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    static final String PATH = "src/main/resources/";
    public static void main(String[] args) {
        if (args == null) {
            System.out.println("Running Main");
        } else {
            // Get the instance of the App
            App app = App.Instance();
            app.addPaths(args[0], args[1]);
            CommandInvoker invoker = new CommandInvoker();
            CommandProcessor commandProcessor = new CommandProcessor(app, invoker);

            try {
                File myObj = new File(PATH + args[2]);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    // Read the commands one by one and send the parameters to the Command Processor
                    String data = myReader.nextLine();
                    String[] params = data.split(" ");
                    commandProcessor.processCommand(params);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}