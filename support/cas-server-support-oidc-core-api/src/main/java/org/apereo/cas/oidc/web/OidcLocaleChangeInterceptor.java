package org.apereo.cas.oidc.web;

import org.apereo.cas.configuration.CasConfigurationProperties;
import org.apereo.cas.oidc.OidcConstants;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.support.ArgumentExtractor;
import org.apereo.cas.web.support.CasLocaleChangeInterceptor;

import lombok.val;
import org.springframework.beans.factory.ObjectProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * This is {@link OidcLocaleChangeInterceptor}.
 *
 * @author Misagh Moayyed
 * @since 6.4.0
 */
public class OidcLocaleChangeInterceptor extends CasLocaleChangeInterceptor {
    public OidcLocaleChangeInterceptor(final ObjectProvider<CasConfigurationProperties> casProperties,
                                       final ObjectProvider<ArgumentExtractor> argumentExtractor,
                                       final ObjectProvider<ServicesManager> servicesManager) {
        super(casProperties, argumentExtractor, servicesManager);
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) {
        resolveUiLocale(request, response);
        return true;
    }

    private void resolveUiLocale(final HttpServletRequest request,
                                 final HttpServletResponse response) {
        val service = argumentExtractor.getObject().extractService(request);
        if (service != null) {
            val newLocale = service.getAttributes().get(OidcConstants.UI_LOCALES);
            if (newLocale != null && !newLocale.isEmpty()) {
                configureLocale(request, response, new Locale((String) newLocale.get(0)));
            }
        }
    }
}
