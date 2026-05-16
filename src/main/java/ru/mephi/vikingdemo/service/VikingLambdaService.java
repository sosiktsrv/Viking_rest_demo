package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VikingLambdaService {

    private final VikingService vikingService;
    private final VikingFactory vikingFactory;

    public VikingLambdaService(VikingService vikingService, VikingFactory vikingFactory) {
        this.vikingService = vikingService;
        this.vikingFactory = vikingFactory;
    }

    public long countByAgeGreaterThan(int age) {
        return vikingService.findAll().stream()
                .filter(v -> v.age() > age)
                .count();
    }

    public long countByAgeLessThan(int age) {
        return vikingService.findAll().stream()
                .filter(v -> v.age() < age)
                .count();
    }

    public long countByAgeBetween(int min, int max) {
        return vikingService.findAll().stream()
                .filter(v -> v.age() >= min && v.age() <= max)
                .count();
    }

    public long countByAgeOutside(int min, int max) {
        return vikingService.findAll().stream()
                .filter(v -> v.age() < min || v.age() > max)
                .count();
    }

    public long countByBeardAndHair(BeardStyle beard, HairColor hair) {
        return vikingService.findAll().stream()
                .filter(v -> v.beardStyle() == beard && v.hairColor() == hair)
                .count();
    }

    public long countByAxesCount(int count) {
        return vikingService.findAll().stream()
                .filter(v -> v.equipment().stream()
                        .filter(item -> item.name().equalsIgnoreCase("Axe"))
                        .count() == count)
                .count();
    }

    public Viking getRandomVikingHeightAbove180() {
        List<Viking> filtered = vikingService.findAll().stream()
                .filter(v -> v.heightCm() > 180)
                .collect(Collectors.toList());
        if (filtered.isEmpty()) return null;
        int randomIndex = (int) (Math.random() * filtered.size());
        return filtered.get(randomIndex);
    }

    public List<Viking> getVikingsWithLegendaryEquipment() {
        return vikingService.findAll().stream()
                .filter(v -> v.equipment().stream()
                        .anyMatch(item -> item.quality().equalsIgnoreCase("Legendary")))
                .collect(Collectors.toList());
    }

    public List<Viking> getRedBeardedVikingsSortedByAge() {
        return vikingService.findAll().stream()
                .filter(v -> v.hairColor() == HairColor.Red && v.beardStyle() == BeardStyle.LONG)
                .sorted((v1, v2) -> Integer.compare(v1.age(), v2.age()))
                .collect(Collectors.toList());
    }

    private List<Integer> getAllIds() {
        List<Viking> vikings = vikingService.findAll();
        return java.util.stream.IntStream.range(0, vikings.size())
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
        java.util.stream.IntStream.range(0, count)
                .forEach(i -> {
                    Viking viking = vikingFactory.createRandomViking();
                    vikingService.addSpecificViking(viking);
                });
    }
}