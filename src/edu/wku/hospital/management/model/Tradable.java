package edu.wku.hospital.management.model;

import edu.wku.hospital.management.exception.TransactionFailedException;
/**
 * Tradable
 * @version 1.0
 * @date May 17, 2024
 */

//接口：病人医院交易

public interface Tradable {
    public void pay(double amount) throws TransactionFailedException;
    public void charge(Tradable o,double amount) throws TransactionFailedException;
}