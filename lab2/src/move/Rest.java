package move;
import ru.ifmo.se.pokemon.*;
public class Rest extends StatusMove{
    public Rest(){
        super(Type.PSYCHIC,0,0);
    }
    @Override
    protected void applySelfEffects(Pokemon p){
       Effect.sleep(p);
    }
    @Override
    protected String describe(){
        return "Using Rest!!!";
    }
}
