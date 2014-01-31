
public class NativeAdd 
{
		public native double add(double n1, double n2);
		
		
		static
		{
			System.loadLibrary("JNI TEST");
			
			
			
		}
		
		public static void main(String[] args) 
		{
			NativeAdd q = new NativeAdd();
			double result = q.add(2.0, 3.0);
			System.out.println(result);
		}
		
		
		
		
}
