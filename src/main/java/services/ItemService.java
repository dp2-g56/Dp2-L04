
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ItemRepository;
import domain.Item;
import domain.Provider;
import forms.FormObjectItem;

@Service
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository	itemRepository;

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
	
	public List<Item> getLoggedProviderItems(){
		Provider loggedProvider = this.providerService.securityAndProvider();
		return this.itemRepository.getItemsByProvider(loggedProvider);
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
	
	public void deleteItem(int itemId) {
		Provider provider = this.providerService.securityAndProvider();
		Item item = this.getItemOfProvider(itemId, provider.getId());
		Assert.notNull(item);
		
		List<Item> items = provider.getItems();
		items.remove(item);
		provider.setItems(items);
		
		this.providerService.save(provider);
		this.delete(item);
		
	}

	private Item getItemOfProvider(int itemId, int providerId) {
		return this.itemRepository.getItemOfProvider(itemId, providerId);
	}

	public List<String> getLinksOfItem(int itemId) {
		Provider loggedProvider = this.providerService.securityAndProvider();
		return this.itemRepository.getLinksOfItem(itemId, loggedProvider.getId());
	}

	public List<String> getPicturesOfItem(int itemId) {
		Provider loggedProvider = this.providerService.securityAndProvider();
		return this.itemRepository.getPicturesOfItem(itemId, loggedProvider.getId());
	}

	public void deleteInBatch(Iterable<Item> lista) {
		this.itemRepository.deleteInBatch(lista);
	}

}
