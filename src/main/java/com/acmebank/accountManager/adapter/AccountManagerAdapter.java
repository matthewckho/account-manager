package com.acmebank.accountManager.adapter;

import com.acmebank.accountManager.adapter.mapper.AmountDTOMapper;
import com.acmebank.accountManager.adapter.mapper.AmountResponseMapper;
import com.acmebank.accountManager.adapter.mapper.TransferMoneyDTOMapper;
import com.acmebank.accountManager.data.dto.AmountDTO;
import com.acmebank.accountManager.data.dto.TransferMoneyDTO;
import com.acmebank.accountManager.data.request.TransferMoneyRequest;
import com.acmebank.accountManager.data.response.AmountResponse;
import com.acmebank.accountManager.entity.AccountEntity;
import org.mapstruct.factory.Mappers;

public class AccountManagerAdapter {

    public static AmountDTO toAmountDTO(AccountEntity entity) {
        return Mappers.getMapper(AmountDTOMapper.class).asAmountDTO(entity);
    }

    public static AmountResponse toAmountResponse(AmountDTO amountDto) {
        return Mappers.getMapper(AmountResponseMapper.class).asAmountResponse(amountDto);
    }

    public static TransferMoneyDTO toTransferMoneyDTO(TransferMoneyRequest request) {
        return Mappers.getMapper(TransferMoneyDTOMapper.class).asTransferMoneyDTO(request);
    }
}
