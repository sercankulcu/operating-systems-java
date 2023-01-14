/*
 * Here is an example of how you can use the jHardware library to print information about the 
 * system's hardware components:
 * 
 * This will print the information about the system's hardware components like CPU, Memory, OS, 
 * Disk, Network, etc. It's worth noting that this library is not part of the standard JDK, so 
 * you'll need to download and include it in your project.
 * */

import java.util.Map.Entry;
import java.util.Set;

import org.jutils.jhardware.HardwareInfo;
import org.jutils.jhardware.model.ProcessorInfo;

public class SystemInfoPureJava {
	
	public static void main(String[] args)
	{
		ProcessorInfo info = HardwareInfo.getProcessorInfo();
    //Get named info
    System.out.println("Cache size: " + info.getCacheSize());        
    System.out.println("Family: " + info.getFamily());
    System.out.println("Speed (Mhz): " + info.getMhz());
    //[...]

    //Get all info from map
    Set<Entry<String, String>> fullInfos = info.getFullInfo().entrySet();
        
    for (final Entry<String, String> fullInfo : fullInfos) {
        System.out.println(fullInfo.getKey() + ": " + fullInfo.getValue());
    }
	}

}
