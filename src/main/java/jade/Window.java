package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

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

    //on cree la seule fenetre qu'on aura
    private static Window window = null;
    //on cree un constructeur privé pour etre sur de n'avoir qu'une seule fenêtre
    private Window() {
        this.width =1920;
        this.height=1080;
        this.title = "Mario";
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
        // make the openGLK context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make window visible
        glfwShowWindow(glfwWindow);

        // this line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context taht is managed externally.
        // creates the GLCapabilities instance and makers the OpenGL
        // bindings availaible for use.
        GL.createCapabilities();


    }
    public void loop(){
        while (!glfwWindowShouldClose(glfwWindow)){
            // Poll events
            glfwPollEvents();

            //C'est de couleur c'est un format bizarre mais il y'a surement des convertisseur online
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);


        }
    }
}
