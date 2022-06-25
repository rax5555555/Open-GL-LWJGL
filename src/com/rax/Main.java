package com.rax;

import org.lwjgl.opengl.GL11;
import org.lwjgl.system.Configuration;

import static org.lwjgl.opengl.GL11.*;

public class Main {

    public static Configuration configuration;

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("os.arch"));

        //System.out.println("OpenGL version " + GL11.glGetString(GL11.GL_RENDERER));
        new Engine().run();
    }


}
