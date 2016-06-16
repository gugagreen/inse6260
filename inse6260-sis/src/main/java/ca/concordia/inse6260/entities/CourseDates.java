package ca.concordia.inse6260.entities;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ca.concordia.inse6260.entities.enums.Season;

@Entity
@NamedQuery(name="CourseDates.findSeasons", query="SELECT DISTINCT CONCAT(cd.season,substring(cd.startDate,1,4)) FROM CourseDates cd")
public class CourseDates {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;
	@Temporal(TemporalType.DATE)
	private Calendar startDate;
	@Temporal(TemporalType.DATE)
	private Calendar endDate;
	private String weekDays;
	@Temporal(TemporalType.TIME)
	private Date startTime;
	@Temporal(TemporalType.TIME)
	private Date endTime;
	@Column(name = "season", nullable = false)
	@Enumerated(EnumType.STRING)
	private Season season;

	public CourseDates() {
		super();
	}

	@Override
	public String toString() {
		return "CourseDates [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", weekDays="
				+ weekDays + ", startTime=" + startTime + ", endTime=" + endTime + ", season=" + season + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public String getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(String weekDays) {
		this.weekDays = weekDays;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}
}
