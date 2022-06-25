package com.rax;

import com.rax.renderer.*;
import org.joml.Vector2f;
import com.rax.input.Mouse;
import com.rax.input.Keyboard;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.NVXGPUMemoryInfo;

import java.io.File;
import java.nio.IntBuffer;

public class Engine 
{

	public static final int WIDHT = 500;
	public static final int HEIGHT = 500;
	public static final String TITLE = "Engine 0.0.1 pre-alpha";
	private EngineWindow engineWindow;
	public Shader shader;
	
	public void run() 
	{
		this.init();
	}
	
	public void init() 
	{
		this.engineWindow = new EngineWindow(WIDHT, HEIGHT, TITLE);
		this.engineWindow.create();

		System.out.println(GL11.glGetString(GL11.GL_VENDOR));
		System.out.println(GL11.glGetString(GL11.GL_VERSION));
		System.out.println(GL11.glGetString(GL11.GL_RENDERER));
		if(Double.parseDouble(GL11.glGetString(GL11.GL_VERSION).substring(0,3)) < 3.0) {
			System.out.println("Версия OpenGL не поддерживается");
			System.exit(-1);
		}

		IntBuffer maxVram = BufferUtils.createIntBuffer(1);
		IntBuffer usedVram = BufferUtils.createIntBuffer(1);
		glGetIntegerv(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_DEDICATED_VIDMEM_NVX, maxVram);
		glGetIntegerv(NVXGPUMemoryInfo.GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX, usedVram);
		System.out.println(maxVram.get(0)/1024 + "MB / " + (maxVram.get(0)-usedVram.get(0))/1024 + "MB");


		this.shader = new Shader("","");
		this.update();
	}
	
	public void update() 
	{
		float[] vertices =
			{
//                  positions          colours
				1f,  1f, 0,  1.0f, 1.0f, 0.0f, 1.0f,
			   -1f,  1f, 0,  1.0f, 0.0f, 1.0f, 1.0f,
			   -1f, -1f, 0,  1.0f, 1.0f, 0.0f, 1.0f,
			    1f, -1f, 0,  0.0f, 1.0f, 0.0f, 1.0f
			};
		
		int [] indices = {0, 1, 2, 0, 2, 3};
		
		VertexArrayObject vertexArray = new VertexArrayObject();
		VertexBufferObject vertexBuffer = new VertexBufferObject(vertices);
		vertexBuffer.setLayout(new BufferLayout
		(
				new VertexAttribute("attrib_Position", VertexAttribute.ShaderDataType.t_float3),
				new VertexAttribute("attrib_Colour", VertexAttribute.ShaderDataType.t_float4)
		));
		vertexArray.putBuffer(vertexBuffer);
		IndexBufferObject indexBuffer = new IndexBufferObject(indices);
		vertexArray.putBuffer(indexBuffer);

		Vector2f resolution = new Vector2f(WIDHT,HEIGHT);

		while(!this.engineWindow.isCloseRequest())
		{
			Timer.getFPS();

			Keyboard.handleKeyboardInput();
			Mouse.handleMouseInput();
			
			glClearColor(0, 1, 1, 1);
			glClear(GL_COLOR_BUFFER_BIT);

			vertexArray.bind();
			this.shader.bind();
			this.shader.setUnifromFloat("time", (float) System.nanoTime()/1000000000);
			//System.out.println((float) System.nanoTime()/1000000000);

			this.shader.setUnifromVec2("resolution", resolution);
			glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
			this.shader.unBind();
			vertexArray.unBind();
			this.engineWindow.update();

		}
		
		this.engineWindow.destroy();
	}

	public EngineWindow getEngineWindow() 
	{
		return this.engineWindow;
	}


}
