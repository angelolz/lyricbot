package dataobjects;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Lyricer
{
    long userId;
    String link;
    boolean banned;
}
