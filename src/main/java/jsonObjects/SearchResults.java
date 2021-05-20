package jsonObjects;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import jsonObjects.SearchResults.VideoInfo.Snippet.ThumbnailList;

public class SearchResults
{
	public class PageInfo
	{
		public int totalResults, resultsPerPage;
	}
	
	public class VideoInfo
	{
		public class Id
		{
			public String videoId;
		}
		
		public class Snippet
		{
			public class ThumbnailList
			{
				public class Thumbnail
				{
					public String url = "";
				}
				
				@SerializedName("default")
				public Thumbnail defaultThumb;
				
				public Thumbnail medium, high;
			}
			
			public String publishedAt, title, description, channelTitle;
			
			public ThumbnailList thumbnails;
		}
		
		public Id id;
		public Snippet snippet;
		
	}
	
	public PageInfo pageInfo;
	public List<VideoInfo> items;
	
	public VideoInfo getVideo()
	{
		return items.get(0);
	}
	
	public ThumbnailList getThumbnails()
	{
		return items.get(0).snippet.thumbnails;
	}
}
