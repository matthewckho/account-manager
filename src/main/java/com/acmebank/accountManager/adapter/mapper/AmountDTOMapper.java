package com.acmebank.accountManager.adapter.mapper;

import com.acmebank.accountManager.data.dto.AmountDTO;
import com.acmebank.accountManager.entity.AccountEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AmountDTOMapper {
    AmountDTO asAmountDTO(AccountEntity entity);
}
