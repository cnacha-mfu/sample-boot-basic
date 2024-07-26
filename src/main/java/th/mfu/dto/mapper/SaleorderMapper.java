package th.mfu.dto.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import th.mfu.domain.Saleorder;
import th.mfu.dto.SaleorderDTO;

@Mapper(componentModel = "spring")
public interface SaleorderMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateSaleorderFromDto(SaleorderDTO dto, @MappingTarget Saleorder entity);
    // Map from Entity to DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateSaleorderFromEntity(Collection<Saleorder> orders, @MappingTarget List<SaleorderDTO> dtos);

        // Map from List of Entity to List of DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public void updateSaleOrderFromEntity(List<Saleorder> entities, @MappingTarget List<SaleorderDTO> dtos);
}
