/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.web.redirect.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.jpredirect.web.redirect.model.RedirectRequest;


@Component
public class RedirectValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_REDIRECT_NOT_FOUND = "2" ;
    public static final String ERRCODE_REDIRECT_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return RedirectRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //RedirectRequest request = (RedirectRequest) target;
    }

    public void validateBodyName(String redirectId, RedirectRequest redirectRequest, Errors errors) {
        if (!StringUtils.equals(redirectId, String.valueOf(redirectRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{redirectId, redirectRequest.getId()}, "redirect.id.mismatch");
        }
    }

}
