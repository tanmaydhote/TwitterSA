import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
public class RunSA
{
	public static LinkedHashMap<String, Double> PositivePolarities = new LinkedHashMap<String , Double>();
	public static LinkedHashMap<String, Double> NegativePolarities = new LinkedHashMap<String , Double>();
	public static LinkedHashMap<String, Double> Counts = new LinkedHashMap<String , Double>();
	public static ArrayList<Integer> assignedLabels = new ArrayList<Integer>();
	static Training T = new Training();
	static RunWordnet w1 = new RunWordnet();
	public static int miss = 0;
	public RunSA() throws IOException
	{
		System.out.println("Analysis");
		Training();
		Testing();
	}
	@SuppressWarnings("static-access")
	public static void Training() throws IOException
	{	
		T.findPolarities();
		System.out.println("Training Over");
	}
	@SuppressWarnings("static-access")
	public static void Testing() throws IOException
	{
		String testingFile = "/home/tanmay/workspace/TwitterSA/data/4k_unlabeled.txt";
		BufferedReader br = new BufferedReader(new FileReader(new File(testingFile)));
		String S=null;
		LinkedHashMap<String, String> PoStags = new LinkedHashMap<String , String>();
		String S1="";
		String[] parts1;
		Scanner input1 = new Scanner(new File("/home/tanmay/workspace/TwitterSA/data/out1.txt"));
		int negationCount = 0,unigramCount=0;
		String input = null,f=null;
		String acronymS = null, acronymf = null;
		String[] parts = null;
		String[] acronymParts = null;
		boolean negationFlag = false,thoughMidFlag,thoughStartFlag;
		boolean butFlag = false;
		ArrayList<String> unigrams = new ArrayList<String>();
		ArrayList<Boolean> negationFlags = new ArrayList<Boolean>();
		Double PositivePolarity,NegativePolarity;
		String Part1Polarity,Part2Polarity;
		Double part1PositivePolarity,part2PositivePolarity,part1NegativePolarity,part2NegativePolarity;
		while ((input = br.readLine()) != null) {
			while(input1.hasNext()){
				if(input1.hasNext())
				S1 = input1.nextLine();
				if(input1.hasNext())
				{	S1 = input1.nextLine();
				//System.out.println(S1);
				//System.out.println(S);
				//System.out.println("hi1");
				if(S1.isEmpty()){
					//System.out.println("hi");
					break;
				}
				parts1 = S1.split("\\s+");
				PoStags.put(parts1[0], parts1[1]); }
			}			
	//		System.out.println(PoStags);
			
			unigramCount = 0;
			PositivePolarity = (double) 0;
			NegativePolarity = (double) 0;
			unigrams.clear();
			negationFlags.clear();
			f = input;
			S = f.toLowerCase();
			parts = S.split("\\s+");
			for(int i = 0;i<parts.length;i++)
			{
				if(!parts[i].substring(0,1).equals("@"))
				{
					parts[i] = parts[i].replaceAll("[^a-zA-Z ]", "");
				if(!T.stopWords.contains(parts[i])&& parts[i].length()>1)
					{
				//	if(!T.acronyms.containsKey(parts[i]))
				//	{
						unigrams.add(parts[i]);
						negationFlags.add(negationFlag);
						negationFlag = false;
						if(parts[i].equals("not") || parts[i].equals("no") ) 
							{
								negationFlag = true;
								if(unigramCount!=0) negationFlags.set(unigramCount-1, true);
							}
						unigramCount++;
				//	}		
							
				/*	else		{
								
									acronymf = T.acronyms.get(parts[i]);
									acronymS = f.toLowerCase();
									acronymParts = acronymS.split("\\s+");
									for(int acronymi = 0; acronymi< acronymParts.length;acronymi++)
									{
										unigrams.add(acronymParts[acronymi]);
										negationFlags.add(negationFlag);
										negationFlag = false;
										if(acronymParts[acronymi].equals("not") || acronymParts[acronymi].equals("no") ) 
											{
												negationFlag = true;
												if(unigramCount!=0) negationFlags.set(unigramCount-1, true);
											}
										unigramCount++;
									}
								} */
							
					}
				}
			}
			System.out.println(unigrams);
			int currentUnigramCount = 0;
			part1PositivePolarity = (double) 0;
			part1NegativePolarity = (double) 0;
			part2PositivePolarity = (double) 0;
			part2NegativePolarity = (double) 0;
			butFlag = false;
			thoughMidFlag = false;
			thoughStartFlag = false;
			for (String s : unigrams)
			{
				if((s.equals("though") || s.equals("although")) && currentUnigramCount > 2) thoughMidFlag = true;
				if((s.equals("though") || s.equals("although")) && currentUnigramCount < 2) thoughMidFlag = true;
				if(s.equals("but")) butFlag = true;
			//	if(PoStags.containsKey(s))
			//	{
			//	if(PoStags.get(s).equals("A") || PoStags.get(s).equals("R") )
			//	{
				if(!negationFlags.get(currentUnigramCount))
				{
					PositivePolarity += getPositivePolarity(s);
					NegativePolarity += getNegativePolarity(s);
			/*		if(currentUnigramCount<unigramCount/2)
					{
						part1PositivePolarity += getPositivePolarity(s);
						part1NegativePolarity += getNegativePolarity(s);
					}
					else
					{
						part2PositivePolarity += getPositivePolarity(s);
						part2NegativePolarity += getNegativePolarity(s);;
					}*/
				}
				else
				{
					PositivePolarity += getNegativePolarity(s);
					NegativePolarity += getPositivePolarity(s);
			/*		if(currentUnigramCount<unigramCount/2)
					{
						part1PositivePolarity += getNegativePolarity(s);
						part1NegativePolarity += getPositivePolarity(s);
					}
					else
					{
						part2PositivePolarity += getNegativePolarity(s);
						part2NegativePolarity += getPositivePolarity(s);;
					} */
				}
			//	}
			//}
				currentUnigramCount++;
			}
			//System.out.println("+ve = " + PositivePolarity + " -ve = " + NegativePolarity);
			//System.out.println(S); 
			if(part1PositivePolarity<part1NegativePolarity) Part1Polarity = "Negative";
			else Part1Polarity = "Positive";
			if(part2PositivePolarity<part2NegativePolarity) Part2Polarity = "Negative";
			else Part2Polarity = "Positive";
			
			//if(thoughMidFlag)
				//{
			//	if (Part1Polarity.equals("Negative")) assignedLabels.add(0);
			//	else assignedLabels.add(4);
			//	}
			
		//	if(butFlag || thoughStartFlag)
			//{
		//	if (Part2Polarity.equals("Negative")) assignedLabels.add(0);
		//	else assignedLabels.add(4);
		//	}
		//	else
		//	{
				if (PositivePolarity<NegativePolarity)
				{
	//				System.out.println("Negative");
					assignedLabels.add(0);
				}
				else 
				{  
	//				System.out.println("Positive");
					assignedLabels.add(4);
				}
		//	}
		}
	//	System.out.println(negationCount);
	//	System.out.println(PoStags);
	//	System.out.println(miss);
	}
	@SuppressWarnings("static-access")
	public static double getPositivePolarity(String s) throws IOException
	{
		int synonymCount = 0;
		Double Polarity = (double) 0;
		ArrayList<String> synonyms = new ArrayList<String>();
		if(T.PositivePolarities.containsKey(s))
		{
			if(T.Saliences.get(s)>0.0)
			Polarity = T.PositivePolarities.get(s);			
		}
			
		else
		{
			miss++;
			/*synonyms = w1.GetSynonyms(s);
			for (String s1 : synonyms)
			{
				if(T.PositivePolarities.containsKey(s1))
				{   
					if(T.Saliences.get(s1)>0.0)
					{
					Polarity +=T.PositivePolarities.get(s1);
					synonymCount++;
					}
				}
			}
			if(synonymCount>0)
			{
				Polarity = Polarity/synonymCount;
				T.PositivePolarities.put(s, Polarity);
				T.Saliences.put(s,0.5);
			}
			else miss++;*/
		}
			return Polarity;   
	}
	@SuppressWarnings("static-access")
	public static double getNegativePolarity(String s) throws IOException
	{
		int synonymCount = 0;
		Double Polarity = (double) 0;
		ArrayList<String> synonyms = new ArrayList<String>();
		if(T.NegativePolarities.containsKey(s))
		{
			if(T.Saliences.get(s)>0.0)
			Polarity = T.NegativePolarities.get(s);
		}
			else
		{
		/*	miss++;
			synonyms = w1.GetSynonyms(s);
			for (String s1 : synonyms)
			{
				if(T.NegativePolarities.containsKey(s1))
				{
					if(T.Saliences.get(s1)>0.3){
					Polarity +=T.NegativePolarities.get(s1);
					synonymCount++;
					}
				}
			}
			if(synonymCount>0)
			{
				Polarity = Polarity/synonymCount;
				T.NegativePolarities.put(s, Polarity);
				T.Saliences.put(s,0.5);
			}
				else miss++;*/ 
		}
			return Polarity;
	}
}