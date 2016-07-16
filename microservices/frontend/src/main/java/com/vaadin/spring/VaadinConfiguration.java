package com.vaadin.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.internal.DefaultViewCache;
import com.vaadin.spring.internal.UIScopeImpl;
import com.vaadin.spring.internal.VaadinSessionScope;
import com.vaadin.spring.internal.ViewCache;
import com.vaadin.spring.internal.ViewScopeImpl;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.spring.server.SpringApplicationContext;

/**
 * Spring configuration for registering the custom Vaadin scopes, the
 * {@link SpringViewProvider view provider} and some other stuff.
 *
 * Instead of using this class directly, it is recommended to add the
 * {@link EnableVaadin} annotation to a configuration class to automatically
 * import {@link VaadinConfiguration}.
 *
 * @author Josh Long (josh@joshlong.com)
 * @author Petter Holmström (petter@vaadin.com)
 * @author Gert-Jan Timmer (gjr.timmer@gmail.com)
 */
@Configuration
public class VaadinConfiguration implements ApplicationContextAware,
BeanDefinitionRegistryPostProcessor {

    private ApplicationContext applicationContext;
    private BeanDefinitionRegistry beanDefinitionRegistry;

    @Bean
    static VaadinSessionScope vaadinSessionScope() {
        return new VaadinSessionScope();
    }

    @Bean
    static UIScopeImpl uIScope() {
        return new UIScopeImpl();
    }

    @Bean
    static ViewScopeImpl viewScope() {
        return new ViewScopeImpl();
    }

    @Bean
    SpringViewProvider viewProvider() {
        SpringApplicationContext.setApplicationContext(applicationContext);
        return new SpringViewProvider(beanDefinitionRegistry);
    }

    @Bean
    @com.vaadin.spring.annotation.UIScope
    ViewCache viewCache() {
        return new DefaultViewCache();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(
            BeanDefinitionRegistry registry) throws BeansException {
        beanDefinitionRegistry = registry;
    }

    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // NOP
    }

}

