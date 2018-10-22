package com.zf.config;

import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafView;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangfei on 2018/10/22/022.
 */
@Configuration
public class ThymeleafTemplateView   {

	/*@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.render(model, request, response);
		try{
			model.put("baseUrl", request.getContextPath());
		}catch (Exception e){

		}
	}*/

/*	@Resource
	private void configureThymeleafStaticVars(ThymeleafViewResolver viewResolver) {
		if(viewResolver != null) {
			viewResolver.getApplicationContext().
			Map<String, Object> vars = new HashMap<>();
			vars.put("baseUrl", "http://localhost:8083");
			viewResolver.setStaticVariables(vars);
		}
	}*/

}
