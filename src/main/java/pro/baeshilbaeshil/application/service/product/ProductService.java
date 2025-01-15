package pro.baeshilbaeshil.application.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.domain.product.Product;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.service.dto.product.GetProductsResponse;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public GetProductsResponse getProducts(Pageable pageable) {
        List<Product> productPage = productRepository.findAll(pageable).getContent();
        return GetProductsResponse.of(productPage);
    }
}
