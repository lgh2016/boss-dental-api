package mx.com.bossdental.api.branches.mapper;

import mx.com.bossdental.api.branches.dto.BranchResponse;
import mx.com.bossdental.api.branches.entity.Branch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    BranchResponse toResponse(Branch branch);

}