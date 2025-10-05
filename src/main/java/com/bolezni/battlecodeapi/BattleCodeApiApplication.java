package com.bolezni.battlecodeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.bolezni")
public class BattleCodeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BattleCodeApiApplication.class, args);
	}

}
