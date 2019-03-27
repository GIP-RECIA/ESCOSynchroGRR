package fr.recia.grr.utils;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class RegroupementEtablissementLoader {
    public static Properties load(ResourceLoader resourceLoader) throws IOException {
         return PropertiesLoaderUtils.loadProperties(resourceLoader.getResource("classpath:regroupementEtablissements.properties"));
    }

    private RegroupementEtablissementLoader() {
    }

    public static Optional<String> loadPrincipal(ResourceLoader resourceLoader, String codeSecondaire) throws IOException {
        Properties load = load(resourceLoader);
        String uaiPrincipal = load.getProperty(codeSecondaire+".principal");
        return Optional.ofNullable(uaiPrincipal);
    }
}
