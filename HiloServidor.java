import java.lang.Exception;
import java.net.Socket;
import java.io.*;
import java.util.Random;

public class HiloServidor extends Thread {

	private Socket skCliente;
	
	public HiloServidor(Socket p_cliente)
	{
		this.skCliente = p_cliente;
	}
	
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
	
    public void run() {
		try {
			// 1. Leemos qué tipo de juego ha elegido el cliente
			String tipoJuego = "";
			tipoJuego = this.leeSocket(skCliente, tipoJuego);
			
			System.out.println("SRV: El cliente ha seleccionado el modo: " + tipoJuego);
			
			Random rand = new Random();
			String cadena = "";
			boolean acertado = false;
			
			if (tipoJuego.equalsIgnoreCase("NUMERO"))
			{
				int numeroSecreto = rand.nextInt(100) + 1; // 1 al 100
				System.out.println("SRV: Número secreto generado: " + numeroSecreto);
				
				while (!acertado)
				{
					cadena = this.leeSocket(skCliente, cadena);
					if (cadena.equalsIgnoreCase("fin")) break;
					
					int intento = Integer.parseInt(cadena);
					System.out.println("SRV [Número]: Intento del cliente -> " + intento);
					
					if (intento < numeroSecreto)
					{
						this.escribeSocket(skCliente, "BAJO");
					}
					else if (intento > numeroSecreto)
					{
						this.escribeSocket(skCliente, "ALTO");
					}
					else
					{
						this.escribeSocket(skCliente, "ACERTADO");
						acertado = true;
					}
				}
			}
			else if (tipoJuego.equalsIgnoreCase("LETRA"))
			{
				char letraSecreta = (char) ('A' + rand.nextInt(26)); // A a Z
				System.out.println("SRV: Letra secreta generada: " + letraSecreta);
				
				while (!acertado)
				{
					cadena = this.leeSocket(skCliente, cadena);
					if (cadena.equalsIgnoreCase("fin")) break;
					
					char intento = cadena.charAt(0);
					System.out.println("SRV [Letra]: Intento del cliente -> " + intento);
					
					if (intento < letraSecreta)
					{
						this.escribeSocket(skCliente, "DESPUES");
					}
					else if (intento > letraSecreta)
					{
						this.escribeSocket(skCliente, "ANTES");
					}
					else
					{
						this.escribeSocket(skCliente, "ACERTADO");
						acertado = true;
					}
				}
			}
			
			skCliente.close();
			System.out.println("SRV: Conexión cerrada con el cliente.");
        }
        catch (Exception e) {
          System.out.println("Error: " + e.toString());
        }
      }
}