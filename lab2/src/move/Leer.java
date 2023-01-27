package move;
import ru.ifmo.se.pokemon.*;
public class Leer extends StatusMove{
    public Leer(){
        super(Type.NORMAL,0,100);
    }
    private int StageLowered = -1;
    @Override
    protected void applyOppEffects(Pokemon p){
        if(StageLowered >=-6){
            p.setMod(Stat.DEFENSE,StageLowered);
            StageLowered--;
        }
    }
    @Override
    protected String describe(){
        return "Using Leer!!!";
    }
}
