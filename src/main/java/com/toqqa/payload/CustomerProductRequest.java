package com.toqqa.payload;

import java.util.List;

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
	
//	@NotNull
	private List<String> productCategoryIds;
	
	private Boolean showBulkProducts=false;
	
}
