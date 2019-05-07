
package services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EducationDataRepository;
import domain.Curriculum;
import domain.EducationData;
import domain.Mark;
import domain.Rookie;

@Service
@Transactional
public class EducationDataService {

	@Autowired
	private EducationDataRepository	educationDataRepository;
	@Autowired
	private RookieService			rookieService;
	@Autowired
	private CurriculumService		curriculumService;


	public void save(EducationData e) {
		this.educationDataRepository.save(e);
	}

	public void deleteInBatch(Iterable<EducationData> entities) {
		this.educationDataRepository.deleteInBatch(entities);
	}

	public void addOrUpdateEducationDataAsRookie(EducationData educationData, int curriculumId) {
		Rookie rookie = this.rookieService.securityAndRookie();

		if (educationData.getId() == 0) {
			Curriculum curriculum = this.curriculumService.getCurriculumOfRookie(rookie.getId(), curriculumId);
			Assert.notNull(curriculum);
			if (educationData.getMark().equals(Mark.APLUS) || educationData.getMark().equals(Mark.APLUSPLUS)) {
				educationData.setMark(Mark.FMINUS);
			}
			List<EducationData> educationsData = curriculum.getEducationData();
			educationsData.add(educationData);
			this.curriculumService.save(curriculum);
			this.curriculumService.flush();
		} else {
			Assert.notNull(this.educationDataRepository.getEducationDataOfRookie(rookie.getId(), educationData.getId()));
			if (educationData.getMark().equals(Mark.APLUS) || educationData.getMark().equals(Mark.APLUSPLUS)) {
				educationData.setMark(Mark.FMINUS);
			}
			this.save(educationData);
			this.flush();
		}
	}
	private void flush() {
		this.educationDataRepository.flush();
	}

	public EducationData findOne(int educationDataId) {
		return this.educationDataRepository.findOne(educationDataId);
	}

	public void deleteEducationDataAsRookie(int educationDataId) {
		Rookie rookie = this.rookieService.securityAndRookie();
		Assert.notNull(this.educationDataRepository.getEducationDataOfRookie(rookie.getId(), educationDataId));
		EducationData educationData = this.findOne(educationDataId);
		Curriculum curriculum = this.curriculumService.getCurriculumOfEducationData(educationDataId);
		List<EducationData> educationsData = curriculum.getEducationData();
		educationsData.remove(educationData);
		curriculum.setEducationData(educationsData);
		this.curriculumService.save(curriculum);
		this.educationDataRepository.delete(educationData);
	}

}
