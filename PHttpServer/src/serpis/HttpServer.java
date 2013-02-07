package serpis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpServer {
	private static final String newLine = "\r\n";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//final String newLine = "\r\n";
		final int port = 8080;
		ServerSocket serverSocket = new ServerSocket(port);
		
	//String threadName = Thread.currentThread().getName();	
	//System.out.println("threadName=" + threadName);
	
		
		while (true) {
			Socket socket = serverSocket.accept();
			
			//SimpleServer.Process(socket);
			
			Runnable runnable = new ThreadServer(socket);
			Thread thread =new Thread(runnable);
			thread.start();
			
		}
	}	
		//serverSocket.close();
}
	
