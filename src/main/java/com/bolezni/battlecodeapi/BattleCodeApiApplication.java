package com.bolezni.battlecodeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.bolezni")
@EnableJpaRepositories(basePackages = "com.bolezni.store.repository")
@EntityScan(basePackages = "com.bolezni.store.entity")
public class BattleCodeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BattleCodeApiApplication.class, args);
	}

}
