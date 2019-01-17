package e;

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
}
