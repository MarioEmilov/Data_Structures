import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AgencyImpl implements Agency {
    private Map<String, Invoice> all;
    private Set<Invoice> payed;
    private Map<LocalDate, Map<String, Invoice>> dueDates;

    public AgencyImpl() {
        this.all = new HashMap<>();
        this.payed = new HashSet<>();
        this.dueDates = new HashMap<>();
    }

    @Override
    public void create(Invoice invoice) {
        if (this.contains(invoice.getNumber())) {
            throw new IllegalArgumentException();
        }

        this.all.put(invoice.getNumber(), invoice);

        Map<String, Invoice> currentByDate = this.dueDates.get(invoice.getDueDate());

        if(currentByDate == null){
            currentByDate = new HashMap<>();
        }

        currentByDate.put(invoice.getNumber(), invoice);
        this.dueDates.put(invoice.getDueDate(), currentByDate);
    }

    @Override
    public boolean contains(String number) {
        return this.all.containsKey(number);
    }

    @Override
    public int count() {
        return this.all.size();
    }

    @Override
    public void payInvoice(LocalDate dueDate) {
        if(!this.dueDates.containsKey(dueDate)){
            throw new IllegalArgumentException();
        }

        Map<String, Invoice> atDate = this.dueDates.get(dueDate);
        atDate.forEach((k, v) -> {
            v.setSubtotal(0);
            this.payed.add(v);
            this.all.put(k, v);
        });

        this.dueDates.put(dueDate, atDate);
    }

    @Override
    public void throwInvoice(String number) {
        Invoice invoice = this.all.get(number);


        if (invoice == null) {
            throw new IllegalArgumentException();
        }

        this.dueDates.get(invoice.getDueDate()).remove(invoice.getNumber());
        this.all.remove(number);
    }

    @Override
    public void throwPayed() {
        this.payed.forEach(invoice -> {
            this.dueDates.get(invoice.getDueDate()).remove(invoice.getNumber());
            this.all.remove(invoice.getNumber());
        });

        this.payed.clear();
    }

    @Override
    public Iterable<Invoice> getAllInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        return this.all
                .values()
                .stream()
                .filter(i -> (i.getIssueDate().isAfter(startDate) || i.getIssueDate().equals(startDate)) &&
                        i.getIssueDate().isBefore(endDate) || i.getIssueDate().equals(endDate))
                .sorted(Comparator.comparing(Invoice::getIssueDate).thenComparing(Invoice::getDueDate))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Iterable<Invoice> searchByNumber(String number) {
        List<Invoice> result = this.all
                .values()
                .stream()
                .filter(invoice -> invoice.getNumber().contains(number))
                .collect(Collectors.toUnmodifiableList());

        if (result.isEmpty()){
            throw new IllegalArgumentException();
        }
        return result;
    }

    @Override
    public Iterable<Invoice> throwInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        List<Invoice> result = this.all
                .values()
                .stream()
                .filter(i -> i.getDueDate().isAfter(startDate) && i.getDueDate().isBefore(endDate))
                .collect(Collectors.toUnmodifiableList());

        if (result.isEmpty()){
            throw new IllegalArgumentException();
        }

        result.forEach(invoice -> {
            this.all.remove(invoice.getNumber());
            this.dueDates.get(invoice.getDueDate()).remove(invoice.getNumber());
        });

        return result;
    }

    @Override
    public Iterable<Invoice> getAllFromDepartment(Department department) {
        return this.all
                .values()
                .stream()
                .filter(i -> i.getDepartment().equals(department))
                .sorted((l, r) -> {
                    int subtotalResult = Double.compare(r.getSubtotal(), l.getSubtotal());

                    if (subtotalResult == 0){
                        return l.getIssueDate().compareTo(r.getIssueDate());
                    } else {
                        return subtotalResult;
                    }
                })
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Iterable<Invoice> getAllByCompany(String companyName) {
        return this.all
                .values()
                .stream()
                .filter(i -> i.getCompanyName().equals(companyName))
                .sorted((l,r) -> r.getNumber().compareTo(l.getNumber()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void extendDeadline(LocalDate endDate, int days) {
        if(!this.dueDates.containsKey(endDate)){
            throw new IllegalArgumentException();
        }

        this.dueDates.get(endDate).forEach((k, v) -> v.getDueDate().plusDays(days));
    }
}