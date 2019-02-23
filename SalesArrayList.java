// ****************************************************************
// SalesArrayList.java
//
// Reads in and stores sales for each of 5 salespeople.  Displays
// sales entered by salesperson id and total sales for all salespeople.
//
// ****************************************************************
import java.util.Scanner;
import java.util.ArrayList;

public class SalesArrayList
{
    public static void main(String[] args)
    {
    Scanner scan = new Scanner(System.in);
    System.out.println("Please print in the required number of Salespeople");
    final int SALESPEOPLE = scan.nextInt();
    ArrayList<Integer> sales = new ArrayList<Integer>();
    int sum;
    
    for (int i=0; i<SALESPEOPLE; i++)
        {
        System.out.print("Enter sales for salesperson " + (i+1) + ": ");
        sales.add(scan.nextInt());
        }

    System.out.println("\nSalesperson   Sales");
    System.out.println("--------------------");
    sum = 0;
    int maximumSale = sales.get(0);
    int idMaxPerson = 0;
    int idMinPerson = 0;
    int minimumSale = sales.get(0);
    System.out.println("Please type in a sale amount"); 
    int compareValue = scan.nextInt();
    int exceedCount = 0;
    
    int i=0;
    for (int term:sales)
        {
           System.out.println("     " + i + "         " + sales.get(i));
        sum += sales.get(i);
        if (compareValue < sales.get(i))
        { 
            System.out.println("Person ID: " + i + " Amount: " + sales.get(i));
            exceedCount += 1;
        }
        
        if (maximumSale < sales.get(i))
        {
            maximumSale = sales.get(i);
            idMaxPerson = i+1;
        }
        else 
        {
            minimumSale = sales.get(i);
            idMinPerson = i+1;
        }
        i++;
        }
    int average = sum/SALESPEOPLE;
    System.out.println("The total amount of people who exceeded " +
    compareValue + " dollars is " + exceedCount);
    System.out.println("\nTotal sales: " + sum);
    System.out.println("\nAverage sales:" + average);
    System.out.println("\nMaximum sales:" + maximumSale);
    System.out.println("\nMaximum sales ID:" + idMaxPerson);
    System.out.println("\nMinimum sales:" + minimumSale);
    System.out.println("\nMinimum sales ID:" + idMinPerson);
    }
}