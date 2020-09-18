/*
    Code developed for CMPSC 402 class 
    by Professor. Mohan using the Jsch Library.
    This class provides the methods such as running shell commands on remote machines, 
    copying data from one machine to another, etc.
*/
package cloud;
import java.io.*;
import java.util.*;
import com.jcraft.jsch.*;
public class Shell {
	public void download(String src, String dest,
			String host, String user, String pem, int port) throws SftpException {
	    // Create local folders if absent.
	    try {
	    	JSch jsch = new JSch();
			Session session = null;
			Channel channel = null;
			ChannelSftp channelSftp = null;
			jsch.addIdentity(pem);
			session = jsch.getSession(user, host, port);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPort(port);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.get(src,dest);	 
			channel.disconnect();
			session.disconnect();       
	    } catch (Exception e) {
	        System.out.println("Error at : " + e.toString());
	    }
	    
	}
	@SuppressWarnings("unchecked")
	private void recursiveFolderDelete(ChannelSftp channelSftp, String path) throws SftpException {
	    Collection<ChannelSftp.LsEntry> list = channelSftp.ls(path);
	    // Loop through the objects in the list to get the directory listing.
	    System.out.println("#deleting:" + path);
	    for (ChannelSftp.LsEntry item : list) {
	        if (!item.getAttrs().isDir()) {
	            channelSftp.rm(path + "/" + item.getFilename()); // Remove file.
	        } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) { // If it is a subdir.
	            try {
	                // removing sub directory.
	                channelSftp.rmdir(path + "/" + item.getFilename());
	            } catch (Exception e) { // If subdir is not empty and error occurs.
	                recursiveFolderDelete(channelSftp, path + "/" + item.getFilename());
	            }
	        }
	    }
	    channelSftp.rmdir(path); // delete the parent directory after empty
	}
	public void upload(String src, String dest,
			String host, String user, String pem, int port) {
		try {
			JSch jsch = new JSch();
			Session session = null;
			Channel channel = null;
			ChannelSftp channelSftp = null;
			jsch.addIdentity(pem);
			session = jsch.getSession(user, host, port);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPort(port);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(dest);
			File file = new File(src);
			if(file.isDirectory()){
				/* delete the directory if exists*/
				String currentDirectory=channelSftp.pwd();
				SftpATTRS attrs = null;
				try {
	  				attrs = channelSftp.stat(currentDirectory+"/" + file.getName());
				} catch (Exception e) {}
				if (attrs != null) {
					recursiveFolderDelete(channelSftp, file.getName());
				} 	
				channelSftp.mkdir(file.getName());
				System.out.println("#created folder: " + file.getName() + " in " + dest);
				dest = dest + "/" + file.getName();
				for(File item: file.listFiles()){
					upload(item.getAbsolutePath(), dest, host, user, pem, port);
				}
				channelSftp.cd(dest.substring(0, dest.lastIndexOf('/')));
				
			}
			else{
				channelSftp.put(new FileInputStream(file), file.getName());	
			}
			channel.disconnect();
			session.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public String execute(String host, String user, String pem, int port, 
			ArrayList<String> commands){
		String strLogMessages = "";
	    try {
			JSch jsch = new JSch();
	    	jsch.addIdentity(pem);
			Session session = jsch.getSession(user, host, port);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
		    Channel channel=session.openChannel("shell");//only shell
	        OutputStream inputstream_for_the_channel = channel.getOutputStream();
	        PrintStream shellStream = new PrintStream(inputstream_for_the_channel, true);
	        channel.connect(); 
	        for(String command: commands) {
	            shellStream.println(command); 
	            shellStream.flush();
	        }
	        shellStream.close();
	        InputStream outputstream_from_the_channel = channel.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(outputstream_from_the_channel));
	        String line;
	        while ((line = br.readLine()) != null){
	        		strLogMessages = strLogMessages + line+"\n";
	        }
	        do {
	        } while(!channel.isEOF());
	        outputstream_from_the_channel.close();
	        br.close();
	        session.disconnect();
	        channel.disconnect();
	        return strLogMessages;
	    } catch (Exception e) { 
	        e.printStackTrace();
	    }
	    return strLogMessages;
	}
	
}
