package com.webflux.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.dto.ProductDTO;
import com.webflux.entity.Product;
import com.webflux.service.ProductService;
import com.webflux.validation.ObjectValidator;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

	private final ProductService productService;
	
	private final ObjectValidator objectValidator;
	
	public Mono<ServerResponse> getAll(ServerRequest request){
		Flux<Product> products = productService.getAll();
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(products,Product.class);
	}
	
	
	public Mono<ServerResponse> getProductById(ServerRequest request){
		int id = Integer.valueOf(request.pathVariable("id"));
		Mono<Product> product =productService.getProductById(id);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(product,Product.class);
	}
	
	public Mono<ServerResponse> saveProduct(ServerRequest request){
		Mono<ProductDTO> product = request.bodyToMono(ProductDTO.class).doOnNext(objectValidator::validate);
		return product.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productService.save(p),Product.class));
		
	}
	
	public Mono<ServerResponse> updateProduct(ServerRequest request){
		int id = Integer.valueOf(request.pathVariable("id"));
		Mono<ProductDTO> product = request.bodyToMono(ProductDTO.class).doOnNext(objectValidator::validate);
		return product.flatMap(p -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productService.update(id, p),Product.class));
	}
	
	public Mono<ServerResponse> delete(ServerRequest request){
		int id = Integer.valueOf(request.pathVariable("id"));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productService.delete(id),Product.class);
	}
}
