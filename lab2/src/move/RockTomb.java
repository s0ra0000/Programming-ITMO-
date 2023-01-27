package move;

import ru.ifmo.se.pokemon.*;

public class RockTomb extends PhysicalMove {
    private int stageLowered = -1;

    public RockTomb() {
        super(Type.ROCK, 60, 95);
    }

    @Override
    protected void applyOppEffects(Pokemon p) {
        if (stageLowered >= -6) {
            p.setMod(Stat.SPEED, stageLowered);
            stageLowered--;
        }
    }

    @Override
    protected String describe() {
        return "Using Rock Tomb!!!";
    }
}
