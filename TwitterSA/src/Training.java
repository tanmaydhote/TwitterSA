import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
public class Training
{
	public static LinkedHashMap<String, Double> PositivePolarities = new LinkedHashMap<String , Double>();
	public static LinkedHashMap<String, Double> NegativePolarities = new LinkedHashMap<String , Double>();
	public static LinkedHashMap<String, Double> Counts = new LinkedHashMap<String , Double>();
	public static LinkedHashMap<String, Double> Saliences = new LinkedHashMap<String , Double>();
	public static ArrayList<String> stopWords = new ArrayList<String>();
	public static LinkedHashMap<String, String> acronyms = new LinkedHashMap<String , String>();
	public static void findPolarities() throws IOException
	{
		Scanner enable = new Scanner(new File("/home/tanmay/workspace/TwitterSA/data/stop.txt"));
		String trainingFile = "/home/tanmay/workspace/TwitterSA/data/150kTraining.txt";
		BufferedReader br = new BufferedReader(new FileReader(new File(trainingFile)));
		String S=null;
		while(enable.hasNext()){
			S = enable.next();
			stopWords.add(S);
		}
		
		
		String S1,f1,expansion1;		
		String[] parts1;
		int i1;
		Scanner input1 = new Scanner(new File("/home/tanmay/workspace/TwitterSA/data/acronyms.txt"));
		while(input1.hasNext()){
			expansion1 = "";
			f1 = input1.nextLine();
			//System.out.println(S);
			//System.out.println("hi1");
			S1 = f1.toLowerCase();
			parts1 = S1.split("\\s+");
			for(i1 = 1; i1<parts1.length;i1++)
			{
			expansion1 = expansion1.concat(parts1[i1]);
			if(i1<parts1.length-1)
			expansion1 = expansion1.concat(" ");
			}
			acronyms.put(parts1[0],expansion1);
		}
		
		
		
	//	System.out.println(acronyms);
		
		
		
		
		double salience;
		String input = null,f=null;
		String[] parts = null;
		ArrayList<String> unigrams = new ArrayList<String>();
		int Polarity;
		while ((input = br.readLine()) != null) {
			unigrams.clear();
			f = input;
			S = f.toLowerCase();
			parts = S.split("\\s+");
			Polarity = Integer.parseInt(parts[0]);
			for(int i = 0;i<parts.length;i++)
			{
				if(!parts[i].substring(0,1).equals("@"))
				{
					parts[i] = parts[i].replaceAll("[^a-zA-Z ]", "");
					if(!stopWords.contains(parts[i])&& parts[i].length()>1)
					unigrams.add(parts[i]);
				}
			}
			//System.out.println(unigrams);
			for (String s : unigrams)
			{
				UpdatePolarities(s, Polarity);
			}
				
		}
	//	System.out.println("Positive");
	//	System.out.println(PositivePolarities);
	//	System.out.println("Negative");
	//	System.out.println(NegativePolarities);
	//	System.out.println(Counts);
		for (String s : Counts.keySet()) {
		    salience = 1 - Math.min(PositivePolarities.get(s),NegativePolarities.get(s))/Math.max(PositivePolarities.get(s),NegativePolarities.get(s));
		    Saliences.put(s,salience);
		   // System.out.println(s + "  " + salience);
		}
	//System.out.println("Saliences");
	//System.out.println(Saliences);
	//System.out.println("..............");
	}
	public static void UpdatePolarities(String s,int polarity)
	{
		Double totalCount;
		Double positiveCount,negativeCount,newPolarity;
		if(Counts.containsKey(s))
		{
			totalCount = Counts.get(s);
		}
		else 
		{
			totalCount = (double) 0;
		}
		Counts.put(s, totalCount + 1);
			if (polarity > 0)
		{
			if(PositivePolarities.containsKey(s))
			{
				positiveCount = totalCount * PositivePolarities.get(s) + 1;
			}
			else positiveCount = (double) 1;
			totalCount++;
			newPolarity = positiveCount/totalCount;
			PositivePolarities.put(s,newPolarity);
			newPolarity = 1 - newPolarity;
			NegativePolarities.put(s,newPolarity);
		}
			else if (polarity == 0)
			{
				if(NegativePolarities.containsKey(s))
				{
					negativeCount = totalCount * NegativePolarities.get(s) + 1;
				}
				else negativeCount = (double) 1;
				totalCount++;
				newPolarity = negativeCount/totalCount;
				NegativePolarities.put(s,newPolarity);
				newPolarity = 1 - newPolarity;
				PositivePolarities.put(s,newPolarity);
			}
	}
}