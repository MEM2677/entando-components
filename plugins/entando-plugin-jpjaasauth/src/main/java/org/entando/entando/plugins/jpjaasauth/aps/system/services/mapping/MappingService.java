/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping;

import  org.entando.entando.plugins.jpjaasauth.aps.system.services.mapping.model.MappingDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.jpjaasauth.web.mapping.model.MappingRequest;
import org.entando.entando.plugins.jpjaasauth.web.mapping.validator.MappingValidator;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class MappingService implements IMappingService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IMappingManager mappingManager;
    private IDtoBuilder<Mapping, MappingDto> dtoBuilder;


    protected IMappingManager getMappingManager() {
        return mappingManager;
    }

    public void setMappingManager(IMappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }

    protected IDtoBuilder<Mapping, MappingDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<Mapping, MappingDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<Mapping, MappingDto>() {

            @Override
            protected MappingDto toDto(Mapping src) {
                MappingDto dto = new MappingDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<MappingDto> getMappings(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(MappingDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<Mapping> mappings = this.getMappingManager().getMappings(filters);
            List<MappingDto> dtoList = dtoBuilder.convert(mappings.getList());

            PagedMetadata<MappingDto> pagedMetadata = new PagedMetadata<>(requestList, mappings);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search mappings", t);
            throw new RestServerError("error in search mappings", t);
        }
    }

    @Override
    public MappingDto updateMapping(MappingRequest mappingRequest) {
        try {
	        Mapping mapping = this.getMappingManager().getMapping(mappingRequest.getId());
	        if (null == mapping) {
	            throw new RestRourceNotFoundException(MappingValidator.ERRCODE_MAPPING_NOT_FOUND, "mapping", String.valueOf(mappingRequest.getId()));
	        }
        	BeanUtils.copyProperties(mappingRequest, mapping);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(mapping);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getMappingManager().updateMapping(mapping);
            return this.getDtoBuilder().convert(mapping);
        } catch (ApsSystemException e) {
            logger.error("Error updating mapping {}", mappingRequest.getId(), e);
            throw new RestServerError("error in update mapping", e);
        }
    }

    @Override
    public MappingDto addMapping(MappingRequest mappingRequest) {
        try {
            Mapping mapping = this.createMapping(mappingRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(mapping);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getMappingManager().addMapping(mapping);
            MappingDto dto = this.getDtoBuilder().convert(mapping);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a mapping", e);
            throw new RestServerError("error in add mapping", e);
        }
    }

    @Override
    public void removeMapping(int  id) {
        try {
            Mapping mapping = this.getMappingManager().getMapping(id);
            if (null == mapping) {
                logger.info("mapping {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(mapping);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getMappingManager().deleteMapping(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete mapping {}", id, e);
            throw new RestServerError("error in delete mapping", e);
        }
    }

    @Override
    public MappingDto getMapping(int  id) {
        try {
	        Mapping mapping = this.getMappingManager().getMapping(id);
	        if (null == mapping) {
	            logger.warn("no mapping found with code {}", id);
	            throw new RestRourceNotFoundException(MappingValidator.ERRCODE_MAPPING_NOT_FOUND, "mapping", String.valueOf(id));
	        }
	        MappingDto dto = this.getDtoBuilder().convert(mapping);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading mapping {}", id, e);
            throw new RestServerError("error in loading mapping", e);
        }
    }

    private Mapping createMapping(MappingRequest mappingRequest) {
        Mapping mapping = new Mapping();
        BeanUtils.copyProperties(mappingRequest, mapping);
        return mapping;
    }


    protected BeanPropertyBindingResult validateForAdd(Mapping mapping) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(mapping, "mapping");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(Mapping mapping) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(mapping, "mapping");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(Mapping mapping) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(mapping, "mapping");
        return errors;
    }

}

