package mx.com.bossdental.api.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.com.bossdental.api.branches.dto.BranchResponse;
import mx.com.bossdental.api.roles.dto.RoleResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String lastName;

    private String email;

    private RoleResponse role;

    private BranchResponse branch;
}
