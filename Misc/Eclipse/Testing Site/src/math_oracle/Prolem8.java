package math_oracle;

import java.math.BigInteger;
import java.util.Stack;
import java.util.ArrayList;

public class Prolem8
{
	public static void main(String[] args)
	{
		int max_minus = sum(new BigInteger("98999999999999999999"));
		BigInteger big = new BigInteger("99887766554433221100"); 
		Stack<BigInteger> stack = new Stack<BigInteger>();
		stack.push(big);
		
		ArrayList<ArrayList<Integer>> l = new ArrayList<ArrayList<Integer>>();
		Class c = l.getClass();
		
		while(true)
		{
			big = stack.pop();
			int[] res = max_min(big,max_minus);
			
			for(int i = res[0];i <= res[1];i++)
			{
				BigInteger base = big.subtract(new BigInteger(new Integer(i).toString()));
				BigInteger base2 = base.add(new BigInteger(new Integer(sum(base)).toString()));
				
				if(base2.compareTo(big) == 0)
					stack.push(base);
			}
			
			if(stack.isEmpty())
				break;
		}
		
		return;
	}
	
	public static int sum(BigInteger i)
	{
		String digits = i.toString(10);
		
		int sum = 0;
		
		for(int j = 0;j < digits.length();j++)
			sum += digits.charAt(j) - '0';
		
		return sum;
	}
	
	public static int[] max_min(BigInteger big, int max_minus)
	{
		int[] digits = new int[max_minus];
		int min = 0x7FFFFFFF;
		int max = 0;
		
		for(int i = 1;i <= max_minus;i++)
			digits[i-1] = sum(big.subtract(new BigInteger(new Integer(i).toString())));
		
		for(int i = 0;i < digits.length;i++)
			if(digits[i] > max)
				max = digits[i];
			else if(digits[i] < min)
				min = digits[i];
		
		int[] ret = new int[2];
		ret[0] = min;
		ret[1] = max;
		
		return ret;
	}
}
