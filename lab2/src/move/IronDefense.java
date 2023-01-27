package move;
import ru.ifmo.se.pokemon.*;
public class IronDefense extends StatusMove{
    public IronDefense(){
        super(Type.STEEL,0,0);
    }
    private int StageRaised = 1;
    @Override
    protected void applySelfEffects(Pokemon p){
        if(StageRaised <= 6){
            p.setMod(Stat.DEFENSE, StageRaised);
            StageRaised++;
        }
    }
    @Override
    protected String describe(){
        return "Using Iron Defense!!!";
    }
}
