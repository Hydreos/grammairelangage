public class Main {
    public static void main(String[] args) throws Exception {

        Automate afn = LectureFichier.lireFichier("afn.txt");
        System.out.println("\nAFN");
        afn.affiche();

        Automate afd = new Automate();
        afd.conversionAfnAfd(afn.etats.get(1), afn);
        afd.remplirSymboles();
        System.out.println("\nAFD :");
        afd.affiche();

        Automate afd2 = LectureFichier.lireFichier("afd2.txt");
        Automate.minimisationAfd(afd2);

        EcritureFichier.ecrireAutomate("afd.txt", afd);
    }
}