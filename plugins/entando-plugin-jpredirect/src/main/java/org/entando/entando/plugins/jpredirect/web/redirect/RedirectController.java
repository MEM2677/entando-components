/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.web.redirect;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.IRedirectService;
import org.entando.entando.plugins.jpredirect.aps.system.services.redirect.model.RedirectDto;
import org.entando.entando.plugins.jpredirect.web.redirect.model.RedirectRequest;
import org.entando.entando.plugins.jpredirect.web.redirect.validator.RedirectValidator;

@RestController
@RequestMapping(value = "/jpredirect/redirects")
public class RedirectController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRedirectService redirectService;

    @Autowired
    private RedirectValidator redirectValidator;

    protected IRedirectService getRedirectService() {
        return redirectService;
    }

    public void setRedirectService(IRedirectService redirectService) {
        this.redirectService = redirectService;
    }

    protected RedirectValidator getRedirectValidator() {
        return redirectValidator;
    }

    public void setRedirectValidator(RedirectValidator redirectValidator) {
        this.redirectValidator = redirectValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getRedirects(RestListRequest requestList) throws JsonProcessingException {
        this.getRedirectValidator().validateRestListRequest(requestList, RedirectDto.class);
        PagedMetadata<RedirectDto> result = this.getRedirectService().getRedirects(requestList);
        this.getRedirectValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{redirectId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getRedirect(@PathVariable String redirectId) {
		RedirectDto redirect = this.getRedirectService().getRedirect(Integer.valueOf(redirectId));
        return new ResponseEntity<>(new RestResponse(redirect), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{redirectId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateRedirect(@PathVariable String redirectId, @Valid @RequestBody RedirectRequest redirectRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getRedirectValidator().validateBodyName(String.valueOf(redirectId), redirectRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        RedirectDto redirect = this.getRedirectService().updateRedirect(redirectRequest);
        return new ResponseEntity<>(new RestResponse(redirect), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addRedirect(@Valid @RequestBody RedirectRequest redirectRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getRedirectValidator().validate(redirectRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        RedirectDto dto = this.getRedirectService().addRedirect(redirectRequest);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{redirectId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteRedirect(@PathVariable String redirectId) {
        logger.info("deleting {}", redirectId);
        this.getRedirectService().removeRedirect(Integer.valueOf(redirectId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(redirectId));
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}

