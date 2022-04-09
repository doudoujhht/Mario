package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static java.lang.invoke.MethodHandles.loop;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;



public class Window {

    private int width;
    private int height;
    private String title;
    private long glfwWindow;

    //color
    public float r;
    public float g;
    public float b;
    public float a;

    private boolean fadeToBlack;

    //on cree la seule fenetre qu'on aura
    private static Window window = null;

    private static Scene currentScene;
    //on cree un constructeur privé pour etre sur de n'avoir qu'une seule fenêtre
    private Window() {
        this.width =1920;
        this.height=1080;
        this.title = "Mario";
        r=1;
        g=1;
        b=1;
        a=1;
    }

    public static void  changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene =new LevelEditorScene();
                //currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                //rien a voir mais je viens de catch a quoi les assertion servaient geeksforkeeks mgl
                assert false: "unknow scene" +newScene;
                break;
        }
    }

    /**
     * ca crée une fenetre si il y'en a pas deja et la retourne
     * @return la fenetre de jeu
     */
    public static Window get(){

        if (Window.window == null){
            Window.window = new Window();
        }

        return Window.window;

    }

    /**
     * ca run le programme avec les methode init et loop
     * et ca vide la memoire une fois le programe finit
     */
    public void run(){
        System.out.println("hello LWGL " + Version.getVersion() + "!");
        init();
        loop();

        //free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    /**
     * on peut retrouver ca sur le site de Lwjgl.org/guide
     * c'est le setup basic pour afficher une fenetre
     * je comprend pas toius mais c'est chill pcq on doit ecrire ca une seule fois et on peut copier coller
     * on doit juste change les variables
     * c'est comme le ddebut de html c'est long et un jour je vais retenir
     */

    public void init(){
        //setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //la fenetre est invisible pcq on a pas encore fini de la creer
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); //la fenetre est resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); //la fenetre est maximiser

        // create the window
        //ca retourne un long qui creer la fenetre un peu comme les nombre daens chmod
        //null dans ce cas equivaut a 0l so zero mais en long
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if (glfwWindow == NULL){
            throw new IllegalStateException("Failled to create the GLFW window");
        }

        /*
         les deux deux point servent à referencer une methode deja existante
        dans notre cas ça veut dire que lorsqu'on appelle les methodes de call back de la souris
         ces méthodes là appellent nos méthodes à la place
         tout ca vient de la doc de glfw
         et oui j'utilise le mot doc pour paraitre plus intelligent
         */

        glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow,KeyListener::keycallback);
        // make the openGLK context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        // en gros ca gere les fps pour qu'il soit adapte a ton ecran
        glfwSwapInterval(1);

        // Make window visible
        glfwShowWindow(glfwWindow);

        // this line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context taht is managed externally.
        // creates the GLCapabilities instance and makers the OpenGL
        // bindings availaible for use.
        GL.createCapabilities();

        Window.changeScene(0);


    }
    public void loop(){
        float beginTime= Time.getTime();
        float endTime= Time.getTime();
        float dt = -1.0f;
        while (!glfwWindowShouldClose(glfwWindow)){
            // Poll events
            glfwPollEvents();

            //C'est de couleur c'est un format bizarre mais il y'a surement des convertisseur online
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt>=0){
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            /*
            dt ca veux dire delta time je sais c'est stylé
            en gros end time c'est le temps ou la boucle finit et begin time c'est qd elle commence
            ca permet de savoir combien de temps la boucle dure
            on reset le begin time ici et pas en haut pcq le systeme d'exploitation peut faire des choses avant de relancer le while
            et si on met le resetter en haut ca fausse un peu les résultat
            non mais imagine c'est quoi 2nano seconde dans un speed run
             */
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;


        }
    }
}
