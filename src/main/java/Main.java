import entity.Currency;
import service.Service;

public class Main {
    public static void main(String[] args){
        Service service = new Service();

        service.insertUser("Anatoliy", "0661112233", "name@gmail.com");
        service.insertUser("Andrew", "0662223344", "andrew@gmail.com");

        service.insertCurrency("UAH", 1d);
        service.insertCurrency("EUR", 32d);
        service.insertCurrency("USD", 28d);

        service.insertAccount(
                service.getCurrencyByName("UAH"),
                service.getUserByNumber("0661112233"),
                Double.valueOf(100));

        service.insertAccount(
                service.getCurrencyByName("EUR"),
                service.getUserByNumber("0661112233"),
                Double.valueOf(100)
        );


        service.insertAccount(
                service.getCurrencyByName("USD"),
                service.getUserByNumber("0661112233"),
                Double.valueOf(100)
        );

        service.insertAccount(
                service.getCurrencyByName("USD"),
                service.getUserByNumber("0662223344"),
                Double.valueOf(50)
        );

        System.out.println("Users summary in UAH = " + service.summary(service.getUserByNumber("0661112233")));
        System.out.println("Users summary in UAH = " + service.summary(service.getUserByNumber("0662223344")));

        service.addMoney(
                service.getAccountByUserAndCurrency(
                        service.getUserByNumber("0661112233"),
                        service.getCurrencyByName("UAH")),
                service.getCurrencyByName("UAH"),
                100D);

        service.addMoney(
                service.getAccountByUserAndCurrency(
                        service.getUserByNumber("0662223344"),
                        service.getCurrencyByName("USD")),
                service.getCurrencyByName("UAH"),
                100D);

        service.transaction(
                service.getAccountByUserAndCurrency(
                        service.getUserByNumber("0661112233"),
                        service.getCurrencyByName("UAH")),
                service.getAccountByUserAndCurrency(
                        service.getUserByNumber("0662223344"),
                        service.getCurrencyByName("USD")),
                90D);
        service.close();


    }

}
