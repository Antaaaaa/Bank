package entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private Double quantity;

    public Account() { }

    public Account(Currency currency, User user, Double quantity){
        this.currency = currency;
        this.user = user;
        this.quantity = quantity;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
