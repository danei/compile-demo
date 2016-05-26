package edu.utdallas;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Created by danei on 5/26/2016.
 */
@WebServlet("/CompileServlet")
public class Compile extends HttpServlet{
	public static File temp=null;
	static{
		try{
			temp=Files.createTempDirectory(null).toFile();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	protected void doPost(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException{
		doGet(request,response);
	}

	protected void doGet(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		PrintStream ps=new PrintStream(baos);
		try{
			JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
			DiagnosticCollector<JavaFileObject> diagnosticCollector=new DiagnosticCollector<>();
			StandardJavaFileManager fileManager=compiler.getStandardFileManager(diagnosticCollector,null,null);
			String java_src=request.getParameter("src");
			String java_name=request.getParameter("name");
			String[] options=new String[]{"-d",temp.getCanonicalPath()};    // add -cp classpath option, etc here
			JavaFileObject java_file=new SimpleJavaFileObj(java_name,java_src);
			Iterable<? extends JavaFileObject> unit=Arrays.asList(java_file);
			JavaCompiler.CompilationTask task=compiler.getTask(null,fileManager,diagnosticCollector,Arrays.asList(options),null,unit);
			boolean success=task.call();
			ps.println("Compilation "+(success ? "is successful." : "failed!"));
			for(Diagnostic d : diagnosticCollector.getDiagnostics()){
				ps.println(d.getCode());
				ps.println(d.getKind());
				ps.println(d.getPosition());
				ps.println(d.getStartPosition());
				ps.println(d.getEndPosition());
				ps.println(d.getSource());
				ps.println(d.getMessage(null));
			}
			if(success){

			}
		}catch(Exception e){
			e.printStackTrace(ps);
		}
		request.setAttribute("log",baos.toString(Charset.defaultCharset().name()));
		RequestDispatcher dispatcher=getServletContext().getRequestDispatcher("/result.jsp");
		dispatcher.forward(request,response);
	}
}
