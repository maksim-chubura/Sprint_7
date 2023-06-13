package org.example;

public class OrderPage {
    private String firstName;
    private String lastName;
    private String address;
    private String metroName;
    private String phoneNumber;
    private String deliveryDate;
    private int rentTime;
    private String[] color;
    private String comment;

    public OrderPage (String firstName, String lastName, String address, String metroName, String phoneNumber,
                      String deliveryDate, int rentTime, String[] color, String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroName = metroName;
        this.phoneNumber = phoneNumber;
        this.deliveryDate = deliveryDate;
        this.rentTime = rentTime;
        this.color = color;
        this.comment = comment;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMetroName() {
        return metroName;
    }

    public void setMetroName(String metroName) {
        this.metroName = metroName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getRentTime() {
        return rentTime;
    }

    public void setRentTime(int rentTime) {
        this.rentTime = rentTime;
    }

    public String[] getColor() {
        return color;
    }

    public void setColor(String[] color) {
        this.color = color;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
