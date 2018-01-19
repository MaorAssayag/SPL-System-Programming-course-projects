package bgu.spl181.net.impl.Blockbuster;

public class test {
 public static void main(String[] args) {
	String[] array = {"REGISTER","maor","password","country="+'"'+"israel","ziland"+'"'};
	for (int i = 0; i < array.length; i++) {
		System.out.print(array[i]+" ");
	}
	System.out.println("");
 	String ans = "";
 	ans = array[3].substring(array[3].indexOf('"'));
 	for(int i = 4; i < array.length; i++) {
 		ans +=" " + array[i];
 	}
 	ans = ans.substring(ans.indexOf('"')+1, ans.indexOf('"', ans.indexOf('"')+1));
 	System.out.println(ans);

 	}
}
