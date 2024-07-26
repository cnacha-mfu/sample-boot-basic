package th.mfu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import th.mfu.domain.Customer;
import th.mfu.domain.Product;
import th.mfu.dto.CustomerDTO;
import th.mfu.dto.ProductDTO;
import th.mfu.dto.mapper.ProductMapper;
import th.mfu.repository.ProductRepository;

@RestController
public class ProductController {
    @Autowired
    private ProductRepository proReo;
    @Autowired
    ProductMapper proMapper;
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductId(@PathVariable Long id){
        if (!proReo.existsById(id)){
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        Optional<Product> product = proReo.findById(id);
        return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
    }
    @GetMapping("/products")
    public ResponseEntity<Collection> getAllProduct(){
        List<Product> products = proReo.findAll();
        List<ProductDTO> dtos = new ArrayList<ProductDTO>();
        proMapper.updateProductFromEntity(products, dtos);
        return new ResponseEntity<Collection>(proReo.findAll(), HttpStatus.OK);
    }
    @PatchMapping("/product/{id}")
    public ResponseEntity<String> updateProduct (@PathVariable Long id,@RequestBody ProductDTO productDTO){
        if (!proReo.existsById(id)) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        Optional<Product> productEnt = proReo.findById(id);
        Product product = productEnt.get();
        proMapper.updateProductFromDto(productDTO,product);
        proReo.save(product);
        return new ResponseEntity<String>("Product update",HttpStatus.OK);
    }

    @PostMapping("/product")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO){
        Product newProduct = new Product();
        proMapper.updateProductFromDto(productDTO, newProduct);
        proReo.save(newProduct);
        return new ResponseEntity<String>("product created", HttpStatus.CREATED);
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        proReo.deleteById(id);
        return new ResponseEntity<String>("product deleted", HttpStatus.NO_CONTENT);
    }
}
