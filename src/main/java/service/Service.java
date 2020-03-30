package service;

import entity.Account;
import entity.Currency;
import entity.Transaction;
import entity.User;
import repository.AccountRepository;
import repository.CurrencyRepository;
import repository.Repository;
import repository.UserRepository;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

public class Service implements Repository, CurrencyRepository, AccountRepository, UserRepository {
    private static EntityManagerFactory emFactory;
    private static EntityManager em;

    static{
        emFactory = Persistence.createEntityManagerFactory("BankJPA");
        em = emFactory.createEntityManager();
    }
    private static void transactionCommit(Object c) {
        em.getTransaction().begin();
        try {
            em.persist(c);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public void insertCurrency(String name, Double value){
        transactionCommit(new Currency(name, value));
    }

    public void insertAccount(Currency currency, User user, Double value){
        transactionCommit(new Account(currency,user,value));
    }

    public void insertUser(String name, String phone, String email){
        transactionCommit(new User(name,phone,email));
    }

    /* public void insertTransaction(Account from, Account to, double sum){
        transactionCommit(new Transaction(from, to, sum));
    } */

    public List<Account> findAllAccounts(){
        return em.createQuery("Select a from Account a", Account.class).getResultList();
    }
    public List<Account> findAccountsByUser(User user){
        TypedQuery<Account> accountTypedQuery = em.createQuery(
                "SELECT a from Account a where a.user = :user", Account.class);
        accountTypedQuery.setParameter("user", user);
        return accountTypedQuery.getResultList();

    }
    public List<User> findAllUsers(){
        return em.createQuery("Select a from User a", User.class).getResultList();
    }
    public List<Currency> findAllCurrencies(){
        return em.createQuery("Select a from Currency a", Currency.class).getResultList();
    }

    /*public List<Transaction> findAllTransactions(){
        return em.createQuery("Select a from Transaction a", Transaction.class).getResultList();
    }*/

    public void addMoney(Account account, Currency currencyInput, Double value) {
        List<Account> list = findAllAccounts();
        if (list.contains(account)){
            EntityTransaction entityTransaction = em.getTransaction();
            try{
                double coefficient = transformCurrency(currencyInput, account.getCurrency());
                account.setQuantity(account.getQuantity() + coefficient * value);
                entityTransaction.begin();
                em.persist(account);
                entityTransaction.commit();
                transaction(account, account, coefficient * value);
            } catch (Exception e){
                if (entityTransaction.isActive())
                    entityTransaction.rollback();
            }
        }
    }

    public Double transformCurrency(Currency from, Currency to){
      return from.getValue()/to.getValue();
    }

    public void transaction(Account accountFrom, Account accountTo, Double value) {
        if (em == null && emFactory == null) return;
        List<Account> list = findAllAccounts();
        if (list.contains(accountFrom) && list.contains(accountTo) && (accountFrom.getQuantity() >= value)) {
            double coefficient = transformCurrency(accountFrom.getCurrency(), accountTo.getCurrency());
            EntityTransaction et = em.getTransaction();
            et.begin();
            try {
                accountFrom.setQuantity(accountFrom.getQuantity() - value);
                accountTo.setQuantity(accountTo.getQuantity() + value * coefficient);
                Transaction transaction = new Transaction(accountFrom, accountTo, value * coefficient);
                em.persist(accountFrom);
                em.persist(accountTo);
                em.persist(transaction);
                et.commit();
            } catch (Exception e) {
                if (et.isActive())
                    et.rollback();
            }
        }
    }

    public void convertAllToUAH(User user) {
        List<Account> accounts = findAccountsByUser(user);

        TypedQuery<Currency> typedQueryCurrency = em.createQuery(
                "SELECT c from Currency c where c.name = :name", Currency.class);
        typedQueryCurrency.setParameter("name", "UAH");
        Currency nationalCurrency = typedQueryCurrency.getSingleResult();

        TypedQuery<Account> typedQueryAccount = em.createQuery(
                "SELECT a from Account a where a.currency = :nationalCurrency and a.user = :user", Account.class);
        typedQueryAccount.setParameter("nationalCurrency", nationalCurrency);
        typedQueryAccount.setParameter("user", user);

        Account nationalCurrencyAccount = typedQueryAccount.getSingleResult();
        Double summary = nationalCurrencyAccount.getQuantity();
        try {
            for (Account account : accounts) {
                if (account != nationalCurrencyAccount) {
                    double coefficient = transformCurrency(account.getCurrency(), nationalCurrency);
                    summary += coefficient * account.getQuantity();
                    transaction(account, nationalCurrencyAccount, account.getQuantity());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Double summary(User user){
        List<Account> accounts = findAccountsByUser(user);

        TypedQuery<Currency> typedQueryCurrency = em.createQuery(
                "SELECT c from Currency c where c.name = :name", Currency.class);
        typedQueryCurrency.setParameter("name", "UAH");
        Currency nationalCurrency = typedQueryCurrency.getSingleResult();


        Double summary = Double.valueOf(0);
        for (Account account : accounts){
            double coefficient = transformCurrency(account.getCurrency(), nationalCurrency);
            summary += coefficient*account.getQuantity();
        }
        return summary;
    }

    public void close(){
        if (em != null) em.close();
        if (emFactory != null) emFactory.close();
    }

    public Account getAccountByUserAndCurrency(User user, Currency currency){
        TypedQuery<Account> typedQuery = em.createQuery(
                "SELECT a from Account a where a.user = :user and a.currency = :currency", Account.class);
        typedQuery.setParameter("user", user);
        typedQuery.setParameter("currency", currency);
        return typedQuery.getSingleResult();
    }
    public Currency getCurrencyByName(String name){
        TypedQuery<Currency> typedQuery = em.createQuery(
                "SELECT c from Currency c where c.name = :name", Currency.class);
        typedQuery.setParameter("name", name);
        return typedQuery.getSingleResult();
    }
    public User getUserByNumber(String phone){
        TypedQuery<User> typedQuery = em.createQuery(
                "SELECT u from User u where u.phone = :phone", User.class);
        typedQuery.setParameter("phone", phone);
        return typedQuery.getSingleResult();
    }
}
