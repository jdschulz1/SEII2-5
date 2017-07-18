package schoolUT;

import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;

public class Log {

	public Log(){};
	
	public void log(String message){
	
	
    String message1 = message +"\n";
    byte data[] = message1.getBytes();
    Path p = Paths.get("/Users/David.North/log/srlog.txt");

    try (OutputStream out = new BufferedOutputStream(
      Files.newOutputStream(p, CREATE, APPEND))) {
      out.write(data, 0, data.length);
      out.close();
    } catch (IOException x) {
      
    }

  }
}

