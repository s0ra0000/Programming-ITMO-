package interfaces;

import exceptions.CouldNotSeeException;

public interface IPiglet extends ThingInterface {
    void answer(String text);

    void see(Object obj) throws CouldNotSeeException;

    void callOut(Object obj);
}
