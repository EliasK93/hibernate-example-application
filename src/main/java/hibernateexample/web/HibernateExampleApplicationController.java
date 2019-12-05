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
        CompanyGenerator companyGenerator = new CompanyGenerator();
        LinkedList<Company> companies = companyGenerator.generateRandomCompanies(50);
        repository.saveAll(companies);
    }

}
