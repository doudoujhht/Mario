package jade;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private String vertexShaderSrc = "    #version 330 core\n" +
            "    // dans aPos a signifie attribute\n" +
            "    layout (location = 0) in vec3 aPos;\n" +
            "    layout (location = 1) in vec4 aColor;\n" +
            "\n" +
            "    out vec4 fColor;\n" +
            "\n" +
            "    void main(){\n" +
            "        fColor = aColor;\n" +
            "        gl_Position = vec4(aPos, 1.0);\n" +
            "    }";

    private String fragmentShaderSrc = "    #version 330 core\n" +
            "\n" +
            "    in vec4 fColor;\n" +
            "\n" +
            "    out vec4 color;\n" +
            "\n" +
            "    void main(){\n" +
            "        color = fColor;\n" +
            "    }";

    private int vertexID;
    private int shaderProgram;
    private int fragmentID;


    /* position c'est des position et color c'est des couleur oui je sais c'est très instructif
    je ne fume pas donc perso je vais utiliser des generateurs sur internet
    vous avez vu les gens qui font des svg a la main
    non mais c'est des fumeurs c'est pas possible
    c'est pas humain
    bon pour la theorie chaque position et couleur c'est un point le element array dit comment on doit relier les point
    chaque point a un index en locurrence
    bottom right = 0;
    top left = 1;
    top right = 2;
    bottom left = 3;
    et quand on les relient les couleurs de chaque point point crée un gradient
    */
    private float[] vertexArray={
            //positon                // color
             0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // bottom right 0
            -0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
             0.5f,  0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            -0.5f, -0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // bottom left  3

    };


    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                   x       x


                   x       x
            */

            2, 1, 0, // top right triangle
            0, 1, 3 // top left triangle
    };

    private int vaoID; // vertex array object
    private int vboID; // vertex buffer object
    private int eboID; // element buffer object


    public LevelEditorScene(){
    }

    @Override
    public  void init(){
        // =========================================================
        // Compile and link shaders
        // =========================================================

        // first load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // pass the shader source code to the GPU
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        // check for error ;
        // the french en gros on verifie que tous les truc complique d'affichage c'est bien passé
        // puisque ca vient de C on doit tout gerer avec des nombres et des constantes ca clc
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if (success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl '\n\t Vertex shader compilation failed. ");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // first load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // pass the shader source code to the GPU
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        // check for error ;
        // the french en gros on verifie que tous les truc complique d'affichage c'est bien passé
        // puisque ca vient de C on doit tout gerer avec des nombres et des constantes ca clc
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);

        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl '\n\t Fragment shader compilation failed. ");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram,vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        //check for linking error

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl '\n\tLinking of shaders failed. ");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        // =============================================================//
        // Genereate VAO, VBO, and EBO buffer objects, and send to GPU  //
        // =============================================================//

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // creates a float buffer of vertices

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // create VBO upload the vertex buffer
        vboID = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //create indices and upload

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // add vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0) ;

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize *floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void update(float dt) {
        //System.out.println((1.0f/dt)+ "FPS"); afficher les fps

        // Bind shader program
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);

        //enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT,0);

        // unbind everything

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        glUseProgram(0);





    }
}
