package com.shbs;

import com.shbs.product.Product;
import com.shbs.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TestHelper {

    @Autowired
    private ProductRepository productRepository;

    public void cleanUp() {
        productRepository.deleteAll();
    }

    public void createProducts() {
        final Product product = new Product();
        product.setSku("sku");
        product.setName("name");
        product.setDescription("desc");
        product.setWarehouseId(1);
        product.setQuantity(10);
        product.setReserved(2);
        product.setPrice(new BigDecimal("100000"));
        productRepository.save(product);

        final Product product2 = new Product();
        product2.setSku("sku2");
        product2.setName("name2");
        product2.setDescription("desc2");
        product2.setWarehouseId(1);
        product2.setQuantity(20);
        product2.setReserved(0);
        product2.setPrice(new BigDecimal("200000"));
        productRepository.save(product2);
    }
}