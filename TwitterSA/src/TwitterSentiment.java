import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class TwitterSentiment
{
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException
	{
		
		/*	String wordForm = "sad";
			RunWordnet w1 = new RunWordnet();
			@SuppressWarnings("rawtypes")
			ArrayList arr = new ArrayList();
			arr = w1.GetSynonyms(wordForm);
			System.out.println(arr);
			arr = w1.GetSynonyms("happy");
			System.out.println(arr);*/
		String labelledFile = "/home/tanmay/workspace/TwitterSA/data/4k_label_test.txt";
		BufferedReader br = new BufferedReader(new FileReader(new File(labelledFile)));
		ArrayList<Integer> trueLabels = new ArrayList<Integer>();
		String input = null,f=null,S;
		Double precision;
		int Polarity,correctPositiveLabels = 0 ,correctNegativeLabels = 0 
				,totalPositiveLabels = 0,totalNegativeLabels = 0;
		String[] parts = null;
		while ((input = br.readLine()) != null) {
			f = input;
			S = f.toLowerCase();
			parts = S.split("\\s+");
			Polarity = Integer.parseInt(parts[0]);
			trueLabels.add(Polarity);
			if (Polarity > 0)
			totalPositiveLabels++;
			else totalNegativeLabels++;
		}
		RunSA R = new RunSA();
		//System.out.println(R.assignedLabels);
		for (int i =0 ; i<trueLabels.size();i++)
		{
			if(trueLabels.get(i)==R.assignedLabels.get(i))
			{
				if(trueLabels.get(i)>0)  correctPositiveLabels++;
				else correctNegativeLabels++;
			}
		}
		System.out.println("Confusion Matrix :");
		System.out.println(correctPositiveLabels + "  " + (totalPositiveLabels - correctPositiveLabels));
		System.out.println((totalNegativeLabels - correctNegativeLabels)  + "  " + correctNegativeLabels);
		precision = (double) (correctPositiveLabels + correctNegativeLabels)/(totalPositiveLabels + totalNegativeLabels);
		System.out.println(precision);
	}
}