package com.StorageService.StorageApp;

import org.springframework.boot.SpringApplication;

public class TestStorageAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(StorageAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
