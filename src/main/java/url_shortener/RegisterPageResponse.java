package url_shortener;

public class RegisterPageResponse {
    private String shortUrl;

    public RegisterPageResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    @Override
    public int hashCode() {
        return this.shortUrl == null ? 0 : this.shortUrl.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegisterPageResponse)) {
            return false;
        }
        RegisterPageResponse r = (RegisterPageResponse) o;
        return r.shortUrl == null ? this.shortUrl == null
                                      : r.shortUrl.equals(this.shortUrl);
    }
}
