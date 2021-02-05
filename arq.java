import java.util.ArrayList;
import java.io.FileWriter;
import java.util.stream.Collectors; 
import java.util.concurrent.TimeUnit; 
import java.util.*;
import java.io.IOException;

public class arq {
    public void demo(String ack,String nack) { 
        int k;
        Modem modem;
        modem = new Modem(); 
        modem.setSpeed(80000);
        modem.setTimeout(2000); 
        modem.open("ithaki");
        
        int rp=0;
        int[] RepeatNacksNumber = new int[20];
        ArrayList<Character> CharArray = new ArrayList<Character>();

        byte[] ackbytes = ack.getBytes(); 
        byte[] nackbytes = nack.getBytes();

        ArrayList<Long> PacketTimes = new ArrayList<Long>(); 
        long first = System.currentTimeMillis();
        long startTime;
        long elapsed;
        int countN = 0;
        int countA = 0;
        int total = 0; modem.write(ackbytes);


        while ((System.currentTimeMillis()- first)< 4*60*1000) {
            startTime = System.currentTimeMillis();
            for (;;) { 
                try {
                    k = modem.read();
                    if ( k == -1 ) break; 
                    System.out.print( (char)k ); 
                    CharArray.add((char)k);
                    if((char)k == 'T') {
                        k = modem.read(); 
                        System.out.print((char)k); 
                        CharArray.add((char)k);
                        if ((char)k == 'O') {
                            k = modem.read(); 
                            System.out.print((char)k); 
                            CharArray.add((char)k); 
                            if((char)k == 'P') {
                                elapsed = System.currentTimeMillis() - startTime;
                                break; 
                            }
                        }
                    }
                } catch (Exception e) {
                    break; 
                }
            }

            System.out.println('\n' + '\n');
            int FCS = calcFCS(CharArray);
            char[] Data_is_xor = calc_xorChars(CharArray); 
            int result = xor(Data_is_xor);
            CharArray.clear();
            total++;

            if( XorIsOk(FCS, result) ) {
                elapsed = System.currentTimeMillis() - startTime; 
                PacketTimes.add(elapsed); 
                modem.write(ackbytes);
                countA++;
                if(rp != 0){ 
                    RepeatNacksNumber[rp]++; rp=0;
                }
            } else {
                countN++;
                rp++; modem.write(nackbytes);
            }
        }
        System.out.println("ACK: "); 
        System.out.println(countA); 
        System.out.println("NACK: "); 
        System.out.println(countN); 
        System.out.println("ALL: "); 
        System.out.println(total); 
        System.out.println('\n'); 
        
        modem.close();
        for(int i=1;i<5;i++){
            System.out.println(RepeatNacksNumber[i]); 
        }
        
        int all = 0;
        for (long elem : PacketTimes) {
            System.out.println(elem + ' ');
            all += elem; 
        }

        System.out.print("average time: " + total/PacketTimes.size()); 
    }
    
    public static int xor(char[] sequence) {
        int xorOut = sequence[0]^sequence[1]; 
        for(int i = 2; i < sequence.length; i++) {
            xorOut = xorOut^sequence[i]; 
        }
        return xorOut;
    }


    public static int calcFCS(ArrayList<Character> CharArray) {
        String CharArraySTR = CharArray.stream().map(e- >e.toString()).reduce((acc, e) -> acc + e).get();
        String FCS_str = CharArraySTR.substring(CharArraySTR.length() - 9, CharArraySTR.length() - 6);
        char[] FCS_to_char = FCS_str.toCharArray();
        int FCS = (FCS_to_char[0] - '0')*100 + (FCS_to_char[1] - '0')*10 + (FCS_to_char[2] - '0');
        return FCS; 
    }

    public static char[] calc_xorChars(ArrayList<Character> CharArray) {
        String CharArraySTR = CharArray.stream().map(e- >e.toString()).reduce((acc, e) -> acc + e).get();
        String sentence = CharArraySTR.substring(CharArraySTR.length() - 27, CharArraySTR.length() - 11);
        char[] sequence2 = sentence.toCharArray();
        return sequence2; 
    }

    public static boolean XorIsOk(int FCS, int xor) { 
        if (FCS == xor) {
            return true; 
        } else {
            return false; 
        }
    } 
}