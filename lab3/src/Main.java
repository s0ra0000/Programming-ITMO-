import classes.*;

public class Main {
    public static void main(String[] args) {
        Christopher christopher = new Christopher("Кристофер Робин");
        Piglet piglet = new Piglet("Пятачок");
        GrandFather grandFather = new GrandFather("Дедушка", "Посторонним В.");
        WinniePooh winniePooh = new WinniePooh("Винни-Пух");
        Winter winter = new Winter();
        christopher.joinStory();
        christopher.say("не может быть такого имени -- Посторонним В.");
        piglet.joinStory();
        piglet.answer("нет, может, нет, может");
        grandFather.joinStory();
        grandFather.setFullName("Вильям Посторонним");
        grandFather.compareName();
        christopher.leaveStory();
        winter.joinStory();
        winter.setWonderful(true);
        piglet.liftHead(true);
        piglet.see(winniePooh);
        piglet.callOut(winniePooh);
        winniePooh.joinStory();
        winniePooh.setAttentivelyLook(true);
        winniePooh.think();
        winniePooh.walk();
    }
}
