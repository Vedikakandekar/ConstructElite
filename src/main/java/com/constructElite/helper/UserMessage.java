package com.constructElite.helper;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMessage {


    @Setter
    @Getter
    private String content;
    private String type;


}
