import ru.ifmo.se.pokemon.*;
import pokemons.*;
public class Main {
    public static void main(String[] args) {
        Battle battle = new Battle();
        Torkoal Torkoal = new Torkoal("Chandler",3);
        Pansear Pansear = new Pansear("Joey",2);
        Simisear Simisear = new Simisear("Ross",1);
        Tynamo Tynamo = new Tynamo("Rachel",3);
        Eelektrik Eelektrik = new Eelektrik("Monica",2);
        Eelektross Eelektross = new Eelektross("Pheobe",1);
        battle.addAlly(Torkoal);
        battle.addAlly(Pansear);
        battle.addAlly(Simisear);
        battle.addFoe(Tynamo);
        battle.addFoe(Eelektrik);
        battle.addFoe(Eelektross);
        battle.go();
    }
}
