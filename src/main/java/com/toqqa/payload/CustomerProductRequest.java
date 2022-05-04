package com.toqqa.payload;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.toqqa.bo.PaginationBo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductRequest extends PaginationBo{
	
	@NotNull
	private List<String> productCategoryId;	
	
}
