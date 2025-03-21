import java.io.*;
import java.util.*;

public class SortingApp {

    // (1) Create an array of random integers (or any Comparable type) between 0 and 100
    public static <T extends Comparable<T>> T[] createRandomArray(Class<T> type, int arrayLength) {
        Random rand = new Random();
        @SuppressWarnings("unchecked")
        T[] array = (T[]) java.lang.reflect.Array.newInstance(type, arrayLength);
        
        for (int i = 0; i < arrayLength; i++) {
            if (type == Integer.class) {
                array[i] = type.cast(rand.nextInt(101));  // random number between 0 and 100
            }
            // You can extend this to handle other types if needed (e.g., Double, String, etc.)
        }
        
        return array;
    }

    // (2) Write the array to a file
    public static <T> void writeArrayToFile(T[] array, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (T element : array) {
                writer.write(element.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // (3) Read integers (or any Comparable type) from a file into an array
    public static <T extends Comparable<T>> T[] readFileToArray(Class<T> type, String filename) {
        List<T> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (type == Integer.class) {
                    list.add(type.cast(Integer.parseInt(line.trim())));
                }
                // Handle other types (like Double, String) if needed.
            }
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }

        @SuppressWarnings("unchecked")
        T[] array = (T[]) java.lang.reflect.Array.newInstance(type, list.size());
        return list.toArray(array);
    }

    // (4) Generic Bubble Sort
    public static <T extends Comparable<T>> void bubbleSort(T[] array) {
        int n = array.length;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (array[i - 1].compareTo(array[i]) > 0) {
                    // Swap elements
                    T temp = array[i - 1];
                    array[i - 1] = array[i];
                    array[i] = temp;
                    swapped = true;
                }
            }
            n--;  // After each pass, the largest element is placed in the correct position
        } while (swapped);
    }

    // (5) Generic Merge Sort
    public static <T extends Comparable<T>> void mergeSort(T[] array) {
        if (array.length < 2) {
            return;
        }
        int mid = array.length / 2;
        T[] left = Arrays.copyOfRange(array, 0, mid);
        T[] right = Arrays.copyOfRange(array, mid, array.length);

        mergeSort(left);
        mergeSort(right);
        merge(array, left, right);
    }

    private static <T extends Comparable<T>> void merge(T[] array, T[] left, T[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i].compareTo(right[j]) <= 0) {
                array[k++] = left[i++];
            } else {
                array[k++] = right[j++];
            }
        }
        while (i < left.length) {
            array[k++] = left[i++];
        }
        while (j < right.length) {
            array[k++] = right[j++];
        }
    }

    // Main function to handle user input and control program flow
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Choose an action
        System.out.println("Select an option:");
        System.out.println("1. Generate an array of random integers and store it in a file.");
        System.out.println("2. Read an existing file, sort the integers, and store them in another file.");
        int choice = scanner.nextInt();

        if (choice == 1) {
            // (1) Generate random array of integers
            System.out.print("Enter the length of the array: ");
            int length = scanner.nextInt();
            System.out.print("Enter the filename to store the array: ");
            String filename = scanner.next();

            Integer[] array = createRandomArray(Integer.class, length);
            writeArrayToFile(array, filename);
            System.out.println("Array generated and written to file: " + filename);
        } else if (choice == 2) {
            // (2) Read from file, sort, and write to another file
            System.out.print("Enter the filename to read the array from: ");
            String inputFilename = scanner.next();
            System.out.print("Enter the filename to store the sorted array: ");
            String outputFilename = scanner.next();

            Integer[] array = readFileToArray(Integer.class, inputFilename);

            // Sorting the array using either bubbleSort or mergeSort
            System.out.println("Choose sorting algorithm:");
            System.out.println("1. Bubble Sort");
            System.out.println("2. Merge Sort");
            int sortChoice = scanner.nextInt();

            if (sortChoice == 1) {
                bubbleSort(array);
                System.out.println("Array sorted using Bubble Sort.");
            } else if (sortChoice == 2) {
                mergeSort(array);
                System.out.println("Array sorted using Merge Sort.");
            }

            writeArrayToFile(array, outputFilename);
            System.out.println("Sorted array written to file: " + outputFilename);
        } else {
            System.out.println("Invalid choice!");
        }

        scanner.close();
    }
}
