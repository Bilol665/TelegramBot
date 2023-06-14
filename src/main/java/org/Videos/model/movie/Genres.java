package org.Videos.model.movie;

import jdk.jfr.Threshold;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Threshold
@Accessors(chain = true)
public class Genres {
    private Integer id;
    private String title;
}