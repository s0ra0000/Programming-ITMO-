package move;
import ru.ifmo.se.pokemon.*;
public class HeatWave extends SpecialMove{
    public HeatWave(){
        super(Type.FIRE,95,90);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        if(Math.random() <= 0.1){
            Effect.burn(p);
        }
    }
    @Override
    protected String describe(){
        return "Using Heat Wave!!!";
    }
}
