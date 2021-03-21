package jatek;

/**
 * Játék szimuláció példányosítására és futtatására létrehozott osztály.
 */
public class Main {
    public static void main(String[] args) {
        Jatek jatek = new Jatek();

        jatek.init();

        //
        System.out.println("\nA játék játékosai:");
        for (int i = 0; i < 4; ++i){
            System.out.println(jatek.getJatekosok().get(i).getNev() + " " + jatek.getJatekosok().get(i).getNem());
        }

        jatek.start();
    }

}
