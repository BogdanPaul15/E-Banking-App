package org.poo.cb;

import java.util.ArrayList;

public class User {
    private String email;
    private String firstname;
    private String lastname;
    private String address;
    private boolean hasPremium;
    private ArrayList<String> friends;
    private ArrayList<Account> accounts;
    private ArrayList<Stock> stocks;

    private User(UserBuilder builder) {
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.email = builder.email;
        this.address = builder.address;
        this.friends = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.stocks = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public boolean isHasPremium() {
        return hasPremium;
    }

    public void setHasPremium(boolean hasPremium) {
        this.hasPremium = hasPremium;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public static class UserBuilder {
        private String email;
        private String firstname;
        private String lastname;
        private String address;
        private boolean hasPremium;

        public UserBuilder(String firstname, String lastname, String email, String address) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.email = email;
            this.address = address;
        }

        // Manage the premium option for users via Builder Design Pattern
        public UserBuilder hasPremium(boolean hasPremium) {
            this.hasPremium = hasPremium;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

// Different models with specific attributes to print
// the User instance in Json format

class SimpleUserModel {
    private String email;
    private String firstname;
    private String lastname;
    private String address;
    private ArrayList<String> friends;

    public SimpleUserModel(String email, String firstname, String lastname, String address, ArrayList<String> friends) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.friends = friends;
    }
}

class PortfolioUserModel {
    private ArrayList<Stock> stocks;
    private ArrayList<SimpleAccountModel> accounts;

    public PortfolioUserModel(ArrayList<Stock> stocks, ArrayList<SimpleAccountModel> accounts) {
        this.stocks = stocks;
        this.accounts = accounts;
    }
}
