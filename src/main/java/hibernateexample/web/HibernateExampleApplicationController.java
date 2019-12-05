package hibernateexample.web;

import hibernateexample.database.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    private void fillWithExamplesIfEmpty() {
        if (repository.count() != 0) return;
        CompanyGenerator companyGenerator = new CompanyGenerator();
        LinkedList<Company> companies = companyGenerator.generateRandomCompanies(50);
        repository.saveAll(companies);
    }

}
