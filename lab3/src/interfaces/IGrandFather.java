package interfaces;

public interface IGrandFather extends ThingInterface {
   default void compareName() {
       System.out.println("HHHH");
   }
}
