package entity;

import com.sun.istack.internal.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Account accountFrom;

    @ManyToOne
    private Account accountTo;

    @Column
    private Double amount;

    public Transaction() {
    }

    public Transaction(Account accountFrom, Account accountTo, double amount){
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
