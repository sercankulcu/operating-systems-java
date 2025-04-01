import java.util.Map.Entry;
import java.util.Set;

import org.jutils.jhardware.HardwareInfo;
import org.jutils.jhardware.model.*;

public class SystemInfoPureJava {

    public static void main(String[] args) {
        ProcessorInfo processorInfo = HardwareInfo.getProcessorInfo();
        System.out.println("--- Processor Info ---");
        System.out.println("Cache size: " + processorInfo.getCacheSize());
        System.out.println("Family: " + processorInfo.getFamily());
        System.out.println("Speed (Mhz): " + processorInfo.getMhz());

        Set<Entry<String, String>> processorFullInfos = processorInfo.getFullInfo().entrySet();
        for (final Entry<String, String> fullInfo : processorFullInfos) {
            System.out.println(fullInfo.getKey() + ": " + fullInfo.getValue());
        }

        MemoryInfo memoryInfo = HardwareInfo.getMemoryInfo();
        System.out.println("\n--- Memory Info ---");
        System.out.println("Total Memory: " + memoryInfo.getTotalMemory());
        System.out.println("Available Memory: " + memoryInfo.getAvailableMemory());

        Set<Entry<String, String>> memoryFullInfos = memoryInfo.getFullInfo().entrySet();
        for (final Entry<String, String> fullInfo : memoryFullInfos) {
            System.out.println(fullInfo.getKey() + ": " + fullInfo.getValue());
        }

        OSInfo osInfo = HardwareInfo.getOSInfo();
        System.out.println("\n--- OS Info ---");

        // get OS Family from full info map.
        String osFamily = osInfo.getFullInfo().get("OS Family");
        System.out.println("OS Family: " + osFamily);

        System.out.println("OS Version: " + osInfo.getVersion());
        System.out.println("OS Manufacturer: " + osInfo.getManufacturer());

        Set<Entry<String, String>> osFullInfos = osInfo.getFullInfo().entrySet();
        for (final Entry<String, String> fullInfo : osFullInfos) {
            System.out.println(fullInfo.getKey() + ": " + fullInfo.getValue());
        }
    }
}