package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class VikingLambdaService {

    private final VikingService vikingService;
    private final VikingFactory vikingFactory;
    private final Random random = new Random();

    public VikingLambdaService(VikingService vikingService, VikingFactory vikingFactory) {
        this.vikingService = vikingService;
        this.vikingFactory = vikingFactory;
    }

    private Predicate<Viking> ageGreaterThan(int age) {
        return v -> v.age() > age;
    }

    private Predicate<Viking> ageLessThan(int age) {
        return v -> v.age() < age;
    }

    private Predicate<Viking> ageBetween(int min, int max) {
        return v -> v.age() >= min && v.age() <= max;
    }

    private Predicate<Viking> ageOutside(int min, int max) {
        return v -> v.age() < min || v.age() > max;
    }

    private Predicate<Viking> beardAndHair(BeardStyle beard, HairColor hair) {
        return v -> v.beardStyle() == beard && v.hairColor() == hair;
    }

    private Predicate<Viking> heightAbove180() {
        return v -> v.heightCm() > 180;
    }

    private Predicate<Viking> hasLegendaryEquipment() {
        return v -> v.equipment().stream()
                .anyMatch(item -> item.quality().equalsIgnoreCase("Legendary"));
    }

    private Predicate<Viking> isRedBearded() {
        return v -> v.hairColor() == HairColor.Red && v.beardStyle() == BeardStyle.LONG;
    }

    public long countByAgeGreaterThan(int age) {
        return vikingService.findAll().stream()
                .filter(ageGreaterThan(age))
                .count();
    }

    public long countByAgeLessThan(int age) {
        return vikingService.findAll().stream()
                .filter(ageLessThan(age))
                .count();
    }

    public long countByAgeBetween(int min, int max) {
        return vikingService.findAll().stream()
                .filter(ageBetween(min, max))
                .count();
    }

    public long countByAgeOutside(int min, int max) {
        return vikingService.findAll().stream()
                .filter(ageOutside(min, max))
                .count();
    }

    public long countByBeardAndHair(BeardStyle beard, HairColor hair) {
        return vikingService.findAll().stream()
                .filter(beardAndHair(beard, hair))
                .count();
    }

    public long countByAxesOneOrTwo() {
        return vikingService.findAll().stream()
                .filter(v -> {
                    long axesCount = v.equipment().stream()
                            .filter(item -> item.name().equalsIgnoreCase("Axe"))
                            .count();
                    return axesCount == 1 || axesCount == 2;
                })
                .count();
    }

    public Viking getRandomVikingHeightAbove180() {
        List<Viking> filtered = vikingService.findAll().stream()
                .filter(heightAbove180())
                .collect(Collectors.toList());
        if (filtered.isEmpty()) return null;
        return filtered.get(random.nextInt(filtered.size()));
    }

    public List<Viking> getVikingsWithLegendaryEquipment() {
        return vikingService.findAll().stream()
                .filter(hasLegendaryEquipment())
                .collect(Collectors.toList());
    }

    public List<Viking> getRedBeardedVikingsSortedByAge() {
        return vikingService.findAll().stream()
                .filter(isRedBearded())
                .sorted((v1, v2) -> Integer.compare(v1.age(), v2.age()))
                .collect(Collectors.toList());
    }

    private List<Integer> getAllIds() {
        return IntStream.range(0, vikingService.findAll().size())
                .boxed()
                .collect(Collectors.toList());
    }

    public Integer getMaxId() {
        return getAllIds().stream()
                .max(Integer::compareTo)
                .orElse(null);
    }

    public List<Integer> getEvenIds() {
        return getAllIds().stream()
                .filter(id -> id % 2 == 0)
                .collect(Collectors.toList());
    }

    public void generateRandomVikings(int count) {
        IntStream.range(0, count)
                .forEach(i -> {
                    Viking viking = vikingFactory.createRandomViking();
                    vikingService.addSpecificViking(viking);
                });
    }
}