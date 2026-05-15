package mx.com.bossdental.api.roles.mapper;

import mx.com.bossdental.api.roles.dto.RoleResponse;
import mx.com.bossdental.api.roles.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toResponse(Role role);

}