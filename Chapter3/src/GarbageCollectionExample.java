/*
 * Another important concept in operating systems is memory management. In Java, memory management 
 * is handled automatically by the Java Virtual Machine (JVM) through a process called garbage 
 * collection. Here is an example of a Java program that demonstrates how the garbage collector 
 * works: 
 * 
 * In this example, a method createObject() is called repeatedly, creating a new Object each time. 
 * After using the object, the reference to it is set to null. This makes the object eligible for 
 * garbage collection. The garbage collector periodically runs in the background, freeing up 
 * memory by removing any objects that are no longer reachable.
 * 
 * */

public class GarbageCollectionExample {
    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            createObject();
        }
    }

    public static void createObject() {
        Object obj = new Object();
        // Do something with the object
        // ...
        // The object is no longer needed
        obj = null;
    }
}
