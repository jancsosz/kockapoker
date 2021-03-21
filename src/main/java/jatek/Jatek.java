package jatek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Játék szimulációját megvalósító osztály.
 */
public class Jatek {
    private int jatekosSzam;
    private ArrayList<Jatekos> jatekosok = new ArrayList<>();


    public Jatek(){}

    public ArrayList<Jatekos> getJatekosok() {
        return jatekosok;
    }

    /**
     * Automata játékosokat generáló metódus. (ha nincs elég bekért játékos)
     */
    private void automataJatekosok(){
        int botSzam = 4 - jatekosSzam;
        if (botSzam > 0){
            for (int i = 0; i < botSzam; ++i){

                    No no = new No();

                    no.setNem("NŐ");
                    no.setNev("Bot" + (i + 1));

                    this.jatekosok.add(no);
            }
        }
    }

    /**
     * Egy körben egy játékos dobásait szimuláló függvény.
     * @return 6 darab 1-6 közti random egész szám egy ArrayListbe foglalva.
     */
    public static ArrayList<Integer> dobas(){
        ArrayList<Integer> dobasok = new ArrayList<>();

        for (int i = 0; i < 5; ++i){
            dobasok.add((int) (Math.random() * 6 + 1));
        }

        Collections.sort(dobasok);

        return dobasok;
    }

    /**
     * Metódus a játék kezdéséhez szükséges adatok bekérésére.
     */
    public void init(){
        System.out.println("Adja meg a játékosok számát!");

        Scanner scan = new Scanner(System.in);

        this.jatekosSzam = scan.nextInt();

        if (!(jatekosSzam >= 1 && jatekosSzam <= 4)){
            throw new IllegalArgumentException("A játékososk számának 1 és 4 között kell lennie.");
        }

        for (int i = 0; i < jatekosSzam; i++){
            System.out.println("Adja meg a(z) " + (i + 1) + ". játékos nevét:");
            String nev = scan.next();

            System.out.println("Adja meg a(z) " + (i + 1) + ". játékos nemét (ferfi/no):");
            String nem = scan.next();

            if (!(nem.equals("no")) && !(nem.equals("ferfi")) ){
                throw new IllegalArgumentException("A nemnek \"no\"-nek vagy \"ferfi\"-nak kell lennie.");
            }

            if (nem.equals("no")){
                No no = new No();

                no.setNev(nev);
                no.setNem(nem);

                this.jatekosok.add(no);
            } else {
                Ferfi ferfi = new Ferfi();

                ferfi.setNev(nev);
                ferfi.setNem(nem);

                this.jatekosok.add(ferfi);
            }
        }
        automataJatekosok();
    }

    /**
     * Metódus a játékmenet lebonyolítására.
     */
    public void start(){
        ArrayList<ArrayList<Integer>> korDobasok = new ArrayList<>();

        System.out.println("\nA játék indításához nyomj ENTER-t.");
        Scanner scan = new Scanner(System.in);
        scan.nextLine();

        for (int i = 1; i <= 10; ++i){
            System.out.println(i + ". forduló eredményei:\n");

            korDobasok.add(dobas());
            korDobasok.add(dobas());
            korDobasok.add(dobas());
            korDobasok.add(dobas());

            for (int j = 0; j < this.jatekosok.size(); ++j){
                System.out.println(this.jatekosok.get(j).getNev() + " " + korDobasok.get(j));
            }

            // logika itt
            int korNyertes = korNyertes(korDobasok);
            System.out.println("\nA fordulót " + this.jatekosok.get(korNyertes).getNev() + " nyerte.\n");
            this.jatekosok.get(korNyertes).nyert();

            // test
            System.out.println("Eddigi győzelmek száma:");
            for (Jatekos jatekos : this.jatekosok) {
                System.out.println(jatekos.getNev()+ ": " + jatekos.getNyertJatekokSzama());
            }

            if (i == 5 || i == 8) {
                kieses();
            }

            korDobasok.clear();

            if (i == 10) {
                jatekNyertes();
            }
            if (i < 10) {
                System.out.println("\nA következő forduló indításához nyomj ENTER-t.");
                scan.nextLine();
            }
        }
    }


    /** Kör nyertesét meghatározó függvény.
     *  Először előállításra kerül egy ArrayList, amiben játékosonként, további ArrayList-ekben
     *  el van tárolva, hogy az adott számból hányat dobott a körben.
     *  Ezt az ArrayList-et elemenként vizsgálja az összes lehetséges sorozatra,
     *  amiből végül játékosonként egyetles érték áll elő.
     *  Ezeket az értékeket egy újabb ArrayListben tárolja el és ezek közül maximum-kiválasztással
     *  meghatározza a legnagyobbat. Ha több teljesen megegyező van, akkor aki először dobta
     *  az eredményt az nyer.
     *
     * @param korDobasok a játékosok adott körben dobott értékeinek listái egy listában összefoglalva.
     * @return a nyertes játékos indexe.
     */
    private int korNyertes(ArrayList<ArrayList<Integer>> korDobasok){
        ArrayList<Integer> mibolMennyi = new ArrayList<>();
        ArrayList<ArrayList<Integer>> mibolMennyiLista = new ArrayList<>();
        int darab = 0;
        for (int i = 0; i < this.jatekosok.size(); ++i){
            for (int j = 1; j <= 6; ++j){
                for (int k = 0; k < 5; ++k){
                    if (korDobasok.get(i).get(k) == j){
                        ++darab;
                    }
                }
                mibolMennyi.add(darab);
                darab = 0;
            }
            mibolMennyiLista.add(new ArrayList<>(mibolMennyi));
            mibolMennyi.clear();
        }

        ArrayList<Integer> eredmenyek = new ArrayList<>();
        for (int i = 0; i < this.jatekosok.size(); ++i){
            if (par(mibolMennyiLista.get(i)) != 0) eredmenyek.add(par(mibolMennyiLista.get(i)));
            else if (terc(mibolMennyiLista.get(i)) != 0) eredmenyek.add(terc(mibolMennyiLista.get(i)));
            else if (ketPar(mibolMennyiLista.get(i)) != 0) eredmenyek.add(ketPar(mibolMennyiLista.get(i)));
            else if (kisSor(mibolMennyiLista.get(i)) != 0) eredmenyek.add(kisSor(mibolMennyiLista.get(i)));
            else if (nagySor(mibolMennyiLista.get(i)) != 0) eredmenyek.add(nagySor(mibolMennyiLista.get(i)));
            else if (full(mibolMennyiLista.get(i)) != 0) eredmenyek.add(full(mibolMennyiLista.get(i)));
            else if (kisPoker(mibolMennyiLista.get(i)) != 0) eredmenyek.add(kisPoker(mibolMennyiLista.get(i)));
            else if (nagyPoker(mibolMennyiLista.get(i)) != 0) eredmenyek.add(nagyPoker(mibolMennyiLista.get(i)));
            else eredmenyek.add(0);
        }

        int nyertesIndex = 0;
        int i = 0;
        while (nyertesIndex == 0 && i < this.jatekosok.size()) {
            if (eredmenyek.get(i).equals(Collections.max(eredmenyek))) nyertesIndex = i;
            i++;
        }

        return nyertesIndex;
    }

    /**
     * Metódus a plusz feladat teljesítéséhez.
     * Adott hívás esetén meghatározza és törli a leggyengébb játékost.
     */
    private void kieses(){
        int min = 0;
        for (int i = 0; i < this.jatekosok.size(); ++i){

            if (this.jatekosok.get(min).getNyertJatekokSzama() > this.jatekosok.get(i).getNyertJatekokSzama()){
                min = i;
            }
        }

        int leggyengebb = 0;
        for (Jatekos jatekos : this.jatekosok) {
            if (jatekos.getNyertJatekokSzama() == this.jatekosok.get(min).getNyertJatekokSzama()) {
                ++leggyengebb;
            }
        }


        if (leggyengebb == 1) {
            System.out.println("\n" + this.jatekosok.get(min).getNev() + " kiesett.");
            this.jatekosok.remove(min);
        }
    }

    /**
     * Metódus a játék nyertese meghatározására.
     * Ha több embernek van ugyanannyi győzelme, döntetlent ír.
     */
    private void jatekNyertes(){
        int max = 0;
        for (int i = 0; i < this.jatekosok.size(); ++i){

            if (this.jatekosok.get(max).getNyertJatekokSzama() < this.jatekosok.get(i).getNyertJatekokSzama()){
                max = i;
            }
        }

        int nyertes = 0;
        for (Jatekos jatekos : this.jatekosok) {
            if (jatekos.getNyertJatekokSzama() == this.jatekosok.get(max).getNyertJatekokSzama()) {
                ++nyertes;
            }
        }


        if (nyertes == 1) {
            System.out.println("\nA játékot " + this.jatekosok.get(max).getNev() + " nyerte.");
        } else if (nyertes > 1) System.out.println("\nA játék végeredménye döntetlen.");
    }

    /**
     * Függvény a szimpla pár meghatározására.
     * @param egyDobas egy játékos egy körben dobott értékei
     * @return egy pár meghatározott értéke (100) + a pár rangja (párok közti megkülönböztetés céljából)
     */
    private int par(ArrayList<Integer> egyDobas){
        int parSzam = 0;
        int index = 0;
        int tercSzam = 0;
        for (int i = 0; i < 6; ++i){
            if (egyDobas.get(i) == 2) {
                ++parSzam;
                index = i;
            } else if (egyDobas.get(i) == 3) ++tercSzam;
        }

        return parSzam == 1 && tercSzam == 0 ? (index + 1 + 100) : 0;
    }

    /**
     * Függvény a szimpla terc meghatározására.
     * @param egyDobas egy játékos egy körben dobott értékei
     * @return egy terc meghatározott értéke (200) + a pár rangja (tercek közti megkülönböztetés céljából)
     */
    private int terc(ArrayList<Integer> egyDobas){
        int tercSzam = 0;
        int parSzam = 0;
        int index = 0;
        for (int i = 0; i < 6; ++i){
            if (egyDobas.get(i) == 3) {
                ++tercSzam;
                index = i;
            } else if (egyDobas.get(i) == 2) ++parSzam;
        }

        return tercSzam == 1 && parSzam == 0 ? (index + 1 + 200) : 0;
    }

    /**
     * Függvény két pár meghatározására.
     * @param egyDobas egy játékos egy körben dobott értékei
     * @return két pár meghatározott értéke (300) + két pár rangja (két-párok közti megkülönböztetés céljából)
     */
    private int ketPar(ArrayList<Integer> egyDobas){
        int parSzam = 0;
        ArrayList<Integer> index = new ArrayList<>();

        for (int i = 0; i < 6; ++i){
            if (egyDobas.get(i) == 2) {
                ++parSzam;
                index.add(i);
            }
        }

        return parSzam == 2 ? (index.get(0) + index.get(1) + 2 + 300) : 0;
    }

    /**
     * Függvény a kis sor meghatározására.
     * @param egyDobas egy játékos egy körben dobott értékei
     * @return egy kis sor meghatározott értéke (400)
     */
    private int kisSor(ArrayList<Integer> egyDobas){
        int egyesSzam = 0;

        for (int i = 0; i < 6; ++i){
            if (egyDobas.get(i) > 1) {
                ++egyesSzam;
            }
        }

        return egyesSzam == 0 && egyDobas.get(5) == 0 ? 400 : 0;
    }

    /**
     * Függvény a nagy sor meghatározására.
     * @param egyDobas egy játékos egy körben dobott értékei
     * @return egy nagy sor meghatározott értéke (500)
     */
    private int nagySor(ArrayList<Integer> egyDobas){
        int egyesSzam = 0;

        for (int i = 0; i < 6; ++i){
            if (egyDobas.get(i) > 1) {
                ++egyesSzam;
            }
        }

        return egyesSzam == 0 && egyDobas.get(0) == 0 ? 500 : 0;
    }

    /**
     * Függvény a full meghatározására.
     * @param egyDobas egy játékos egy körben dobott értékei
     * @return full meghatározott értéke (600) + full rangja (pár rangja + terc rangja) (fullok közti megkülönböztetés céljából)
     */
    private int full(ArrayList<Integer> egyDobas){
        int tercSzam = 0;
        int parSzam = 0;
        int parIndex = 0;
        int tercIndex = 0;

        for (int i = 0; i < 6; ++i){
            if (egyDobas.get(i) == 3) {
                ++tercSzam;
                tercIndex = i;
            } else if (egyDobas.get(i) == 2){
                parIndex = i;
                ++parSzam;
            }
        }
        return tercSzam == 1 && parSzam == 1 ? (tercIndex + parIndex + 2 + 600) : 0;
    }

    /**
     * Függvény a kis póker meghatározására.
     * @param egyDobas egy játékos egy körben dobott értékei
     * @return kis póker meghatározott értéke (700) + kis póker rangja (kis pókerek közti megkülönböztetés céljából)
     */
    private int kisPoker(ArrayList<Integer> egyDobas){
        int index = 0;

        for (int i = 1; i <= 6; ++i){
            if (egyDobas.get(i-1) == 4) {
                index = i;
            }
        }

        return index == 0 ? index : index + 700;
    }

    /**
     * Függvény a nagy póker meghatározására.
     * @param egyDobas egy játékos egy körben dobott értékei
     * @return nagy póker meghatározott értéke (800) + nagy póker rangja (nagy pókerek közti megkülönböztetés céljából)
     */
    private int nagyPoker(ArrayList<Integer> egyDobas){
        int index = 0;

        for (int i = 1; i <= 6; ++i){
            if (egyDobas.get(i-1) == 5) {
                index = i;
            }
        }

        return index == 0 ? index : index + 800;
    }
}
