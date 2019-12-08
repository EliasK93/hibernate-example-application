package hibernateexample;

import hibernateexample.database.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DatabaseService {

    private EntryGenerator entryGenerator;

    private final CompanyRepository companyRepository;
    private final IndustryRepository industryRepository;

    public DatabaseService(CompanyRepository companyRepository, IndustryRepository industryRepository) {
        this.companyRepository = companyRepository;
        this.industryRepository = industryRepository;
        fillWithExamplesIfEmpty();
    }

    private void fillWithExamplesIfEmpty() {
        if (companyRepository.count() != 0) return;
        entryGenerator = new EntryGenerator();
        LinkedList<Company> companies = entryGenerator.generateRandomCompanies(50);
        LinkedList<Industry> industries = entryGenerator.returnAlphabeticallySortedIndustries();
        companyRepository.saveAll(companies);
        industryRepository.saveAll(industries);
    }

    public Product findProductById(Long product_id) {
        return StreamSupport.stream(companyRepository.findAll().spliterator(), false)
                .flatMap(s -> s.getProducts().stream())
                .filter(p -> p.getId().equals(product_id))
                .findAny()
                .orElse(null);
    }

    public List<Company> getCompaniesFromSearch(String search) {
        return StreamSupport.stream(companyRepository.findAll().spliterator(), false)
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Product> getProductsFromSearch(String search) {
        return StreamSupport.stream(companyRepository.findAll().spliterator(), false)
                .flatMap(s -> s.getProducts().stream())
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Industry> getIndustriesFromSearch(String search) {
        return StreamSupport.stream(industryRepository.findAll().spliterator(), false)
                .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Company createCompanyFromFormInput(String name, String about, List<String> industryIdSelection) {
        LinkedList<Industry> industriesSelected = createIndustryListFromSelection(industryIdSelection);
        Company company = new Company(name, about, LocalDateTime.now());
        company.setIndustries(industriesSelected);
        for (Industry industry : industriesSelected) {
            industry.getCompanies().add(company);
        }
        companyRepository.save(company);
        return company;
    }

    private LinkedList<Industry> createIndustryListFromSelection(List<String> industryIdSelection) {
        LinkedList<Industry> industryList = new LinkedList<>();
        for (String industry_id : industryIdSelection) {
            if (industry_id.length() > 0) { //checkbox was checked (String is not empty)
                industryList.add(industryRepository.findIndustryById(Long.parseLong(industry_id)));
            }
        }
        return industryList;
    }

    public Iterable<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public Company findCompanyById(Long company) {
        return companyRepository.findCompanyById(company);
    }

    public Industry findIndustryById(Long industry_id) {
        return industryRepository.findIndustryById(industry_id);
    }

    public List<Industry> returnAlphabeticallySortedIndustries() {
        return entryGenerator.returnAlphabeticallySortedIndustries();
    }

}
