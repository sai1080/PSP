package serpis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ThreadServer  implements Runnable {
	
	//primer propuesta
	//codigo buscado de un servidor multihilo
	
	/*private ServerSocket server = null;
	private Thread running = null;

	public ThreadServer (int port){
		
		  try
          {
                  server = new ServerSocket(port); // Iniciando servidor en el puerto especificado
          }
          catch(Exception e)
          {
                  System.out.println("Error!"); // ERROR!
                  return; // Impedir que continué el código para que no se cree un thread sin servidor...
          }
         
          running = new Thread(this); // Creando el Thread
          running.start(); // Iniciando el thread, ejecutara la función run inmediatamente
  }
 
  public void run() // Función necesaria en todos los objetos Thread ó Runnable
  {
          while(!server.isClosed()) // Mientras el servidor NO esté cerrado...
          {
                  try
                  {
                          Socket client = server.accept(); // Aceptando nueva conexión de un cliente
                          HttpClient clientMgr = new HttpClient(client); // Creando el controlador del cliente
                  }
                  catch(Exception e)
                  {
                          crash(); // En caso de error cerrar el servidor
                  }
          }
         
          crash(); // Cuando el servidor  está cerrado, cerrar puertos y destruir instancias
  }
 
  public void crash()
  {
          try
          {
                  server.close(); // Cerrar servidor
          }
          catch(Exception e)
          {
         
          }
          finally
          {
                  server = null; // Destruir la instancia del servidor
          }
         
          running.interrupt(); // Interrumpir el Thread para que pare
          running = null; // Destruyendo Thread
  }*/

	//-----------------------------------------------------------------------------//
	
	//segunda propuesta
	
	/*private Socket socket = null;
	private static final String newLine = "\r\n";
	
	//creamos el constructor
	public ThreadServer (Socket socketClient){	
		socket = socketClient;	
	}
	
	public static void main(String[]args) throws IOException{
		
		final int port = 8080;
		
		ServerSocket serverSocket = null;
		
		try{
			
			
			serverSocket = new ServerSocket(port);		
		}catch(IOException e){
			System.out.println("error");
			//return;
		}
		
		while(true){
			
			ThreadServer threadServer = new ThreadServer(serverSocket.accept());
			Thread thread = new Thread(threadServer);
			thread.start();
			
		}
		
		
	}

	@Override
	public void run() {
		try{
		
				String fileName =  getFileName(socket.getInputStream());
				//metodos para escribir la cabecera y el resto del archivo
				writeHeader(socket.getOutputStream(), fileName);
				writeFile(socket.getOutputStream(), fileName);
				socket.close();
			}
		
		
		catch(Exception e){
			System.err.println("error: "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				}
				
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
		
	}*/
		
	//--------------------------------------------solucion clase-----------------
	private static final String newLine = "\r\n";
	private final String defaultFileName = "index.html";
	private final String response200 = "HTTP/1.0 200 OK";
	private final String response404 = "HTTP/1.0 NOt Found";
	private final String fileNameError404 = " fileError 404";
	
	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private String fileName;
	private boolean fileExists;
	
	
	public ThreadServer(Socket socket){
		this.socket = socket;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
			System.out.println(Thread.currentThread().getName()+ "inicio.");
			
			try{
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			getFileName();
			getFileExists();
			writeHeader();
			writeFile();
			
			socket.close();
			}
			catch(IOException ex){			
			}
			catch(InterruptedException ex){				
			}
			
			System.out.println(Thread.currentThread().getName()+ "fin.");
		}

	
		private  void getFileName(){
			
			Scanner scanner = new Scanner(inputStream);
			
			
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
			System.out.println(Thread.currentThread().getName()+"fileName="+fileName);
	
			//otra manera de hacer en vez del if
			//return !fileName.equals("") ? fileName : defaultFileName;
			}
		
		private void getFileExists(){
			File file = new File(fileName);
			fileExists = file.exists();
			
			
		}
		
		private  void writeHeader( ) throws IOException{
			
			File file = new File(fileName);		
			
			String response = file.exists() ? response200 : response404;
			
			String header = response + newLine + newLine;
			
			byte[] headerBuffer = header.getBytes();
			
			outputStream.write(headerBuffer);					
		}
		
		private  void writeFile() throws IOException, InterruptedException{
					
			 
			 File file = new File(fileName);
			 
			 String responseFileName = file.exists() ? fileName : fileNameError404;
			 
			 final int bufferSize = 2048;
			 
			 byte[] buffer = new byte[bufferSize];
			 
			FileInputStream fileInputStream = new FileInputStream(responseFileName);
			
			int count;
			
			while((count = fileInputStream.read(buffer)) != -1){
				System.out.print(Thread.currentThread().getName()+",");
				Thread.sleep(1000);
			outputStream.write(buffer, 0, count);
			}
			fileInputStream.close();		
		}
}
