package serpis;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClient 
{


	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException 
	{
		pingpong();
	}
	
	private static void holaMundo() throws UnknownHostException, IOException
	{
		OutputStream FlujoDeSalida;
		DataOutputStream Flujo;	
		
		Socket SocketCliente = new Socket("localhost", 8000);
		
		FlujoDeSalida = SocketCliente.getOutputStream();
		Flujo = new DataOutputStream(FlujoDeSalida);
		Flujo.writeBytes("Hola Mundo");
		
		
		SocketCliente.close();	
	}
	
	private static void pingpong() throws IOException, InterruptedException
	{
		Socket s = new Socket("localhost", 10001);
		Scanner scanner = new Scanner(s.getInputStream());              // entrada del socket cliente
		DataOutputStream output = new DataOutputStream(s.getOutputStream()); // salida del socket cliente

		int i = 0;
		
		while(i++<10)//(true)
		{
			sendPing(s, output);
			receivePong(s, scanner);						
		}
		
		s.close();

	}
	
	
	private static void sendPing(Socket s, DataOutputStream output) throws IOException 
	{
		output.writeBytes("ping\n");
		output.flush();
		System.out.println("[client send]: ping");
	}
	
	private static void receivePong(Socket s, Scanner sc) throws InterruptedException
	{
		String line = sc.nextLine();//aqui lee el mensaje (pong)
		System.out.println("[client receive]: " + line);//aqui imprime el mensaje
		
		Thread.sleep(4000);// dormimos 4 seg.
	}

}
