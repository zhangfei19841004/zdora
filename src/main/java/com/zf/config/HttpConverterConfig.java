package com.zf.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
//@EnableWebMvc
public class HttpConverterConfig implements WebMvcConfigurer {

	public List<HttpMessageConverter<?>> httpMessageConverters() {
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		List<MediaType> stringMediaTypes = new ArrayList<>();
		stringMediaTypes.add(MediaType.TEXT_PLAIN);
		stringMediaTypes.add(MediaType.TEXT_HTML);
		stringMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		stringConverter.setSupportedMediaTypes(stringMediaTypes);
		// 1.定义一个converters转换消息的对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		// 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setCharset(StandardCharsets.UTF_8);
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue);
		List<MediaType> fastMediaTypes = new ArrayList<>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastMediaTypes.add(MediaType.TEXT_HTML);
		fastMediaTypes.add(MediaType.TEXT_PLAIN);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);
		// 3.在converter中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);
		// 4.将converter赋值给HttpMessageConverter
		HttpMessageConverter<?> converter = fastConverter;
		// 5.返回HttpMessageConverters对象
		List<HttpMessageConverter<?>> list = new ArrayList<>();
		list.add(stringConverter);
		list.add(converter);
		return list;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		ByteArrayHttpMessageConverter byteMessage = new ByteArrayHttpMessageConverter();
		converters.add(byteMessage);
		converters.addAll(this.httpMessageConverters());
	}

}