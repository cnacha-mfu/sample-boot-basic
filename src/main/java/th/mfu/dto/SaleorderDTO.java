package th.mfu.dto;

import java.util.List;

public class SaleorderDTO {

    private Long id;
    private String notes;
    private CustomerDTO customer;
    private List<ProductDTO> product;
    public List<ProductDTO> getProduct() {
        return product;
    }
    public void setProduct(List<ProductDTO> product) {
        this.product = product;
    }    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public CustomerDTO getCustomer() {
        return customer;
    }
    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }


}
