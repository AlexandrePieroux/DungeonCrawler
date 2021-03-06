package be.ac.ulg.info0940.g08.dungeoncrawler.GraphicGameEngine;

import android.opengl.GLES20;

/**
 * Created by alexandre on 16/03/16.
 * Static class that load the shader.
 */
public class GlShader {
    public static int sp_Image;
    public static final String vs_Image =
                      "uniform mat4 uMVPMatrix;"
                    + "attribute vec4 vPosition;"
                    + "attribute vec2 a_texCoord;"
                    + "varying vec2 v_texCoord;"
                    + "void main() {"
                    + "  gl_Position = uMVPMatrix * vPosition;"
                    + "  v_texCoord = a_texCoord;"
                    + "}";
    public static final String fs_Image =
                      "precision mediump float;"
                    + "varying vec2 v_texCoord;"
                    + "uniform sampler2D s_texture;"
                    + "void main() {"
                    + "  gl_FragColor = texture2D(s_texture, v_texCoord);"
                    + "}";

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
