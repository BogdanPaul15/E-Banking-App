package org.poo.cb;

public interface Command {
    void execute();
}
class CommandInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command != null) {
            command.execute();
        } else {
            System.out.println("No command set.");
        }
    }
}

// Each possible command has its own class-based like command which will
// execute it via the command invoker

class AddUserCommand implements Command {
    private App app;
    private String firstname;
    private String lastname;
    private String email;
    private String address;

    public AddUserCommand(App app, String email, String firstname, String lastname, String address) {
        this.app = app;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
    }

    public void execute() {
        app.addUser(firstname, lastname, email, address);
    }
}

class AddFriendCommand implements Command {
    private App app;
    private String userEmail;
    private String friendEmail;

    public AddFriendCommand(App app, String userEmail, String friendEmail) {
        this.app = app;
        this.userEmail = userEmail;
        this.friendEmail = friendEmail;
    }

    public void execute() {
        app.addFriend(userEmail, friendEmail);
    }
}

class AddAccountCommand implements Command {
    private App app;
    private String email;
    private String currency;

    public AddAccountCommand(App app, String email, String currency) {
        this.app = app;
        this.email = email;
        this.currency = currency;
    }

    public void execute() {
        app.addAccount(email, currency);
    }
}

class AddMoneyCommand implements Command {
    private App app;
    private String email;
    private String currency;
    private Double amount;

    public AddMoneyCommand(App app, String email, String currency, Double amount) {
        this.app = app;
        this.email = email;
        this.currency = currency;
        this.amount = amount;
    }

    public void execute() {
        app.addMoney(email, currency, amount);
    }
}

class ExchangeMoneyCommand implements Command {
    private App app;
    private String email;
    private String sourceCurrency;
    private String destinationCurrency;
    private Double amount;

    public ExchangeMoneyCommand(App app, String email, String sourceCurrency, String destinationCurrency, Double amount) {
        this.app = app;
        this.email = email;
        this.sourceCurrency = sourceCurrency;
        this.destinationCurrency = destinationCurrency;
        this.amount = amount;
    }

    public void execute() {
        app.exchangeMoney(email, sourceCurrency, destinationCurrency, amount);
    }
}

class TransferMoneyCommand implements Command {
    private App app;
    private String email;
    private String friendEmail;
    private String currency;
    private Double amount;

    public TransferMoneyCommand(App app, String email, String friendEmail, String currency, Double amount) {
        this.app = app;
        this.email = email;
        this.friendEmail = friendEmail;
        this.currency = currency;
        this.amount = amount;
    }

    public void execute() {
        app.transferMoney(email, friendEmail, currency, amount);
    }
}

class BuyStocksCommand implements Command {
    private App app;
    private String email;
    private String company;
    private Integer noOfStocks;

    public BuyStocksCommand(App app, String email, String company, Integer noOfStocks) {
        this.app = app;
        this.email = email;
        this.company = company;
        this.noOfStocks = noOfStocks;
    }

    public void execute() {
        app.buyStocks(email, company, noOfStocks);
    }
}

class RecommandStocksCommand implements Command {
    private App app;

    public RecommandStocksCommand(App app) {
        this.app = app;
    }

    public void execute() {
        app.recommendStocks();
    }
}

class ListUserCommand implements Command {
    private App app;
    private String email;

    public ListUserCommand(App app, String email) {
        this.app = app;
        this.email = email;
    }

    public void execute() {
        app.listUser(email);
    }
}

class ListPortfolioCommand implements Command {
    private App app;
    private String email;

    public ListPortfolioCommand(App app, String email) {
        this.app = app;
        this.email = email;
    }

    public void execute() {
        app.listPortfolio(email);
    }
}

class BuyPremiumCommand implements Command {
    private App app;
    private String email;

    public BuyPremiumCommand(App app, String email) {
        this.app = app;
        this.email = email;
    }

    public void execute() {
        app.buyPremium(email);
    }
}
