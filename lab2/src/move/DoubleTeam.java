package move;
import ru.ifmo.se.pokemon.*;
public class DoubleTeam extends StatusMove{
    public DoubleTeam(){
        super(Type.NORMAL,0,0);
    }
    private int StageRaised = 1;
    @Override
    protected void applySelfEffects(Pokemon p){
        if(StageRaised <= 6){
            p.setMod(Stat.EVASION,StageRaised);
            StageRaised++;
        }
    }
    @Override
    protected String describe(){
        return "Using Double Team!!!";
    }
}
