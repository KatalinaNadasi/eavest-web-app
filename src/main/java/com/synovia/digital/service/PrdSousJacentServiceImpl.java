/**
 * 
 */
package com.synovia.digital.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synovia.digital.dto.PrdSousJacentValueDto;
import com.synovia.digital.dto.PrdSousjacentDto;
import com.synovia.digital.exceptions.EavConstraintViolationEntry;
import com.synovia.digital.exceptions.EavDuplicateEntryException;
import com.synovia.digital.exceptions.EavEntryNotFoundException;
import com.synovia.digital.exceptions.EavException;
import com.synovia.digital.model.PrdSousJacent;
import com.synovia.digital.model.PrdSousJacentValue;
import com.synovia.digital.repository.PrdSousJacentRepository;

/**
 * This class defines TODO
 * 
 * @author TeddyCouriol
 * @since 14 févr. 2017
 */
@Service
public class PrdSousJacentServiceImpl implements PrdSousJacentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrdSousJacentServiceImpl.class);

	protected final PrdSousJacentRepository repo;

	protected final PrdSousJacentValueService sousJacentValueService;

	protected static Validator dtoValidator;

	/**
	 * TODO Constructs ... based on ...
	 *
	 */
	@Autowired
	public PrdSousJacentServiceImpl(PrdSousJacentRepository repo, PrdSousJacentValueService sousJacentValueService) {
		this.repo = repo;
		this.sousJacentValueService = sousJacentValueService;
		this.dtoValidator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synovia.digital.service.PrdSousJacentService#add(com.synovia.digital.dto.
	 * PrdSousjacentDto)
	 */
	@Override
	public PrdSousJacent add(PrdSousjacentDto sousJacentDto)
			throws EavDuplicateEntryException, EavConstraintViolationEntry {
		LOGGER.debug("Adding a new PrdSousjacent entry with information: {}", sousJacentDto);
		if (sousJacentDto == null)
			return null;

		// Validate the entry
		validateEntry(sousJacentDto);

		// Search for an existing entry with the same label.
		PrdSousJacent duplicate = repo.findByLabel(sousJacentDto.getLabel());
		if (duplicate != null)
			throw new EavDuplicateEntryException(PrdSousJacent.class.getTypeName());

		// Create the PrdSousJacent object.
		PrdSousJacent toAdd = new PrdSousJacent();
		updateFromDto(toAdd, sousJacentDto);

		// Save the object to add.
		return repo.save(toAdd);

	}

	private void validateEntry(PrdSousjacentDto entry) throws EavConstraintViolationEntry {
		Set<ConstraintViolation<Object>> constraintViolations = dtoValidator.validate(entry);
		if (!constraintViolations.isEmpty())
			throw new EavConstraintViolationEntry(PrdSousjacentDto.class.getTypeName(), constraintViolations);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synovia.digital.service.PrdSousJacentService#addValue(java.lang.Long,
	 * java.util.Date, java.lang.Double)
	 */
	@Override
	public PrdSousJacent addValue(Long idSousJacent, Date date, Double value) throws EavEntryNotFoundException {
		LOGGER.debug("Updating an existing entry (add value) with information: {}", idSousJacent);
		LOGGER.debug("Add: {}", date, value);
		// Find the underlying asset.
		PrdSousJacent sousjacent = repo.findOne(idSousJacent);

		if (sousjacent == null)
			throw new EavEntryNotFoundException(PrdSousJacent.class.getTypeName());

		// Create underlying asset value.
		PrdSousJacentValueDto valueDto = new PrdSousJacentValueDto();
		valueDto.setDate(date);
		valueDto.setValue(value);
		// Add the created value.
		PrdSousJacentValue valueToAdd = sousJacentValueService.create(sousjacent, valueDto);
		sousjacent.getPrdSousJacentValues().add(valueToAdd);

		return sousjacent;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synovia.digital.service.PrdSousJacentService#addValues(java.lang.Long,
	 * java.util.Map)
	 */
	@Override
	public PrdSousJacent addValues(Long idSousJacent, Map<Date, Double> values) throws EavEntryNotFoundException {
		LOGGER.debug("Updating an existing entry (add values) with information: {}", idSousJacent);
		LOGGER.debug("Add: {}", values);
		// Find the underlying asset.
		PrdSousJacent sousjacent = repo.findOne(idSousJacent);

		if (sousjacent == null)
			throw new EavEntryNotFoundException(PrdSousJacent.class.getTypeName());

		for (Date date : values.keySet()) {
			Double value = values.get(date);
			// Create underlying asset value.
			PrdSousJacentValueDto valueDto = new PrdSousJacentValueDto();
			valueDto.setDate(date);
			valueDto.setValue(value);
			// Add the created value.
			PrdSousJacentValue valueToAdd = sousJacentValueService.create(sousjacent, valueDto);
			sousjacent.getPrdSousJacentValues().add(valueToAdd);

		}

		return sousjacent;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synovia.digital.service.PrdSousJacentService#removeValue(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public PrdSousJacent removeValue(Long idSousJacent, Long idSousJacentValue) throws EavEntryNotFoundException {
		LOGGER.debug("Updating an existing PrdSousJacent entry (remove value) with information: {}", idSousJacent);
		// Find the identified underlying asset.
		PrdSousJacent toUpdate = repo.findOne(idSousJacent);

		if (toUpdate == null)
			throw new EavEntryNotFoundException(PrdSousJacent.class.getTypeName());

		sousJacentValueService.delete(idSousJacentValue);
		return toUpdate;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synovia.digital.service.PrdSousJacentService#removeValues(java.lang.Long,
	 * java.util.List)
	 */
	@Override
	public PrdSousJacent removeValues(Long idSousJacent, List<Long> idSousJacentValues)
			throws EavEntryNotFoundException {
		LOGGER.debug("Updating an existing PrdSousJacent entry (remove values) with information: {}", idSousJacent);
		// Find the identified underlying asset.
		PrdSousJacent toUpdate = repo.findOne(idSousJacent);

		if (toUpdate == null)
			throw new EavEntryNotFoundException(PrdSousJacent.class.getTypeName());

		for (Long idValues : idSousJacentValues) {
			sousJacentValueService.delete(idValues);

		}
		return toUpdate;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synovia.digital.service.PrdSousJacentService#update(com.synovia.digital.dto.
	 * PrdSousjacentDto)
	 */
	@Override
	public PrdSousJacent update(PrdSousjacentDto updatedSousJacentDto)
			throws EavEntryNotFoundException, EavConstraintViolationEntry {
		if (updatedSousJacentDto == null)
			return null;
		// Validate entry.
		validateEntry(updatedSousJacentDto);
		// Find the corresponding entity.
		PrdSousJacent entity = repo.findOne(updatedSousJacentDto.getId());

		if (entity == null)
			throw new EavEntryNotFoundException(PrdSousJacent.class.getTypeName());

		// Update the entity.
		updateFromDto(entity, updatedSousJacentDto);

		return entity;

	}

	/**
	 * Updates an underlying asset entity from a container of data to update that
	 * represents an underlying asset.
	 * 
	 * @param entity
	 *            The underlying asset to update.
	 * @param dto
	 *            The updated underlying asset.
	 * @throws EavException
	 */
	private void updateFromDto(PrdSousJacent entity, PrdSousjacentDto dto) {
		if (dto.getLabel() != null) {
			entity.setLabel(dto.getLabel());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synovia.digital.service.PrdSousJacentService#findAll()
	 */
	@Override
	public List<PrdSousJacent> findAll() {
		return repo.findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synovia.digital.service.PrdSousJacentService#findById(java.lang.Long)
	 */
	@Override
	public PrdSousJacent findById(Long id) throws EavEntryNotFoundException {
		PrdSousJacent toFind = repo.findOne(id);

		if (toFind == null)
			throw new EavEntryNotFoundException(PrdSousJacent.class.getTypeName());

		return toFind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.synovia.digital.service.PrdSousJacentService#addValue(com.synovia.digital.dto.
	 * PrdSousJacentValueDto)
	 */
	@Override
	public PrdSousJacent addValue(PrdSousJacentValueDto valueDto) throws EavEntryNotFoundException {
		// Find the PrdSousJacent from input value.
		PrdSousJacent toUpdate = repo.findOne(valueDto.getIdPrdSousJacent());

		// If not found, throws an exception.
		if (toUpdate == null)
			throw new EavEntryNotFoundException(PrdSousJacent.class.getTypeName());

		// Create the PrdSousJacentValue object.
		PrdSousJacentValue sousJacentValue = sousJacentValueService.create(toUpdate, valueDto);

		// Add the value.
		toUpdate.getPrdSousJacentValues().add(sousJacentValue);

		return toUpdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synovia.digital.service.PrdSousJacentService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		repo.delete(id);

	}

}
