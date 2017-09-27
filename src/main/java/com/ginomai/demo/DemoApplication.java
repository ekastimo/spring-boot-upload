package com.ginomai.demo;

import com.ginomai.demo.filemanager.models.StorageProperties;
import com.ginomai.demo.filemanager.IFileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(IFileService fileService) {
		return (args) -> {
			fileService.deleteAll();
			fileService.init();
		};
	}
}
