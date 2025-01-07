public class Main {
    public static void main(String[] args) throws Exception {

        // AFN -> AFD
        Automate afn = LectureFichier.lireFichier("afn.txt");
        Automate afd = new Automate();
        afd.conversionAfnAfd(afn.etats.get(1), afn);
        afd.remplirSymboles();
        EcritureFichier.ecrireAutomate("afd.txt", afd);

        // AFD -> AFD minimisé
        Automate afd2 = LectureFichier.lireFichier("afd2.txt");
        Automate afd2mini = Automate.minimisationAfd(afd2);
        EcritureFichier.ecrireAutomate("afd2mini.txt", afd2mini);
    }
}