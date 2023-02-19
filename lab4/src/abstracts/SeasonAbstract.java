package abstracts;

import interfaces.ThingInterface;

public abstract class SeasonAbstract implements ThingInterface {
    protected boolean wonderful;

    public SeasonAbstract() {
        wonderful = false;
    }

    public SeasonAbstract(boolean wonderful) {
        this.wonderful = wonderful;
    }

    public boolean getWonderful() {
        return wonderful;
    }

    public void setWonderful(boolean wonderful) {
        this.wonderful = wonderful;
    }

}
