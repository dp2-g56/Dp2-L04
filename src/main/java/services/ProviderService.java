
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import repositories.ProviderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.CreditCard;
import domain.Item;
import domain.Provider;
import forms.FormObjectEditProvider;

@Service
@Transactional
public class ProviderService {

	@Autowired
	private ProviderRepository		providerRepository;

	@Autowired
	private ItemService				itemService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ConfigurationService	configurationService;


	public Provider findOne(int id) {
		return this.providerRepository.findOne(id);
	}

	public void delete(Provider provider) {
		this.providerRepository.delete(provider);
	}

	public List<Provider> findAll() {
		return this.providerRepository.findAll();
	}

	public Provider save(Provider provider) {
		return this.providerRepository.save(provider);
	}

	public Provider loggedProvider() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		return this.providerRepository.getProviderByUsername(userAccount.getUsername());
	}

	// Security

	public void loggedAsProvider() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("PROVIDER"));

	}

	public Provider securityAndProvider() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		List<Authority> authorities = (List<Authority>) userAccount.getAuthorities();
		Assert.isTrue(authorities.get(0).toString().equals("PROVIDER"));
		return this.providerRepository.getProviderByUsername(userAccount.getUsername());
	}

	public void deleteProvider() {
		Provider provider = new Provider();
		List<Item> items = new ArrayList<Item>();
		provider = this.loggedProvider();

		items = provider.getItems();

		provider.getItems().clear();

		this.itemService.deleteInBatch(items);

		this.sponsorshipService.deleteAllSponsorships();

		this.providerRepository.delete(provider);
	}

	public FormObjectEditProvider getFormObjectEditProvider(Provider provider) {

		FormObjectEditProvider res = new FormObjectEditProvider();

		//Company
		res.setAddress(provider.getAddress());
		res.setName(provider.getName());
		res.setVATNumber(provider.getVATNumber());
		res.setPhoto(provider.getPhoto());
		res.setEmail(provider.getEmail());
		res.setAddress(provider.getAddress());
		res.setSurname(provider.getSurname());
		res.setPhone(provider.getPhone());
		res.setMake(provider.getMake());

		//Credit Card
		CreditCard c = provider.getCreditCard();

		res.setHolderName(c.getHolderName());
		res.setBrandName(c.getBrandName());
		res.setNumber(c.getNumber());
		res.setExpirationMonth(c.getExpirationMonth());
		res.setExpirationYear(c.getExpirationYear());
		res.setCvvCode(c.getCvvCode());

		return res;

	}

	public Provider reconstructAuditorPersonalData(FormObjectEditProvider formObjectProvider, BindingResult binding) {
		Provider res = new Provider();

		Provider provider = this.findOne(formObjectProvider.getId());

		CreditCard card = new CreditCard();

		//Credit Card
		card.setBrandName(formObjectProvider.getBrandName());
		card.setCvvCode(formObjectProvider.getCvvCode());
		card.setExpirationMonth(formObjectProvider.getExpirationMonth());
		card.setExpirationYear(formObjectProvider.getExpirationYear());
		card.setHolderName(formObjectProvider.getHolderName());
		card.setNumber(formObjectProvider.getNumber());

		res.setAddress(formObjectProvider.getAddress());

		res.setEmail(formObjectProvider.getEmail());
		res.setPhone(formObjectProvider.getPhone());
		res.setPhoto(formObjectProvider.getPhoto());
		res.setMake(formObjectProvider.getMake());
		res.setSurname(formObjectProvider.getSurname());
		res.setName(formObjectProvider.getName());
		res.setId(formObjectProvider.getId());
		res.setVATNumber(formObjectProvider.getVATNumber());
		res.setCreditCard(card);
		res.setHasSpam(provider.getHasSpam());
		res.setMessages(provider.getMessages());
		res.setSocialProfiles(provider.getSocialProfiles());
		res.setUserAccount(provider.getUserAccount());
		res.setVersion(provider.getVersion());
		res.setItems(provider.getItems());
		res.setSponsorships(provider.getSponsorships());

		if (card.getNumber() != null)
			if (!this.creditCardService.validateNumberCreditCard(card))
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
					binding.addError(new FieldError("formObject", "number", formObjectProvider.getNumber(), false, null, null, "El numero de la tarjeta es invalido"));
				else
					binding.addError(new FieldError("formObject", "number", formObjectProvider.getNumber(), false, null, null, "The card number is invalid"));

		if (card.getExpirationMonth() != null && card.getExpirationYear() != null)
			if (!this.creditCardService.validateDateCreditCard(card))
				if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "La tarjeta no puede estar caducada"));
				else
					binding.addError(new FieldError("formObject", "expirationMonth", card.getExpirationMonth(), false, null, null, "The credit card can not be expired"));

		List<String> cardType = this.configurationService.getConfiguration().getCardType();

		if (!cardType.contains(res.getCreditCard().getBrandName()))
			if (LocaleContextHolder.getLocale().getLanguage().toUpperCase().contains("ES"))
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "Tarjeta no admitida"));
			else
				binding.addError(new FieldError("formObject", "brandName", card.getBrandName(), false, null, null, "The credit card is not accepted"));

		return res;

	}
}
