package org.poo.cb;
import java.util.HashMap;

abstract class Account {
    String currencyName;
    Double amount;
    transient HashMap<String, Double> exchanges;

    public String getCurrencyName() {
        return currencyName;
    }

    public Double getAmount() {
        return amount;
    }

    public void addMoney(Double amount) {
        this.amount += amount;
    }

    public void deleteMoney(Double amount) {
        this.amount -= amount;
    }

    public Double getExchangeRate(String currency) {
        return exchanges.get(currency);
    }
}

class USDAccount extends Account {
    public USDAccount(Double amount, String currencyName, HashMap<String, Double> exchanges) {
        this.amount = amount;
        this.currencyName = currencyName;
        this.exchanges = exchanges;
    }
}

class EURAccount extends Account {
    public EURAccount(Double amount, String currencyName, HashMap<String, Double> exchanges) {
        this.amount = amount;
        this.currencyName = currencyName;
        this.exchanges = exchanges;
    }
}

class GBPAccount extends Account {
    public GBPAccount(Double amount, String currencyName, HashMap<String, Double> exchanges) {
        this.amount = amount;
        this.currencyName = currencyName;
        this.exchanges = exchanges;
    }
}

class JPYAccount extends Account {
    public JPYAccount(Double amount, String currencyName, HashMap<String, Double> exchanges) {
        this.amount = amount;
        this.currencyName = currencyName;
        this.exchanges = exchanges;
    }
}

class CADAccount extends Account {
    public CADAccount(Double amount, String currencyName, HashMap<String, Double> exchanges) {
        this.amount = amount;
        this.currencyName = currencyName;
        this.exchanges = exchanges;
    }
}

class AccountFactory {
    public enum AccountType {
        USD, EUR, GBP, JPY, CAD
    }

    public static Account createAccount(AccountType accountType, Double amount, HashMap<String, Double> exchanges) {
        Account account;
        switch (accountType) {
            case USD:
                account = new USDAccount(amount, "USD", exchanges);
                break;
            case EUR:
                account = new EURAccount(amount, "EUR", exchanges);
                break;
            case GBP:
                account = new GBPAccount(amount, "GBP", exchanges);
                break;
            case JPY:
                account = new JPYAccount(amount, "JPY", exchanges);
                break;
            case CAD:
                account = new CADAccount(amount, "CAD", exchanges);
                break;
            default:
                throw new IllegalArgumentException("Unsupported account type: " + accountType);
        }
        return account;
    }
}

class SimpleAccountModel {
    String currencyName;
    String amount;

    public SimpleAccountModel(String currencyName, Double amount) {
        this.currencyName = currencyName;
        this.amount = String.format("%.2f", amount);
    }
}