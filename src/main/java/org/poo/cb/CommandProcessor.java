package org.poo.cb;

public class CommandProcessor {
    private App app;
    private CommandInvoker invoker;

    public CommandProcessor(App app, CommandInvoker invoker) {
        this.app = app;
        this.invoker = invoker;
    }

    public void processCommand(String[] args) {
        Command command = createCommand(args);
        invoker.setCommand(command);
        invoker.executeCommand();
    }

    private Command createCommand(String[] args) {
        if (args.length > 0) {
            String command = args[0] + " " + args[1];

            // Process every command using Command Design Pattern
            switch (command) {
                case "CREATE USER":
                    String address = "";
                    for (int i = 5; i < args.length; i++) {
                        if (i == 5) {
                            address += args[i];
                        } else {
                            address += " " + args[i];
                        }
                    }
                    return new AddUserCommand(app, args[2], args[3], args[4], address);
                case "ADD FRIEND":
                    return new AddFriendCommand(app, args[2], args[3]);
                case "ADD ACCOUNT":
                    return new AddAccountCommand(app, args[2], args[3]);
                case "ADD MONEY":
                    return new AddMoneyCommand(app, args[2], args[3], Double.parseDouble(args[4]));
                case "EXCHANGE MONEY":
                    return new ExchangeMoneyCommand(app, args[2], args[3], args[4], Double.parseDouble(args[5]));
                case "TRANSFER MONEY":
                    return new TransferMoneyCommand(app, args[2], args[3], args[4], Double.parseDouble(args[5]));
                case "BUY STOCKS":
                    return new BuyStocksCommand(app, args[2], args[3], Integer.parseInt(args[4]));
                case "RECOMMEND STOCKS":
                    return new RecommandStocksCommand(app);
                case "LIST USER":
                    return new ListUserCommand(app, args[2]);
                case "LIST PORTFOLIO":
                    return new ListPortfolioCommand(app, args[2]);
                case "BUY PREMIUM":
                    return new BuyPremiumCommand(app, args[2]);
                default:
                    return null;
            }
        } else {
            System.out.println("Command with no arguments.");
            return null;
        }
    }
}
