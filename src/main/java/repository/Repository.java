package repository;

import entity.Account;
import entity.Currency;
import entity.User;

import java.util.List;

public interface Repository{
    void addMoney(Account account, Currency currencyInput, Double value);
    void transaction(Account accountFrom, Account accountTo, Double value);
    Double summary(User user);
}
