package pro.baeshilbaeshil.application.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.domain.product.Product;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductRequest;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductResponse;

import static pro.baeshilbaeshil.application.common.exception_type.ShopExceptionType.NO_SUCH_SHOP;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    @Transactional
    public CreateProductResponse createProduct(CreateProductRequest request) {
        Long shopId = request.getShopId();
        shopRepository.findById(shopId).orElseThrow(() -> new NotFoundException(NO_SUCH_SHOP));

        Product product = Product.builder()
                .shopId(shopId)
                .name(request.getName())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .build();

        Product savedProduct = productRepository.save(product);
        return CreateProductResponse.of(savedProduct);
    }
}
