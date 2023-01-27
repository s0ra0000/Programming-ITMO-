package move;
import ru.ifmo.se.pokemon.*;
public class WorkUp extends StatusMove{
    public WorkUp(){
        super(Type.NORMAL,0,0);
    }
    private int StageRaised = 1;
    @Override
    protected void applySelfEffects(Pokemon p){
        if(StageRaised <= 6) {
            p.setMod(Stat.ATTACK, StageRaised);
            p.setMod(Stat.SPECIAL_ATTACK, StageRaised);
            StageRaised++;
        }
    }
    @Override
    protected String describe(){
        return "Using Work Up!!!";
    }
}
