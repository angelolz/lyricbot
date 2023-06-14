package dataobjects;

import lombok.Getter;

import java.util.List;

@Getter
public class Video
{
	@Getter
	public class VideoInfo
	{
		@Getter
		public class ContentDetails
		{
			private String duration;
		}

		private ContentDetails contentDetails;
	}
	private List<VideoInfo> items;

	private VideoInfo getVideo()
	{
		return items.get(0);
	}
}
