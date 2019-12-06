package hibernateexample.web;

import hibernateexample.database.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/product")
    public String product(Model model, Long product_id) {
        Product product = getProductFromID(product_id);
        if (product != null) {
            model.addAttribute("product", product);
            return "product";
        } else {
            return "error";
        }
    }

    @GetMapping("/new_company")
    public String addCompanyForm() {
        return "new_company";
    }

    @RequestMapping(path = "/company_added", method = RequestMethod.POST)
    public String showAddCompanySuccess(
            @RequestParam(name = "name", required = true) String name,
            @RequestParam(name = "about", required = true) String about,
            Model model) {
        Company company = new Company(name, about);
        repository.save(company);
        model.addAttribute("company", company);
        return "company_added";
    }

    @GetMapping("/search")
    public String searchResult(Model model, String search, String selection) {
        if (!selection.equals("products")) model.addAttribute("result_companies", getCompaniesFromSearch(search));
        if (!selection.equals("companies")) model.addAttribute("result_products", getProductsFromSearch(search));
        model.addAttribute("selection", selection);
        return "search_result";
    }

    private void fillWithExamplesIfEmpty() {
        if (repository.count() != 0) return;
        CompanyGenerator companyGenerator = new CompanyGenerator();
        LinkedList<Company> companies = companyGenerator.generateRandomCompanies(50);
        repository.saveAll(companies);
    }

    private Product getProductFromID(Long product_id) {
        LinkedList<Product> allProducts = new LinkedList<>();
        for (Company company : repository.findAll()) {
            allProducts.addAll(company.getProducts());
        }
        return allProducts.stream().filter(p -> p.getId().equals(product_id)).findAny().orElse(null);
    }

    private List<Company> getCompaniesFromSearch(String search) {
        LinkedList<Company> allCompanies = new LinkedList<>();
        for (Company company : repository.findAll()) {
            allCompanies.add(company);
        }
        return allCompanies.stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<Product> getProductsFromSearch(String search) {
        LinkedList<Product> allProducts = new LinkedList<>();
        for (Company company : repository.findAll()) {
            allProducts.addAll(company.getProducts());
        }
        return allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

}
