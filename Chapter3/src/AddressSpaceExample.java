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
 * In this example, the address of the array and the values of the elements are printed to the 
 * console.
 * */

public class AddressSpaceExample {
    public static void main(String[] args) {
        // Create an array of integers
        int[] array = new int[5];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        // Print the address of the array and the values of the elements
        System.out.println("Array address: " + array);
        
        // Print the address of the array in HEX format
        String str = array.toString();
        byte[] bytes = str.getBytes();
        for (byte b : bytes) {
            System.out.print(Integer.toHexString(b & 0xff));
        }System.out.println();
        
        for (int i = 0; i < array.length; i++) {
            System.out.println("array[" + i + "] = " + array[i]);
        }
    }
}
