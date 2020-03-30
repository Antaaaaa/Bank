package entity;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accountList;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false, unique = true)
    private String phone;

    @Column (nullable = true)
    private String email;

    public User() {}

    public User(String name, String phone, String email){
        this.name = name;
        if (phone.length() == 10) this.phone = phone;
        if (email.matches("^(.+)@(.+)$"))
            this.email = email;
        else this.email = "";
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void addAccount(Account account){
        accountList.add(account);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
