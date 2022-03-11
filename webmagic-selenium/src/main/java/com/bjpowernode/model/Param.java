package com.bjpowernode.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Param {
    private String dir;
    private String out;
    private String referer;
}
