package com.bolezni.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SecurityAuthConfig.class)
public class SecurityCourseConfig {
}
