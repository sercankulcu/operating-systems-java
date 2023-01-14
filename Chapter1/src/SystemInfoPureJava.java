

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
