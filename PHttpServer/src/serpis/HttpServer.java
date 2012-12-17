package serpis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;



public class HttpServer 
{
	public static void main(String[] args) throws IOException
	{
		final String newLine = "\r\n";
		final int port = 8080;
		final String fileNameError404 = "fileError404.html";
		final String response200 = "HTTP/1.0 200 OK";
		final String response404 = "HTTP/1.0 404 Not Found";
		
		
		ServerSocket serverSocket = new ServerSocket(port);
		
		Socket socket = serverSocket.accept();
		
		Scanner scanner = new Scanner(socket.getInputStream());
		
		String fileName = "index.html";
		
		while(true)
		{
			String line = scanner.nextLine();

			System.out.println(line);
			if(line.equals(""))
				break;
		}
		
		File file =new File(fileName);
		
		String responseFileName = file.exists() ? fileName : fileNameError404;
		String response = file.exists() ? response200 : response404;
			
		String header = response + newLine + newLine;		
		byte[] headerBuffer = header.getBytes();
		
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(headerBuffer);
		
		final int bufferSize = 2048;
		byte[] buffer = new byte[bufferSize];
		
		FileInputStream fileInputStream = new FileInputStream(responseFileName);
		
		int count;
		
		while((count = fileInputStream.read(buffer)) !=-1 );
			outputStream.write(buffer, 0, count);
			
		fileInputStream.close();
		
		socket.close();
		serverSocket.close();	
	}
	
		
		
		
		
		
		/*int    port   = 8080;
		String wwwhome = "127.0.0.1";
		
		// open server socket
		ServerSocket socket = null; 
		
		try 
		{
		    socket = new ServerSocket(port); 
		} 
		catch (IOException e) 
		{
		    System.err.println("No se ha podido iniciar el servidor: " + e);
		    System.exit(-1);
		}
		
		
		
		System.out.println("Aceptando conexiones en el puerto " + port);
		    
		// bucle principal para aceptar peticiones
		while (true) 
		{
		    Socket connection = null;
		    try 
		    {
				// esperamos la petición y la aceptamos
				connection = socket.accept();
				
				// Cogemos la entrada y la salida para ese socket
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				OutputStream out = new BufferedOutputStream(connection.getOutputStream());
				PrintStream pout = new PrintStream(out);
				
				// read first line of request (ignore the rest)
				String request = in.readLine();
				if (request==null)
				    continue;
				
				// imprimimos en consola información sobre la peticion
				log(connection, request);
				
				while (true) 
				{
				    String misc = in.readLine();
				    if (misc==null || misc.length()==0)
				    	break;
				}
			
			// parseamos la primera linea para saber si hay algún error en el GET
			if (!request.startsWith("GET") || request.length()<14 ||
			    !(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"))) 
			{
			    // PETICIÓN ERRÓNEA (LA PRIMERA LÍNEA NO ES CORRECTA)
			    errorReport(pout, connection, "400", "Bad Request", 
					"Tu navegador ha enviado una peticion que este servidor no puede entender");
			} 
			else 
			{
				// SI NO TENEMOS NINGÚN ERROR
				
				// en req obtenemos el nombre del fichero que nos piden (index.html, xx.html, ...)
			    String req = request.substring(4, request.length()-9).trim();
			    
			    // medidas de seguridad, para no subir a directorios padre
			    if (req.indexOf("..")!=-1 || req.indexOf("/.ht")!=-1 || req.endsWith("~")) 
			    {
			    	// Petición malvada, un hacker intenta leer un directorio que no es del home www o archivo protegido
			    	errorReport(pout, connection, "403", "Forbidden", "No tienes permisos para acceder a la URL solicitada.");
			    } 
			    else
			    {
			    	// lo conviertes al path completo en el servidor ????????
					String path = wwwhome + "/" + req;
					File f = new File(path);
					if (f.isDirectory() && !path.endsWith("/"))
					{
					    // redirect browser if referring to directory without final '/'
					    pout.print("HTTP/1.0 301 Moved Permanently\r\n" +
						       "Location: http://" + 
						       connection.getLocalAddress().getHostAddress() + ":" +
						       connection.getLocalPort() + "/" + req + "/\r\n\r\n");
					    log(connection, "301 Moved Permanently");
					} 
					else 
					{
				    
						if (f.isDirectory()) 
						{ 
							// if directory, implicitly add 'index.html'
							path = path + "index.html";
							f = new File(path);
						}
						
					    try 
					    { 
					    	
					    	//if(!path.endsWith("index.html"))
					    	//	throw new FileNotFoundException("fichero no es index.html");
					    	
					    	
							// send file
							InputStream file = new FileInputStream(f);
							pout.print("HTTP/1.0 200 OK\r\n" +
								   "Content-Type: " + guessContentType(path) + "\r\n" +
								   "Date: " + new Date() + "\r\n" +
								   "Server: FileServer 1.0\r\n\r\n");
							sendFile(file, out); // send raw file 
							log(connection, "200 OK");
					    } 
					    catch (FileNotFoundException e) 
					    { 
					    	// Fichero no encontrado
					    	errorReport(pout, connection, "404", "Not Found",
							    "El fichero pedido no está en nuestro servidor.");
					    }
					}
			    }
			}
			
			out.flush();
		    } 
		    catch (IOException e) 
		    { 
		    	System.err.println(e); }
		    try 
		    {
		    	if (connection != null) 
		    		connection.close(); 
		    }
		    catch (IOException e) 
		    { 
		    	System.err.println(e); 
		    }
		}
		
	} // end main
	    
	    
	    private static void log(Socket connection, String msg)
	    {
	    	System.err.println(new Date() + " [" + connection.getInetAddress().getHostAddress() +  ":" + connection.getPort() + "] " + msg);
	    }

	    private static void errorReport(PrintStream pout, Socket connection, String code, String title, String msg)
	    {
	    	pout.print("HTTP/1.0 " + code + " " + title + "\r\n" +
			   "\r\n" +
			   "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
			   "<TITLE>" + code + " " + title + "</TITLE>\r\n" +
			   "</HEAD><BODY>\r\n" +
			   "<H1>" + title + "</H1>\r\n" + msg + "<P>\r\n" +
			   "<HR><ADDRESS>FileServer 1.0 at " + 
			   connection.getLocalAddress().getHostName() + 
			   " Port " + connection.getLocalPort() + "</ADDRESS>\r\n" +
			   "</BODY></HTML>\r\n");
		log(connection, code + " " + title);
	    }

	    private static String guessContentType(String path)
	    {
			if (path.endsWith(".html") || path.endsWith(".htm")) 
			    return "text/html";
			else if (path.endsWith(".txt") || path.endsWith(".java")) 
			    return "text/plain";
			else if (path.endsWith(".gif")) 
			    return "image/gif";
			else if (path.endsWith(".class"))
			    return "application/octet-stream";
			else if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
			    return "image/jpeg";
			else 	
			    return "text/plain";
	    }

	    private static void sendFile(InputStream file, OutputStream out)
	    {
	    	
	    	
	    	// enviar el fichero
			try 
			{
			    byte[] buffer = new byte[1000];
			    while (file.available()>0) 
				out.write(buffer, 0, file.read(buffer));
			} 
			catch (IOException e) 
			{ 
				System.err.println(e);
			}
	    }

	
	
	
	
	
	
	
	
	  //this method makes the HTTP header for the response
	  //the headers job is to tell the browser the result of the request
	  //among if it was successful or not.
	  private String construct_http_header(int return_code) 
	  {
	    String s = "HTTP/1.0 ";
	    //you probably have seen these if you have been surfing the web a while
	    switch (return_code) 
	    {
	      case 200:	        s = s + "200 OK";	        break;
	      case 400:	        s = s + "400 Bad Request";	        break;
	      case 403:	        s = s + "403 Forbidden";	        break;
	      case 404:	        s = s + "404 Not Found";	        break;
	      case 500:	        s = s + "500 Internal Server Error";	        break;
	      case 501:	        s = s + "501 Not Implemented";	        break;
	    }

	    s = s + "\r\n"; //other header fields,
	    s = s + "Connection: close\r\n"; //we can't handle persistent connections
	    s = s + "Server: SimpleHTTPtutorial v0\r\n"; //server name
	    
	    return s;
	    
	  }*/

}
