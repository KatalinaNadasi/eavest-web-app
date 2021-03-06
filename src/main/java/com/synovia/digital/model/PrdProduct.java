package com.synovia.digital.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "prd_product", schema = "test")
public class PrdProduct extends AbstractBean {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "ISIN", nullable = false, unique = true)
	private String isin;

	@Column(name = "LABEL", nullable = false)
	private String label;

	@Column(name = "DUE_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dueDate;

	@Column(name = "LAUNCH_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date launchDate;

	@JoinColumn(name = "ID_PRD_SOUSJACENT", referencedColumnName = "ID", nullable = false)
	@ManyToOne
	private PrdSousJacent prdSousJacent;

	@JoinColumn(name = "ID_PRD_RULE", referencedColumnName = "ID")
	@OneToOne()
	private PrdRule prdRule;

	@OneToMany(mappedBy = "idPrdProduct")
	private Set<PrdObservationDate> observationDates;

	@OneToMany(mappedBy = "idPrdProduct")
	private Set<PrdEarlierRepaymentDate> earlyRepaymentDates;

	@OneToMany(mappedBy = "idPrdProduct")
	private Set<PrdCouponDate> couponPaymentDates;

	@Column(name = "SUBSCRIBE_START_DATE")
	@Temporal(TemporalType.DATE)
	private Date subscriptionStartDate;

	@Column(name = "SUBSCRIBE_END_DATE")
	@Temporal(TemporalType.DATE)
	private Date subscriptionEndDate;

	@Column(name = "COUPON", precision = 2)
	private Double couponValue;

	@Column(name = "NOMINAL")
	private Double nominalValue;

	@Column(name = "CAPITAL_GUARANTEED")
	private Boolean capitalGuaranteed;

	@Column(name = "START_PRICE")
	/** Translation of "prix d'emission" */
	private Double startPrice;

	@Column(name = "DELIVER")
	private String deliver;

	@Column(name = "GUARANTOR")
	private String guarantor;

	/** The Status of this entity. Is updated in post-update method. */
	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_PRD_STATUS", referencedColumnName = "ID")
	private PrdStatus status;

	@ManyToMany(mappedBy = "products")
	private Set<PrdUser> prdUsers;

	@Column(name = "END_DATE")
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Column(name = "IS_EAVEST")
	private Boolean isEavest = false;

	@Column(name = "BEST_SELLER")
	private Boolean isBestSeller = false;

	@Column(name = "PATH")
	private String path;

	@Column(name = "STRIKE")
	private Double strike;

	@Transient
	private DateFormat format = new SimpleDateFormat("dd/MM/yyyy");

	public PrdProduct() {
	}

	/**
	 * @param label
	 * @param dueDate
	 * @param dateOfCreation
	 * @param idSousJacent
	 * @param idRule
	 * @param subscriptionStart
	 * @param subscriptionEnd
	 * @param coupon
	 * @param nominalValue
	 * @param isGuaranteed
	 * @param deliver
	 * @param guarantor
	 * @param startPrice
	 */
	public PrdProduct(String label, Date dueDate, Date dateOfCreation, PrdSousJacent idSousJacent, PrdRule idRule,
			Date subscriptionStart, Date subscriptionEnd, Double coupon, Double nominalValue, Boolean isGuaranteed,
			String deliver, String guarantor, Double startPrice) {
		this.label = label;
		this.dueDate = dueDate;
		this.launchDate = dateOfCreation;
		this.prdSousJacent = idSousJacent;
		this.prdRule = idRule;
		this.subscriptionEndDate = subscriptionEnd;
		this.subscriptionStartDate = subscriptionStart;
		this.couponValue = coupon;
		this.nominalValue = nominalValue;
		this.capitalGuaranteed = isGuaranteed;
		this.deliver = deliver;
		this.guarantor = guarantor;
		this.startPrice = startPrice;
	}

	/**
	 * @param label
	 * @param dueDate
	 * @param dateOfCreation
	 * @param idSousJacent
	 * @param idRule
	 * @param subscriptionStart
	 * @param subscriptionEnd
	 * @param coupon
	 * @param nominalValue
	 * @param isGuaranteed
	 * @param deliver
	 * @param guarantor
	 * @param startPrice
	 * @throws ParseException
	 */
	public PrdProduct(String label, String dueDate, String dateOfCreation, PrdSousJacent idSousJacent, PrdRule idRule,
			String subscriptionStart, String subscriptionEnd, Double coupon, Double nominalValue, Boolean isGuaranteed,
			String deliver, String guarantor, Double startPrice) throws ParseException {
		this(label, (Date) null, (Date) null, idSousJacent, idRule, (Date) null, (Date) null, coupon, nominalValue,
				isGuaranteed, deliver, guarantor, startPrice);
		this.dueDate = format.parse(dueDate);
		this.launchDate = format.parse(dateOfCreation);
		this.subscriptionEndDate = format.parse(subscriptionEnd);
		this.subscriptionStartDate = format.parse(subscriptionStart);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getDueDateAsString() {
		return format.format(dueDate);
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getLaunchDate() {
		return launchDate;
	}

	public String getLaunchDateAsString() {
		return format.format(launchDate);
	}

	public void setLaunchDate(Date creationDate) {
		this.launchDate = creationDate;
	}

	public PrdSousJacent getPrdSousJacent() {
		return prdSousJacent;
	}

	public void setPrdSousJacent(PrdSousJacent idPrdSousJacent) {
		this.prdSousJacent = idPrdSousJacent;
	}

	public PrdRule getPrdRule() {
		return prdRule;
	}

	public void setPrdRule(PrdRule idPrdRule) {
		this.prdRule = idPrdRule;
	}

	public Set<PrdObservationDate> getWatchingDates() {
		return observationDates;
	}

	public void setWatchingDates(Set<PrdObservationDate> watchingDates) {
		this.observationDates = watchingDates;
	}

	public Set<PrdEarlierRepaymentDate> getEarlyRepaymentDates() {
		return earlyRepaymentDates;
	}

	public void setEarlyRepaymentDates(Set<PrdEarlierRepaymentDate> earlyRepaymentDates) {
		this.earlyRepaymentDates = earlyRepaymentDates;
	}

	public Set<PrdCouponDate> getCouponPaymentDates() {
		return couponPaymentDates;
	}

	public void setCouponPaymentDates(Set<PrdCouponDate> couponPaymentDates) {
		this.couponPaymentDates = couponPaymentDates;
	}

	public Date getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	public String getSubscriptionStartDateAsString() {
		return format.format(subscriptionStartDate);
	}

	public void setSubscriptionStartDate(Date subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	public Date getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public String getSubscriptionEndDateAsString() {
		return format.format(subscriptionEndDate);
	}

	public void setSubscriptionEndDate(Date subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public Double getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(Double couponValue) {
		this.couponValue = couponValue;
	}

	public Boolean getCapitalGuaranteed() {
		return capitalGuaranteed;
	}

	public void setCapitalGuaranteed(Boolean capitalGuaranteed) {
		this.capitalGuaranteed = capitalGuaranteed;
	}

	public Double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}

	public String getDeliver() {
		return deliver;
	}

	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}

	public String getGuarantor() {
		return guarantor;
	}

	public void setGuarantor(String guarantor) {
		this.guarantor = guarantor;
	}

	public Double getNominalValue() {
		return nominalValue;
	}

	public void setNominalValue(Double nominalValue) {
		this.nominalValue = nominalValue;
	}

	public String getIsin() {
		return this.isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public Set<PrdObservationDate> getObservationDates() {
		return this.observationDates;
	}

	public void setObservationDates(Set<PrdObservationDate> observationDates) {
		this.observationDates = observationDates;
	}

	public Set<PrdUser> getPrdUsers() {
		return this.prdUsers;
	}

	public void setPrdUsers(Set<PrdUser> prdUsers) {
		this.prdUsers = prdUsers;
	}

	public PrdStatus getStatus() {
		return this.status;
	}

	public void setStatus(PrdStatus status) {
		this.status = status;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Double getStrike() {
		return this.strike;
	}

	public void setStrike(Double strike) {
		this.strike = strike;
	}

	public Boolean getIsEavest() {
		return this.isEavest;
	}

	public void setIsEavest(Boolean isEavest) {
		this.isEavest = isEavest;
	}

	public Boolean getIsBestSeller() {
		return this.isBestSeller;
	}

	public void setIsBestSeller(Boolean isBestSeller) {
		this.isBestSeller = isBestSeller;
	}
}
