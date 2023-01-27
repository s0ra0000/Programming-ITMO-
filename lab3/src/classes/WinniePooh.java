package classes;

import abstracts.RoleAbstract;
import interfaces.IWinniePooh;

import java.util.Objects;

public class WinniePooh extends RoleAbstract implements IWinniePooh {

    private boolean isDeepThink = false;
    private boolean isAttentivelyLook = false;

    public WinniePooh(String name) {
        super(name);
    }

    public boolean getAttentivelyLook() {
        return isAttentivelyLook;
    }

    public void setAttentivelyLook(boolean isAttentivelyLook) {
        this.isAttentivelyLook = isAttentivelyLook;
        if (isAttentivelyLook) {
            System.out.printf("%s внимательно глядел себе\n", fullName);
        } else {
            System.out.printf("%s не глядел на себе\n", fullName);
        }
    }

    public boolean getDeepThink() {
        return isDeepThink;
    }

    public void setDeepThink(boolean isDeepThink) {
        this.isDeepThink = isDeepThink;
    }

    @Override
    public void think() {
        if (isAttentivelyLook) {
            isDeepThink = true;
            System.out.printf("%s глубоко задумался \n", fullName);
        } else {
            isDeepThink = false;
            System.out.printf("%s не задумался \n", fullName);
        }
    }

    @Override
    public void walk() {
        if (isDeepThink) {
            System.out.printf("%s медленно шел\n", fullName);
        } else {
            System.out.printf("%s шел\n", fullName);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WinniePooh)) {
            return false;
        }
        WinniePooh winniePooh = (WinniePooh) obj;
        return Objects.equals(this.getFullName(), winniePooh.getFullName()) &&
                Objects.equals(isDeepThink, winniePooh.getDeepThink()) &&
                Objects.equals(isAttentivelyLook, winniePooh.getAttentivelyLook());
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, isDeepThink, isAttentivelyLook);
    }

    @Override
    public void joinStory() {
        System.out.printf("%s joined story.\n", fullName);
    }

    @Override
    public void leaveStory() {
        System.out.printf("%s left story.\n", fullName);
    }
}
