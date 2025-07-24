package edu.wku.hospital.management.exception;

//缴费失败
public class TransactionFailedException extends  Exception {
    public TransactionFailedException(String message){
        super(message);
    }
}
