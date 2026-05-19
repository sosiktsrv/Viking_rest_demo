package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

@Service
public class VikingLambdaService {

    private final VikingService vikingService;
    private final Random random = new Random();

    public VikingLambdaService(VikingService vikingService) {
        this.vikingService = vikingService;
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
        Viking[] filtered = vikingService.findAll().stream()
                .filter(heightAbove180())
                .toArray(Viking[]::new);
        if (filtered.length == 0) return null;
        return filtered[random.nextInt(filtered.length)];
    }

    public Viking[] getVikingsWithLegendaryEquipment() {
        return vikingService.findAll().stream()
                .filter(hasLegendaryEquipment())
                .toArray(Viking[]::new);
    }

    public Viking[] getRedBeardedVikingsSortedByAge() {
        return vikingService.findAll().stream()
                .filter(isRedBearded())
                .sorted((v1, v2) -> Integer.compare(v1.age(), v2.age()))
                .toArray(Viking[]::new);
    }

    private Integer[] getAllIds() {
        return vikingService.findAll().stream()
                .map(v -> vikingService.findAll().indexOf(v))
                .toArray(Integer[]::new);
    }

    public Integer getMaxId() {
        Integer[] ids = getAllIds();
        if (ids.length == 0) return null;
        return Arrays.stream(ids).max(Integer::compareTo).orElse(null);
    }

    public Integer[] getEvenIds() {
        return Arrays.stream(getAllIds())
                .filter(id -> id % 2 == 0)
                .toArray(Integer[]::new);
    }
}