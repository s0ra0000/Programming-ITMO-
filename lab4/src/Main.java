import exceptions.CouldNotSeeException;
import exceptions.CouldNotSweepSnowException;
import classes.*;

public class Main {
    interface IStory {
        void endStory();
        void startStory();
    }
    public static void main(String[] args) {
        IStory story = new IStory() {
            @Override
            public void startStory() {
                System.out.println("НАЧАЛО ИСТОРИИ!!!");
            }
            @Override
            public void endStory() {
                System.out.println("КОНЕЦ ИСТОРИИ!!!");
            }
        };
        Christopher christopher = new Christopher("Кристофер Робин");
        Piglet piglet = new Piglet("Пятачок");
        GrandFather grandFather = new GrandFather("Дедушка", "Посторонним В.");
        WinniePooh winniePooh = new WinniePooh("Винни-Пух");
        Winter winter = new Winter();
        story.startStory();
        christopher.joinStory();
        christopher.ask("что тут, на доске написано", piglet);
        piglet.joinStory();
        piglet.answer("тут написано имя его дедушки и что это доска с надписью -- их фамильная реликвая.");
        christopher.say("не может быть такого имени -- Посторонним В.");
        piglet.answer("нет, может, нет, может");
        grandFather.joinStory();
        grandFather.setFullName("Вильям Посторонним");
        grandFather.compareName();
        christopher.leaveStory();
        winter.joinStory();
        winter.setWonderful(true);
        Piglet.Snow snow = new Piglet.Snow();
        snow.setSnowy(true);

        try {
            snow.sweep(true);
        } catch (CouldNotSweepSnowException err) {
            System.out.println("Не может разметить снег, потому что " + err.getMessage());
        }

        Piglet.Head head = piglet.new Head();
        head.liftHead();

        try {
            piglet.see(winniePooh);
        } catch (CouldNotSeeException err) {
            System.out.println(piglet.getFullName() + " не может видеть, потому что " + err.getMessage());
        }

        piglet.callOut(winniePooh);
        winniePooh.joinStory();
        winniePooh.setAttentivelyLook(true);
        winniePooh.think();
        winniePooh.walk();
        story.endStory();
    }
}






