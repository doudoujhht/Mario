    /*
    bon la faut etre honnete je suis pas un expert en shader en vrai ca me fait chier
    je saurais pas expliquer ce que la plupart de ces lignes veulent dire donc me demandez pas
    je pense que stack overflow sait plus que moi la dessus
    je pourrais mettre des commentaire mais c'est trop abstrait meme pour moi
    */

    #type vertex
    #version 330 core
    // dans aPos a signifie attribute
    layout (location = 0) in vec3 aPos;
    layout (location = 1) in vec4 aColor;

    out vec4 fColor;

    void main(){
        fColor = aColor;
        gl_Position = vec4(aPos, 1.0);
    }

    #type fragment
    #version 330 core

    in vec4 fColor;

    out vec4 color;

    void main(){
        color = fColor;
    }