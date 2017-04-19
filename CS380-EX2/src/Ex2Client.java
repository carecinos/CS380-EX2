import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.CRC32;


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
				int byte1 = is.read();
				int byte2 = is.read();
				array[i] = (byte1<<4)|byte2;
				if(i%10==0)
					System.out.print("\t");


				System.out.printf("%02X", array[i]);
				if((i+1)%10==0)
					System.out.println();
				byteArray[i] = (byte)array[i];
			}
			CRC32 crc = new CRC32();
			crc.update(byteArray);
			long errorCode = crc.getValue();
			
			byte[] outArray = new byte[4];
			for(int i = outArray.length-1; i >= 0; i--){
				long ones = 255;
				outArray[i] = (byte)(errorCode & ones);
				errorCode>>=8;
			}
			os.write(outArray);
			if(is.read()==1)
				System.out.println("Response good.");
		}

	}

}
