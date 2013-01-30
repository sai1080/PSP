package serpis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleServer {
	
	private static final String newLine = "\r\n";
	
	public static void main(String[]args) throws IOException{
		
		final int port = 8080;
		ServerSocket serverSocket = new ServerSocket(port);
		
		String threadName = Thread.currentThread().getName();//obtenemos el nombre del thread
		System.out.println("threadName =" + threadName);
				
	
	
	try{
			while(true)
			{
				Socket socket = serverSocket.accept();
				
				String fileName =  getFileName(socket.getInputStream());
				//metodos para escribir la cabecera y el resto del archivo
				writeHeader(socket.getOutputStream(), fileName);
				writeFile(socket.getOutputStream(), fileName);
				socket.close();
			}
		
		}
		catch(Exception e){
			System.err.println("error: "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			serverSocket.close();
		}
	
	}
		private static String getFileName(InputStream inputStream){
			
			Scanner scanner = new Scanner(inputStream);
			final String defaultFileName = "index.html";
			
			//String fileName = "index.html";
			String fileName= "";
			
			while(true){
				
				String line = scanner.nextLine();
				if(line.startsWith("GET")){//GET /index.html HTTP/1.1
					//opcion1
					//fileName = line.split(" ")[1].substring(1);//obtenemos index.html
					
					//opcion2
					fileName = line.substring(5, line.indexOf(" ", 5));
					
					/*opcion3
					int index = 5;
					while(line.charAt(index) != ' '){
						fileName +=  line.charAt(index++);					
					}*/
					
					System.out.println("fileName = " + fileName);				
				}
				
				if (line.equals(""))
					break;
			}
			//cuando pedimos la raiz:localhost:8080, obtendremos el fichero index.
			if(fileName.equals(""))
				fileName = defaultFileName;	
			System.out.println("fileName="+fileName);
			return fileName;
			//otra manera de hacer en vez del if
			//return !fileName.equals("") ? fileName : defaultFileName;
			}
		
		private static void writeHeader(OutputStream outputStream, String fileName) throws IOException{
			
			File file = new File(fileName);
			
			final String response200 = "HTTP/1.0 200 OK";
			final String response404 = "HTTP/1.0 NOt Found";
			
			String response = file.exists() ? response200 : response404;
			
			String header = response + newLine + newLine;
			
			byte[] headerBuffer = header.getBytes();
			
			outputStream.write(headerBuffer);					
		}
		
		private static void writeFile(OutputStream outputStream, String fileName) throws IOException{
			
			 String fileNameError404 = " fileError 404";
			 
			 File file = new File(fileName);
			 
			 String responseFileName = file.exists() ? fileName : fileNameError404;
			 
			 final int bufferSize = 2048;
			 
			 byte[] buffer = new byte[bufferSize];
			 
			FileInputStream fileInputStream = new FileInputStream(responseFileName);
			
			int count;
			
			while((count = fileInputStream.read(buffer)) != -1)
			outputStream.write(buffer, 0, count);
			
			fileInputStream.close();
			
		}
	
	
	
}