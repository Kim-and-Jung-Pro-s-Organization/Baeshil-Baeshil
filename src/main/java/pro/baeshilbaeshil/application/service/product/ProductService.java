package pro.baeshilbaeshil.application.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.domain.product.Product;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.service.dto.product.GetProductsResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public GetProductsResponse getProductsByOffset(Pageable pageable) {
        List<Product> productPage = productRepository.findAll(pageable).getContent();
        return GetProductsResponse.of(productPage);
    }

    public GetProductsResponse getProductsByCursor(Long startId, int pageSize) {
        List<Product> productPage = productRepository.findAllByCursor(startId, pageSize);
        return GetProductsResponse.of(productPage);
    }
}
