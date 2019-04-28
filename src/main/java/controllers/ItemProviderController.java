
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curriculum;
import domain.Hacker;
import domain.Item;
import domain.PersonalData;
import forms.FormObjectCurriculumPersonalData;
import services.CurriculumService;
import services.HackerService;
import services.ItemService;
import services.PersonalDataService;
import services.ProviderService;

@Controller
@RequestMapping("/item/provider")
public class ItemProviderController extends AbstractController {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private ProviderService providerService;

	public ItemProviderController() {
		super();
	} 

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		
		try {
			List<Item> items = this.itemService.getLoggedProviderItems();
			
			result = new ModelAndView("provider/items");
			result.addObject("items", items);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		
		return result;	
	}


}
