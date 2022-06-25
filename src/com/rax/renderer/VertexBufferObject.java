package com.rax.renderer;

import com.rax.util.MemoryManagment;

import static org.lwjgl.opengl.GL15C.*;

public class VertexBufferObject 
{
	private int id;
	private float[] data;
	private int usage;
	private BufferLayout layout;
	
	public VertexBufferObject(float... data) 
	{
		this.data = data;
		this.usage = GL_STATIC_DRAW;
		this.create();
		this.bind();
		this.putData();
	}
	
	private void create()
	{
		//this.id = glCreateBuffers();
		this.id = glGenBuffers();
	}
	
	public void destroy()
	{
		glDeleteBuffers(this.id);
	}
	
	private void putData()
	{
		glBufferData(GL_ARRAY_BUFFER, MemoryManagment.putData(this.data), this.usage);
	}
	
	public void bind()
	{
		glBindBuffer(GL_ARRAY_BUFFER, this.id);
	}
	
	public void unBind()
	{
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public BufferLayout getLayout() 
	{
		return layout;
	}

	public void setLayout(BufferLayout layout) 
	{
		this.layout = layout;
	}

	public float[] getData() 
	{
		return data;
	}

	public int getUsage() 
	{
		return usage;
	}
}
