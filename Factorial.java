public class Factorial {
    public static void main(String[] args) {
    	int[] arr = new int[13];
    	for(int j=1; j < 13; j++){
          int i,fact=1;  
  			int number=j;//It is the number to calculate factorial    
			  for(i=1;i<=number;i++){    
			      fact=fact*i;

			  }  
			  arr[j-1] = fact;
			  System.out.print(fact + " ");

		}
		for(int i = 0; i < 12; i++){
			System.out.println();
			for(int j = 0; j < 11-i; j++){
				arr[j] = arr[j+1] - arr[j];
				System.out.print(arr[j] + " ");
			}
		}
    }
}