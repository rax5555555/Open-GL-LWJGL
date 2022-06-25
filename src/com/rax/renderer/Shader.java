package com.rax.renderer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.*;

public class Shader 
{
	public int programId;

	String vsSrc1 =
	"#version 400 core" + "\n" +
	"layout (location = 0) in vec3 attrib_Position;" + "\n" +
	"void main(){" + "\n" +
			"gl_Position = vec4(attrib_Position, 1.0f);" + "\n" +
	"}";

	String vsSrc2 =
	"#version 400 core" + "\n" +
	"uniform float time;" + "\n" +
	"uniform vec2 resolution;" + "\n" +
	"void main( void ) {" + "\n" +
		"vec2 p = ( gl_FragCoord.xy / resolution.xy ) * 2.0 - 1.0;" + "\n" +
		"vec3 c = vec3( 0.0 );" + "\n" +
		"float amplitude = 0.10;" + "\n" +
		"float glowT = sin(time) * 0.5 + 0.5;" + "\n" +
		"float glowFactor = mix( 0.1, 0.15, glowT );" + "\n" +
		"c += vec3(0.1, 0.03, 0.13) * ( glowFactor * abs( 1.0 / sin(p.x + sin( p.y + time ) * amplitude ) ));" + "\n" +
		"c += vec3(0.02, 0.10, 0.03) * ( glowFactor * abs( 1.0 / sin(p.x + sin( p.y + time+1.00 ) * amplitude+0.1 ) ));" + "\n" +
		"c += vec3(0.3, 0.10, 0.02) * ( glowFactor * abs( 1.0 / sin(p.x + sin( p.y + time+3.00 ) * amplitude+0.2 ) ));" + "\n" +
		"gl_FragColor = vec4( c, 1.0 );" + "\n" +
	"}";

	/*"uniform float time;\n" +
			"uniform vec2  resolution;\n" +
			"#define PI 3.14159265359\n" +
			"mat2 rotate2d(float angle){\n" +
			"return mat2(cos(angle), -sin(angle), sin(angle), cos(angle));\n" +
			"}\n" +
			"void main(void){\n" +
			"vec2 p = (gl_FragCoord.xy * 2.0 - resolution) / min(resolution.x, resolution.y);\n" +
			"p = rotate2d(time / 1.0 * PI) * p;\n" +
			"float t = 8983.0784789378498377387873891 / abs(abs(sin(time)) - length(p));\n" +
			"gl_FragColor = vec4(vec3(t) * vec3(p.x,p.y,1.08933783973793739373), 1793793223383.0);\n" +
			"}\n";*/
	
	public Shader(String vsSrc, String fsSrc) 
	{
		Map<Integer, String> shaderSources = new HashMap<Integer, String>(2);
		shaderSources.put(1, vsSrc1);
		shaderSources.put(2, vsSrc2);
		this.compile(shaderSources);
	}
	
	public void compile(Map<Integer, String> shaderSources)
	{
		int program = glCreateProgram();
		
		List<Integer> shaderIds = new ArrayList<Integer>();
		int shaderIdIdxs = 1;
		
		for(int i = 0; i < shaderSources.size(); i++)
		{
			int type = i == 0 ? GL_VERTEX_SHADER : i == 1 ? GL_FRAGMENT_SHADER : - 1;
			String source = shaderSources.get(shaderIdIdxs);
			
			int shader = glCreateShader(type);
			glShaderSource(shader, source);
			glCompileShader(shader);
			
			int isCompiled = 0;
			isCompiled = glGetShaderi(shader, GL_COMPILE_STATUS);
			if(isCompiled == GL_FALSE)
			{
				int maxLength = 0;
				maxLength = glGetShaderi(shader, GL_INFO_LOG_LENGTH);
				
				String infoLog = "";
				infoLog = glGetShaderInfoLog(shader, maxLength);
				glDeleteShader(shader);
				
				String st = type == 0 ? "Vertex Shader" : "Fragment Shader";
				System.out.println("Cannot compile " + st + ": " + infoLog);
				System.exit(-1);
			}
			
			glAttachShader(program, shader);
			shaderIdIdxs++;
		}
		
		glLinkProgram(program);
		
		int isLinked = 0;
		isLinked = glGetProgrami(program, GL_LINK_STATUS);
		if(isLinked == GL_FALSE)
		{
			int maxLength = 0;
			maxLength = glGetProgrami(program, GL_INFO_LOG_LENGTH);
			
			String infoLog = "";
			infoLog = glGetProgramInfoLog(program, maxLength);
			
			for(int shaderId : shaderIds)
			{
				glDetachShader(program, shaderId);
			}
			
			for(int shaderId : shaderIds)
			{
				glDeleteShader(shaderId);
			}
		
			System.out.println("Cannot link shader program! ");
			System.out.println(infoLog);
			System.exit(-1);

		}
		
		for(int shaderId : shaderIds)
		{
			glDetachShader(program, shaderId);
		}
		
		this.programId = program;
	}
	
	
	public String readFile(String file)
	{
		StringBuilder shaderSource = new StringBuilder();
		try {
			File filer = new File(file);
			FileReader fr = new FileReader(filer);
			BufferedReader reader = new BufferedReader(fr);
			String line;
			while ((line = reader.readLine())!=null) {
				shaderSource.append(line);
				shaderSource.append("\n");
			}
			//System.out.println(shaderSource.toString());
			reader.close();
			return shaderSource.toString();
		} catch(Exception e)
		{
			System.out.println("This file '" + file + "' cound be read!");
			e.printStackTrace();
			Runtime.getRuntime().exit(-1);
		}
		return "[Reading Error]: This file '" + file + "' cound be read!";
	}
	
	public void bind()
	{
		glUseProgram(this.programId);
	}
	
	public void unBind()
	{
		glUseProgram(0);
	}
	
	public void setUnifromInt(String name, int value)
	{
		glUniform1i(glGetUniformLocation(programId, name), value);
	}
	
	public void setUnifromInt2(String name, int x, int y)
	{
		glUniform2i(glGetUniformLocation(programId, name), x, y);
	}
	
	public void setUnifromInt3(String name, int x, int y, int z)
	{
		glUniform3i(glGetUniformLocation(programId, name), x, y, z);
	}
	
	public void setUnifromInt4(String name, int x, int y, int z, int w)
	{
		glUniform4i(glGetUniformLocation(programId, name), x, y, z, w);
	}
	
	public void setUnifromFloat(String name, float value)
	{
		glUniform1f(glGetUniformLocation(programId, name), value);
	}
	
	public void setUnifromFloat2(String name , float x, float y)
	{
		glUniform2f(glGetUniformLocation(programId, name), x, y);
	}
	
	public void setUnifromFloat3(String name, float x, float y, float z)
	{
		glUniform3f(glGetUniformLocation(programId, name), x, y, z);
	}
	
	public void setUnifromFloat4(String name, float x, float y, float z, float w)
	{
		glUniform4f(glGetUniformLocation(programId, name), x, y, z, w);
	}
	
	public void setUniformBoolean(String name, boolean value)
	{
		glUniform1i(glGetUniformLocation(programId, name), value == true ? 1 : 0);
	}

	public void setUnifromVec2(String name, Vector2f value)
	{
		glUniform2f(glGetUniformLocation(programId, name), value.x, value.y);
	}
	
	public void setUnifromVec3(String name, Vector3f value)
	{
		glUniform3f(glGetUniformLocation(programId, name), value.x, value.y, value.z);
	}

	public void setUnifromVec4(String name, Vector4f value)
	{
		glUniform4f(glGetUniformLocation(programId, name), value.x, value.y, value.z, value.w);
	}
}
