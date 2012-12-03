package serpis;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPServer 
{

	public static void main(String[] args) throws IOException, InterruptedException 
	{
		//holaMundo();
		pingPong();

	}
	
	private static void pingPong() throws IOException, InterruptedException
	{
		
		//DatagramSocket datagramSocket = new DatagramSocket(port);		
		
		int port = 10001;		
		ServerSocket serverSocket = new ServerSocket(port);
		
		System.out.println("Servidor a la espera de conexión del cliente...");
		
		// solo aceptamos una peticion
		Socket socket = serverSocket.accept();//escucha peticiones entrantes
		Scanner scanner = new Scanner(socket.getInputStream());//aqui tiene el mensaje de client
		
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		
		int i = 0;
		
		while(i++<10)//(true)
		{
			receivePing(socket, scanner);			
			sendPong(socket, output);			
		}
		
		socket.close();
		serverSocket.close();
	}
	
	
	private static void sendPong(Socket s, DataOutputStream output) throws IOException 
	{
		output.writeBytes("pong\n");
		output.flush();
		System.out.println("[server send]: pong");
	}
	
	private static void receivePing(Socket s, Scanner sc) throws InterruptedException
	{
		String line = sc.nextLine();//aqui lee el mensaje (ping)
		System.out.println("[server receive]: " + line);//aqui imprime el mensaje
		
		Thread.sleep(3000);// dormimos 3 seg.
	}
	
	
	
	
	private static void holaMundo() throws IOException
	{
		int port = 10001;
		
		ServerSocket serverSocket = new ServerSocket(port);
		
		Socket socket = serverSocket.accept();//escucha peticiones entrantes
		
		Scanner scanner = new Scanner(socket.getInputStream());//aqui tiene el mensaje de client
		
		String line = scanner.nextLine();//aqui lee el mensaje
		System.out.println("Line = " + line);//aqui imprime el mensaje
		
		socket.close();
		serverSocket.close();
		
	}


}


