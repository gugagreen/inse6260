package ca.concordia.inse6260.dao;

import org.springframework.data.repository.CrudRepository;

import ca.concordia.inse6260.entities.AcademicRecordEntry;

public interface AcademicRecordEntryDAO extends CrudRepository<AcademicRecordEntry, Long> {

}
