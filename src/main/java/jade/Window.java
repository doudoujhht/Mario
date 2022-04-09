package jade;

public class Window {

    private int width;
    private int height;
    private String title;


    //on cree la seule fenetre qu'on aura
    private static Window window = null;
    //on cree un constructeur privé pour etre sur de n'avoir qu'une seule fenêtre
    private Window() {
        this.width =1920;
        this.height=1080;
        this.title = "Mario";
    }

    public static Window get(){

        //si il n'y a pas encore de fenetre creer une nouvelle fenetre
        if (Window.window == null){
            Window.window = new Window();
        }

        //retourner la fenetre qu'il y'a
        return Window.window;

    }
}
