package e.model;

import javax.validation.constraints.NotNull;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;

@Entity(name = "account")
public class Account {
    @NotNull
    @Column(name = "account_id")
    @Id
    private String accountId;
    @Column(name = "password")
    private String password;

    public Account() {
    }

    public Account(@NotNull String accountId) {
        this.accountId = accountId;
    }

    public Account(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int result =
                  this.accountId == null ? 0 : this.accountId.hashCode();
        return 31 * result
                + (this.password == null ? 0 : this.password.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account a = (Account) o;
        return (a.accountId == null ? this.accountId == null
                                    : a.accountId.equals(this.accountId))
            && (a.password == null ? this.password == null
                                     : a.password.equals(this.password));
    }

    @Override
    public String toString() {
        return String.format("Account{accountId:%s,password:%s}",
                                          this.accountId, this.password);
    }
}
