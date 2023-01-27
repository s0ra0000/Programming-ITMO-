package pokemons;
import move.*;
import ru.ifmo.se.pokemon.*;
public class Torkoal extends Pokemon{
    public Torkoal(String name,int lvl){
        super(name,lvl);
        setStats(70,85,140,85,70,20);
        setType(Type.FIRE);
        setMove(new IronDefense(),new RockTomb(),new ShellSmash(),new HeatWave());
    }
}
