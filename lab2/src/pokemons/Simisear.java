package pokemons;
import move.*;
public class Simisear extends Pansear{
    public Simisear(String name,int lvl){
        super(name,lvl);
        setStats(50,53,48,53,48,64);
        addMove(new FocusBlast());
    }
}