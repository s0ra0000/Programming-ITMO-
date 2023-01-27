package move;
import ru.ifmo.se.pokemon.*;
public class ChargeBeam extends SpecialMove{
    public ChargeBeam(){
        super(Type.ELECTRIC,50,90);
    }
    private int StageRaised = 1;
    @Override
    protected void applySelfEffects(Pokemon p){
        if(Math.random() <= 0.7 && StageRaised <=6){
            p.setMod(Stat.SPECIAL_ATTACK,StageRaised);
            StageRaised++;
        }
    }
    @Override
    protected String describe(){
        return "Using Charge Beam!!!";
    }
}
