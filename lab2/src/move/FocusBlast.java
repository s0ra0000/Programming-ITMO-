package move;
import ru.ifmo.se.pokemon.*;
public class FocusBlast extends SpecialMove{
    public FocusBlast(){
        super(Type.FIGHTING,120,70);
    }

    private int stageLowered = -1;

    @Override
    protected void applyOppEffects(Pokemon p){
        if(Math.random() <= 0.1 && stageLowered >= -6){
            p.setMod(Stat.SPECIAL_DEFENSE,stageLowered);
            stageLowered--;
        }
    }

    @Override
    protected String describe(){
        return "Using Focus Blast!!!";
    }
}
