
package controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Item;
import forms.FormObjectItem;
import services.ItemService;
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
		} catch (Throwable oops) {
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
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/");
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
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteItem(@RequestParam int itemId) {
		ModelAndView result;

		try {
			this.itemService.deleteItem(itemId);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/");
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
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int itemId) {
		ModelAndView result;

		try {
			FormObjectItem formObjectItem = this.itemService.prepareFormObject(itemId);

			result = new ModelAndView("provider/editItem");
			result.addObject("formObjectItem", formObjectItem);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/");
		}

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView saveItem(@ModelAttribute("formObjectItem") @Valid FormObjectItem formObjectItem,
			BindingResult binding) {
		ModelAndView result;

		String tiles;
		if (formObjectItem.getId() > 0)
			tiles = "provider/editItem";
		else
			tiles = "provider/createItem";

		Item item = new Item();

		item = this.itemService.reconstructItem(formObjectItem);

		String locale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();

		// Links URL
		if (!formObjectItem.getLinks().isEmpty())
			for (String s : item.getLinks())
				if (!this.itemService.isUrl(s))
					if (locale.contains("ES"))
						binding.addError(new FieldError("formObjectItem", "links", formObjectItem.getLinks(), false,
								null, null, "URLs incorrecta"));
					else
						binding.addError(new FieldError("formObjectItem", "links", formObjectItem.getLinks(), false,
								null, null, "Wrong URLs"));

		// Links URL
		if (!formObjectItem.getPictures().isEmpty())
			for (String s : item.getPictures())
				if (!this.itemService.isUrl(s))
					if (locale.contains("ES"))
						binding.addError(new FieldError("formObjectItem", "pictures", formObjectItem.getPictures(),
								false, null, null, "URLs incorrecta"));
					else
						binding.addError(new FieldError("formObjectItem", "pictures", formObjectItem.getPictures(),
								false, null, null, "Wrong URLs"));

		if (binding.hasErrors())
			result = this.createEditModelAndView(tiles, formObjectItem);
		else
			try {
				this.itemService.addOrUpdateItem(item);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(tiles, formObjectItem, "commit.error");
			}
		return result;
	}

	private ModelAndView createEditModelAndView(String tiles, FormObjectItem formObjectItem) {
		ModelAndView result = new ModelAndView(tiles);
		result.addObject("formObjectItem", formObjectItem);
		return result;
	}

	private ModelAndView createEditModelAndView(String tiles, FormObjectItem formObjectItem, String message) {
		ModelAndView result = this.createEditModelAndView(tiles, formObjectItem);
		result.addObject("message", message);
		return result;
	}

}
