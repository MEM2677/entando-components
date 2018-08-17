/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.web.mapping;

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

import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.IMappingService;
import org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.model.MappingDto;
import org.entando.entando.plugins.jpjaasauth.web.mapping.model.MappingRequest;
import org.entando.entando.plugins.jpjaasauth.web.mapping.validator.MappingValidator;

@RestController
@RequestMapping(value = "/jpjaasauth/mappings")
public class MappingController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IMappingService mappingService;

    @Autowired
    private MappingValidator mappingValidator;

    protected IMappingService getMappingService() {
        return mappingService;
    }

    public void setMappingService(IMappingService mappingService) {
        this.mappingService = mappingService;
    }

    protected MappingValidator getMappingValidator() {
        return mappingValidator;
    }

    public void setMappingValidator(MappingValidator mappingValidator) {
        this.mappingValidator = mappingValidator;
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getMappings(RestListRequest requestList) throws JsonProcessingException {
        this.getMappingValidator().validateRestListRequest(requestList, MappingDto.class);
        PagedMetadata<MappingDto> result = this.getMappingService().getMappings(requestList);
        this.getMappingValidator().validateRestListResult(requestList, result);
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{mappingId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getMapping(@PathVariable String mappingId) {
		MappingDto mapping = this.getMappingService().getMapping(Integer.valueOf(mappingId));
        return new ResponseEntity<>(new RestResponse(mapping), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{mappingId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateMapping(@PathVariable String mappingId, @Valid @RequestBody MappingRequest mappingRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getMappingValidator().validateBodyName(String.valueOf(mappingId), mappingRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        MappingDto mapping = this.getMappingService().updateMapping(mappingRequest);
        return new ResponseEntity<>(new RestResponse(mapping), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addMapping(@Valid @RequestBody MappingRequest mappingRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        getMappingValidator().validate(mappingRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        MappingDto dto = this.getMappingService().addMapping(mappingRequest);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "superuser")
    @RequestMapping(value = "/{mappingId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteMapping(@PathVariable String mappingId) {
        logger.info("deleting {}", mappingId);
        this.getMappingService().removeMapping(Integer.valueOf(mappingId));
        Map<String, Integer> result = new HashMap<>();
        result.put("id", Integer.valueOf(mappingId));
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}

