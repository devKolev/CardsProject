package com.devkolev.cards.model;


public class Phone_contact {


    String name;

    public Phone_contact(String name, String phone) {
        this.name = name;
        this.phone = phone;

    }

    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
