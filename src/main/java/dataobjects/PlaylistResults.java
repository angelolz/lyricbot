package dataobjects;

import lombok.Getter;

import java.util.List;

@Getter
public class PlaylistResults
{
	@Getter
	public class VideoInfo
	{
		@Getter
		public class Snippet
		{
			@Getter
			public class ResourceId
			{
				private String videoId;
			}
			
			private String publishedAt;
			private String title;
			private String description;

			//for playlists
			private ResourceId resourceId;
		}
		
		private Snippet snippet;
	}

	private List<VideoInfo> items;
	private String nextPageToken = "";
	
	public VideoInfo getVideo(int index)
	{
		return items.get(index);
	}

	public void setNextPage(String nextPageToken)
	{
		this.nextPageToken = nextPageToken;
	}
}
