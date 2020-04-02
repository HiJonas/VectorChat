package Server;

import java.util.Comparator;

import Models.Message;

public class VectorComparator implements Comparator<String> {

	@Override
	public int compare(String a, String b) {
		String[] vectorA = getVector(a);
		String[] vectorB = getVector(b);
		if(vectorB.length > vectorA.length)return compareVectors(vectorB, vectorA);
		return compareVectors(vectorA, vectorB);
	}

	private int compareVectors(String[] vectorA, String[] vectorB) {
		
		boolean aLesserEqualB=true;
		boolean aLesserB=false;
		
		boolean bLesserEqualA=true;
		boolean bLesserA=false;
		
		for(int i = 0; i<vectorA.length; i++) {
			if(i >= vectorB.length) {
				if(Integer.parseInt(vectorA[i]) > 0)aLesserEqualB=false;
				if(Integer.parseInt(vectorA[i]) < 0)aLesserB=true;
				if(0 > Integer.parseInt(vectorA[i]))bLesserEqualA=false;
				if(0 < Integer.parseInt(vectorA[i]))bLesserA=true;
			}else {
				if(Integer.parseInt(vectorA[i]) > Integer.parseInt(vectorB[i]))aLesserEqualB=false;
				if(Integer.parseInt(vectorA[i]) < Integer.parseInt(vectorB[i]))aLesserB=true;
				if(Integer.parseInt(vectorB[i]) > Integer.parseInt(vectorA[i]))bLesserEqualA=false;
				if(Integer.parseInt(vectorB[i]) < Integer.parseInt(vectorA[i]))bLesserA=true;
			}		
		}
		
		if(aLesserEqualB && aLesserB) return -1;
		if(bLesserEqualA && bLesserA) return 1;
		return 0;
	}

	private String[] getVector(String string) {
		int start = string.indexOf("AT")+3;
		int end = string.indexOf(":");
		String vectorString = string.substring(start, end);
		return vectorString.split(",");
	}

}
