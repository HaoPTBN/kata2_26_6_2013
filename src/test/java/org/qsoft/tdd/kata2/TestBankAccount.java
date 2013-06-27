package org.qsoft.tdd.kata2;

import ogr.qsoft.tdd.entity.BankAccount;
import ogr.qsoft.tdd.entity.DetailDeposit;
import ogr.qsoft.tdd.entity.DetailWithDraw;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: haopt
 * Date: 6/12/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestBankAccount {

    DetailDeposit mockDetailDeposit = mock(DetailDeposit.class);
    DetailWithDraw mockDetailWithDraw = mock(DetailWithDraw.class);
    Date dateTimes = mock(Date.class);
    BankAccountDao mockAccountDao = mock(BankAccountDao.class);
    DetailDepositDao mockDetailDepositDao = mock(DetailDepositDao.class);
    BankAccountService bankAccountService = new BankAccountService();

    @Before
    public void setUp() {
        reset(mockAccountDao);
        reset(mockDetailDepositDao);
        reset(dateTimes);
        reset(mockDetailDeposit);
        reset(mockDetailWithDraw);

        bankAccountService.setBankAccountDao(mockAccountDao);
        bankAccountService.setDetailDepositDao(mockDetailDepositDao);
        bankAccountService.setDateTimes(dateTimes);
        bankAccountService.setDetailDeposit(mockDetailDeposit);
        bankAccountService.setDetailWithDraw(mockDetailWithDraw);
    }

    @Test
    public void openNewAccount() {
        bankAccountService.openBankAccount("1234567890");

        ArgumentCaptor<BankAccount> savedAccountRecords = ArgumentCaptor.forClass(BankAccount.class);
        verify(mockAccountDao).save(savedAccountRecords.capture());

        assertEquals(savedAccountRecords.getValue().getBalance(), 0.0, 0.01);
        assertEquals(savedAccountRecords.getValue().getAccountNumber(), "1234567890");
    }

    @Test
    public void testGetAccountByAccountNumber() {
        String strParam = "123";
        when(mockAccountDao.getBankAccountByAccountNumber(strParam)).thenReturn(new BankAccount(strParam, 0, new Date()));

        BankAccount bankAccount = bankAccountService.getBankAccountByAccountNumber(strParam);
        assertNotNull(bankAccount);

        assertEquals(bankAccount.getAccountNumber(), strParam);
    }

    @Test
    public void testAccountDeposit() {
        String strParam = "123";
        when(mockAccountDao.getBankAccountByAccountNumber(strParam)).thenReturn(new BankAccount(strParam, 0, new Date()));
        when(dateTimes.getTime()).thenReturn(1000L);
        bankAccountService.deposit(strParam, 100, "Deposit money");

        ArgumentCaptor<BankAccount> savedAccountRecords = ArgumentCaptor.forClass(BankAccount.class);
        verify(mockAccountDao).getBankAccountByAccountNumber(strParam);
        verify(mockAccountDao).save(savedAccountRecords.capture());

        assertEquals(savedAccountRecords.getValue().getBalance(), 100, 0.01);
        assertEquals(savedAccountRecords.getValue().getAccountNumber(), strParam);
    }

    @Test
    public void testAddLogDetailDeposit() {
        String strParam = "123";
        when(mockAccountDao.getBankAccountByAccountNumber(strParam)).thenReturn(new BankAccount(strParam, 0, new Date()));
        when(dateTimes.getTime()).thenReturn(1000L);

        bankAccountService.deposit(strParam, 100, "Deposit money");
        ArgumentCaptor<BankAccount> savedAccountRecords = ArgumentCaptor.forClass(BankAccount.class);
        verify(mockAccountDao).save(savedAccountRecords.capture());
        assertEquals(1000L, savedAccountRecords.getValue().getOpenTimestamp().getTime());
    }

    @Test
    public void testWithDraw() {
        String strParam = "123";
        when(mockAccountDao.getBankAccountByAccountNumber(strParam)).thenReturn(new BankAccount(strParam, 2000, new Date()));
        bankAccountService.withDraw(strParam, 100, "withdraw");
        ArgumentCaptor<BankAccount> savedAccountRecords = ArgumentCaptor.forClass(BankAccount.class);
        verify(mockAccountDao).save(savedAccountRecords.capture());
        assertEquals(1900, savedAccountRecords.getValue().getBalance());

    }

    @Test
    public void testAddLogWithDraw() {
        String strParam = "123";
        when(mockAccountDao.getBankAccountByAccountNumber(strParam)).thenReturn(new BankAccount(strParam, 2000, new Date()));
        when(dateTimes.getTime()).thenReturn(1000L);

        bankAccountService.withDraw(strParam, 100, "withdraw");
        ArgumentCaptor<BankAccount> savedAccountRecords = ArgumentCaptor.forClass(BankAccount.class);
        verify(mockAccountDao).save(savedAccountRecords.capture());

        assertEquals(1900, savedAccountRecords.getValue().getBalance());
        assertEquals(1000L, savedAccountRecords.getValue().getOpenTimestamp().getTime());
        assertEquals(strParam, savedAccountRecords.getValue().getDetailWithDraws().get(0).getAccountNumber());
        assertEquals(1000L, savedAccountRecords.getValue().getDetailWithDraws().get(0).getOpenTimestamp());
    }

    @Test
    public void testGetListTransactionOccurred() {
        String strParam = "123";
        when(mockAccountDao.getBankAccountByAccountNumber(strParam)).thenReturn(new BankAccount(strParam, 5000, new Date()));
        when(dateTimes.getTime()).thenReturn(1000L).thenReturn(2000L).thenReturn(3000L).thenReturn(4000L).thenReturn(5000L);

        bankAccountService.withDraw(strParam, 100, "withdraw");
        ArgumentCaptor<BankAccount> savedAccountRecords = ArgumentCaptor.forClass(BankAccount.class);
        verify(mockAccountDao).save(savedAccountRecords.capture());
        assertEquals(4900, savedAccountRecords.getValue().getBalance());
        bankAccountService.withDraw(strParam, 200, "withdraw");
        assertEquals(4700, savedAccountRecords.getValue().getBalance());

        assertEquals(2, savedAccountRecords.getValue().getDetailWithDraws().size());
    }

    @Test
    public void testGetListTransactionFromTimeToTime() {
        String strParam = "123";
        when(mockAccountDao.getBankAccountByAccountNumber(strParam)).thenReturn(new BankAccount(strParam, 5000, new Date()));
        when(dateTimes.getTime()).thenReturn(1000L).thenReturn(2000L).thenReturn(3000L).thenReturn(4000L);

        ArgumentCaptor<BankAccount> savedAccountRecords = ArgumentCaptor.forClass(BankAccount.class);

        bankAccountService.withDraw(strParam, 100, "withdraw");
        verify(mockAccountDao).save(savedAccountRecords.capture());
        assertEquals(4900, savedAccountRecords.getValue().getBalance());
        verify(mockAccountDao).save(savedAccountRecords.capture());
        bankAccountService.withDraw(strParam, 200, "withdraw");
        assertEquals(4700, savedAccountRecords.getValue().getBalance());
        bankAccountService.withDraw(strParam, 300, "withdraw");
        assertEquals(4400, savedAccountRecords.getValue().getBalance());
        bankAccountService.withDraw(strParam, 400, "withdraw");
        assertEquals(4000, savedAccountRecords.getValue().getBalance());

        assertEquals(3, savedAccountRecords.getValue().getListWithDrawFromTimeToTime(1100L, 4200L).size());
    }

    @Test
    public void testGetListNumberTransactionNew() {
        String strParam = "123";
        when(mockAccountDao.getBankAccountByAccountNumber(strParam)).thenReturn(new BankAccount(strParam, 5000, new Date()));
        when(dateTimes.getTime()).thenReturn(1000L).thenReturn(2000L).thenReturn(3000L).thenReturn(4000L);
        ArgumentCaptor<BankAccount> savedAccountRecords = ArgumentCaptor.forClass(BankAccount.class);

        bankAccountService.withDraw(strParam, 100, "withdraw");
        verify(mockAccountDao).save(savedAccountRecords.capture());
        assertEquals(4900, savedAccountRecords.getValue().getBalance());
        verify(mockAccountDao).save(savedAccountRecords.capture());
        bankAccountService.withDraw(strParam, 200, "withdraw");
        assertEquals(4700, savedAccountRecords.getValue().getBalance());
        bankAccountService.withDraw(strParam, 300, "withdraw");
        assertEquals(4400, savedAccountRecords.getValue().getBalance());
        bankAccountService.withDraw(strParam, 400, "withdraw");
        assertEquals(4000, savedAccountRecords.getValue().getBalance());

        assertEquals(2, savedAccountRecords.getValue().getListNumberTransactionNew(2));
    }

    @Test
    public void testSaveTimeWhenOpenAccount() {

    }
}

