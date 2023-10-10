package fr.recia.grr.batch.synchronisation.repository.ldap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableLdapRepositories(basePackages = "fr.recia.grr.batch.synchronisation.repository.ldap.etablissement", ldapTemplateRef = "ldapTemplate1")
public class FirstLDAPConfig {

    @Value("${spring.ldap.urls}")
    private String ldapUrls;

    @Value("${spring.ldap.base_etablissements}")
    private String ldapBase;

    @Value("${spring.ldap.username}")
    private String ldapUsername;

    @Value("${spring.ldap.password}")
    private String ldapPassword;

    @Bean(name="contextSource1")
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(this.ldapUrls);
        contextSource.setUserDn(this.ldapUsername);
        contextSource.setPassword(this.ldapPassword);
        contextSource.setBase(this.ldapBase);
        return contextSource;
    }

    @Bean(name="ldapTemplate1")
    public LdapTemplate ldapTemplate(@Qualifier("contextSource1") LdapContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }

}