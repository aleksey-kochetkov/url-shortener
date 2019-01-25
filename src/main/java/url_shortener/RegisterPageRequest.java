package url_shortener;

import javax.validation.constraints.NotNull;

public class RegisterPageRequest {
    @NotNull
    private String url;
    private Short redirectType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Short getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(Short redirectType) {
        this.redirectType = redirectType;
    }

    @Override
    public int hashCode() {
        int result = this.url == null ? 0 : this.url.hashCode();
        return 31 * result + (this.redirectType == null ? 0
                                         : this.redirectType.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RegisterPageRequest)) {
            return false;
        }
        RegisterPageRequest r = (RegisterPageRequest) o;
        return (r.url == null ? this.url == null : r.equals(this.url))
            && (r.redirectType == null ? this.redirectType == null
                             : r.redirectType.equals(this.redirectType));
    }
}
