package hibernateexample;

import hibernateexample.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DatabaseService {

    private final EntryGenerator entryGenerator;

    private final CompanyRepository companyRepository;
    private final IndustryRepository industryRepository;

    @Autowired
    public DatabaseService(EntryGenerator entryGenerator, CompanyRepository companyRepository, IndustryRepository industryRepository) {
        this.entryGenerator = entryGenerator;
        this.companyRepository = companyRepository;
        this.industryRepository = industryRepository;
        fillWithExamplesIfEmpty();
    }

    private void fillWithExamplesIfEmpty() {
        if (companyRepository.count() != 0) return;
        LinkedList<Company> companies = entryGenerator.generateRandomCompanies(50);
        LinkedList<Industry> industries = entryGenerator.returnAllIndustries();
        companyRepository.saveAll(companies);
        industryRepository.saveAll(industries);
    }

    public List<Company> getCompaniesFromSearch(String search) {
        return findAllCompanies().stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Product> getProductsFromSearch(String search) {
        return findAllProducts().stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Industry> getIndustriesFromSearch(String search) {
        return findAllIndustries().stream()
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Company createCompanyFromFormInput(String name, String about, List<String> industryIdSelection) {
        List<Industry> industriesSelected = createIndustryListFromSelection(industryIdSelection);
        Company company = new Company(name, about, LocalDateTime.now());
        company.setIndustries(industriesSelected);
        for (Industry industry : industriesSelected) {
            industry.getCompanies().add(company);
        }
        companyRepository.save(company);
        return company;
    }

    private List<Industry> createIndustryListFromSelection(List<String> industryIdSelection) {
        LinkedList<Industry> industryList = new LinkedList<>();
        for (String industry_id : industryIdSelection) {
            if (industry_id.length() > 0) { //checkbox was checked (String is not empty)
                industryList.add(industryRepository.findIndustryById(Long.parseLong(industry_id)));
            }
        }
        return industryList;
    }

    public List<Company> findAllCompanies() {
        return StreamSupport.stream(companyRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public List<Product> findAllProducts() {
        return findAllCompanies().stream()
                .flatMap(s -> s.getProducts().stream())
                .collect(Collectors.toList());
    }

    public List<Industry> findAllIndustries() {
        return StreamSupport.stream(industryRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Company findCompanyById(Long company) {
        return companyRepository.findCompanyById(company);
    }

    public Product findProductById(Long product_id) {
        return findAllProducts().stream()
                .filter(p -> p.getId().equals(product_id))
                .findAny()
                .orElse(null);
    }

    public Industry findIndustryById(Long industry_id) {
        return industryRepository.findIndustryById(industry_id);
    }

    public List<Industry> findAllIndustriesSorted() {
        return findAllIndustries().stream()
                .sorted(Comparator.comparing(industry -> industry.getName().toLowerCase()))
                .collect(Collectors.toList());

    }

}
