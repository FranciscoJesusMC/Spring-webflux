package com.webflux.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.webflux.entity.Product;

import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
	
	Mono<Product> findByName(String name);

	@Query("SELECT * FROM product WHERE id <> :id AND name = :name")
	Mono<Product> repeateName(int id, String name);
}
