package jsonObjects;

import java.util.List;

public class PlaylistResults
{
	public class VideoInfo
	{
		public class Snippet
		{
			public class ResourceId
			{
				public String videoId;
			}
			
			public String publishedAt, title, description;

			//for playlists
			public ResourceId resourceId;
		}
		
		public Snippet snippet;
	}
	
	public List<VideoInfo> items;
	public String nextPageToken = "";
	
	public VideoInfo getVideo(int index)
	{
		return items.get(index);
	}
	

	public void setNextPage(String nextPageToken)
	{
		this.nextPageToken = nextPageToken;
	}
}
