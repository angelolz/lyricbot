package jsonObjects;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import jsonObjects.PlaylistResults.VideoInfo.Snippet.ThumbnailList;

public class PlaylistResults
{
	public class PageInfo
	{
		public int totalResults, resultsPerPage;
	}
	
	public class VideoInfo
	{
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
				
				//for playlists??
				public Thumbnail standard, maxres;
			}
			
			public class ResourceId
			{
				public String videoId;
			}
			
			public String publishedAt, title, description, channelTitle;
			public ThumbnailList thumbnails;
			
			//for playlists
			public ResourceId resourceId;
			public String videoOwnerChannelTitle, videoOwnerChannelId;
		}
		
		public String id;
		public Snippet snippet;
		
	}
	
	public PageInfo pageInfo;
	public List<VideoInfo> items;
	public String nextPageToken = "";
	
	public VideoInfo getVideo(int index)
	{
		return items.get(index);
	}
	
	public ThumbnailList getThumbnails(int index)
	{
		return items.get(index).snippet.thumbnails;
	}
	
	public String getNextPage()
	{
		return nextPageToken;
	}
	
	public void setNextPage(String nextPageToken)
	{
		this.nextPageToken = nextPageToken;
	}
}
