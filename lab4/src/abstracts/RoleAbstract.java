package abstracts;

public abstract class RoleAbstract {
    protected String fullName;

    public RoleAbstract(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
