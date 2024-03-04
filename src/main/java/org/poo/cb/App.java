package org.poo.cb;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.poo.cb.AccountFactory.createAccount;
import static org.poo.cb.Main.PATH;

public class App {
    private static App singleInstance;
    private final HashMap<String, User> users;
    private ArrayList<String> stocksToBuy;
    String pathToExchanges;
    String pathToStocks;

    private App() {
        this.users = new HashMap<>();
        this.stocksToBuy = new ArrayList<>();
    }

    // Make sure that App accepts only one instance via Singleton Design Pattern
    public static App Instance() {
        if (singleInstance == null) {
            singleInstance = new App();
        }
        singleInstance.users.clear();
        return  singleInstance;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void addPaths(String pathToExchanges, String pathToStocks) {
        this.pathToExchanges = PATH + pathToExchanges;
        this.pathToStocks = PATH + pathToStocks;
    }

    public void addUser(String firstname, String lastname, String email, String address) {
        if (users.containsKey(email)) {
            System.out.println("User with " + email + " already exists");
        } else {
            User user = new User.UserBuilder(firstname, lastname, email, address)
                            .hasPremium(false)
                            .build();
            users.put(email, user);
        }
    }

    public void addFriend(String userEmail, String friendEmail) {
        if (!users.containsKey(userEmail)) {
            System.out.println("User with " + userEmail + " doesn't exist");
        } else if (!users.containsKey(friendEmail)) {
            System.out.println("User with " + friendEmail + " doesn't exist");
        } else if (users.get(userEmail).getFriends().contains(friendEmail)) {
            System.out.println("User with " + friendEmail + " is already a friend");
        } else {
            users.get(userEmail).getFriends().add(friendEmail);
            users.get(friendEmail).getFriends().add(userEmail);
        }
    }

    public void addAccount(String email, String currency) {
        User user = users.get(email);
        Account account = findAccount(user, currency);
        if (account == null) {
            // Add an account with a specific currency
            HashMap<String, Double> exchanges = getExchanges(currency);
            Account newAccount = createAccount(AccountFactory.AccountType.valueOf(currency), 0.0, exchanges);
            users.get(email).getAccounts().add(newAccount);
        }
    }

    public void addMoney(String email, String currency, Double amount) {
        User user = users.get(email);
        Account userAccount = findAccount(user, currency);
        userAccount.addMoney(amount);
    }

    public void exchangeMoney(String email, String sourceCurrency, String destinationCurrency, Double amount) {
        User user = users.get(email);
        Account sourceAccount = findAccount(user, sourceCurrency);
        Account destinationAccount = findAccount(user, destinationCurrency);
        Double lostMoney = amount * destinationAccount.getExchangeRate(sourceCurrency);
        if (sourceAccount.getAmount() < amount) {
            System.out.println("Insufficient amount in account " + sourceCurrency + " for exchange");
        } else if ((lostMoney > sourceAccount.getAmount() / 2) && !user.isHasPremium()) {
            // Send the amount of money with an extra commission of 1%
            sourceAccount.deleteMoney(lostMoney + 0.01 * amount * destinationAccount.getExchangeRate(sourceCurrency));
            destinationAccount.addMoney(amount);
        } else {
            // Users with premium accounts don't have to pay extra 1% commission
            sourceAccount.deleteMoney(lostMoney);
            destinationAccount.addMoney(amount);
        }
    }

    public void transferMoney(String email, String friendEmail, String currency, Double amount) {
        User user = users.get(email);
        User friend = users.get(friendEmail);
        Account sourceAccount = findAccount(user, currency);
        if (sourceAccount.getAmount() < amount) {
            System.out.println("Insufficient amount in account " + currency + " for transfer");
        } else if (!user.getFriends().contains(friendEmail)) {
            System.out.println("You are not allowed to transfer money to " + friendEmail);
        } else {
            Account destinationAccount = findAccount(friend, currency);
            sourceAccount.deleteMoney(amount);
            destinationAccount.addMoney(amount);
        }
    }

    public void buyStocks(String email, String company, Integer noOfStocks) {
        User user = users.get(email);
        ArrayList<Double> stockPrices = getStockPrices().get(company);
        Account userAccount = findAccount(user, "USD");
        Double stockPrice = stockPrices.get(stockPrices.size() - 1) * noOfStocks;
        if (stockPrice > userAccount.getAmount()) {
            System.out.println("Insufficient amount in account for buying stock");
        } else {
            Stock stock = new Stock(company, noOfStocks);
            user.getStocks().add(stock);
            if (user.isHasPremium()) {
                if (this.stocksToBuy.contains(company)) {
                    // Users with premium accounts get a 5% discount on recommended stocks
                    userAccount.deleteMoney(stockPrice - 0.05 * stockPrice);
                } else {
                    userAccount.deleteMoney(stockPrice);
                }
            } else {
                userAccount.deleteMoney(stockPrice);
            }
        }
    }

    public void recommendStocks() {
        ArrayList<String> stocksToBuy = new ArrayList<>();
        HashMap<String, ArrayList<Double>> stockPrices = getStockPrices();
        for (Map.Entry<String, ArrayList<Double>> entry : stockPrices.entrySet()) {
            String companyName = entry.getKey();
            ArrayList<Double> prices = entry.getValue();
            Double lastTenDays = 0.0;
            Double lastFiveDays = 0.0;

            // Iterate over prices for each company
            for (int i = 0; i < prices.size(); i++) {
                if (i > 4) {
                    lastFiveDays += prices.get(i);
                }
                lastTenDays += prices.get(i);
            }

            // Compute the average
            lastTenDays = lastTenDays / 10;
            lastFiveDays = lastFiveDays / 5;

            if (lastFiveDays > lastTenDays) {
                // Add stock to recommendations
                stocksToBuy.add(companyName);
            }
        }
        this.stocksToBuy = stocksToBuy;
        System.out.println(JsonHandler.convertToJson(new SimpleRecommendModel(stocksToBuy)));
    }

    public void listUser(String email) {
        if (!users.containsKey(email)) {
            System.out.println("User with " + email + " doesn't exist");
        } else {
            User user = users.get(email);
            System.out.println(JsonHandler.convertToJson(new SimpleUserModel(user.getEmail(), user.getFirstname(), user.getLastname(), user.getAddress(), user.getFriends())));
        }
    }

    public void listPortfolio(String email) {
        if (!users.containsKey(email)) {
            System.out.println("User with " + email + " doesn't exist");
        } else {
            User user = users.get(email);
            ArrayList<Account> accounts = user.getAccounts();
            ArrayList<SimpleAccountModel> formattedAccounts = new ArrayList<>();

            // Format each account amount to 2 decimals with double quotes
            for (Account account : accounts) {
                formattedAccounts.add(new SimpleAccountModel(account.getCurrencyName(), account.getAmount()));
            }

            System.out.println(JsonHandler.convertToJson(new PortfolioUserModel(user.getStocks(), formattedAccounts)));
        }
    }

    public void buyPremium(String email) {
        if (!users.containsKey(email)) {
            System.out.println("User with " + email + " doesn't exist");
        } else {
            User user = users.get(email);
            Account userAccount = findAccount(user, "USD");
            if (userAccount.getAmount() < 100.00) {
                System.out.println("Insufficient amount in account for buying premium option");
            } else {
                userAccount.deleteMoney(100.00);
                user.setHasPremium(true);
            }
        }
    }

    public Account findAccount(User user, String currencyName) {
        for (Account account: user.getAccounts()) {
            if (account.getCurrencyName().equals(currencyName))
                return account;
        }
        return null;
    }

    public HashMap<String, Double> getExchanges(String currencyName) {
        HashMap<String, Double> exchangeRates = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(pathToExchanges));
            String[] line;
            String[] headers = reader.readNext(); // Read headers to skip them
            while ((line = reader.readNext()) != null) {
                String currency = line[0];
                if (currency.equals(currencyName)) {
                    // Map all currency's for a specific base currency
                    for (int i = 1; i < headers.length; i++) {
                        exchangeRates.put(headers[i], Double.parseDouble(line[i]));
                    }
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return exchangeRates;
    }

    public HashMap<String, ArrayList<Double>> getStockPrices() {
        HashMap<String, ArrayList<Double>> stockPrices = new HashMap<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(pathToStocks));
            String[] line;
            String[] headers = reader.readNext();
            while ((line = reader.readNext()) != null) {
                String companyName = line[0];
                ArrayList<Double> prices = new ArrayList<>();
                // Add all stock values from the last 10 days
                for (int i = 1; i < headers.length; i++) {
                    prices.add(Double.parseDouble(line[i]));
                }
                // Map each company with its last 10 days stock values
                stockPrices.put(companyName, prices);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return stockPrices;
    }
}
