package com.toqqa.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class ListResponse<T> {
    
    private List<T> data;
    private String message; 
    public ListResponse(List<T> d,String msg){
    	this.data=d;
    	this.message=msg;
    }
}
