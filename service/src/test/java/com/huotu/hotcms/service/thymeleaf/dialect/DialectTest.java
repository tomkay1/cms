package com.huotu.hotcms.service.thymeleaf.dialect;

import com.huotu.hotcms.service.TestBase;
import com.huotu.hotcms.service.config.ServiceTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 方言测试
 * <p>
 * 大致流程应该是,构建一个支持该方言的处理器,并且让当前{@link org.thymeleaf.spring4.view.ThymeleafViewResolver}支持它
 *
 * @author CJ
 */
@ContextConfiguration(classes = {ServiceTestConfig.class, DialectTest.DialectTestConfig.class})
//@PropertySource()
public abstract class DialectTest extends TestBase {

    private static final String URI = "/DialectTest";

//    @Controller
//    static class TestController{
//        @RequestMap
//        public String
//    }

    private static String TemplateName;

    abstract String templateName();

    ResultActions perform() throws Exception {
        TemplateName = templateName();
        return mockMvc.perform(get(URI)).andExpect(status().isOk());
    }

    @EnableWebMvc
    static class DialectTestConfig extends WebMvcConfigurerAdapter {

        @Autowired
        private Set<IDialect> allDialects;

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            super.addViewControllers(registry);
            registry.addViewController(URI).setViewName("this");
        }

        @Bean
        public ThymeleafViewResolver widgetViewResolver() {
            ThymeleafViewResolver resolver = new ThymeleafViewResolver();

            SpringTemplateEngine engine = new SpringTemplateEngine();

            engine.setTemplateResolver(new ClassLoaderTemplateResolver() {
                @Override
                protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration
                        , String ownerTemplate, String template, String resourceName, String characterEncoding
                        , Map<String, Object> templateResolutionAttributes) {
                    if ("this".equals(resourceName)) {
                        String newName = "com/huotu/hotcms/service/thymeleaf/dialect/" + TemplateName;
                        return super.computeTemplateResource(configuration, ownerTemplate, template, newName
                                , characterEncoding, templateResolutionAttributes);
                    }
                    return super.computeTemplateResource(configuration, ownerTemplate, template, resourceName
                            , characterEncoding, templateResolutionAttributes);
                }
            });

//            List<AbstractProcessorDialect> list = CMSDialect.getDialectList();
            allDialects.forEach(engine::addDialect);

            resolver.setTemplateEngine(engine);

            resolver.setOrder(Integer.MIN_VALUE);

//        resolver.setViewNames(ArrayUtil.array("*.shtml"));
//        resolver.setCharacterEncoding(UTF8);
//        resolver.setTemplateEngine(templateEngine(widgetTemplateResolver()));
//        resolver.setApplicationContext(applicationContext);
            return resolver;
        }
    }

}
