package com.acmebank.accountManager.adapter.mapper;

import com.acmebank.accountManager.data.dto.TransferMoneyDTO;
import com.acmebank.accountManager.data.request.TransferMoneyRequest;
import org.mapstruct.Mapper;

@Mapper
public interface TransferMoneyDTOMapper {
    TransferMoneyDTO asTransferMoneyDTO(TransferMoneyRequest request);
}
