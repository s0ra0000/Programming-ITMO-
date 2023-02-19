package classes;

import abstracts.SeasonAbstract;

import enums.Seasons;

import java.util.Objects;

public class Winter extends SeasonAbstract {
    private final Seasons TYPE = Seasons.WINTER;
    public Winter() {
        super.wonderful = false;
    }

    public Winter(boolean wonderful) {
        super(wonderful);
    }

    @Override
    public void setWonderful(boolean wonderful) {
        super.wonderful = wonderful;
        if (wonderful) {
            System.out.println("Был чудесный зимный день");
        } else {
            System.out.println("Был зимный день");
        }
    }

    @Override
    public void joinStory() {
        System.out.printf("%s joined story\n", TYPE);
    }

    @Override
    public void leaveStory() {
        System.out.print("Winter left story");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Winter)) {
            return false;
        }
        Winter winter = (Winter) obj;
        return TYPE == winter.TYPE && Objects.equals(wonderful, winter.wonderful);
    }

    @Override
    public String toString() {
        return "Зима";
    }

    @Override
    public int hashCode() {
        return TYPE.hashCode();
    }


}
