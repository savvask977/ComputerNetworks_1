
public class userapp{
    public static void main (String[] args){

    final String echo_req = "E2195 \r";
    final String Img_req = "M5519\r";
    final String Img_req2 = "G7084\r";
    
    final String gps_Req = "P0669R=1088085\r"; 
    final String ackreq = "Q2540\r";
    final String nackreq = "R5172\r";
    
    echo echo_Request = new echo(); 
    echo_Request.demo(echo_req);
    
    image image_Request = new image(); 
    image_Request.demo(Img_req);
    image image_Request2 = new image(); 
    image_Request.demo(Img_req2);
    
    gps_req gps_Request = new gps_req(); 
    gps_Request.demo(gps_Req);
    
    arq arq_Request = new arq(); 
    arq_Request.demo(ackreq,nackreq); }
}