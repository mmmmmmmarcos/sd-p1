import java.io.*;
import java.net.*;

public class Cliente {

	public String leeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			InputStream aux = p_sk.getInputStream();
			DataInputStream flujo = new DataInputStream( aux );
			p_Datos = flujo.readUTF();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
      return p_Datos;
	}

	public void escribeSocket (Socket p_sk, String p_Datos)
	{
		try
		{
			OutputStream aux = p_sk.getOutputStream();
			DataOutputStream flujo= new DataOutputStream( aux );
			flujo.writeUTF(p_Datos);      
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
		return;
	}
	
	public void jugarNumero(Socket skCliente)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			System.out.println("--- JUEGO: Adivina el Número (1-100) ---");
			String respuesta = "";
			boolean ganado = false;
			
			while (!ganado)
			{
				System.out.println("Introduce tu número (o escribe 'fin' para salir):");
				String entrada = br.readLine();
				
				if (entrada.equalsIgnoreCase("fin"))
				{
					escribeSocket(skCliente, "fin");
					break;
				}
				
				escribeSocket(skCliente, entrada);
				respuesta = leeSocket(skCliente, respuesta);
				
				if (respuesta.equals("BAJO"))
				{
					System.out.println("--> El número secreto es MAYOR (Demasiado bajo).");
				}
				else if (respuesta.equals("ALTO"))
				{
					System.out.println("--> El número secreto es MENOR (Demasiado alto).");
				}
				else if (respuesta.equals("ACERTADO"))
				{
					System.out.println("¡¡¡FELICIDADES! Has adivinado el número secreto.");
					ganado = true;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
	}

	public void jugarLetra(Socket skCliente)
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			System.out.println("--- JUEGO: Adivina la Letra Secreta (A-Z) ---");
			String respuesta = "";
			boolean ganado = false;
			
			while (!ganado)
			{
				System.out.println("Introduce una letra [A-Z] (o escribe 'fin' para salir):");
				String entrada = br.readLine();
				
				if (entrada.equalsIgnoreCase("fin"))
				{
					escribeSocket(skCliente, "fin");
					break;
				}
				
				if (entrada.length() > 0)
				{
					escribeSocket(skCliente, entrada.substring(0, 1).toUpperCase());
				}
				else
				{
					continue;
				}
				
				respuesta = leeSocket(skCliente, respuesta);
				
				if (respuesta.equals("DESPUES"))
				{
					System.out.println("--> La letra secreta está MÁS ADELANTE en el abecedario (es mayor).");
				}
				else if (respuesta.equals("ANTES"))
				{
					System.out.println("--> La letra secreta está MÁS ATRÁS en el abecedario (es menor).");
				}
				else if (respuesta.equals("ACERTADO"))
				{
					System.out.println("¡¡¡FELICIDADES! Has adivinado la letra secreta.");
					ganado = true;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
	}
	
	public void menu(String p_host, String p_puerto)
	{
		int opc = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try
		{
			while (opc != 1 && opc != 2 && opc != 3)
			{
				System.out.println("\n=== MENÚ PRINCIPAL DE JUEGOS ===");
				System.out.println("[1] Adivinar el Número (1-100)");
				System.out.println("[2] Adivinar la Letra Secreta (A-Z)");
				System.out.println("[3] Salir");
				System.out.println("Indique la opcion a realizar:");
				opc = Integer.parseInt(br.readLine());
			}

			if (opc == 3)
			{
				System.exit(0);
			}

			// Abrimos conexión con el servidor
			Socket skCliente = new Socket(p_host, Integer.parseInt(p_puerto));

			if (opc == 1)
			{
				escribeSocket(skCliente, "NUMERO");
				jugarNumero(skCliente);
			}
			else if (opc == 2)
			{
				escribeSocket(skCliente, "LETRA");
				jugarLetra(skCliente);
			}

			skCliente.close();
			System.out.println("Conexión cerrada con el servidor.");
		} 
		catch(Exception e)
		{
			System.out.println("Error " + e.toString());
		}
	}
	
	public static void main(String[] args) {
		Cliente cl = new Cliente();
		if (args.length < 2) {
			System.out.println ("Debe indicar la direccion del servidor y el puerto");
			System.out.println ("$./Cliente nombre_servidor puerto_servidor");
			System.exit(-1);
		}
		String host = args[0];
		String puerto = args[1];

		while(true)
		{
			cl.menu(host, puerto);
		}
	}
}