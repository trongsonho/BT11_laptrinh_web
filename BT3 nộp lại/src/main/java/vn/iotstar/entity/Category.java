package vn.iotstar.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "Category")
@NamedQuery(
    name = "Category.findAll",
    query = "SELECT c FROM Category c"
)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private int id;

    @Column(name = "cate_name")
    private String name;

    @Column(name = "icons")
    private String icon;

    @OneToMany(
        mappedBy = "category",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    private List<Product> products = new ArrayList<>();

    public Category() {
    }

    public Category(int id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Long id) {
        if (id != null) {
            this.id = id.intValue();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category [id=" + id
                + ", name=" + name
                + ", icon=" + icon + "]";
    }
}