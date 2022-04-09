package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    /**
     * les variable qu'une souris a
     * le scroll
     * la position actuelle
     * la position d'avant
     * les bouton appuyer on a compte 3 boutton si ta souris a plus c'est chien
     * ma souris a 12 bouttons
     * oui je suis con et alors
     * ah il y'a aussi le isdragging le nom dis ce que ca fait
     *
     * ah et aussi sur le site de glfw.org dans la partie input guide
     */
    private static MouseListener instance;
    private double scrollX;
    private double scrollY;
    private double xPos;
    private double yPos;
    private double lastX;
    private double lastY;
    private boolean[] mouseButtonPressed =new boolean[3];
    private boolean isDragging;

    /**
     * ca initilise toute les variable du mouse listener
     */
    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    /**
     * il n y'aura qu'un seul mouse event listener du coup le constructeur est privé
     * et la methode get renvoie l'unique contructeur
     *
     * @return le singleton de mouse event listener
     */
    public static MouseListener get(){
        if (MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    /**
     * le last x et le last y devienne l'actuel x et y
     * l'actuel x et y devient le nouveau qu'on a passé en parametre
     * en gros la position de x et y change pour devenir celle qu'on met en argument
     * en plus si un des boutton est appuyer durant le deplacement on deduit qu'on drag la souris
     * @param window le long qui represente la fenetre
     * @param xpos la nouvelle position x de la souris
     * @param ypos la nouvelle position y de la souris
     */
    public static void mousePosCallback(long window, double xpos, double ypos){
        get().lastX =get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;

        /*
        probleme pottentiel:
        si on rajoute des boutton on devra s'assure de les mettre ici aussi une boucle serait mieux pour
        l'upgradabilite
        pk je la fait pas? la fleeeeeeeemmmeeeeeeeee
         */
        get().isDragging= get().mouseButtonPressed[0]||get().mouseButtonPressed[1]||get().mouseButtonPressed[2];
    }


    /**
     * la methode permet d'interagir avec les bouiton de la souris
     * en gros ca gere les clics
     * @param window le long qui represente la fenetre a modifier
     * @param button le bouton de la souris
     * @param action l'action que l'on a fait
     * @param mods jsp encore c'etait dans la doc so j'ai mis
     */
    public static void mouseButtonCallback(long window, int button, int action, int mods){
        //si le boutton a été appuyé
        if(action == GLFW_PRESS){
            // si le boutton est plus petit que 3 parcequ'on a dit que notre souris a 3 boutton
            //on ecrit pas 3 pour penser a l'upgradabilité (c'est pas un mot et alors c'est mes notes)
            //on est samedi a 14h10 et dave ecoute une video oui oui ca sert a rien ca
            if (button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        }

        // si le boutton est relache on met le boutton pressed a false et on sait aussi que ca veut dire qu'on ne le drag pas
        //pcq on ne peux pas drag sans appuyer c'est logique
        else if(action == GLFW_RELEASE){
            if (button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = false;
                get().isDragging= false;
            }
        }
    }


    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    /**
     * <p style="color:orange;">
     *     a la fin de chaque frame on reinitilise les valeur de la souris <br>
     *     celui qui a mis la doc en html connait pas l'erreur qu'il a fait
     * </p>
     *
     */

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastY = get().xPos;
        get().lastY = get().yPos;
    }


    /*
    * plein de getter a la suite on peut appeler ici carrefour getter
    * je suis a deux doigt de faire une animation dans la doc depuis que je sais que c'est du html
    * sauvez moi de la flemme*/


    /**
     * ça renvoie la position X du curseur en float
     * @return la position X du curseur en float
     */
    public static float getX(){
        return (float)get().xPos;
    }

    /**
     * ça renvoie la position Y du curseur en float
     * @return la position Y du curseur en float
     */
    public static float getY(){
        return (float)get().yPos;
    }

    /**
     * on fait la position X actuelle du curseur moins l'ancienne position
     * et ca permet de trouver la distance X que la souris a parcouru
     * @return la distance X que la souris a parcouru en float
     */
    public static float getDx(){
        return (float)(get().lastX - get().xPos);
    }

    /**
     * on fait la position Y actuelle du curseur moins l'ancienne position
     * et ca permet de trouver la distance Y que la souris a parcouru
     * @return la distance Y que la souris a parcouru en float
     */
    public static float getDy(){
        return (float)(get().lastY - get().yPos);
    }

    /**
     * ça renvoie la position scroll X
     * @return la position X du scroll en float
     */
    public static float getScrollX(){
        return (float)get().scrollX;
    }

    /**
     * ça renvoie la position scroll X
     * @return la position X du scroll en float
     */
    public static float getScrollY(){
        return (float)get().scrollY;
    }

    /**
     * @return ça renvoie un booleen qui dit si la souris est drag ou pas
     */
    public static boolean isDragging(){
        return get().isDragging;
    }


    /**
     * ca prend un int representant un boutton et nous dis si ole boutton est appuyer
     * si le boutton n'est pas present ca retourne un false
     * @param button le boutton a verifier
     * @return ca retourne un booleen qui dit si le boutton est appuyer
     */
    public static boolean mouseButtonDown(int button){
        if (button < get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }
        else {
            return false;
        }

    }


}
