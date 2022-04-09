package util;

public class Time {
    //les variables sont initialiser au lancement de l'application ducoup le timestarted sera le moment de lancement
    public static float timeStarted=System.nanoTime();

    /**
     * ca retourne le nombre de seconde depuis lequel l'application tourne
     * @return un float qui represente le nombre de secondes depuios que l'application a été lancé
     */
    public static float getTime(){
        //on multiplie par 1E-9 pour convertir le temps de nanoseconde a seconde
        //nano c'est 9 so 1e-9 j'ai pas fait math spé ok? j'applique la formule
        return (float) ((System.nanoTime() - timeStarted)*1E-9);
    }
}
