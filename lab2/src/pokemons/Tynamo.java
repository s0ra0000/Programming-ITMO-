package pokemons;
import move.*;
import ru.ifmo.se.pokemon.*;
public class Tynamo extends Pokemon {
    public Tynamo(String name, int level){
        super(name,level);
        setStats(35, 55, 40,45,40,60);
        setType(Type.ELECTRIC);
        setMove(new ChargeBeam(), new ThunderWave());
    }
}
