package edu.wku.hospital.management.model;

import edu.wku.hospital.management.exception.TransactionFailedException;

//医院缴费

public class Hospital implements Tradable{
    @Override
    public void pay(double amount) throws TransactionFailedException {
        // TODO Auto-generated method stub
        System.out.println("Not yet realized");
    }

    @Override
    public void charge(Tradable o,double amount) throws TransactionFailedException {
        o.pay(amount);
    }
}
