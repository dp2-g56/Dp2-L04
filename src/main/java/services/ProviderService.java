package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import domain.Hacker;
import domain.Provider;
import repositories.ProviderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ProviderService {
	
	@Autowired
	private ProviderRepository providerRepository;
	
	
	public Provider findOne(int id) {
		return providerRepository.findOne(id);
	}
	
	public void delete(Provider provider) {
		this.providerRepository.delete(provider);
	}
	
	public List<Provider> findAll(){
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
	
	


}
