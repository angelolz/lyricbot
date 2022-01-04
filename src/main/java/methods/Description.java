package methods;

public class Description 
{
	public static String shorten(String desc)
	{
		if(desc.length() > 400)
			return desc.substring(0, 397).concat("...");

		else
			return desc;
	}
}
