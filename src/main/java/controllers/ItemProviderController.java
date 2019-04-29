
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
import domain.Rookie;
import domain.Item;
import domain.PersonalData;
import forms.FormObjectCurriculumPersonalData;
import forms.FormObjectItem;
import services.CurriculumService;
import services.RookieService;
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
	
	@RequestMapping(value = "/listLinks", method = RequestMethod.GET)
	public ModelAndView listLinks(@RequestParam int itemId) {
		ModelAndView result;
		
		try {
			List<String> links = this.itemService.getLinksOfItem(itemId);
			
			result = new ModelAndView("provider/links");
			result.addObject("links", links);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/listPictures", method = RequestMethod.GET)
	public ModelAndView listPictures(@RequestParam int itemId) {
		ModelAndView result;
		
		try {
			List<String> pictures = this.itemService.getPicturesOfItem(itemId);
			
			result = new ModelAndView("provider/pictures");
			result.addObject("pictures", pictures);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteItem(@RequestParam int itemId) {
		ModelAndView result;
		
		try {
			this.itemService.deleteItem(itemId);
			result = new ModelAndView("redirect:list.do");
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}
		
		return result;	
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		
		try {
			this.providerService.securityAndProvider();
			
			FormObjectItem formObjectItem = new FormObjectItem();
			
			result = new ModelAndView("provider/createItem");
			result.addObject("formObjectItem", formObjectItem);
		} catch(Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		
		return result;	
	}

}
