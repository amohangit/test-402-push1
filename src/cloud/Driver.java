/*
    Code developed for CMPSC 402 class 
    by Professor. Mohan using the Jsch Library.
    This class provides the methods such as running shell commands on remote machines, 
    copying data from one machine to another, etc.
*/
package cloud;
import java.util.ArrayList;
import org.w3c.dom.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
public class Driver{
    public static void main(String[] args) throws Exception, InterruptedException{
        Shell sh = new Shell();
        String path = System.getProperty("user.dir");
        if (path.indexOf("\\") >= 0){
            path = path.substring(0, path.lastIndexOf("\\") + 1);
            path = path + "data\\config.xml";
        }
        else if (path.indexOf("/") >= 0){
            path = path.substring(0, path.lastIndexOf("/") + 1);
            path = path + "data/config.xml";
        }
        Document config = Utility.readFileAsDocument(path);
        String host_b = ((Element) config.getDocumentElement().getElementsByTagName("host_b").item(0)).getTextContent().trim();
        String host_c = ((Element) config.getDocumentElement().getElementsByTagName("host_c").item(0)).getTextContent().trim();
        String host_d = ((Element) config.getDocumentElement().getElementsByTagName("host_d").item(0)).getTextContent().trim();
        String upload_b_src = ((Element) config.getDocumentElement().getElementsByTagName("upload_b_src").item(0)).getTextContent().trim();
        String upload_c_src = ((Element) config.getDocumentElement().getElementsByTagName("upload_c_src").item(0)).getTextContent().trim();
        String upload_d_src = ((Element) config.getDocumentElement().getElementsByTagName("upload_d_src").item(0)).getTextContent().trim();
        String upload_b_dest = ((Element) config.getDocumentElement().getElementsByTagName("upload_b_dest").item(0)).getTextContent().trim();
        String upload_c_dest = ((Element) config.getDocumentElement().getElementsByTagName("upload_c_dest").item(0)).getTextContent().trim();
        String upload_d_dest = ((Element) config.getDocumentElement().getElementsByTagName("upload_d_dest").item(0)).getTextContent().trim();
        String download_b_src = ((Element) config.getDocumentElement().getElementsByTagName("download_b_src").item(0)).getTextContent().trim();
        String download_c_src = ((Element) config.getDocumentElement().getElementsByTagName("download_c_src").item(0)).getTextContent().trim();
        String download_d_src = ((Element) config.getDocumentElement().getElementsByTagName("download_d_src").item(0)).getTextContent().trim();
        String download_dest = ((Element) config.getDocumentElement().getElementsByTagName("download_dest").item(0)).getTextContent().trim();
        String user = ((Element) config.getDocumentElement().getElementsByTagName("user").item(0)).getTextContent().trim();
        String pem_b = ((Element) config.getDocumentElement().getElementsByTagName("pem_b").item(0)).getTextContent().trim();
        String pem_c = ((Element) config.getDocumentElement().getElementsByTagName("pem_c").item(0)).getTextContent().trim();
        int port = Integer.parseInt(((Element) config.getDocumentElement().getElementsByTagName("port").item(0)).getTextContent().trim());
        

        sh.upload(upload_b_src, upload_b_dest, host_b, user, pem_b, port);
        System.out.println("\t#upload completed to b machine successfully");
        sh.upload(pem_c, upload_b_dest, host_b, user, pem_b, port);
        System.out.println("\t#upload c's pem completed to b machine successfully");
        sh.upload(pem_c, upload_c_dest, host_c, user, pem_c, port);
        System.out.println("\t#upload c's pem completed to c machine successfully");
        sh.upload(upload_c_src, upload_c_dest, host_c, user, pem_c, port);
        System.out.println("\t#upload completed to c machine successfully");
        sh.upload(upload_d_src, upload_d_dest, host_d, user, pem_c, port);
        System.out.println("\t#upload completed to d machine successfully");

        
        ArrayList<String> cmds1 = new ArrayList<String>();
        cmds1.add("cd code_b");
        cmds1.add("chmod +x compile.sh");
        cmds1.add("./compile.sh");
        cmds1.add("chmod +x run.sh");
        cmds1.add("./run.sh");
        cmds1.add("scp -i ../" 
            + pem_c.substring(pem_c.lastIndexOf("/") + 1, pem_c.length())
            + " " + download_b_src 
            + " ubuntu@" + host_c 
            + ":" + download_c_src.substring(0, download_c_src.lastIndexOf("/")));
        cmds1.add("exit");
        String logs1 = sh.execute(host_b, user, pem_b, port, cmds1);
        System.out.println(logs1);
        System.out.println("\t#commands executed in b machine successfully");
        
        ArrayList<String> cmds2 = new ArrayList<String>();
        cmds2.add("chmod 600 " 
            + pem_c.substring(pem_c.lastIndexOf("/") + 1, pem_c.length()));
        cmds2.add("cd code_c");
        cmds2.add("chmod +x compile.sh");
        cmds2.add("./compile.sh");
        cmds2.add("chmod +x run.sh");
        cmds2.add("./run.sh");
        cmds2.add("scp -i ../" 
            + pem_c.substring(pem_c.lastIndexOf("/") + 1, pem_c.length())
            + " " + download_c_src 
            + " ubuntu@" + host_d 
            + ":" + download_d_src.substring(0, download_d_src.lastIndexOf("/")));
        cmds2.add("exit");
        String logs2 = sh.execute(host_c, user, pem_c, port, cmds2);
        System.out.println(logs2);
        System.out.println("\t#commands executed in c machine successfully");
        
        ArrayList<String> cmds3 = new ArrayList<String>();
        cmds3.add("cd code_d");
        cmds3.add("chmod 600 " 
            + pem_c.substring(pem_c.lastIndexOf("/") + 1, pem_c.length()));
        cmds3.add("chmod +x compile.sh");
        cmds3.add("./compile.sh");
        cmds3.add("chmod +x run.sh");
        cmds3.add("./run.sh");
        cmds3.add("exit");
        String logs3 = sh.execute(host_d, user, pem_c, port, cmds3);
        System.out.println(logs3);
        System.out.println("\t#commands executed in d machine successfully");
                

        sh.download(download_b_src, download_dest, host_b, user, pem_b, port);
        System.out.println("\t#download from b machine completed successfully");
        sh.download(download_c_src, download_dest, host_c, user, pem_c, port);
        System.out.println("\t#download from c machine completed successfully");
        sh.download(download_d_src, download_dest, host_d, user, pem_c, port);
        System.out.println("\t#download from d machine completed successfully");
        
    }
}    
    


