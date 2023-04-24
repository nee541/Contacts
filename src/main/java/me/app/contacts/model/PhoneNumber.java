package me.app.contacts.model;

import com.google.gson.Gson;

public class PhoneNumber {
    private long phoneNumber = 0;

    PhoneNumber() {
    }

    public PhoneNumber(String stringNumber) {
        phoneNumber = Long.parseLong(stringNumber);
    }

    public PhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return Long.toString(phoneNumber);
    }
}
