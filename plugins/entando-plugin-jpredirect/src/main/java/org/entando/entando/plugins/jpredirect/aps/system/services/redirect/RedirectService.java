/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpredirect.aps.system.services.redirect;

import  org.entando.entando.plugins.jpredirect.aps.system.services.redirect.model.RedirectDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.jpredirect.web.redirect.model.RedirectRequest;
import org.entando.entando.plugins.jpredirect.web.redirect.validator.RedirectValidator;

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

public class RedirectService implements IRedirectService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IRedirectManager redirectManager;
    private IDtoBuilder<Redirect, RedirectDto> dtoBuilder;


    protected IRedirectManager getRedirectManager() {
        return redirectManager;
    }

    public void setRedirectManager(IRedirectManager redirectManager) {
        this.redirectManager = redirectManager;
    }

    protected IDtoBuilder<Redirect, RedirectDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<Redirect, RedirectDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<Redirect, RedirectDto>() {

            @Override
            protected RedirectDto toDto(Redirect src) {
                RedirectDto dto = new RedirectDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<RedirectDto> getRedirects(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(RedirectDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<Redirect> redirects = this.getRedirectManager().getRedirects(filters);
            List<RedirectDto> dtoList = dtoBuilder.convert(redirects.getList());

            PagedMetadata<RedirectDto> pagedMetadata = new PagedMetadata<>(requestList, redirects);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search redirects", t);
            throw new RestServerError("error in search redirects", t);
        }
    }

    @Override
    public RedirectDto updateRedirect(RedirectRequest redirectRequest) {
        try {
	        Redirect redirect = this.getRedirectManager().getRedirect(redirectRequest.getId());
	        if (null == redirect) {
	            throw new RestRourceNotFoundException(RedirectValidator.ERRCODE_REDIRECT_NOT_FOUND, "redirect", String.valueOf(redirectRequest.getId()));
	        }
        	BeanUtils.copyProperties(redirectRequest, redirect);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(redirect);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getRedirectManager().updateRedirect(redirect);
            return this.getDtoBuilder().convert(redirect);
        } catch (ApsSystemException e) {
            logger.error("Error updating redirect {}", redirectRequest.getId(), e);
            throw new RestServerError("error in update redirect", e);
        }
    }

    @Override
    public RedirectDto addRedirect(RedirectRequest redirectRequest) {
        try {
            Redirect redirect = this.createRedirect(redirectRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(redirect);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getRedirectManager().addRedirect(redirect);
            RedirectDto dto = this.getDtoBuilder().convert(redirect);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a redirect", e);
            throw new RestServerError("error in add redirect", e);
        }
    }

    @Override
    public void removeRedirect(int  id) {
        try {
            Redirect redirect = this.getRedirectManager().getRedirect(id);
            if (null == redirect) {
                logger.info("redirect {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(redirect);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getRedirectManager().deleteRedirect(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete redirect {}", id, e);
            throw new RestServerError("error in delete redirect", e);
        }
    }

    @Override
    public RedirectDto getRedirect(int  id) {
        try {
	        Redirect redirect = this.getRedirectManager().getRedirect(id);
	        if (null == redirect) {
	            logger.warn("no redirect found with code {}", id);
	            throw new RestRourceNotFoundException(RedirectValidator.ERRCODE_REDIRECT_NOT_FOUND, "redirect", String.valueOf(id));
	        }
	        RedirectDto dto = this.getDtoBuilder().convert(redirect);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading redirect {}", id, e);
            throw new RestServerError("error in loading redirect", e);
        }
    }

    private Redirect createRedirect(RedirectRequest redirectRequest) {
        Redirect redirect = new Redirect();
        BeanUtils.copyProperties(redirectRequest, redirect);
        return redirect;
    }


    protected BeanPropertyBindingResult validateForAdd(Redirect redirect) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(redirect, "redirect");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(Redirect redirect) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(redirect, "redirect");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(Redirect redirect) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(redirect, "redirect");
        return errors;
    }

}

