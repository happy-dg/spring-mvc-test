package org.ksug.springcamp.testmvc.config;


import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

public class AppConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    private static final String DISPATCHER_SERVLET_MAPPING = "/";
    @Override

    protected String[] getServletMappings() {
        return new String[]{DISPATCHER_SERVLET_MAPPING};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppContext.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebAppContext.class};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("utf-8");

        return new Filter[] { characterEncodingFilter, new HiddenHttpMethodFilter() };
    }
}
