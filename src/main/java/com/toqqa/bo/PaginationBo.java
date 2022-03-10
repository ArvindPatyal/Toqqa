package com.toqqa.bo;


import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginationBo {

	@NotNull
	private Integer pageNumber = 0;
	@NotNull
	private Boolean byUserflag=false;
}
