package org.example;

public class LogCourier {
    private String login;
    private String password;
    public LogCourier() {

    }
    public LogCourier(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public LogCourier from(Courier courier) {
        return new LogCourier(courier.getLogin(), courier.getPassword());
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
