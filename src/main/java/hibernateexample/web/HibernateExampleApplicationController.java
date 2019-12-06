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

    private EntryGenerator entryGenerator;

    private final CompanyRepository companyRepository;
    private final IndustryRepository industryRepository;

    public HibernateExampleApplicationController(CompanyRepository companyRepository, IndustryRepository industryRepository) {
        this.companyRepository = companyRepository;
        this.industryRepository = industryRepository;
        fillWithExamplesIfEmpty();
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("allCompanies", companyRepository.findAll());
        return "start";
    }

    @GetMapping("/profile")
    public String details(Model model, Long company) {
        model.addAttribute("company", companyRepository.findCompanyById(company));
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

    @GetMapping("/industry")
    public String industry(Model model, Long industry_id) {
        Industry industry = industryRepository.findIndustryById(industry_id);
        model.addAttribute("industry", industry);
        return "industry";
    }

    @GetMapping("/new_company")
    public String addCompanyForm(Model model) {
        model.addAttribute("industry_options", entryGenerator.returnAlphabeticallySortedIndustries());
        return "new_company";
    }

    @RequestMapping(path = "/company_added", method = RequestMethod.POST)
    public String showAddCompanySuccess(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "about") String about,
            @RequestParam(name = "industry_options") List<String> industryIdSelection,
            Model model) {
        Company company = new Company(name, about);
        LinkedList<Industry> industriesSelected = new LinkedList<>();
        for (String industry_id : industryIdSelection) {
            if (industry_id.length() > 0) { //checkbox was checked (String is not empty)
                industriesSelected.add(industryRepository.findIndustryById(Long.parseLong(industry_id)));
            }
        }
        company.setIndustries(industriesSelected);
        for (Industry industry : industriesSelected) {
            industry.getCompanies().add(company);
        }
        companyRepository.save(company);
        model.addAttribute("company", company);
        return "company_added";
    }

    @GetMapping("/search")
    public String searchResult(Model model, String search, String selection) {
        if (selection.equals("companies") || selection.equals("all")) {
            model.addAttribute("result_companies", getCompaniesFromSearch(search));
        }
        if (selection.equals("products") || selection.equals("all")) {
            model.addAttribute("result_products", getProductsFromSearch(search));
        }
        if (selection.equals("industries") || selection.equals("all")) {
            model.addAttribute("result_industries", getIndustriesFromSearch(search));
        }
        model.addAttribute("selection", selection);
        return "search_result";
    }

    private void fillWithExamplesIfEmpty() {
        if (companyRepository.count() != 0) return;
        entryGenerator = new EntryGenerator();
        LinkedList<Company> companies = entryGenerator.generateRandomCompanies(50);
        LinkedList<Industry> industries = entryGenerator.returnAlphabeticallySortedIndustries();
        companyRepository.saveAll(companies);
        industryRepository.saveAll(industries);
    }

    private Product getProductFromID(Long product_id) {
        LinkedList<Product> allProducts = new LinkedList<>();
        for (Company company : companyRepository.findAll()) {
            allProducts.addAll(company.getProducts());
        }
        return allProducts.stream().filter(p -> p.getId().equals(product_id)).findAny().orElse(null);
    }

    private List<Company> getCompaniesFromSearch(String search) {
        LinkedList<Company> allCompanies = new LinkedList<>();
        for (Company company : companyRepository.findAll()) {
            allCompanies.add(company);
        }
        return allCompanies.stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<Product> getProductsFromSearch(String search) {
        LinkedList<Product> allProducts = new LinkedList<>();
        for (Company company : companyRepository.findAll()) {
            allProducts.addAll(company.getProducts());
        }
        return allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<Industry> getIndustriesFromSearch(String search) {
        LinkedList<Industry> allIndustries = new LinkedList<>();
        for (Industry industry : industryRepository.findAll()) {
            allIndustries.add(industry);
        }
        return allIndustries.stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

}
