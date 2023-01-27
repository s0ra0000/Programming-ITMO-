package pokemons;
import move.*;
import ru.ifmo.se.pokemon.*;
public class Pansear extends Pokemon{
    public Pansear(String name,int lvl){
        super(name,lvl);
        setStats(50,53,48,53,48,64);
        setType(Type.FIRE);
        setMove(new DoubleTeam(),new WorkUp(),new Leer());
    }
}
