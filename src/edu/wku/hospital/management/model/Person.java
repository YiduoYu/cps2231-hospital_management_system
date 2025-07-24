package edu.wku.hospital.management.model;

//抽象类，给医生和病人规定所需的基本信息

import java.io.Serializable;

/**
 * Person
 * @version 1.0
 * @date May 17, 2024
 */
public abstract class Person implements Serializable{
    protected String name;
    protected String address;
    protected String phone;

    public Person(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Person(){
        this.name = "null";
        this.address = "wku";
        this.phone = "1234213123";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
