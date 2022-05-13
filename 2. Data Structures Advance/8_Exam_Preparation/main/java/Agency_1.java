import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Agency_1 implements Agency {
    private Map<String, Invoice> invoices;

    public Agency_1() {
        this.invoices = new HashMap<>();
    }

    @Override
    public void create(Invoice invoice) {
        if (this.contains(invoice.getNumber())) {
            throw new IllegalArgumentException();
        }

        this.invoices.put(invoice.getNumber(), invoice);
    }

    @Override
    public boolean contains(String number) {
        if (this.invoices.containsKey(number)) {
            return true;
        }
        return false;
    }

    @Override
    public int count() {
        return this.invoices.size();
    }

    @Override
    public void payInvoice(LocalDate dueDate) {
        int count = 0;

        for (Invoice invoice : invoices.values()) {
            if (invoice.getDueDate().equals(dueDate)) {
                invoice.setSubtotal(0);
                count++;
            }
        }

        if (count == 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void throwInvoice(String number) {
        if (!this.contains(number)) {
            throw new IllegalArgumentException();
        }

        this.invoices.remove(number);
    }

    @Override
    public void throwPayed() {
        for (Invoice invoice : invoices.values()) {
            if (invoice.getSubtotal() == 0) {
                this.throwInvoice(invoice.getNumber());
            }
        }
    }

    @Override
    public Iterable<Invoice> getAllInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {

        List<Invoice> result = new ArrayList<>();

        result = this.invoices.values()
                .stream()
                .filter(i -> (i.getIssueDate().isAfter(startDate) || i.getIssueDate().equals(startDate)) && // fixme exclusive dates
                        i.getIssueDate().isBefore(endDate) || i.getIssueDate().equals(endDate))
                .sorted(Comparator.comparing(Invoice::getIssueDate).thenComparing(Invoice::getDueDate))
                .collect(Collectors.toUnmodifiableList());

        return result;
    }

    @Override
    public Iterable<Invoice> searchByNumber(String number) {

        List<Invoice> result = new ArrayList<>();

        for (Invoice invoice : this.invoices.values()) {
            if (invoice.getNumber().contains(number)) {
                result.add(invoice);
            }
        }

        if (result.size() > 0) {
            return result;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Iterable<Invoice> throwInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {

        List<Invoice> result = new ArrayList<>();

        for (Invoice invoice : this.invoices.values()) {
            if (
                    (invoice.getDueDate().isAfter(startDate) || invoice.getDueDate().equals(startDate))
                            && (invoice.getDueDate().isBefore(endDate) || invoice.getDueDate().equals(endDate))
            ) {
                this.invoices.remove(invoice.getNumber());
                result.add(invoice);
            }
        }

        if (result.size() > 0) {
            return result;
        } else {
            throw new IllegalArgumentException();
        }

    }

    @Override
    public Iterable<Invoice> getAllFromDepartment(Department department) {
        List<Invoice> result = new ArrayList<>();

        result = this.invoices.values()
                .stream()
                .filter(i -> i.getDepartment().equals(department))
                .sorted((a,b) -> Double.compare(b.getSubtotal(), a.getSubtotal()))
                .sorted(Comparator.comparingLong(a -> a.getIssueDate().toEpochDay()))  // fixme double sorting
                .collect(Collectors.toUnmodifiableList());

        return result;
    }

    @Override
    public Iterable<Invoice> getAllByCompany(String companyName) {
        List<Invoice> result = new ArrayList<>();

        result = this.invoices.values()
                .stream()
                .filter(i -> i.getCompanyName().equals(companyName))
                .sorted((a,b) -> b.getNumber().compareTo(a.getNumber()))
                .collect(Collectors.toUnmodifiableList());

        return result;
    }

    @Override
    public void extendDeadline(LocalDate endDate, int days) {
        int count = 0;

        for (Invoice invoice : invoices.values()) {
            if(invoice.getDueDate().equals(endDate)){
                invoice.setDueDate(invoice.getDueDate().plusDays(days));
                count++;
            }
        }

        if (count == 0){
            throw new IllegalArgumentException();
        }
    }
}