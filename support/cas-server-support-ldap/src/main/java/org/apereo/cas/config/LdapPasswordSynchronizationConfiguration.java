package org.apereo.cas.config;

import org.apereo.cas.authentication.AuthenticationEventExecutionPlanConfigurer;
import org.apereo.cas.authentication.LdapPasswordSynchronizationAuthenticationPostProcessor;
import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.configuration.model.core.authentication.PasswordSynchronizationProperties;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is {@link LdapPasswordSynchronizationConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 6.1.0
 */
@Configuration("ldapPasswordSynchronizationConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
@Slf4j
public class LdapPasswordSynchronizationConfiguration {
    @Autowired
    private CasConfigurationProperties casProperties;

    @ConditionalOnMissingBean(name = "ldapPasswordSynchronizationAuthenticationEventExecutionPlanConfigurer")
    @Bean
    public AuthenticationEventExecutionPlanConfigurer ldapPasswordSynchronizationAuthenticationEventExecutionPlanConfigurer() {
        return plan -> {
            val ldap = casProperties.getAuthn().getPasswordSync().getLdap();
            ldap.stream()
                .filter(PasswordSynchronizationProperties.Ldap::isEnabled)
                .forEach(instance -> plan.registerAuthenticationPostProcessor(new LdapPasswordSynchronizationAuthenticationPostProcessor(instance)));
        };
    }
}