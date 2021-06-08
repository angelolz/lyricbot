package jsonObjects;

import java.util.List;

public class Video
{
	public class VideoInfo
	{
		public class ContentDetails
		{
			public String duration;
		}
		public ContentDetails contentDetails;
	}
	public List<VideoInfo> items;
	
	public VideoInfo getVideo()
	{
		return items.get(0);
	}
}
