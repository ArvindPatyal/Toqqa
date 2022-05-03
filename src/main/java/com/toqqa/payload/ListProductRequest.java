package com.toqqa.payload;

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
public class ListProductRequest extends PaginationBo {

	@NotNull
	private Boolean isInActive;
}
