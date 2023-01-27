package classes;

import abstracts.RoleAbstract;
import interfaces.IPiglet;

import java.util.Objects;


public class Piglet extends RoleAbstract implements IPiglet {
    protected boolean canSee;

    public Piglet(String fullName) {
        super(fullName);
    }

    @Override
    public void answer(String text) {
        System.out.printf("%s ответил, что %s\n", fullName, text);
    }

    public void liftHead(boolean canSee) {
        this.canSee = canSee;
        System.out.printf("%s поднял голову\n", fullName);
    }


    public void see(Object role) {
        if (canSee) {
            System.out.printf("%s увидел %s\n", fullName, role);
        } else {
            System.out.printf("%s не смог увидеть", fullName);
        }
    }

    @Override
    public void joinStory() {
        System.out.printf("%s joined story\n", fullName);
    }

    @Override
    public void leaveStory() {
        System.out.printf("%s left story\n", fullName);
    }

    public void callOut(Object role) {
        System.out.printf("%s окликнул %s \n", fullName, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Piglet)) {
            return false;
        }
        Piglet piglet = (Piglet) obj;
        return Objects.equals(this.getFullName(), piglet.getFullName());
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public int hashCode() {
        return getFullName().hashCode();
    }


}
