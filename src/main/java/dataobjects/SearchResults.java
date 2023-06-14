package dataobjects;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import dataobjects.SearchResults.VideoInfo.Snippet.ThumbnailList;
import lombok.Getter;

@Getter
public class SearchResults
{
	@Getter
	public static class PageInfo
	{
		private int totalResults;
		private int resultsPerPage;
	}

	@Getter
	public static class VideoInfo
	{
		@Getter
		public class Id
		{
			private String videoId;
		}

		@Getter
		public static class Snippet
		{
			@Getter
			public static class ThumbnailList
			{
				@Getter
				public static class Thumbnail
				{
					private String url = "";
				}
				
				@SerializedName("default")
				private Thumbnail defaultThumb;
				private Thumbnail medium;
				private Thumbnail high;
			}

			private String publishedAt;
			private String title;
			private String description;
			private String channelTitle;
			private ThumbnailList thumbnails;
		}

		@Getter
		public static class ContentDetails
		{
			private String duration;
		}

		private Id id;
		private Snippet snippet;
		private ContentDetails contentDetails;
	}

	private PageInfo pageInfo;
	private List<VideoInfo> items;
	
	public VideoInfo getVideo()
	{
		return items.get(0);
	}
	
	public ThumbnailList getThumbnails()
	{
		return items.get(0).snippet.thumbnails;
	}
}
