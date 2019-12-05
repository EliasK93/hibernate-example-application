package hibernateexample.web;

import hibernateexample.database.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.LinkedList;

@Controller
public class HibernateExampleApplicationController {

    private final CompanyRepository repository;

    public HibernateExampleApplicationController(CompanyRepository repository) {
        this.repository = repository;
        fillWithExamplesIfEmpty();
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("allCompanies", repository.findAll());
        return "start";
    }

    @GetMapping("/profile")
    public String details(Model model, Long company) {
        model.addAttribute("company", repository.findCompanyById(company));
        return "profile";
    }

    private void fillWithExamplesIfEmpty() {
        if (repository.count() != 0) return;

        String exampleDescription =
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                "sed diam nonumy eirmod tempor invidunt ut labore et dolore " +
                "magna aliquyam erat, sed diam voluptua. At vero eos et accusam " +
                "et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                "no sea takimata sanctus est Lorem ipsum dolor sit amet.";

        LinkedList<Company> examples = new LinkedList<>();

        Company example1 = new Company("Example Company 1", exampleDescription);
        LinkedList<Product> productList1 = new LinkedList<>();
        productList1.add(new Product(example1, "Example Product", exampleDescription));
        example1.setProducts(productList1);
        examples.add(example1);

        Company example2 = new Company("Example Company 2", exampleDescription);
        LinkedList<Product> productList2 = new LinkedList<>();
        example2.setProducts(productList2);
        examples.add(example2);

        Company example3 = new Company("Example Company 3", exampleDescription);
        LinkedList<Product> productList3 = new LinkedList<>();
        productList3.add(new Product(example3, "First Example Product", exampleDescription));
        productList3.add(new Product(example3, "Second Example Product", exampleDescription));
        productList3.add(new Product(example3, "Third Example Product", exampleDescription));
        example3.setProducts(productList3);
        examples.add(example3);

        repository.saveAll(examples);
    }

}
