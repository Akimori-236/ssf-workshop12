package sg.edu.nus.iss.app.ssfworkshop12.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.app.ssfworkshop12.exception.RandomNumberException;
import sg.edu.nus.iss.app.ssfworkshop12.models.Generate;

@Controller
@RequestMapping(path = "/rand")
public class GenerateController {

    // @RequestMapping(path = { "/", "index.html" })
    // public class IndexResource {

    // fully qualified uri being /rand/show
    @GetMapping(path = "/show")
    public String showRandForm(Model model) {
        // instantiate generate object
        Generate g = new Generate();
        // associate the bind var to the view/page
        model.addAttribute("generateObj", g);
        return "generate"; // actually generate.html in the templates folder
    }

    // USING POST
    @PostMapping(path = "/generate")
    public String postRandomNumber(@ModelAttribute Generate generate, Model model) {
        this.randomiseNum(model, generate.getNumVal());
        return "result";
    }

    // USING QUERY STRING
    @GetMapping(path = "/generate")
    public String GenerateWithQueryString(@RequestParam Integer numberVal, Model model) {
        this.randomiseNum(model, numberVal.intValue());
        return "result";
    }

    // USING PATH VARIABLE
    @GetMapping(path = "/generate/{numberVal}")
    public String GenerateWithPathVar(@PathVariable Integer numberVal, Model model) {
        this.randomiseNum(model, numberVal.intValue());
        return "result";
    }

    private void randomiseNum(Model model, int noOfGenerateNo) {
        int maxGenNo = 30;
        String[] imgNumbers = new String[maxGenNo + 1];

        // Validation: only accept ge 0 le 30
        if (noOfGenerateNo < 0 || noOfGenerateNo > maxGenNo) {
            throw new RandomNumberException();
        }

        // pre-generating all the image filenames and store in array
        for (int i = 0; i <= maxGenNo; i++) {
            imgNumbers[i] = "number" + i + ".jpg";
        }

        // generating the random numbers
        List<String> selectedImg = new ArrayList<>();
        Random rnd = new Random();
        // get unique results
        Set<Integer> uniqueResults = new LinkedHashSet<Integer>();
        while (uniqueResults.size() < noOfGenerateNo) {
            Integer resultofRand = rnd.nextInt(maxGenNo);
            uniqueResults.add(resultofRand);
        }

        Iterator<Integer> it = uniqueResults.iterator();
        Integer currElem = null;
        while (it.hasNext()) {
            currElem = it.next();
            selectedImg.add(imgNumbers[currElem.intValue()]);
        }

        model.addAttribute("RandomNum", noOfGenerateNo);
        model.addAttribute("RandomResult", selectedImg.toArray());
    }

}
