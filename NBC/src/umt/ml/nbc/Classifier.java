package umt.ml.nbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
/**
 * 
 * @author Zhong Ziyue
 *
 * @email zhongzy@strongit.com.cn
 * 
 * Feb 14, 2016
 */
public class Classifier {
	private double[][] trainingData;
	private int[] trainingClass;
	private String dataPath;
	private Map<String,Integer> classMap;
	private Map<Integer,String> mapToClass;
	private double[] max;
	private double[] min;
	private double[] binSize;
	private int numOfBins;
	private int[][][] bin;
	private int[] total;
	/**
	 * Constructor
	 * @param path: The path of csv file
	 * @param numOfBins: Number of Bins
	 */
	public Classifier(String path,int numOfBins){
		this.dataPath=path;
		this.numOfBins=numOfBins;
		trainingData=new double[1000][4];
		trainingClass=new int[1000];
		binSize=new double[4];
		bin=new int[4][4][numOfBins];
		total=new int[4];
		this.classMap=new HashMap<String,Integer>();
		this.mapToClass=new HashMap<Integer,String>();
		max=new double[4];
		min=new double[4];
		Arrays.fill(max,Double.MIN_VALUE);
		Arrays.fill(min,Double.MAX_VALUE);
		classMap.put("apple",0);
		classMap.put("orange",1);
		classMap.put("lemon",2);
		classMap.put("peach", 3);
		mapToClass.put(0,"apple");
		mapToClass.put(1,"orange");
		mapToClass.put(2,"lemon");
		mapToClass.put(3,"peach");
	}
	/**
	 * This function helps with reading in the fruit data csv file
	 * Will also get the max and min value for each dimension while loading the data
	 * This function set the bin size for each dimension
	 * @throws IOException: file I/O exceptions
	 */
	public void loadData() throws IOException{
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
				trainingData[index][i]=Double.valueOf(st.nextToken());
				max[i]=Math.max(max[i],trainingData[index][i]);
				min[i]=Math.min(min[i],trainingData[index][i]);
			}
			trainingClass[index]=classMap.get(st.nextToken());
			total[trainingClass[index]]++;
			index++;
		}
		br.close();
		//calculate the bin size according to the number of bins and the range of attributes
		for(int i=0;i<4;i++){
			binSize[i]=(max[i]-min[i])/this.numOfBins;
		}
	}
	/**
	 * This function for bining
	 */
	public void bining(){
		for(int i=0;i<trainingData.length;i++){
			for(int j=0;j<4;j++){
				//calculate how many samples in a specific bin
				//for those samples go outside of the range will go to the first or the last bin
				int binId=getBinId(trainingData[i][j],j);
				bin[j][trainingClass[i]][binId]++;
			}
		}
	}
	/**
	 * 
	 * @param attr: the value of a attribute of the fruit
	 * @param attrId: which attribute
	 * @return which bin this sample should go
	 */
	private int getBinId(double attr,int attrId){
		double remap=attr-min[attrId];
		int index=1;
		for(;index<=numOfBins;index++){
			if(remap<=index*binSize[attrId]) break;
		}
		return index-1;
	}
	/**
	 * Returns the classify result on a specific dimension
	 * @param attr: attribute value
	 * @param attrId: which attribute
	 * @return: the id of the class
	 */
	private int getClassOnDim(double attr,int attrId){
		int[] arr=new int[4];
		for(int i=0;i<4;i++){
			arr[i]=bin[attrId][i][getBinId(attr,attrId)];
		}
		return maxIdx(arr[0],arr[1],arr[2],arr[3]);
	}
	/**
	 * Returns the result based on all dimensions
	 * @param attr0: redness
	 * @param attr1: yellowness
	 * @param attr2: mass
	 * @param attr3: vloume
	 * @return: the name of class
	 */
	public String classify(double attr0,double attr1,double attr2,double attr3){
		int[] arr=new int[4];
		arr[getClassOnDim(attr0,0)]++;
		arr[getClassOnDim(attr1,1)]++;
		arr[getClassOnDim(attr2,2)]++;
		arr[getClassOnDim(attr3,3)]++;
		return mapToClass.get(maxIdx(arr[0],arr[1],arr[2],arr[3]));
	}
	/**
	 * Return the index of maximum number among the four input
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return: maximum index of input
	 */
	private int maxIdx(int a,int b,int c,int d){
		int[] arr={a,b,c,d};
		int max=Math.max(a,Math.max(b,Math.max(c,d)));
		for(int i=0;i<4;i++){
			if(arr[i]==max) return i;
		}
		return 0;
	}
}
