/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.web.mapping.validator;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.entando.entando.plugins.jpjaasauth.web.mapping.model.MappingRequest;


@Component
public class MappingValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_URINAME_MISMATCH = "1";
    public static final String ERRCODE_MAPPING_NOT_FOUND = "2" ;
    public static final String ERRCODE_MAPPING_ALREADY_EXISTS = "3";


    @Override
    public boolean supports(Class<?> paramClass) {
        return MappingRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //MappingRequest request = (MappingRequest) target;
    }

    public void validateBodyName(String mappingId, MappingRequest mappingRequest, Errors errors) {
        if (!StringUtils.equals(mappingId, String.valueOf(mappingRequest.getId()))) {
            errors.rejectValue("id", ERRCODE_URINAME_MISMATCH, new Object[]{mappingId, mappingRequest.getId()}, "mapping.id.mismatch");
        }
    }

}
