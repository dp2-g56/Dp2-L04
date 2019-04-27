package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Item;
import domain.Provider;
import forms.FormObjectItem;
import repositories.ItemRepository;

@Service
@Transactional
public class ItemService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ProviderService	providerService;
	
	
	public Item createItem() {
		
		Item res = new Item();
		
		List<String> voidList = new ArrayList<String>();
		
		res.setDescription("");
		res.setLinks(voidList);
		res.setPictures(voidList);
		res.setName("");

		return res;
	}
	
	public Item findOne(Integer id) {
		return this.itemRepository.findOne(id);
	}
	
	public Item save(Item item) {
		return this.itemRepository.save(item);
	}
	
	public List<Item> findAll(){
		return this.itemRepository.findAll();
	}
	
	public void delete(Item item) {
		this.itemRepository.delete(item);
	}
	
	public List<Item> getLogguedProviderItems(){
		Provider logguedProvider = this.providerService.loggedProvider();
		return this.itemRepository.getItemsByProvier(logguedProvider);
	}
	
	public void saveItem(Item item) {
		Provider loggProvider = this.providerService.loggedProvider();
		List<Item> items = loggProvider.getItems();
		
		if(item.getId()!=0) {
			
			Item itemDB = this.findOne(item.getId());
			Assert.isTrue(items.contains(itemDB));
			this.save(item);
			
		}else {
		
		Item savedItem = this.save(item);
		
		items.add(savedItem);
		
		loggProvider.setItems(items);
		
		this.providerService.save(loggProvider);
		}
		
	}
	
	public FormObjectItem prepareFormObject(int itemId) {
		Item item = this.findOne(itemId);
		
		FormObjectItem res = new FormObjectItem();
		
		res.setDescription(item.getDescription());
		res.setName(item.getName());
		res.setId(item.getId());

		String links = "";
		for (String link : item.getLinks()) {
			links = links + link + ",";
		}
		res.setLinks(links);

		String pictures = "";
		for (String pic : item.getPictures()) {
			pictures = pictures + pic + ",";
		}
		res.setPictures(pictures);
		
		return res;
	}
	
	public Item reconstructItem(FormObjectItem form) {
		Item res;
		
		if(form.getId()==0) {
			res = this.createItem();
		}else {
			res= this.findOne(form.getId());
		}
		
		res.setDescription(form.getDescription());
		res.setName(form.getName());
		
		String[] link = form.getLinks().trim().split(",");
		List<String> links = Arrays.asList(link);
		
		res.setLinks(links);
		
		
		String[] pic = form.getPictures().trim().split(",");
		List<String> pictures = Arrays.asList(pic);

		res.setPictures(pictures);

		return res;
	}
	
	public void deleteItem(Item item) {
		Provider provider = this.providerService.loggedProvider();
		Assert.notNull(item);
		
		List<Item> items = this.getLogguedProviderItems();
		Assert.isTrue(items.contains(item));
		
		items.remove(item);
		
		provider.setItems(items);
		
		this.providerService.save(provider);
		this.delete(item);
		
	}

}
