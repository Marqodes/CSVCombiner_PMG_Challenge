import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CSVCombine 
{ 
	private static String outputFile;
	private static int rowCounter = 0;


	public static void main(String[] args) throws Exception
	{ 
		validateInput(args);

	} 

	public static void validateInput(String[] args)
	{
		int sizeOfArgs = args.length;
		Scanner in = new Scanner(System.in);

		if(args[0].equals("-help"))
		{
			printHelpScreen();

		}
		// at least two argument must be passed {inputFile.csv output.csv}
		else if(sizeOfArgs < 2)
		{
			System.out.println("Must have at least one input file to merge and one output file...");
			System.out.println("For help type -help");
		}
		else
		{
			// Print to console the files the user wants to combine
			System.out.print("\n-Files to combine:");
			for(int i = 0; i < sizeOfArgs - 1; i++)
			{
				System.out.print(" |" + args[i] + "| ");
			}

			// Print to console the output file the user wants
			System.out.println();
			System.out.println("-Output file will be: " + args[sizeOfArgs - 1]);
			System.out.print("\nAre these the correct files (y/n): ");

			// Make user confirm if they want to move forward with the merge
			String input = in.nextLine();
			while(!input.matches("[Y|y|N|nj]+"))
			{
				System.out.println("*please confirm with 'y' or 'n'*");
				System.out.print("Are these the correct files (y/n): ");
				input = in.nextLine();
			}
			outputFile = args[sizeOfArgs - 1];

			// if the files are not correct program stops
			if(input.toLowerCase().equals("n"))
			{
				System.out.println();
				System.out.println("Please try running the program again...");
				System.out.println("For help type -help");
			}
			// if files are correct, send them to be validated
			else
			{
				for(int i = 0; i < sizeOfArgs - 1; i++)
				{
					valdateFilePath(args[i]);
				}
			}

			in.close();
		}
	}

	public static void valdateFilePath(String filePath)
	{
		File file = new File(filePath);

		// check that file given is a valid path and file name
		if(!file.exists())
		{
			System.out.println("No such file: \"" + filePath + "\"");
			System.out.println("Please try again...");
			System.out.println("For help type -help");
		}
		else
		{
			combineFile(file);
		}
	}

	public static void combineFile(File file)
	{
		File output = new File(outputFile);

		try
		{
			BufferedReader csvInputReader = new BufferedReader(new FileReader(file));
			// get first row "AKA" the header for the and add the new column
			String header = csvInputReader.readLine() + ",filename";

			String row = null;
			int counter = 0;

			// if this is the first time opening output file -> create it 
			if(!output.exists())
			{
				FileWriter csvOutputWriter = new FileWriter(output);

				csvOutputWriter.append(header);
				csvOutputWriter.append("\n");


				while((row = csvInputReader.readLine()) != null)
				{
					csvOutputWriter.append(row);
					csvOutputWriter.append("," + file.getName());
					csvOutputWriter.append("\n");
					rowCounter++;
				}

				System.out.println("All " + (rowCounter - 1) + " rows from " + file.getName() + " has been combine with: " + outputFile);

				csvInputReader.close();
				csvOutputWriter.flush();
				csvOutputWriter.close();
			}
			else
			{
				FileWriter csvOutputWriter2 = new FileWriter(output, true);
				// skip the header since we already have it
				csvInputReader.readLine();

				while((row = csvInputReader.readLine()) != null)
				{
					csvOutputWriter2.append(row);
					csvOutputWriter2.append("," + file.getName());
					csvOutputWriter2.append("\n");
					counter++;
					rowCounter++;
				}

				System.out.println("All " + (counter - 1) + " rows from " + file.getName() + " has been combined with: " + outputFile);


				csvInputReader.close();
				csvOutputWriter2.flush();
				csvOutputWriter2.close();
			}

		}
		catch(IOException ioe)
		{
			System.out.println("Error with file: " + file.getName());
		}
	}
	public static void printHelpScreen()
	{
		System.out.println("\tWelcome to CSV Combiner");
		System.out.println("\t-----------------------");
		System.out.println("Below is the proper format.\n");

		System.out.println("\"java CSVCombine [input filepath One] [input filepath two] [input filepath ...] [output fileName]\"");

		System.out.println("\n*At minimum 1 input filepath is needed");
		System.out.println("*At maximum only 1 output fileName");
	}
} 
