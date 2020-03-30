package repository;

import entity.Currency;

import java.util.List;

public interface CurrencyRepository{
    void insertCurrency(String name, Double value);
    List<Currency> findAllCurrencies();
    Currency getCurrencyByName(String name);
}