package com.zf.config;

import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangfei on 2018/10/22/022.
 */
@Configuration
public class ThymeleafTemplateView {

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
			viewResolver.
		}
	}*/

}
