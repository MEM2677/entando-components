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

public interface IMappingService {

    public String BEAN_NAME = "jpjaasauthMappingService";

    public PagedMetadata<MappingDto> getMappings(RestListRequest requestList);

    public MappingDto updateMapping(MappingRequest mappingRequest);

    public MappingDto addMapping(MappingRequest mappingRequest);

    public void removeMapping(int id);

    public MappingDto getMapping(int  id);

}

