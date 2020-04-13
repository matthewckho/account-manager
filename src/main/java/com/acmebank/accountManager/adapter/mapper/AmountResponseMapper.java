package com.acmebank.accountManager.adapter.mapper;

import com.acmebank.accountManager.data.dto.AmountDTO;
import com.acmebank.accountManager.data.response.AmountResponse;
import org.mapstruct.Mapper;

@Mapper
public interface AmountResponseMapper {
    AmountResponse asAmountResponse(AmountDTO amountDto);
}
