
// QuadraticProbing Hash table class
//
// CONSTRUCTION: an approximate initial size or default of 101
//
// ******************PUBLIC OPERATIONS*********************
// bool insert( x )       --> Insert x
// bool remove( x )       --> Remove x
// bool contains( x )     --> Return true if x is present
// void makeEmpty( )      --> Remove all items


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Probing table implementation of hash tables.
 * Note that all "matching" is based on the equals method.
 * @author Mark Allen Weiss
 */
public class DoubleHashTable<E> {

    private int totalFinds;
    private int totalProbes;

    public int getTotalFinds() { return totalFinds; }
    public int getTotalProbes() { return totalProbes; }

    /**
     * Construct the hash table.
     */
    public DoubleHashTable()
    {
        this( DEFAULT_TABLE_SIZE );
    }

    /**
     * Construct the hash table.
     * @param size the approximate initial size.
     */
    public DoubleHashTable(int size) {
        allocateArray(size);
        doClear();
    }

    /**
     * Insert into the hash table. If the item is
     * already present, do nothing.
     * @param x the item to insert.
     */
    public boolean insert(E x) {
        // Insert x as active
        int currentPos = findPos(x);
        if(isActive(currentPos)) return false;

        array[currentPos] = new HashEntry<>(x, true);
        currentActiveEntries++;

        // Rehash
        if(++occupiedCount > array.length / 2){ rehash(); }

        return true;
    }

    public String toString (int limit){
        StringBuilder sb = new StringBuilder();
        int count=0;
        for (int i=0; i < array.length && count < limit; i++){
            if (array[i]!=null && array[i].isActive) {
                sb.append(i).append(": ").append(array[i].element).append("\n");
                count++;
            }
        }
        return sb.toString();
    }

    /**
     * Expand the hash table.
     */
    private void rehash() {
        HashEntry<E> [] oldArray = array;

        // Create a new double-sized, empty table
        allocateArray(2 * oldArray.length);
        occupiedCount = 0;
        currentActiveEntries = 0;

        // Copy table over
        for(HashEntry<E> entry : oldArray) {
            if (entry != null && entry.isActive) { insert(entry.element); }
        }
    }

    /**
     * Method that performs quadratic probing resolution.
     * @param x the item to search for.
     * @return the position where the search terminates.
     */
    private int findPos(E x) {
        int position1 = myHash1(x);
        int position2 = myHash2(x);

        totalProbes += 1;

        while(array[position1] != null && !array[position1].element.equals(x)) {
            if (!isActive(position1)) { continue; }
            totalProbes += 1;
            position1 += position2;
            position1 %= array.length;
        }
        return position1;
    }

    /**
     * Remove from the hash table.
     * @param x the item to remove.
     * @return true if item removed
     */
    public boolean remove(E x) {
        int currentPos = findPos(x);
        if(isActive(currentPos)) {
            array[currentPos].isActive = false;
            currentActiveEntries--;
            return true;
        }
        else { return false; }
    }

    /**
     * Get current size.
     * @return the size.
     */
    public int size() { return currentActiveEntries; }

    /**
     * Get length of internal table.
     * @return the size.
     */
    public int capacity() { return array.length; }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return true if item is found
     */
    public boolean contains(E x) {
        int currentPos = findPos(x);
        return isActive(currentPos);
    }

    /**
     * Find an item in the hash table.
     * @param x the item to search for.
     * @return the matching item.
     */
    public E find(E x) {
        totalFinds += 1;

        int currentPos = findPos(x);
        if (!isActive(currentPos)) { return null; }
        else { return array[currentPos].element; }
    }

    /**
     * Return true if currentPos exists and is active.
     * @param currentPos the result of a call to findPos.
     * @return true if currentPos is active.
     */
    public boolean isActive(int currentPos) { return array[currentPos] != null && array[currentPos].isActive; }

    /**
     * Make the hash table logically empty.
     */
    public void makeEmpty() { doClear(); }

    private void doClear() {
        occupiedCount = 0;
        Arrays.fill(array, null);
    }

    private int myHash1(E x) {
        int hashVal = x.hashCode();

        hashVal %= array.length;
        if(hashVal < 0){ hashVal += array.length; }

        return hashVal;
    }

    private int myHash2(E x) {
        int hashVal = x.hashCode();

        int primeNum = nextPrime(DEFAULT_TABLE_SIZE / 2);

        hashVal = (primeNum - (hashVal % primeNum));
        if(hashVal < 0){ hashVal += array.length; }

        return hashVal;
    }

    private static class HashEntry<E> {
        public E element;   // the element
        public boolean isActive;  // false if marked deleted

        public HashEntry(E e) {
            this(e, true);
        }

        public HashEntry(E e, boolean i) {
            element  = e;
            isActive = i;
        }
    }

    private static final int DEFAULT_TABLE_SIZE = 101;

    private HashEntry<E>[] array; // The array of elements
    private int occupiedCount;                 // The number of occupied cells
    private int currentActiveEntries;                  // Current size


    /**
     * Internal method to allocate array.
     * @param arraySize the size of the array.
     */
    private void allocateArray(int arraySize) { array = new HashEntry[nextPrime(arraySize)]; }

    /**
     * Internal method to find a prime number at least as large as n.
     * @param n the starting number (must be positive).
     * @return a prime number larger than or equal to n.
     */
    private static int nextPrime(int n) {
        if( n % 2 == 0 ) { n++; }

        while(!isPrime(n))
            n += 2;
        return n;
    }

    /**
     * Internal method to test if a number is prime.
     * Not an efficient algorithm.
     * @param n the number to test.
     * @return the result of the test.
     */
    private static boolean isPrime(int n) {
        if( n == 2 || n == 3 ) return true;

        if( n == 1 || n % 2 == 0 ) return false;

        for( int i = 3; i * i <= n; i += 2 ) {
            if (n % i == 0) return false;
        }
        return true;
    }


    // Simple main
    public static void main( String [] args ) throws IOException {
        DoubleHashTable<String> HashTable = new DoubleHashTable<>();

        long startTime = System.currentTimeMillis();

        System.out.println( "Checking... (no more output means success)" );

        File inputFile = new File("test.txt\\");

        BufferedReader br = new BufferedReader(new FileReader(inputFile));

        String inputString;

        System.out.println("Inserting test file numbers into HashTable");
        while ((inputString = br.readLine()) != null) {
            if (!HashTable.insert(inputString)) {
                System.out.println("Could not insert the value " + inputString);
            }
        }
        System.out.println("Current HashTable:");
        System.out.println(HashTable.toString(HashTable.size()));

        System.out.println("Removing the number 45 from HashTable:");
        HashTable.remove("45");

        System.out.println("Current HashTable:");
        System.out.println(HashTable.toString(HashTable.size()));

        System.out.println("Testing contains() function:");
        if (HashTable.contains("45")) {
            System.out.println("HashTable contains 45");} else {System.out.println("HashTable doesn't contain 45");}
        if (HashTable.contains("12")) {
            System.out.println("HashTable contains 12");} else {System.out.println("HashTable doesn't contain 12");}

        System.out.println("Current HashTable:");
        System.out.println(HashTable.toString(HashTable.size()));

        System.out.println("Testing makeEmpty() function:");
        HashTable.makeEmpty();

        System.out.println("Current HashTable:");
        System.out.println(HashTable.toString(HashTable.size()));

        long endTime = System.currentTimeMillis( );

        System.out.println( "Elapsed time: " + (endTime - startTime) );
        System.out.println( "H size is: " + HashTable.size( ) );
        System.out.println( "Array size is: " + HashTable.capacity( ) );
    }

}

