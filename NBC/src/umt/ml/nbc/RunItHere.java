package umt.ml.nbc;

import umt.ml.nbc.Test;


public class RunItHere {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double res=0;
		try {
			Test t=new Test("src/testFruit.csv",100);//The bin size can be changed
			res=t.testAccuracy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(res);
	}

}
