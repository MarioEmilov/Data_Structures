import java.util.*;
import java.util.stream.Collectors;

public class MicrosystemImpl implements Microsystem {
    private Map<Integer, Computer> computers;

    public MicrosystemImpl() {
        this.computers = new HashMap<>();
    }

    @Override
    public void createComputer(Computer computer) {
        if (this.contains(computer.getNumber())) {
            throw new IllegalArgumentException();
        }
        this.computers.put(computer.getNumber(), computer);
    }

    @Override
    public boolean contains(int number) {
        return this.computers.containsKey(number);
    }

    @Override
    public int count() {
        return this.computers.size();
    }

    @Override
    public Computer getComputer(int number) {
        ensureContains(number);
        return this.computers.get(number);
    }

    @Override
    public void remove(int number) {
        ensureContains(number);
        this.computers.remove(number);
    }

    @Override
    public void removeWithBrand(Brand brand) {
        Map<Integer, Computer> result = this.computers.values()
                .stream()
                .filter(c -> !c.getBrand().equals(brand))
                .collect(Collectors.toMap(c -> c.getNumber(), c -> c));

        if (this.count() == result.size()) {
            throw new IllegalArgumentException();
        }
        this.computers = result;
    }

    @Override
    public void upgradeRam(int ram, int number) {
        Computer current = this.getComputer(number);

        if (ram > current.getRAM()) {
            current.setRAM(ram);
            this.computers.put(current.getNumber(), current);
        }
    }

    @Override
    public Iterable<Computer> getAllFromBrand(Brand brand) {
        return this.computers.values()
                .stream()
                .filter(computer -> computer.getBrand().equals(brand))
                .sorted((l, r) -> Double.compare(r.getPrice(), l.getPrice()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Iterable<Computer> getAllWithScreenSize(double screenSize) {
        return this.computers
                .values()
                .stream()
                .filter(c -> c.getScreenSize() == screenSize)
                .sorted((l, r) -> Integer.compare(r.getNumber(), l.getNumber()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Iterable<Computer> getAllWithColor(String color) {
        return this.computers
                .values()
                .stream()
                .filter(c -> c.getColor().equals(color))
                .sorted((l,r) -> Double.compare(r.getPrice(), l.getPrice()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Iterable<Computer> getInRangePrice(double minPrice, double maxPrice) {
        return this.computers
                .values()
                .stream()
                .filter(c -> c.getPrice() >= minPrice && c.getPrice() <= maxPrice)
                .sorted((l,r) -> Double.compare(r.getPrice(), l.getPrice()))
                .collect(Collectors.toUnmodifiableList());
    }

    private void ensureContains(int number) {
        if (!this.contains(number)) {
            throw new IllegalArgumentException();
        }
    }
}