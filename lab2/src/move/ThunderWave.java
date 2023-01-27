package move;
import ru.ifmo.se.pokemon.*;
public class ThunderWave extends StatusMove{
    public ThunderWave(){
        super(Type.ELECTRIC,0,90);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        Effect.paralyze(p);
        if(Math.random() <= 0.25){
            p.setMod(Stat.SPEED,-1);
        }
    }
}
