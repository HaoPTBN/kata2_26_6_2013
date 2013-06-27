package ogr.qsoft.tdd.entity;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: haopt
 * Date: 6/19/13
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class DetailDeposit {

    private String accountNumber;
    private long openTimestamp;
    private long amount;
    private String description;

    public DetailDeposit() {
    }

    public DetailDeposit(String accountNumber, long openTimestamp, long amount, String description) {
        this.accountNumber = accountNumber;
        this.openTimestamp = openTimestamp;
        this.amount = amount;
        this.description = description;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getOpenTimestamp() {
        return openTimestamp;
    }

    public void setOpenTimestamp(long openTimestamp) {
        this.openTimestamp = openTimestamp;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
