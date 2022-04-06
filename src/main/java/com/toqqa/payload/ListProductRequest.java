package com.toqqa.payload;

import javax.validation.constraints.NotNull;

import com.toqqa.bo.PaginationBo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListProductRequest extends PaginationBo {

	@NotNull
	private Boolean isInActive;
}
