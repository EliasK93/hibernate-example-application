package hibernateexample.database;

import java.util.LinkedList;
import java.util.Random;

public class CompanyGenerator {

    private Random random = new Random();

    private final String exampleDescription =
            "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                    "sed diam nonumy eirmod tempor invidunt ut labore et dolore " +
                    "magna aliquyam erat, sed diam voluptua. At vero eos et accusam " +
                    "et justo duo dolores et ea rebum. Stet clita kasd gubergren, " +
                    "no sea takimata sanctus est Lorem ipsum dolor sit amet.";

    private final String[] firstPartsCompany = new String[]{
            "Advanced", "Excellent", "Basic", "Intuitive", "Futuristic", "Smart", "General"};
    private final String[] lastPartsCompany = new String[]{
            "Tech", "Solutions", "Consulting", "Business", "Systems", "Investments"};
    private final String[] legalForms = new String[]{
            "GbR", "OHG", "GmbH", "KG", "UG", "AG", "eG"};

    private final String[] firstPartsProduct = new String[]{
            "Energizer", "SuperMagnetizer", "Speedster", "Monolith", "MetaTransmitter", "Forge"};
    private final String[] lastPartsProduct = new String[]{
            "8000", "T340", "Compact", "Pro", "All-In-One", "400", "V23", "P87"};

    private String generateRandomCompanyName() {
        String firstPart = firstPartsCompany[random.nextInt(firstPartsCompany.length)];
        String lastPart = lastPartsCompany[random.nextInt(lastPartsCompany.length)];
        String legalForm = legalForms[random.nextInt(legalForms.length)];
        return firstPart + lastPart + " " + legalForm;
    }

    private String generateRandomProductName() {
        String firstPart = firstPartsProduct[random.nextInt(firstPartsProduct.length)];
        String lastPart = lastPartsProduct[random.nextInt(lastPartsProduct.length)];
        return firstPart + " " + lastPart;
    }

    private void addRandomProductListTo(Company company) {
        int numberOfProducts = random.nextInt(9);
        LinkedList<Product> products = new LinkedList<>();
        for (int i = 0; i < numberOfProducts; i++) {
            Product product = new Product(company, generateRandomProductName(), exampleDescription);
            products.add(product);
        }
        company.setProducts(products);
    }

    private Company generateRandomCompany() {
        Company company = new Company(generateRandomCompanyName(), exampleDescription);
        addRandomProductListTo(company);
        return company;
    }

    public LinkedList<Company> generateRandomCompanies(int numberOfCompanies) {
        LinkedList<Company> companies = new LinkedList<>();
        for (int i = 0; i < numberOfCompanies; i++) {
            companies.add(generateRandomCompany());
        }
        return companies;
    }

}