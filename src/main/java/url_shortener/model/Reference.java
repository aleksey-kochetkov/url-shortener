package url_shortener.model;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;


@Entity(name = "reference")
public class Reference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @Column
    private String url;
    @Column(name = "redirect_type")
    private short redirectType;
    @Column(name = "short_url")
    private String shortUrl;
    @Column
    private int count;

    public Reference() {
    }

    public Reference(Account account, String url, short redirectType,
                                                       String shortUrl) {
        this.account = account;
        this.url = url;
        this.redirectType = redirectType;
        this.shortUrl = shortUrl;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public short getRedirectType() {
        return this.redirectType;
    }

    public void setRedirectType(short redirectType) {
        this.redirectType = redirectType;
    }

    public String getShortUrl() {
        return this.shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incCount() {
        this.count++;
    }

    @Override
    public int hashCode() {
        int result = this.id;
        result = result * 31
                  + (this.account == null ? 0 : this.account.hashCode());
        result = result * 31
                          + (this.url == null ? 0 : this.url.hashCode());
        result = result * 31 + this.redirectType;
        result = result * 31
                + (this.shortUrl == null ? 0 : this.shortUrl.hashCode());
        return result * 31 + count;
    }

    @Override
    public String toString() {
        return String.format("Reference{id:%d,account:%s,url:%s,"
                      + "redirectType:%d,shortUrl:%s,count:%d}", this.id,
                    this.account.toString(), this.url, this.redirectType,
                                              this.shortUrl, this.count);
    }
}
