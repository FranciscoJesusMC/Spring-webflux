package com.webflux.router;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.handler.ProductHandler;

@Configuration
public class ProductRouter {

	
	private static final String PATH = "api/product";
	
	
	@Bean
	public WebProperties.Resources Resources(){
		return new WebProperties.Resources();
	}

	@Bean
	RouterFunction<ServerResponse> router(ProductHandler handler){
		return RouterFunctions.route()
				.GET(PATH, handler::getAll)
				.GET(PATH +"/{id}",handler::getProductById)
				.POST(PATH, handler::saveProduct)
				.PUT(PATH +"/{id}",handler::updateProduct)
				.DELETE(PATH + "/{id}",handler::delete)
				.build();
	}
}
