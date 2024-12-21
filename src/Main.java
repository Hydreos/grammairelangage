import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {

        /*
        Etat trois = new Etat(3, 10);
        Etat deux = new Etat(2, 10);
        Etat un = new Etat(1, 10);
        Etat zero = new Etat(0, 10);

        deux.transitions.add(new Transition(trois, "b"));
        un.transitions.add(new Transition(deux, "a"));
        zero.transitions.add(new Transition(un, "a"));
        zero.transitions.add(new Transition(zero, "a"));
        zero.transitions.add(new Transition(zero, "b"));

        Automate afn1 = new Automate(zero);
        afn1.ajoutEtat(zero);
        afn1.ajoutEtat(un);
        afn1.ajoutEtat(deux);
        afn1.ajoutEtat(trois);

        System.out.println("\nafn1");
        for (Integer i : afn1.etats.keySet()) {
            System.out.println(i);
        }
        System.out.println("");

        
        afn1.conversionAfnAfd(zero, new ArrayList<>());
         
        for (Integer i : afn1.etats.keySet()) {
            System.out.println("Etat : "+i);
            for (Transition t : afn1.etats.get(i).transitions) {
                System.out.println(t.symbole+", "+t.arrivEtat.exp);
            }
        }
        */

        Automate afn2 = LectureFichier.lireFichier("test.txt");
        Automate afd = new Automate();
        System.out.println("\nafn2");
        afn2.affiche();

        
        afd.conversionAfnAfd(afn2.etats.get(1), afn2);

        System.out.println("\nAFD :");
        afd.affiche();
        
    }
}