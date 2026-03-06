package com.nantian.nbp.main.hot.reload;

import com.nantian.nbp.flow.engine.service.api.Atom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Service
public class HotReloadSpringBeanService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HotReloadSpringBeanService.class);
    
    private final ApplicationContext applicationContext;

    public HotReloadSpringBeanService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 热加载指定JAR中的Bean
     * @param jarFile JAR文件
     */
    public void reloadBeanFromJar(File jarFile) {
        if(Objects.isNull(jarFile)){
            return;
        }
        try (URLClassLoader newClassLoader = createClassLoader(jarFile)){
            Set<String> classNames = getAllClassNamesFromJar(jarFile);
            for (String className : classNames) {
                Class<?> newBeanClass = newClassLoader.loadClass(className);
                Atom annotation = newBeanClass.getAnnotation(Atom.class);
                if (Objects.isNull(annotation)) {
                    LOGGER.warn("Can't to reload class without @Atom [{}]", newBeanClass);
                    continue;
                }
                String beanName = annotation.value();
                if(!StringUtils.hasText(beanName)){
                    LOGGER.error("Can't to reload class without beanName [{}]", newBeanClass);
                    continue;
                }
                replaceExistingBean(beanName, newBeanClass);
                LOGGER.info("Successfully reloaded bean [{}]", beanName);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to reload jarPath [{}]", jarFile, e);
        }
    }

    private Set<String> getAllClassNamesFromJar(File file) {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".class") && !entry.isDirectory()) {
                    String className = entryName.replace('/', '.')
                            .substring(0, entryName.length() - 6);
                    classNames.add(className);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to read jar file [{}]", file, e);
        }
        return classNames;
    }

    private URLClassLoader createClassLoader(File jarFile) throws MalformedURLException {
        return new URLClassLoader(
                new URL[]{jarFile.toURI().toURL()},
                // 父加载器
                Thread.currentThread().getContextClassLoader()
        );
    }
    
    private void replaceExistingBean(String beanName, Class<?> beanClass) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        if(beanFactory.containsBeanDefinition(beanName)){
            beanFactory.removeBeanDefinition(beanName);
        }
        // 注册新的Bean定义
        BeanDefinitionBuilder builder =
            BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        // 设置Bean为单例
        builder.setScope(BeanDefinition.SCOPE_SINGLETON);
        builder.setLazyInit(Boolean.TRUE);
        // 注册新的Bean定义
        beanFactory.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }
}
