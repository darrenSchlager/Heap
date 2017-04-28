/*
	Author: Darren Schlager
*/

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

public class Heap 
{
	//instance variables
	private ArrayList<Integer> heap;
	private int lengthOfLongestValue = 0;	//used by print()
	
	//constructor
	public Heap()
	{
		heap = new ArrayList<Integer>();
	}
	
	public void insert(int value)
	{
		System.out.println("[insert "+value+"]\n");
		
		//update 'lengthOfLongestValue'
		int lengthOfValue = (value+"").length();
		if(lengthOfValue>lengthOfLongestValue) lengthOfLongestValue = lengthOfValue;
		
		//insert the value at the end of the array
		heap.add(value);
		print();
		
		int i = heap.size()-1;
		while(i>0 && heap.get((i-1)/2)>heap.get(i)) //continue if parent>child
		{
			System.out.println(".swap.\n");
			swap((i-1)/2, i);
			print();
			
			//update the current index to be the parents index
			i = (i-1)/2;
		}
	}
	
	public int remove()
	{
		System.out.println(".replace the root with the last node and remove it.\n");
		int deletedValue = heap.get(0);
		heap.set(0, heap.get(heap.size()-1));
		heap.remove(heap.size()-1);
		print();
		
		int i=0;
		while(true)
		{
			//two children
			if(2*i+2<heap.size())
			{
				//if parent is <= both children
				if(heap.get(i)<=heap.get(2*i+1) && heap.get(i)<=heap.get(2*i+2))
				{
					return deletedValue;
				}
				//parent is > a child
				else
				{
					//find the smallest child
					int indexOfSmallestChild = 2*i+2;
					if(heap.get(2*i+1)<=heap.get(2*i+2))
					{
						indexOfSmallestChild--;
					}
					
					//swap the parent with the smallest child
					System.out.println(".swap.\n");
					swap(i, indexOfSmallestChild);
					print();
					
					//set the index for the next iteration
					i = indexOfSmallestChild;
				}
			}
			//one child
			else if(2*i+1<heap.size())
			{
				//the parent is > the child
				if(heap.get(i)>heap.get(2*i+1))
				{
					//swap the parent and the child
					System.out.println(".swap.\n");
					swap(i, 2*i+1);
					print();
				}
				
				//the child cannot have any children so return
				return deletedValue;
			}
			//no children
			else 
			{
				return deletedValue;
			}
		}
	}
	
	public boolean isEmpty()
	{
		return heap.size()==0;
	}
	
	private void swap(int indexA, int indexB)
	{
		int temp = heap.get(indexA);
		heap.set(indexA, heap.get(indexB));
		heap.set(indexB, temp);
	}
	
	public void print()
	{
		int levels = (int)(Math.log(heap.size())/Math.log(2));
		
		for(int i=0, j=levels; i<=levels; i++, j--)
		{
			int startingIndex = (int)Math.pow(2, i)-1;
			int paddingAtStart = ( (int)Math.pow(2, j)-1 ) * lengthOfLongestValue;
			int paddingBetween = ( (int)Math.pow(2, j+1)-1 ) * lengthOfLongestValue;
			printLevel(startingIndex, startingIndex+1, paddingAtStart, paddingBetween);
			System.out.println();
		}
	}
	
	private void printLevel(int startingIndex, int howMany, int paddingAtStart, int paddingBetween)
	{
		if(startingIndex+howMany>heap.size()) howMany = heap.size()-startingIndex;
		int lastIndex = startingIndex+howMany-1;
		
		for(int j=0; j<paddingAtStart+2; j++) System.out.print(" ");
		for(int i=startingIndex; i<=lastIndex; i++)
		{
			if((i-startingIndex)%2==0) System.out.printf("%-"+lengthOfLongestValue+"d", heap.get(i));
			else System.out.printf("%-"+lengthOfLongestValue+"d", heap.get(i));
			if(i!=lastIndex) for(int j=0; j<paddingBetween; j++) System.out.print(" ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) 
	{
		Heap heap = new Heap();
		run(heap);
	}
	
	public static void run(Heap heap)
	{
		// used to retrieve keyboard input from the user
		Scanner keyboard = new Scanner(System.in);
		
		// print description of expeteted file contents
		System.out.println("Heap\n");
		System.out.println("Format your file as follows:");
		System.out.println("===============================================");
		System.out.println(" <integer 1> <integer 2> ... <integer n>");
		System.out.println("===============================================");
		
		Scanner file;
		boolean dataInputSuccessfully = false;
		System.out.println("Step 1: insert nodes");
		System.out.print("press RETURN to enter data manually\nfile path: ");
		String path = "dummy string";
		
		//insert nodes
		do {
			
			// get the file path from the user
			if(!path.equals("")) path = keyboard.nextLine();
			
			// enter data manually
			if(path.equals(""))
			{
				
				//get the input from the user
				System.out.print("insert node(s): ");
				String input = keyboard.nextLine();
				
				while(!input.equals(""))
				{
					try 
					{
						System.out.println();
						
						//preapare the received input for processing
						Scanner stringScanner = new Scanner(input);
						
						//insert each item
						while(stringScanner.hasNext())
						{
							heap.insert(stringScanner.nextInt());
						}
					}
					catch (Exception e)
					{
						System.out.println("That data is not formatted correctly.");
					}
					
					//get the next input from the user
					System.out.print("insert: ");
					input = keyboard.nextLine();
				}
				dataInputSuccessfully = true;
			}
			//enter data from file
			else
			{
				try 
				{
					// open the file
					file = new Scanner(new FileReader(path));
					
					try 
					{
						System.out.println();
						
						//insert each item
						while(file.hasNext())
						{
							heap.insert(file.nextInt());
							if(!file.hasNext()) dataInputSuccessfully = true;
						}
					}
					catch (Exception e)
					{
						System.out.println("That file is not formatted correctly.\nfile path: ");
					}
					
					//close the file
					file.close();
				} 
				catch (IOException e) // file
				{
					// invalid file
					System.out.print("That file does not exist.\nfile path: ");
				}
			}
			
		} while (!dataInputSuccessfully);
		
		System.out.println("=finished inserting data=\n");
		
		if(!heap.isEmpty())
		{
			//remove nodes
			System.out.println("Step 2: remove nodes");
			System.out.print("remove the root node? (y/n): ");
			
			//get user input
			String input = keyboard.nextLine();
			
			while(input.equals("y"))
			{
				System.out.println();
				
				int removed = heap.remove();
				System.out.println("[finished removing " + removed + "]\n");
				
				if(!heap.isEmpty())
				{
					//get the next nodes from the user
					System.out.print("remove the root node? (y/n): ");
					input = keyboard.nextLine();
				}
				else 
				{
					//exit loop
					input = "n";
				}
			}	
		}
		
		System.out.println("exit");
	}

}