package serpis;

import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer  implements Runnable {
	
	private ServerSocket server = null;
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
  }
}
		
		
		
	

	


