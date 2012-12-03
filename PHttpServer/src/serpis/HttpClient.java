package serpis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient 
{
	public static void main(String [] args) throws IOException
	{
        URL yahoo = new URL("http://www.yahoo.com:8080/");
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
        					new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            System.out.println(inputLine);
        in.close();
	}

}
