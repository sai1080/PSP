package serpis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
 
public class HttpClient implements Runnable // Runnable...
{
        private Socket client = null; // Socket de comunicación entre cliente y servidor
        private Thread running = null; // Thread...
       
        private BufferedReader input = null; // Datos de entrada
        private PrintWriter output = null; // Datos de salida
 
        public HttpClient(Socket client) // Cuando se crea el cliente, se define el socket que se utilizara
        {
                this.client = client; // Definiendo el cliente local
                running = new Thread(this); // Creando Thread
                running.start(); // Iniciano Thread
        }
       
        public void run() // run()...
        {
                while(!client.isClosed()) // Mientras que el cliente esté conectado...
                {
                        try
                        {
                                input = new BufferedReader(new InputStreamReader(client.getInputStream())); // Creando estructura para los datos de entrada
                                output = new PrintWriter(client.getOutputStream(), true); // Creando estructura para los datos de salida
                               
                                inputMgr(input.readLine()); // Llamando función que maneja los paquetes recividos
                        }
                        catch(Exception e)
                        {
                                crash();
                        }
                }
               
                crash();
        }
       
        public void inputMgr(String packet) // Manejando paquetes...
        {
                if(packet.equals("ENTER")) // Si el paquete es igual a ...
                        outputMgr("Bienvenido!"); // Enviar paquete
        }
       
        public void outputMgr(String packet) // Enviando paquetes...
        {
                try
                {
                        output.write(packet); // Escribiendo el paquete en la comunicación
                        output.flush(); // Enviando el paquete completo... el "output" tiene método "add"
                }
                catch(Exception e)
                {
                        crash(); // Cerrar la conexión en caso de error
                }
        }
       
        public void crash()
        {
                try
                {
                        client.close(); // Cerrando comunicación entre cliente y servidor
                        input.close(); // Cerrando la comunicación de datos de entrada
                        output.close(); // Cerrando la comunicación de datos de salida
                }
                catch(Exception e)
                {
               
                }
                finally
                { // Destruyendo todas las instancias
                        client = null;
                        input = null;
                        output = null;
                }
               
                running.interrupt(); // Interrumpiendo Thread
                running = null; // Destruyendo Thread
        }      
}