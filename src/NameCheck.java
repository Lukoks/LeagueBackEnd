import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NameCheck {

	public static String toUnicode(String name) throws IOException {
		String updName = "";
		for (int i =0; i<name.length(); i++) {
			if( (name.charAt(i) >= 'a' && name.charAt(i) <= 'z') || (name.charAt(i) >= 'A' && name.charAt(i) <= 'Z')) {
				updName = updName + name.charAt(i);
			}else if(name.charAt(i) == ' '){
				updName = updName +"%20";
			}
			else {
				String original = new String(name.charAt(i)+"");
		        try {
		            byte[] utf8Bytes = original.getBytes("UTF8");
		            String hex = printBytes(utf8Bytes);
		            hex = hex.replaceAll("0", "");
		            hex =  hex.replaceAll("x", "%");
		            updName = updName+hex;
		        }
		        catch (UnsupportedEncodingException e) {
		            e.printStackTrace();
		        }
				
			}
		}
		return updName;

	}
	
	  public static String printBytes(byte[] array) {
		  String s="";
	        for (int k = 0; k < array.length; k++) {
	        	s = s+("0x" + UnicodeFormatter.byteToHex(array[k]));
	        }
	        return s;
	    }


}
