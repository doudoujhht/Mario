package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private boolean[] keyPressed=new boolean[350];//si ton clavier a plus de touche moi je give up demerde toi

    private KeyListener(){}//constructeur vide inutile mais il doit etre private so on le met

    /**
    la methode singleton on cree un contructeur private et cree un get qui cree le singleton la premiere fois
     el le renvoie a chaque fois
     j'ecris genre j'etais tah philosophe alors que j'ai 17 ans
     a deux doigt encore faire des animations en css
     @return ca retourne le singleton de keyListener
     */
    public static KeyListener get(){
        if (KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    /**
     *
     * @param window la fenetre a modifier en long
     * @param key la touche appuyer
     * @param scancode demandez a la doc
     * @param action l'action effectuer
     * @param mods demander a la doc
     */
    public static void keycallback(long window, int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS){
            get().keyPressed[key] = true;
        }else if(action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    /**
     *
     * @param keyCode la touche a verifier
     * @return retourne un booleen qui dit si la touche est appuyer ou pas
     */
    public static boolean isKeyPressed(int keyCode){
            return get().keyPressed[keyCode];
    }
}
