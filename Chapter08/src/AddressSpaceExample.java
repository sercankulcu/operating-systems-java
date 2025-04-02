/*
 * Here is an example of a Java program that demonstrates address space in the context of 
 * operating systems:
 * 
 * In this example, an array of integers is created using the new operator. The new operator 
 * allocates memory for the array and returns the address of the first element in the array. 
 * This address is assigned to the array variable. 
 * 
 * The array variable holds the address of the array in memory, and the elements of the array 
 * can be accessed using the array indexing notation (e.g., array[i]). 
 * 
 * This extended version includes:
 * - Multiple array types to show different memory allocations
 * - Memory usage estimation
 * - Demonstration of object references
 * - Additional address space visualization
 * 
 * The program prints various memory-related information to illustrate how Java manages
 * address space in the virtual machine.
 * */

public class AddressSpaceExample {
    // Class variable to store a secondary array for comparison
    private static String[] stringArray;
    
    // Method to estimate memory usage of an array
    private static long estimateArrayMemory(Object array) {
        // Rough estimation: object header (12 bytes) + length (4 bytes) + data
        int elementSize = array instanceof int[] ? 4 : 8; // int=4 bytes, reference=8 bytes
        return 16 + (elementSize * java.lang.reflect.Array.getLength(array));
    }
    
    public static void main(String[] args) {
        // Create an array of integers with initial size of 5
        int[] array = new int[5];
        
        // Initialize the integer array with sequential values
        for (int i = 0; i < array.length; i++) {
            array[i] = i * 10; // Using multiples of 10 for clearer output
        }
        
        // Create and initialize a String array to demonstrate object references
        stringArray = new String[3];
        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = "Item " + i;
        }
        
        // Print memory-related information
        System.out.println("=== Memory Address Space Demonstration ===");
        
        // Print the reference (memory address) of the integer array
        System.out.println("Integer array reference: " + array);
        
        // Print the reference in hexadecimal format
        System.out.print("Integer array reference (HEX): ");
        String intArrayStr = array.toString();
        byte[] intBytes = intArrayStr.getBytes();
        for (byte b : intBytes) {
            System.out.print(Integer.toHexString(b & 0xff) + " ");
        }
        System.out.println();
        
        // Print estimated memory usage for integer array
        System.out.println("Estimated memory usage (int array): " + 
            estimateArrayMemory(array) + " bytes");
        
        // Print values and their relative offsets for integer array
        System.out.println("\nInteger Array Contents:");
        for (int i = 0; i < array.length; i++) {
            System.out.printf("array[%d] at offset %d = %d%n", 
                i, i * 4, array[i]); // 4 bytes per int
        }
        
        // Print information about the String array
        System.out.println("\nString array reference: " + stringArray);
        System.out.println("Estimated memory usage (string array): " + 
            estimateArrayMemory(stringArray) + " bytes");
        
        // Print String array contents with references
        System.out.println("\nString Array Contents:");
        for (int i = 0; i < stringArray.length; i++) {
            System.out.println("stringArray[" + i + "] = " + stringArray[i] + 
                " (ref: " + stringArray[i].hashCode() + ")");
        }
        
        // Demonstrate memory allocation with Runtime information
        Runtime runtime = Runtime.getRuntime();
        System.out.println("\nJVM Memory Information:");
        System.out.println("Total memory: " + runtime.totalMemory() / 1024 + " KB");
        System.out.println("Free memory: " + runtime.freeMemory() / 1024 + " KB");
        System.out.println("Max memory: " + runtime.maxMemory() / 1024 + " KB");
    }
}