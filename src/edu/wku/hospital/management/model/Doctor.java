package edu.wku.hospital.management.model;

import java.io.Serializable;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

/**
 * Doctor 
 * @author Yiduo Yu
 * @version 1.0
 * @date May 17, 2024
 */
//医生信息

public class Doctor extends Person implements Serializable {

    private String userID;
    private ArrayList<Patient> patients = new ArrayList<>();
    private String passwordHash;

    public Doctor(String name, String department, String phone, String password,String userID){
        super(name, department, phone);
        this.passwordHash = hashPassword(password);
        this.userID = userID;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your userID");
        String userID = scanner.nextLine();
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your department:");
        String department = scanner.nextLine();
        System.out.println("Enter your phone number:");
        String phone = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        Doctor doctor = new Doctor(name, department, phone, password, userID);
        System.out.println(doctor.getName());
    }



    public Doctor(){
        super("dr. x","null","null");
    }
    public ArrayList<Patient> getPatients() {
        return patients;
    }
    public void setPatients(ArrayList<Patient> list){
        this.patients = patients;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }



    public String getName() {
        return super.getName();//super.getName()
    }

    public void setName(String name) {
        super.setName(name);
    }

    private String hashPassword(String password) {
        char[] chars = new char[password.length()];
        for (int i =0;i<password.length();i++)
           chars[i] = (char)(password.charAt(i)*7+password.charAt(i)%20);
        String str = String.valueOf(chars);
            return str;
        }



    public boolean checkPassword(String password){
        return this.passwordHash.equals(hashPassword(password));
    }
}
    
