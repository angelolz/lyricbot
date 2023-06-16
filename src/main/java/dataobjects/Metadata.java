package dataobjects;

import lombok.Getter;

@Getter
public class Metadata
{
    @Getter
    public class Meta
    {
        private String title;
    }

    private Meta meta;
}
