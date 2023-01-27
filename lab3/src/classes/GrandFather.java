package classes;

import abstracts.RoleAbstract;
import interfaces.IGrandFather;

import java.util.Objects;

public class GrandFather extends RoleAbstract implements IGrandFather {
    private String shortName;

    public GrandFather(String fullName) {
        super(fullName);
    }


    public GrandFather(String fullName, String shortName) {
        super(fullName);
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public void compareName() {
        if (shortName.equals(fullName)) {
            System.out.printf("Дедушку звали %s\n", shortName);
        } else {
            System.out.printf("%s это сокращение имени %s\n", shortName, fullName);
        }
    }

    @Override
    public void joinStory() {
        System.out.printf("%s joined story", fullName);
    }

    @Override
    public void leaveStory() {
        System.out.printf("%s left story", fullName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GrandFather)) {
            return false;
        }
        GrandFather grandFather = (GrandFather) obj;
        return Objects.equals(this.getFullName(), grandFather.getFullName()) &&
                Objects.equals(this.getShortName(), grandFather.getShortName());
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, shortName);
    }

}
