package comunicacion;
import java.io.*;

public class ClasePrincipal {

	public static boolean isAlive(Process p) {
		// Si el proceso hijo ha terminado devuelve falso (exitValue() devuelve el valor 0, que indica terminación normal). Si no ha terminado lanza una excepción y devuelve true
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	public static void main(String[] args) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("java" ,  "-jar" , "C:\\Users\\cjjpa\\Dropbox\\2º DAM\\PSP 2021\\jarTest\\ClaseReserva.jar"); // Definimos el proceso a ejecutar con los argumentos indicados.
		Process process = builder.start();
		InputStream out = process.getInputStream(); // configuramos la salida del proceso hijo
		OutputStream in = process.getOutputStream(); // configuramos la entrada del proceso hijo

		byte[] buffer = new byte[4000]; // buffer de comunicación entre procesos
		while (isAlive(process)) {
			// se comprueba el stream de salida del proceso hijo
			int no = out.available();
			if (no > 0) {
				// si el stream de salida del proceso hijo tiene información se muestra por pantalla
				int n = out.read(buffer, 0, Math.min(no, buffer.length));
				System.out.println(new String(buffer, 0, n));
			}

			// se comprueba si hay información para enviar al proceso hijo
			int ni = System.in.available();
			if (ni > 0) {
				// si existe información se envía al proceso hijo
				int n = System.in.read(buffer, 0, Math.min(ni, buffer.length));
				in.write(buffer, 0, n);
				in.flush();
			}

			try {
				Thread.sleep(10); // se introduce un retardo de 10 milisegundos
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}

		System.out.println(process.exitValue());
	}

}
