package repository;

import entity.Account;
import entity.Currency;
import entity.User;

import java.util.List;

public interface AccountRepository {
    void insertAccount(Currency currency, User user, Double value);
    List<Account> findAllAccounts();
    List<Account> findAccountsByUser(User user);
    Account getAccountByUserAndCurrency(User user, Currency currency);
}
