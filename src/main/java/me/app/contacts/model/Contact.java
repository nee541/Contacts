package me.app.contacts.model;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact implements java.io.Serializable {
    private Long id;
    private String name = null;

    private PhoneNumber phoneNumber;

    private Map<String, String> extraInfoMap = new HashMap<String, String>();

    public Contact() {
    }
    public Contact(String name, PhoneNumber phoneNumber, Map<String, String> extraInfoMap) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.extraInfoMap = extraInfoMap;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Map<String, String> getExtraInfoMap() {
        return extraInfoMap;
    }

    public String getExtraInfoJsonString() {
        Gson gsonObj = new Gson();
        return gsonObj.toJson(extraInfoMap);
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("phoneNumber")
    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setExtraInfoMap(String jsonString) {
        this.extraInfoMap = new Gson().fromJson(jsonString, java.util.HashMap.class);
    }

    @JsonProperty("extraInfo")
    public void setExtraInfoMap(Map<String, String> extraInfoMap) {
        this.extraInfoMap = extraInfoMap;
    }

    // Object overrides ---------------------------------------------------------------------------

    /**
     * The user ID is unique for each User. So this should compare User by ID only.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Contact) && (id != null)
                ? id.equals(((Contact) other).id)
                : (other == this);
    }

    /**
     * The user ID is unique for each User. So User with same ID should return same hashcode.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (id != null)
                ? (this.getClass().hashCode() + id.hashCode())
                : super.hashCode();
    }

    /**
     * Returns the String representation of this User. Not required, it just pleases reading logs.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Contact[id=%d,name=%s,phone number=%d, extra information=%s]",
                id, name, phoneNumber.getNumber(), getExtraInfoJsonString());
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode contact = mapper.createObjectNode();
        contact.put("id", id);
        contact.put("name", name);
        contact.put("phoneNumber", phoneNumber.getNumber());
        ObjectNode extraInfo = mapper.createObjectNode();
        for (Map.Entry<String, String> entry : extraInfoMap.entrySet()) {
            extraInfo.put(entry.getKey(), entry.getValue());
        }
        contact.set("extraInfo", extraInfo);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(contact);
    }

}

