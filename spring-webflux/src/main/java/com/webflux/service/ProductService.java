package com.webflux.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.webflux.dto.ProductDTO;
import com.webflux.entity.Product;
import com.webflux.exception.CustomException;
import com.webflux.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	
	public Flux<Product> getAll(){
		return productRepository.findAll();
	}
	
	public Mono<Product> getProductById(int id){
		return productRepository.findById(id)
				.switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Product not found for id: " + id)));
	}
	
	public Mono<Product> save(ProductDTO productDTO){
		Mono<Boolean> existName = productRepository.findByName(productDTO.getName()).hasElement();
		return existName.flatMap(exists -> exists ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Product name: "+productDTO.getName() + " already in use"))
				:productRepository.save(Product.builder().name(productDTO.getName()).price(productDTO.getPrice()).build()));
		
	}
	
	public Mono<Product> update(int id,ProductDTO productDTO){
		Mono<Boolean> productId = productRepository.findById(id).hasElement();
		Mono<Boolean> productRepeateName = productRepository.repeateName(id, productDTO.getName()).hasElement();
		return productId.flatMap(existsId -> existsId ?
				productRepeateName.flatMap(existsName -> existsName ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Product name : "+ productDTO.getName()+ " already in use"))
						:productRepository.save(Product.builder().name(productDTO.getName()).price(productDTO.getPrice()).build()))
				:Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Product not found for id: " +id)));
		
	}
	
	public Mono<Void> delete(int id){
		Mono<Boolean> productId = productRepository.findById(id).hasElement();
		return productId.flatMap(exists -> exists ? productRepository.deleteById(id) : Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Product wit id :" + productId + "not exists")));
	}
}
