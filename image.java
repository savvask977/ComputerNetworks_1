import java.io.*;
import java.lang.System.*;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream; 
import java.io.ByteArrayInputStream; 
import java.awt.image.BufferedImage; 
import java.io.File;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream; 
import java.util.Arrays;



public class image {
    private static final char[] HexArray = "0123456789ABCDEF".toCharArray();

    public void demo(String imgreq) { 
        int k;
        Modem modem; 
        modem=new Modem(); 
        modem.setSpeed(80000); 
        modem.setTimeout(2000); 
        modem.open("ithaki");
        
        ArrayList<Byte> AL1 = new ArrayList<Byte>(); 
        byte[] imgReq_bytes = imgreq.getBytes(); 
        modem.write(imgReq_bytes);


        for (;;) { 
            try {
                k = modem.read(); 
                AL1.add((byte)k); 
                if (k == -1 ) break;
            } catch (Exception x) { 
                break;
                }
        }
        int s = AL1.size();
        byte[] output = new byte[s];
        for (int counter = 0; counter < AL1.size(); counter++) { 
            output[counter] = AL1.get(counter);
        }


        String TotalStr = Conv_bytes_toHEX(output); 
        String delimiter1 = "FFD8";
        String delimiter2 = "FFD9";

        int start_of_string = TotalStr.indexOf(delimiter1) / 2;
        int end_of_string = (TotalStr.indexOf(delimiter2) + 4) / 2;
        byte [] final_output = new byte [end_of_string - start_of_string]; 
        int temp= 0 ;


        for (int i = start_of_string; i < end_of_string; i++){ 
            final_output[temp] = output[i];
            temp++ ;
        }
        String final_value = Conv_bytes_toHEX(final_output); 
        System.out.println(final_value);

        try {
            InputStream inpS = new ByteArrayInputStream(final_output); 
            BufferedImage image = ImageIO.read(inpS);
            String imageString = "/Users/Savvas/Desktop/computerNetworks/error_image" + '.' + "jpeg"; 
            File outputfile = new File(imageString); 
            ImageIO.write(image, "jpg", outputfile);           
        } catch (Exception e) { 
            System.out.println("error");
            } 
            
        modem.close();
    }

    public static String Conv_bytes_toHEX(byte[] bytes) { 
        char[] chars_in_hex = new char[bytes.length * 2]; 
        for (int k = 0; k < bytes.length; k++) {
            int r = bytes[k] & 0xFF;
            chars_in_hex[k * 2] = HexArray[r >>> 4]; 
            chars_in_hex[k * 2 + 1] = HexArray[r & 0x0F];
        }
        return new String(chars_in_hex); 
    }
}