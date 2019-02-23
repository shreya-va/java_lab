
import java.util.ArrayList;
import java.util.Arrays;
public class Sorting{
    public void selectionSort (int[] numbers){
        int key, temp;
        for(int index = 0;  index < numbers.length-1;  index++){
            key = index;
            for(int position = index+1;   position < numbers.length;   position++){
                if(numbers[position] < numbers[key])
                    key = position;
            }
            temp = numbers[key];
            numbers[key] = numbers[index];
            numbers[index] = temp;
        }
    } 
    public static void insertionSort (int [] numbers){
        for(int index = 1; index < numbers.length; index++){
            int key = numbers[index];
            int position = index;
        
            while(position > 0 && numbers [position - 1] > key){
                numbers[position] = numbers[position - 1];
                position--;
            }
            numbers[position] = key;
        }
    }
    //Implementation by Shogo
    public static void bucketSort(int[] array){
        int[] queue = new int[256];
        for(int i=0;i<256;i++){
            queue[i] = 0;
        }
        for(int j : array){
            queue[j]++;
        }
        int n=0;
        for(int i=0;i<256;i++){
            Arrays.fill(array, n, n+queue[i], i);
            n+=queue[i];
        }
    }
    public static void radixSortModified(int[] array){
        ArrayList[] queue = new ArrayList[256];
        for(int a=0;a<256;a++){
            queue[a] = new ArrayList<Integer>();
        }
        for(int j : array){
            queue[j].add(j);
        }
        int n=0;
        for(int j=0;j<256;j++){
            for(int k=0;k<queue[j].size();k++){
                //generates a warning but works fine
                Integer temp = (Integer)queue[j].get(k);
                array[n] = temp;
                n++;
            }
        }
    }
    public static void radixSort(int[] array){
        ArrayList[] queue = new ArrayList[10];
        for (int i=0;i<3;i++){ //looping for three places: 1s, 10s, 100s
            for(int a=0;a<10;a++){
                queue[a] = new ArrayList<Integer>();
            }
            for(int j : array){
                int num = (j/(10^i))%10;
                queue[num].add(j);
            }
            int n=0;
            for(int j=0;j<10;j++){
                for(int k=0;k<queue[j].size();k++){
                    //generates a warning but works fine
                    Integer temp = (Integer)queue[j].get(k);
                    array[n] = temp;
                    n++;
                }
            }
        }
    }
    
   /* k = number of digits
       r = base of each digit (i.e., r = 10 for standard base 10) */
   //from http://www.cs.usfca.edu/~galles/cs245/lecture/radixsort.java.html
    public static void radixsort(int array[], int k) {
       int i, j, rtok;
       int[] count = new int[10];
       int[] newarray = new int[array.length];
       for(i=0; i<k; i++) {
           rtok = 10^i; //bring the intended digit place to 1s place
           for (j=0; j<10; j++) //10 possible digits for each place
               count[j] = 0;
           for(j=0; j<array.length; j++)
               count[(array[j]/rtok)%10]++; //mod by 10 to get the digit at 1s place
           for(j=1; j<10; j++)
               count[j] = count[j-1] + count[j]; //count = cumulative count of digits before j = place where filling in ends
           for(j=array.length-1; j>=0; j--)
               newarray[--count[(array[j]/rtok)%10]] = array[j]; //decrement count as sorting elements in array to newarray
           for(j=0; j<array.length; j++)
               array[j] = newarray[j]; //copy newarray to array
       }
   }
}