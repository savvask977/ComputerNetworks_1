import java.util.ArrayList;
import java.util.concurrent.TimeUnit; 
import java.util.stream.Collectors; 
import java.util.*;


public class echo {

    public void demo(String req) { 
        int k;
        Modem modem; 
        modem=new Modem(); 
        modem.setSpeed(80000); 
        modem.setTimeout(2000); 
        modem.open("ithaki");

            byte[] ech_info = req.getBytes();
            ArrayList<Long> PacketTimes = new ArrayList<Long>(); 
            long startTime, elapsed;
            long first = System.currentTimeMillis();
        while ((System.currentTimeMillis()- first)< 4*60*1000){ 
            startTime = System.currentTimeMillis();
            modem.write(ech_info); 
            for (;;) {
                try { 
                    k=modem.read(); 
                    if ((char)k == 'O') {
                        System.out.print((char)k);
                        k=modem.read();
                        System.out.print((char)k);
                        elapsed = System.currentTimeMillis() - startTime; 
                        PacketTimes.add(elapsed);
                        break; 
                    }
                    if (k==-1) break; 
                        System.out.print((char)k);
                    } catch (Exception x) { 
                        break;
                        }
            }
            System.out.println('\n');
        }
        int total = 0;
        for (long elem : PacketTimes) {
            System.out.println(elem + ' ');
            total += elem; 
        }
        System.out.println('\n');
        System.out.println("average time: " + total/PacketTimes.size()); 
        modem.close();
    } 
}