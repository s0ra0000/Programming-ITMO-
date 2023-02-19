package classes;

import abstracts.RoleAbstract;
import interfaces.IChristopher;

import java.util.Objects;

public class Christopher extends RoleAbstract implements IChristopher {

    public Christopher(String fullName) {
        super(fullName);
    }

    @Override
    public void say(String text) {
        System.out.printf("%s сказал, что %s\n", fullName, text);
    }

    @Override
    public void ask(String text, Object obj) {
        System.out.printf("%s спросил у %s, %s\n", fullName, obj, text);
    }

    @Override
    public void joinStory() {
        System.out.printf("%s joined story\n", fullName);
    }

    @Override
    public void leaveStory() {
        System.out.printf("%s left story\n", fullName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Christopher)) {
            return false;
        }
        Christopher christopher = (Christopher) obj;
        return Objects.equals(this.getFullName(), christopher.getFullName());
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }


}
