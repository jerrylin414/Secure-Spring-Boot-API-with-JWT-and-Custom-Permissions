package com.lzy.jshow.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigProperties {
	@Value("${tokenSign}")
	private String tokenSign;
}
