package me.app.contacts.model;

import com.google.gson.Gson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PhoneNumber {
    @JsonProperty("phoneNumber")
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = Long.parseLong(phoneNumber);
    }

    @Override
    public String toString() {
        return Long.toString(phoneNumber);
    }
}
