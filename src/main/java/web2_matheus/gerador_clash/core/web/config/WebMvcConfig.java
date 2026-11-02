package web2_matheus.gerador_clash.core.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import web2_matheus.gerador_clash.core.web.interceptor.NotificacaoHeaderInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private NotificacaoHeaderInterceptor notificacaoInterceptor;

    public WebMvcConfig(NotificacaoHeaderInterceptor notificacaoInterceptor) {
        this.notificacaoInterceptor = notificacaoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(notificacaoInterceptor);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
}
