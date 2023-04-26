package com.webflux.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

	@NotBlank(message = "name is required")
	private String name;
	
	@Min(value = 1,message = "price must be greater than zero")
	private float price;
}
