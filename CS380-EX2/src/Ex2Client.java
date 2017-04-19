import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;

/*
 * @author cesar
 * 
 * This program creates a socket connection to codebank.xyz
 * It reads 100 bytes from the server, half a byte at a time.
 * The two pieces are then put back together, and then a CRC32 
 * error code for the bytes.
 */

public class Ex2Client {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try(Socket socket = new Socket("codebank.xyz", 38102)){
			System.out.println("Connected to server.");
			
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			int[] array = new int[100];
			byte[] byteArray = new byte[100];
			
			System.out.println("Received byte:")
			;
			for(int i = 0; i < array.length; i++){
				int byte1 = is.read();			//holds first half
				int byte2 = is.read();			//holds second half
				array[i] = (byte1<<4)|byte2;	//Concatenates halves by shifting
												//and using bitwise OR
				if(i%10==0)
					System.out.print("\t");

				System.out.printf("%02X", array[i]); //print Byte
				
				if((i+1)%10 == 0)
					System.out.println();
				byteArray[i] = (byte)array[i];
			}
			
			CRC32 crc = new CRC32();
			crc.update(byteArray);
			long errorCode = crc.getValue();	//generate CRC32 code
			System.out.printf("Generated CRC32: %08X.\n", errorCode);
			
			byte[] outArray = new byte[4];
			
			for(int i = outArray.length-1; i >= 0; i--){
				long ones = 255;
				outArray[i] = (byte)(errorCode & ones);
				errorCode>>=8;
			}
			
			os.write(outArray);
			
			if(is.read() == 1)
				System.out.println("Response good.");
			
		}
		System.out.println("Disconnected from server.");
	}

}
