package com.toqqa.payload;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.toqqa.bo.PaginationBo;
import com.toqqa.constants.OrderConstants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToggleOrdersStatus extends PaginationBo {

	@NotNull
	private List<OrderConstants> orderStatus;
}
