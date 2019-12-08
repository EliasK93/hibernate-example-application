package hibernateexample.web;

import hibernateexample.DatabaseService;
import hibernateexample.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class HibernateExampleApplicationController {

    private DatabaseService databaseService;

    @Autowired
    public HibernateExampleApplicationController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("allCompanies", databaseService.findAllCompanies());
        return "start";
    }

    @GetMapping("/profile")
    public String details(Model model, Long company) {
        model.addAttribute("company", databaseService.findCompanyById(company));
        return "profile";
    }

    @GetMapping("/product")
    public String product(Model model, Long product_id) {
        Product product = databaseService.findProductById(product_id);
        if (product != null) {
            model.addAttribute("product", product);
            return "product";
        } else {
            return "error";
        }
    }

    @GetMapping("/industry")
    public String industry(Model model, Long industry_id) {
        Industry industry = databaseService.findIndustryById(industry_id);
        model.addAttribute("industry", industry);
        return "industry";
    }

    @GetMapping("/new_company")
    public String addCompanyForm(Model model) {
        model.addAttribute("industry_options", databaseService.returnAlphabeticallySortedIndustries());
        return "new_company";
    }

    @RequestMapping(path = "/company_added", method = RequestMethod.POST)
    public String showAddCompanySuccess(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "about") String about,
            @RequestParam(name = "industry_options") List<String> industryIdSelection,
            Model model) {
        Company company = databaseService.createCompanyFromFormInput(name, about, industryIdSelection);
        model.addAttribute("company", company);
        return "company_added";
    }

    @GetMapping("/search")
    public String searchResult(Model model, String search, String selection) {
        if (selection.equals("companies") || selection.equals("all")) {
            model.addAttribute("result_companies", databaseService.getCompaniesFromSearch(search));
        }
        if (selection.equals("products") || selection.equals("all")) {
            model.addAttribute("result_products", databaseService.getProductsFromSearch(search));
        }
        if (selection.equals("industries") || selection.equals("all")) {
            model.addAttribute("result_industries", databaseService.getIndustriesFromSearch(search));
        }
        model.addAttribute("selection", selection);
        return "search_result";
    }

}
