package interfaces;

public interface IPiglet extends ThingInterface {
    void answer(String text);

    void liftHead(boolean canSee);

    void see(Object obj);

    void callOut(Object obj);
}
