import java.io.ByteArrayInputStream; 
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO; 
import java.util.regex.Pattern;
import java.util.Arrays;
import java.io.File;
import java.io.*;
import java.awt.image.BufferedImage;


public class gps_req {
    public void demo(String gps) { 
        int k;
        Modem modem; modem=new Modem(); 
        modem.setSpeed(80000); 
        modem.setTimeout(2000); 
        modem.open("ithaki");
    
        ArrayList<Character> CharAL = new ArrayList<Character>();
    
        byte[] gps_req_bytes = gps.getBytes(); 
        modem.write(gps_req_bytes);
    
        for (;;) { 
            try {
                k = modem.read(); 
                CharAL.add((char)k);
                if (k == -1 ) break; 
                System.out.print((char)k);
            } catch (Exception x) { 
                break;
                }
        }

        char[] CharArray_of_input = new char[CharAL.size()]; 
        int temp = 0;
        for(char elem : CharAL ){
            CharArray_of_input[temp] = elem;
            temp++; 
        }

        String String_of_input = new String(CharArray_of_input);
        String GpsTracesStr = String_of_input.substring(String_of_input.indexOf('$'), String_of_input.length() - 27);
        String[] strRow = GpsTracesStr.split(Pattern.quote("$")); 
        String[][] trace_splitted = new String[4][15];

        int stopcounter = 0;
        for(int i = 1; i < strRow.length; i += 20) {
            if (stopcounter < 4) {
                trace_splitted[stopcounter] = strRow[i].split(Pattern.quote(",")); 
                stopcounter++;
            } else {
                break;
            }
        }

        String point1 = T_calc(trace_splitted[0]);
        String point2 = T_calc(trace_splitted[1]);
        String point3 = T_calc(trace_splitted[2]);
        String point4 = T_calc(trace_splitted[3]);
        String final_cordinates = "P2993" + point1 + point2 + point3 + point4 + "\r";

        byte[] finaltrace_Bytes = final_cordinates.getBytes(); 
        ArrayList<Byte> AL = new ArrayList<Byte>();

        modem.write(finaltrace_Bytes); 
        for (;;) {
            try {
                k = modem.read(); 
                AL.add((byte)k);
                if (k == -1 ) break;
            } catch (Exception x) { 
                break;
            }
        }

        int a = AL.size();
        byte[] foutput = new byte[a];
        for (int counter = 0; counter < AL.size(); counter++) { 
            foutput[counter] = AL.get(counter);
        }
        try {
            InputStream inS = new ByteArrayInputStream(foutput); 
            BufferedImage image = ImageIO.read(inS);
            String imageString = "/Users/Savvas/Desktop/computerNetworks/gpsImage" + '.' + "jpeg"; 
            File outputfile = new File(imageString); 
            ImageIO.write(image, "jpg", outputfile); 
        } catch (Exception e) {
            System.out.println("error"); 
        }

        modem.close(); 
    }

    public String T_calc(String[] gpsTrace){
        String[] lat_array = gpsTrace[2].split(Pattern.quote(".")); 
        String[] long_array = gpsTrace[4].split(Pattern.quote("."));
        int latfh = Integer.parseInt(lat_array[0]);
        double latsh_temp = Integer.parseInt(lat_array[1]) * 0.006;

        int latsh = (int) latsh_temp;
        int longfh = Integer.parseInt(long_array[0].substring(1)); 
        double longsh_temp = Integer.parseInt(long_array[1]) * 0.006; 
        int longsh = (int) longsh_temp;

        return "T=" + String.valueOf(longfh) + String.valueOf(longsh) + String.valueOf(latfh) + String.valueOf(latsh);
    } 
}