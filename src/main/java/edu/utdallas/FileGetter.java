package edu.utdallas;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by danei on 5/26/2016.
 */
@WebServlet("/internal/*")
public class FileGetter extends HttpServlet{
	protected void doPost(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException{
		doGet(request,response);
	}

	protected void doGet(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException{
		String req_url=request.getPathInfo().substring(1);
		File result=new File(Compile.temp,req_url);
		if(!result.exists() || !result.isFile()) response.sendError(HttpServletResponse.SC_NOT_FOUND);
		else{
			OutputStream out=response.getOutputStream();
			FileInputStream in=new FileInputStream(result);
			byte[] buffer=new byte[4096];
			int length;
			while((length=in.read(buffer))>0){
				out.write(buffer,0,length);
			}
			in.close();
			out.flush();
		}
	}
}
