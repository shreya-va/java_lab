// ****************************************************************
// Sales.java
//
// Reads in and stores sales for each of 5 salespeople.  Displays
// sales entered by salesperson id and total sales for all salespeople.
//
// ****************************************************************
import java.util.Scanner;

public class Sales
{
    public static void main(String[] args)
    {
    Scanner scan = new Scanner(System.in);
    System.out.println("Please print in the required number of Salespeople");
    final int SALESPEOPLE = scan.nextInt();
    int[] sales = new int[SALESPEOPLE];
    int sum;

    int a = 0;
    for (int person:sales)
        {
        System.out.print("Enter sales for salesperson " + (a+1) + ": ");
        sales[a] = scan.nextInt();
        a++;
        }

    System.out.println("\nSalesperson   Sales");
    System.out.println("--------------------");
    sum = 0;
    int maximumSale = sales[0];
    int idMaxPerson = 0;
    int idMinPerson = 0;
    int minimumSale = sales[0];
    System.out.println("Please type in a sale amount"); 
    int compareValue = scan.nextInt();
    int exceedCount = 0;
    for (int i=0; i<sales.length; i++)
        {
           System.out.println("     " + i + "         " + sales[i]);
        sum += sales[i];
        if (compareValue < sales[i])
        { 
            System.out.println("Person ID: " + i + " Amount: " + sales[i]);
            exceedCount += 1;
        }
        
        if (maximumSale < sales[i])
        {
            maximumSale = sales[i];
            idMaxPerson = i+1;
        }
        else 
        {
            minimumSale = sales[i];
            idMinPerson = i+1;
        }
        
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