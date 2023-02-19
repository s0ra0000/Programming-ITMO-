package classes;

import exceptions.CouldNotSeeException;
import exceptions.CouldNotSweepSnowException;
import abstracts.RoleAbstract;
import interfaces.IPiglet;

import java.util.Objects;


public class Piglet extends RoleAbstract implements IPiglet {
    static boolean isSweep;
    static boolean isSnowy;
    protected boolean canSee;

    public Piglet(String fullName) {
        super(fullName);
    }

    @Override
    public void answer(String text) {
        System.out.printf("%s ответил, что %s\n", fullName, text);
    }

    public void callOut(Object role) {
        System.out.printf("%s окликнул %s \n", fullName, role);
    }

    public void see(Object role) throws CouldNotSeeException {
        if (canSee) {
            System.out.printf("%s увидел %s\n", fullName, role);
        } else {
            throw new CouldNotSeeException("пока голову не поднял");
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

    public static class Snow {
        public boolean getSnowy() {
            return Piglet.isSnowy;
        }

        public void setSnowy(boolean isSnowy) {
            Piglet.isSnowy = isSnowy;
        }

        public void sweep(boolean isSweep) throws CouldNotSweepSnowException {
            if (getSnowy()) {
                Piglet.isSweep = isSweep;
                System.out.println("разметился снег");
            } else {
                throw new CouldNotSweepSnowException("нет снега");
            }
        }
    }

    public class Head {
        public void liftHead() {
            canSee = true;
            System.out.printf("%s поднял голову\n", fullName);
        }

        public void putDownHead() {
            canSee = false;
            System.out.printf("%s опустил голову\n", fullName);
        }
    }
}
