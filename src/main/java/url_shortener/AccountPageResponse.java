package url_shortener;

public class AccountPageResponse {
    private boolean success;
    private String description;
    private String password;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int result = (this.success ? 31 : 0)
          + (this.description == null ? 0 : this.description.hashCode());
        return result * 31
                + (this.password == null ? 0 : this.password.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AccountPageResponse)) {
            return false;
        }
        AccountPageResponse r = (AccountPageResponse) o;
        return r.success == this.success
            && r.description == null ? this.description == null
                                 : r.description.equals(this.description)
            && r.password == null ? this.password == null
                                      : r.password.equals(this.password);
    }
}
