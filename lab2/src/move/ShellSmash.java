package move;

import ru.ifmo.se.pokemon.*;

public class ShellSmash extends StatusMove {
    private int StageLowered = -1;
    private int StageRaised = 2;
    public ShellSmash() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon p) {
        if (StageLowered >= -6 && StageRaised <= 6) {
            p.setMod(Stat.DEFENSE, StageLowered);
            p.setMod(Stat.SPECIAL_ATTACK, StageRaised);
            p.setMod(Stat.SPEED, StageRaised);
            StageLowered--;
            StageRaised += 2;
        }
    }

    @Override
    protected String describe() {
        return "Using Shell Smash!!!";
    }
}
