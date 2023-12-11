package ca.gbc.inventoryservice.service;

import ca.gbc.inventoryservice.dto.InventoryRequest;
import ca.gbc.inventoryservice.dto.InventoryResponse;
import ca.gbc.inventoryservice.model.Inventory;
import ca.gbc.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<InventoryRequest> requests) {

        /**
         log.info("Wait Started");
         Thread.sleep(10000);
         log.info("Wait Stopped");
         **/
        List<Inventory> inventories = inventoryRepository.findAllByInventoryRequest(requests);

        return requests.stream().map(request -> {
            boolean isInStock = inventories.stream().anyMatch(inventory ->
                    inventory.getSkuCode().equals(request.getSkuCode()) && inventory.getQuantity() >= request.getQuantity());
            return InventoryResponse.builder().skuCode(request.getSkuCode()).sufficientStock(isInStock).build();
        }).toList();
    }
}

