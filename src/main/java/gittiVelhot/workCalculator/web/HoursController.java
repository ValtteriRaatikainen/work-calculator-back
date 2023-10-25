package gittiVelhot.workCalculator.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import gittiVelhot.workCalculator.domain.User;
import gittiVelhot.workCalculator.domain.UserRepository;
import gittiVelhot.workCalculator.domain.WorkingHours;
import gittiVelhot.workCalculator.domain.WorkingHoursRepository;

@Controller
public class HoursController {

	@Autowired
	private UserRepository urepository;
	@Autowired
	private WorkingHoursRepository wrepository;

	// home sivulta linkki jossa voi lisätä työvuoron userille.
	// localhost:8080/home
	// localhost:8080/add
	// https://work-calculator-back-fe87ca711a8e.herokuapp.com/home
	// https://work-calculator-back-fe87ca711a8e.herokuapp.com/api/tyoaika

	// endpoint for homepage
	@RequestMapping("/home")
	public String homePage() {
		return "home"; // Palautetaan "home", joka viittaa home.html-sivun nimeen.
	}

	// find users by id REST
	@RequestMapping(value = "/api/findbyuser", method = RequestMethod.GET)
	public @ResponseBody User userRest(Principal user) {
		return urepository.findByUsername(user.getName());
	}

	// find ALL existing users REST
	@RequestMapping(value = "/api/findusers", method = RequestMethod.GET)
	public @ResponseBody List<User> userlistRest() {
		return (List<User>) urepository.findAll();
	}

	// List all workinghours REST
	@RequestMapping(value = "/api/workinghours", method = RequestMethod.GET)
	public @ResponseBody List<WorkingHours> tuntilistaRest() {
		return (List<WorkingHours>) wrepository.findAll();
	}

	// add working hours
	@RequestMapping(value = "/api/addhours")
	public String addHours(Model model) {
		model.addAttribute("workingHours", new WorkingHours());

		return "addHours";
	}

	// save new workinghours
	@RequestMapping(value = "/api/savehours", method = RequestMethod.POST)
	public String save(@ModelAttribute("WorkingHours") WorkingHours workingHours, Principal principal) {
		String username = principal.getName();
		User user = urepository.findByUsername(username);

		// Set hours to user
		workingHours.setUser(user);
		wrepository.save(workingHours);
		return "redirect:home";
	}

	// Delete existing workinghours using id
	@RequestMapping(value = "/api/delete/{id}", method = RequestMethod.GET)
	public String deleteHours(@PathVariable Long id) {
		wrepository.deleteById(id);
		return "redirect:../hoursList";
	}

	// List all existing hours using thymeleaf
	@RequestMapping(value = "/api/lishours")
	public String hoursList(Model model) {
		List<WorkingHours> workingHoursList = (List<WorkingHours>) wrepository.findAll();
		model.addAttribute("workingHours", workingHoursList);
		return "hoursList";
	}

	// edit existing workinghours
	@RequestMapping(value = "/api/edithours/{id}", method = RequestMethod.GET)
	public String editHours(@PathVariable("id") Long id, Model model, WorkingHours hours) {
		model.addAttribute("hours", wrepository.findById(id));
		model.addAttribute("workingId", id);
		return "editHours";
	}

	// update your edits to existing working hours
	@RequestMapping(value = "/api/update/{id}", method = RequestMethod.POST)
	public String updateHours(@PathVariable("id") Long id, Model model, WorkingHours hours) {
		wrepository.save(hours);
		return "redirect:../hoursList";
	}

}
