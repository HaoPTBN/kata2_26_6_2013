package ogr.qsoft.tdd.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: haopt
 * Date: 6/12/13
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccount {

    private String accountNumber;
    private long balance;
    private Date openTimestamp;
    private List<String> description = new ArrayList<String>();
    private List<DetailDeposit> detailDeposits = new ArrayList<DetailDeposit>();
    private List<DetailWithDraw> detailWithDraws = new ArrayList<DetailWithDraw>();

    public int getListNumberTransactionNew(int n) {
        List<DetailWithDraw> listReturn = new ArrayList<DetailWithDraw>();
        int index = detailWithDraws.size();
        int count = 0;
        for (int i = index - 1; i >= 0; i--) {
            if (count < n) {
                listReturn.add(detailWithDraws.get(i));
            } else {
                break;
            }
            count++;
        }
        return listReturn.size();
    }

    public List<DetailWithDraw> getListWithDrawFromTimeToTime(long timeStart, long timeEnd) {
        List<DetailWithDraw> listReturn = new ArrayList<DetailWithDraw>();
        for (Iterator<DetailWithDraw> it = detailWithDraws.iterator(); it.hasNext(); ) {
            DetailWithDraw dwd = it.next();
            if (dwd.getOpenTimestamp() > timeStart && dwd.getOpenTimestamp() < timeEnd) {
                listReturn.add(dwd);
            }
        }
        return listReturn;
    }

    public List<DetailWithDraw> getDetailWithDraws() {
        return detailWithDraws;
    }

    public void setDetailWithDraws(List<DetailWithDraw> detailWithDraws) {
        this.detailWithDraws = detailWithDraws;
    }

    public BankAccount() {
    }

    public BankAccount(String accountNumber, long balance, Date openTimestamp) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.openTimestamp = openTimestamp;

    }

    public void addWithDraw(DetailWithDraw detailWithDraw) {
        this.detailWithDraws.add(detailWithDraw);
    }

    public void addDetailDeposit(DetailDeposit detailDeposit) {
        this.detailDeposits.add(detailDeposit);
    }

    public void addDescription(String str) {
        this.description.add(str);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Date getOpenTimestamp() {
        return openTimestamp;
    }

    public void setOpenTimestamp(Date openTimestamp) {
        this.openTimestamp = openTimestamp;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<DetailDeposit> getDetailDeposits() {
        return detailDeposits;
    }

    public void setDetailDeposits(List<DetailDeposit> detailDeposits) {
        this.detailDeposits = detailDeposits;
    }
}
