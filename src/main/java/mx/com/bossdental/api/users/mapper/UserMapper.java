package mx.com.bossdental.api.users.mapper;

import mx.com.bossdental.api.branches.mapper.BranchMapper;
import mx.com.bossdental.api.roles.mapper.RoleMapper;
import mx.com.bossdental.api.users.dto.UserResponse;
import mx.com.bossdental.api.users.entity.User;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {
                RoleMapper.class,
                BranchMapper.class
        }
)
public interface UserMapper {

    UserResponse toResponse(User user);

}
