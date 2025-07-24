package edu.wku.hospital.management.model;

import java.io.Serializable;
import edu.wku.hospital.management.exception.TransactionFailedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

//病人信息

/**
 * Patient 
 * @author Yiduo Yu
 * @version 1.0
 * @date May 17, 2024
 */
public class Patient extends Person implements Tradable, Serializable {

    private String id;
    private double balance;
    private int age;
    private HashMap<String,String> patientRecords = new HashMap<>();
    private Gender gender;



    //枚举，防止输入MA 和 FE 以外的其他东西
    public enum Gender{
        MA,FE
    }

    public Patient(String name, String address, String phone, int age, Gender gender) {
        super(name, address, phone);
        this.age = age;
        this.id = age + "" +  "" + new Date().getDay() + "" + new Date().getMinutes() + "" + new Date().getSeconds() +this.getName();
        this.gender = gender;
        this.balance = 2000;//every patient should prepay 2000 to get service
    }

    public void addRecords(String k,String v){
        patientRecords.put(k,v);

    }

    public String getRecords(String k){
        return patientRecords.get(k);
    }

    @Override
    public void pay(double amount) throws TransactionFailedException {
        if (amount < balance ) {
            balance -= amount;
        } else {
            throw new TransactionFailedException( this.getName() + " has insufficient balance.");
        }

    }

    @Override
    public void charge(Tradable o, double amount) throws TransactionFailedException {
        throw new UnsupportedOperationException("not supported");
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getBalance() {
        return balance;
    }

    public String  getId() {
        return id;
    }
}
