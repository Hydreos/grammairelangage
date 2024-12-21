public class Main {
    public static void main(String[] args) throws Exception {

        Automate afn = LectureFichier.lireFichier("test.txt");
        System.out.println("\nAFN");
        afn.affiche();

        Automate afd = new Automate();
        afd.conversionAfnAfd(afn.etats.get(1), afn);
        System.out.println("\nAFD :");
        afd.affiche();

        EcritureFichier.ecrireAutomate("afd.txt", afd);
    }
}