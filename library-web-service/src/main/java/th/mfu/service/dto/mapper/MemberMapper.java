package th.mfu.service.dto.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import th.mfu.domain.Member;
import th.mfu.service.dto.MemberDTO;

/**
 * The Assembler for Member.
 */
@Mapper(componentModel = "spring")
public interface MemberMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMemberFromEntity(Member entity, @MappingTarget MemberDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    void updateMemberFromDto(MemberDTO dto, @MappingTarget Member entity);
}
