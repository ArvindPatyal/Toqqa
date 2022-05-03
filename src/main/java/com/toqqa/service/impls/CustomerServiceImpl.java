package com.toqqa.service.impls;

import com.toqqa.bo.ProductBo;
import com.toqqa.domain.Product;
import com.toqqa.payload.Response;
import com.toqqa.repository.ProductRepository;
import com.toqqa.service.CustomerService;
import com.toqqa.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private Helper helper;

    @Override
    public Response productlist() {
        List<Product> products = productRepository.findByIsDeleted(false);
        List<ProductBo> productBos = new ArrayList<>();
        products.forEach(product -> {
            ProductBo productBo = new ProductBo(product);
            productBo.setBanner(this.helper.prepareAttachmentResource(product.getBanner()));
            productBo.setImages(this.helper.prepareProductAttachments(product.getAttachments()));
            productBos.add(productBo);
        });
        return new Response(productBos, "");

    }
}


