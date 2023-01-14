/*
 * Copyright 2016-2022 The OSHI Project Contributors
 * SPDX-License-Identifier: MIT
 */
package oshi;

/*
 * Here is an example of how you can use the oshi library to print information about the 
 * system's hardware components:
 * 
 * This will print the information about the system's hardware components like CPU, Memory, OS, 
 * Disk, Network, etc. It's worth noting that this library is not part of the standard JDK, so 
 * you'll need to download and include it in your project.
 * */

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.PhysicalProcessor;
import oshi.hardware.CentralProcessor.ProcessorCache;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.LogicalVolumeGroup;
import oshi.hardware.NetworkIF;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.SoundCard;
import oshi.hardware.UsbDevice;
import oshi.hardware.VirtualMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.InternetProtocolStats;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OSService;
import oshi.software.os.OSSession;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessFiltering;
import oshi.software.os.OperatingSystem.ProcessSorting;
import oshi.util.FormatUtil;
import oshi.util.Util;

/**
 * A demonstration of access to many of OSHI's capabilities
 */
public class SystemInfoJNA { // NOSONAR squid:S5786

    static List<String> oshi = new ArrayList<>();

    /**
     * The main method, demonstrating use of classes.
     *
     * @param args the arguments (unused)
     */
    public static void main(String[] args) {

        System.out.println("Initializing System...");
        SystemInfo si = new SystemInfo();

        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        printOperatingSystem(os);

        System.out.println("Checking computer system...");
        printComputerSystem(hal.getComputerSystem());

        System.out.println("Checking Processor...");
        printProcessor(hal.getProcessor());

        System.out.println("Checking Memory...");
        printMemory(hal.getMemory());

        System.out.println("Checking CPU...");
        printCpu(hal.getProcessor());

        System.out.println("Checking Processes...");
        printProcesses(os, hal.getMemory());

        System.out.println("Checking Services...");
        printServices(os);

        System.out.println("Checking Sensors...");
        printSensors(hal.getSensors());

        System.out.println("Checking Power sources...");
        printPowerSources(hal.getPowerSources());

        System.out.println("Checking Disks...");
        printDisks(hal.getDiskStores());

        System.out.println("Checking Logical Volume Groups ...");
        printLVgroups(hal.getLogicalVolumeGroups());

        System.out.println("Checking File System...");
        printFileSystem(os.getFileSystem());

        System.out.println("Checking Network interfaces...");
        printNetworkInterfaces(hal.getNetworkIFs());

        System.out.println("Checking Network parameters...");
        printNetworkParameters(os.getNetworkParams());

        System.out.println("Checking IP statistics...");
        printInternetProtocolStats(os.getInternetProtocolStats());

        System.out.println("Checking Displays...");
        printDisplays(hal.getDisplays());

        System.out.println("Checking USB Devices...");
        printUsbDevices(hal.getUsbDevices(true));

        System.out.println("Checking Sound Cards...");
        printSoundCards(hal.getSoundCards());

        System.out.println("Checking Graphics Cards...");
        printGraphicsCards(hal.getGraphicsCards());

        StringBuilder output = new StringBuilder();
        for (String line : oshi) {
            output.append(line);
            if (line != null && !line.endsWith("\n")) {
                output.append('\n');
            }
        }
        System.out.println("Printing Operating System and Hardware Info:{}{}" + output.toString());
    }

    private static void printOperatingSystem(final OperatingSystem os) {
        oshi.add(String.valueOf(os));
        oshi.add("Booted: " + Instant.ofEpochSecond(os.getSystemBootTime()));
        oshi.add("Uptime: " + FormatUtil.formatElapsedSecs(os.getSystemUptime()));
        oshi.add("Running with" + (os.isElevated() ? "" : "out") + " elevated permissions.");
        oshi.add("Sessions:");
        for (OSSession s : os.getSessions()) {
            oshi.add(" " + s.toString());
        }
    }

    private static void printComputerSystem(final ComputerSystem computerSystem) {
        oshi.add("System: " + computerSystem.toString());
        oshi.add(" Firmware: " + computerSystem.getFirmware().toString());
        oshi.add(" Baseboard: " + computerSystem.getBaseboard().toString());
    }

    private static void printProcessor(CentralProcessor processor) {
        oshi.add(processor.toString());

        Map<Integer, Integer> efficiencyCount = new HashMap<>();
        int maxEfficiency = 0;
        for (PhysicalProcessor cpu : processor.getPhysicalProcessors()) {
            int eff = cpu.getEfficiency();
            efficiencyCount.merge(eff, 1, Integer::sum);
            if (eff > maxEfficiency) {
                maxEfficiency = eff;
            }
        }
        oshi.add(" Topology:");
        oshi.add(String.format("  %7s %4s %4s %4s %4s %4s", "LogProc", "P/E", "Proc", "Pkg", "NUMA", "PGrp"));
        for (PhysicalProcessor cpu : processor.getPhysicalProcessors()) {
            oshi.add(String.format("  %7s %4s %4d %4s %4d %4d",
                    processor.getLogicalProcessors().stream()
                            .filter(p -> p.getPhysicalProcessorNumber() == cpu.getPhysicalProcessorNumber())
                            .filter(p -> p.getPhysicalPackageNumber() == cpu.getPhysicalPackageNumber())
                            .map(p -> Integer.toString(p.getProcessorNumber())).collect(Collectors.joining(",")),
                    cpu.getEfficiency() == maxEfficiency ? "P" : "E", cpu.getPhysicalProcessorNumber(),
                    cpu.getPhysicalPackageNumber(),
                    processor.getLogicalProcessors().stream()
                            .filter(p -> p.getPhysicalProcessorNumber() == cpu.getPhysicalProcessorNumber())
                            .filter(p -> p.getPhysicalPackageNumber() == cpu.getPhysicalPackageNumber())
                            .mapToInt(p -> p.getNumaNode()).findFirst().orElse(0),
                    processor.getLogicalProcessors().stream()
                            .filter(p -> p.getPhysicalProcessorNumber() == cpu.getPhysicalProcessorNumber())
                            .filter(p -> p.getPhysicalPackageNumber() == cpu.getPhysicalPackageNumber())
                            .mapToInt(p -> p.getProcessorGroup()).findFirst().orElse(0)));
        }
        List<ProcessorCache> caches = processor.getProcessorCaches();
        if (!caches.isEmpty()) {
            oshi.add(" Caches:");
        }
        for (int i = 0; i < caches.size(); i++) {
            ProcessorCache cache = caches.get(i);
            boolean perCore = cache.getLevel() < 3;
            boolean pCore = perCore && i < caches.size() - 1 && cache.getLevel() == caches.get(i + 1).getLevel()
                    && cache.getType() == caches.get(i + 1).getType();
            boolean eCore = perCore && i > 0 && cache.getLevel() == caches.get(i - 1).getLevel()
                    && cache.getType() == caches.get(i - 1).getType();
            StringBuilder sb = new StringBuilder("  ").append(cache);
            if (perCore) {
                sb.append(" (per ");
                if (pCore) {
                    sb.append("P-");
                } else if (eCore) {
                    sb.append("E-");
                }
                sb.append("core)");
            }
            oshi.add(sb.toString());
        }
    }

    private static void printMemory(GlobalMemory memory) {
        oshi.add("Physical Memory: \n " + memory.toString());
        VirtualMemory vm = memory.getVirtualMemory();
        oshi.add("Virtual Memory: \n " + vm.toString());
        List<PhysicalMemory> pmList = memory.getPhysicalMemory();
        if (!pmList.isEmpty()) {
            oshi.add("Physical Memory: ");
            for (PhysicalMemory pm : pmList) {
                oshi.add(" " + pm.toString());
            }
        }
    }

    private static void printCpu(CentralProcessor processor) {
        oshi.add("Context Switches/Interrupts: " + processor.getContextSwitches() + " / " + processor.getInterrupts());

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        long[][] prevProcTicks = processor.getProcessorCpuLoadTicks();
        oshi.add("CPU, IOWait, and IRQ ticks @ 0 sec:" + Arrays.toString(prevTicks));
        // Wait a second...
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        oshi.add("CPU, IOWait, and IRQ ticks @ 1 sec:" + Arrays.toString(ticks));
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

        oshi.add(String.format(
                "User: %.1f%% Nice: %.1f%% System: %.1f%% Idle: %.1f%% IOwait: %.1f%% IRQ: %.1f%% SoftIRQ: %.1f%% Steal: %.1f%%",
                100d * user / totalCpu, 100d * nice / totalCpu, 100d * sys / totalCpu, 100d * idle / totalCpu,
                100d * iowait / totalCpu, 100d * irq / totalCpu, 100d * softirq / totalCpu, 100d * steal / totalCpu));
        oshi.add(String.format("CPU load: %.1f%%", processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100));
        double[] loadAverage = processor.getSystemLoadAverage(3);
        oshi.add("CPU load averages:" + (loadAverage[0] < 0 ? " N/A" : String.format(" %.2f", loadAverage[0]))
                + (loadAverage[1] < 0 ? " N/A" : String.format(" %.2f", loadAverage[1]))
                + (loadAverage[2] < 0 ? " N/A" : String.format(" %.2f", loadAverage[2])));
        // per core CPU
        StringBuilder procCpu = new StringBuilder("CPU load per processor:");
        double[] load = processor.getProcessorCpuLoadBetweenTicks(prevProcTicks);
        for (double avg : load) {
            procCpu.append(String.format(" %.1f%%", avg * 100));
        }
        oshi.add(procCpu.toString());
        long freq = processor.getProcessorIdentifier().getVendorFreq();
        if (freq > 0) {
            oshi.add("Vendor Frequency: " + FormatUtil.formatHertz(freq));
        }
        freq = processor.getMaxFreq();
        if (freq > 0) {
            oshi.add("Max Frequency: " + FormatUtil.formatHertz(freq));
        }
        long[] freqs = processor.getCurrentFreq();
        if (freqs[0] > 0) {
            StringBuilder sb = new StringBuilder("Current Frequencies: ");
            for (int i = 0; i < freqs.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(FormatUtil.formatHertz(freqs[i]));
            }
            oshi.add(sb.toString());
        }
    }

    private static void printProcesses(OperatingSystem os, GlobalMemory memory) {
        OSProcess myProc = os.getProcess(os.getProcessId());
        // current process will never be null. Other code should check for null here
        oshi.add(
                "My PID: " + myProc.getProcessID() + " with affinity " + Long.toBinaryString(myProc.getAffinityMask()));
        oshi.add("My TID: " + os.getThreadId() + " with details " + os.getCurrentThread());

        oshi.add("Processes: " + os.getProcessCount() + ", Threads: " + os.getThreadCount());
        // Sort by highest CPU
        List<OSProcess> procs = os.getProcesses(ProcessFiltering.ALL_PROCESSES, ProcessSorting.CPU_DESC, 5);
        oshi.add("   PID  %CPU %MEM       VSZ       RSS Name");
        for (int i = 0; i < procs.size(); i++) {
            OSProcess p = procs.get(i);
            oshi.add(String.format(" %5d %5.1f %4.1f %9s %9s %s", p.getProcessID(),
                    100d * (p.getKernelTime() + p.getUserTime()) / p.getUpTime(),
                    100d * p.getResidentSetSize() / memory.getTotal(), FormatUtil.formatBytes(p.getVirtualSize()),
                    FormatUtil.formatBytes(p.getResidentSetSize()), p.getName()));
        }
        OSProcess p = os.getProcess(os.getProcessId());
        oshi.add("Current process arguments: ");
        for (String s : p.getArguments()) {
            oshi.add("  " + s);
        }
        oshi.add("Current process environment: ");
        for (Entry<String, String> e : p.getEnvironmentVariables().entrySet()) {
            oshi.add("  " + e.getKey() + "=" + e.getValue());
        }
    }

    private static void printServices(OperatingSystem os) {
        oshi.add("Services: ");
        oshi.add("   PID   State   Name");
        // DO 5 each of running and stopped
        int i = 0;
        for (OSService s : os.getServices()) {
            if (s.getState().equals(OSService.State.RUNNING) && i++ < 5) {
                oshi.add(String.format(" %5d  %7s  %s", s.getProcessID(), s.getState(), s.getName()));
            }
        }
        i = 0;
        for (OSService s : os.getServices()) {
            if (s.getState().equals(OSService.State.STOPPED) && i++ < 5) {
                oshi.add(String.format(" %5d  %7s  %s", s.getProcessID(), s.getState(), s.getName()));
            }
        }
    }

    private static void printSensors(Sensors sensors) {
        oshi.add("Sensors: " + sensors.toString());
    }

    private static void printPowerSources(List<PowerSource> list) {
        StringBuilder sb = new StringBuilder("Power Sources: ");
        if (list.isEmpty()) {
            sb.append("Unknown");
        }
        for (PowerSource powerSource : list) {
            sb.append("\n ").append(powerSource.toString());
        }
        oshi.add(sb.toString());
    }

    private static void printDisks(List<HWDiskStore> list) {
        oshi.add("Disks:");
        for (HWDiskStore disk : list) {
            oshi.add(" " + disk.toString());

            List<HWPartition> partitions = disk.getPartitions();
            for (HWPartition part : partitions) {
                oshi.add(" |-- " + part.toString());
            }
        }

    }

    private static void printLVgroups(List<LogicalVolumeGroup> list) {
        if (!list.isEmpty()) {
            oshi.add("Logical Volume Groups:");
            for (LogicalVolumeGroup lvg : list) {
                oshi.add(" " + lvg.toString());
            }
        }
    }

    private static void printFileSystem(FileSystem fileSystem) {
        oshi.add("File System:");

        oshi.add(String.format(" File Descriptors: %d/%d", fileSystem.getOpenFileDescriptors(),
                fileSystem.getMaxFileDescriptors()));

        for (OSFileStore fs : fileSystem.getFileStores()) {
            long usable = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            oshi.add(String.format(
                    " %s (%s) [%s] %s of %s free (%.1f%%), %s of %s files free (%.1f%%) is %s "
                            + (fs.getLogicalVolume() != null && fs.getLogicalVolume().length() > 0 ? "[%s]" : "%s")
                            + " and is mounted at %s",
                    fs.getName(), fs.getDescription().isEmpty() ? "file system" : fs.getDescription(), fs.getType(),
                    FormatUtil.formatBytes(usable), FormatUtil.formatBytes(fs.getTotalSpace()), 100d * usable / total,
                    FormatUtil.formatValue(fs.getFreeInodes(), ""), FormatUtil.formatValue(fs.getTotalInodes(), ""),
                    100d * fs.getFreeInodes() / fs.getTotalInodes(), fs.getVolume(), fs.getLogicalVolume(),
                    fs.getMount()));
        }
    }

    private static void printNetworkInterfaces(List<NetworkIF> list) {
        StringBuilder sb = new StringBuilder("Network Interfaces:");
        if (list.isEmpty()) {
            sb.append(" Unknown");
        } else {
            for (NetworkIF net : list) {
                sb.append("\n ").append(net.toString());
            }
        }
        oshi.add(sb.toString());
    }

    private static void printNetworkParameters(NetworkParams networkParams) {
        oshi.add("Network parameters:\n " + networkParams.toString());
    }

    private static void printInternetProtocolStats(InternetProtocolStats ip) {
        oshi.add("Internet Protocol statistics:");
        oshi.add(" TCPv4: " + ip.getTCPv4Stats());
        oshi.add(" TCPv6: " + ip.getTCPv6Stats());
        oshi.add(" UDPv4: " + ip.getUDPv4Stats());
        oshi.add(" UDPv6: " + ip.getUDPv6Stats());
    }

    private static void printDisplays(List<Display> list) {
        oshi.add("Displays:");
        int i = 0;
        for (Display display : list) {
            oshi.add(" Display " + i + ":");
            oshi.add(String.valueOf(display));
            i++;
        }
    }

    private static void printUsbDevices(List<UsbDevice> list) {
        oshi.add("USB Devices:");
        for (UsbDevice usbDevice : list) {
            oshi.add(String.valueOf(usbDevice));
        }
    }

    private static void printSoundCards(List<SoundCard> list) {
        oshi.add("Sound Cards:");
        for (SoundCard card : list) {
            oshi.add(" " + String.valueOf(card));
        }
    }

    private static void printGraphicsCards(List<GraphicsCard> list) {
        oshi.add("Graphics Cards:");
        if (list.isEmpty()) {
            oshi.add(" None detected.");
        } else {
            for (GraphicsCard card : list) {
                oshi.add(" " + String.valueOf(card));
            }
        }
    }
}