package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.service.VikingService;
import ru.mephi.vikingdemo.service.VikingLambdaService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;
    private final VikingLambdaService lambdaService;

    public VikingController(VikingService vikingService, VikingListener vikingListener, VikingLambdaService lambdaService) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
        this.lambdaService = lambdaService;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов",
            operationId = "getAllVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов",
            operationId = "getTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    public void addViking(){
        vikingListener.testAdd();
    }

    @PostMapping("/add")
    public Viking addViking(@RequestBody Viking viking) {
        System.out.println("POST /api/vikings/add called");
        vikingListener.addVikingAndRefresh(viking);
        return viking;
    }

    @DeleteMapping("/{index}")
    public boolean deleteViking(@PathVariable int index) {
        System.out.println("DELETE /api/vikings/" + index + " called");
        vikingListener.deleteVikingAndRefresh(index);
        return true;
    }

    @PutMapping("/{index}")
    public boolean updateViking(@PathVariable int index, @RequestBody Viking viking) {
        System.out.println("PUT /api/vikings/" + index + " called");
        vikingListener.updateVikingAndRefresh(index, viking);
        return true;
    }

    @GetMapping("/count/age/greater/{age}")
    public long countAgeGreater(@PathVariable int age) {
        return lambdaService.countByAgeGreaterThan(age);
    }

    @GetMapping("/count/age/less/{age}")
    public long countAgeLess(@PathVariable int age) {
        return lambdaService.countByAgeLessThan(age);
    }

    @GetMapping("/count/age/between")
    public long countAgeBetween(@RequestParam int min, @RequestParam int max) {
        return lambdaService.countByAgeBetween(min, max);
    }

    @GetMapping("/count/age/outside")
    public long countAgeOutside(@RequestParam int min, @RequestParam int max) {
        return lambdaService.countByAgeOutside(min, max);
    }

    @GetMapping("/count/beard-and-hair")
    public long countByBeardAndHair(@RequestParam BeardStyle beard, @RequestParam HairColor hair) {
        return lambdaService.countByBeardAndHair(beard, hair);
    }

    @GetMapping("/count/axes")
    public long countByAxes() {
        return lambdaService.countByAxesOneOrTwo();
    }

    @GetMapping("/random-height-above-180")
    public Viking getRandomVikingHeightAbove180() {
        return lambdaService.getRandomVikingHeightAbove180();
    }

    @GetMapping("/legendary-equipment")
    public List<Viking> getVikingsWithLegendaryEquipment() {
        return lambdaService.getVikingsWithLegendaryEquipment();
    }

    @GetMapping("/red-bearded-sorted-by-age")
    public List<Viking> getRedBeardedSortedByAge() {
        return lambdaService.getRedBeardedVikingsSortedByAge();
    }

    @GetMapping("/ids/max")
    public Integer getMaxId() {
        return lambdaService.getMaxId();
    }

    @GetMapping("/ids/even")
    public List<Integer> getEvenIds() {
        return lambdaService.getEvenIds();
    }

    @PostMapping("/generate/{count}")
    public String generateVikings(@PathVariable int count) {
        vikingListener.generateAndRefresh(count);
        return "Generated " + count + " random vikings";
    }
}