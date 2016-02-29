package umt.ml.nbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;
/**
 * 
 * @author Zhong Ziyue
 *
 * @email zhongzy@strongit.com.cn
 * 
 * Feb 21, 2016
 */
public class Test {
	private double[][] testData;
	private String[] classLabel;
	private String dataPath;
	private Classifier c;
	public Test(String path,int numOfBins){
		this.testData=new double[100][4];
		this.classLabel=new String[100];
		this.dataPath=path;
		this.c=new Classifier("src/fruit.csv",numOfBins);
	}
	/**
	 * Load the test data into memory
	 * @throws Exception
	 */
	public void loadData() throws Exception{
		File csvData= new File(this.dataPath);
		BufferedReader br=new BufferedReader(new FileReader(csvData));
		String line="";
		int index=-1;
		while((line=br.readLine())!=null){
			if(index==-1){
				index++;continue;
			}
			StringTokenizer st=new StringTokenizer(line,",");
			for(int i=0;i<=3;i++){
				testData[index][i]=Double.valueOf(st.nextToken());
			}
			classLabel[index]=st.nextToken();
			index++;
		}
		br.close();
	}
	/**
	 * Returns the accuracy based on the result of classifier
	 * @return
	 * @throws Exception
	 */
	public double testAccuracy() throws Exception{
		c.loadData();
		c.bining();
		this.loadData();
		int count=0;
		for(int i=0;i<100;i++){
			String res=c.classify(testData[i][0], testData[i][1], testData[i][2], testData[i][3]);
			if(res.equals(this.classLabel[i])) count++;
		}
		return count/100.0;
	}
}
