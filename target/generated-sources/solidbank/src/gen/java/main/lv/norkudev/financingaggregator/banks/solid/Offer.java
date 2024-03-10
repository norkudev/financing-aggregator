/*
 * SolidBank financing application API
 * SoldBank API financing application and offer retrieval API
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package lv.norkudev.financingaggregator.banks.solid;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.math.BigDecimal;
/**
 * Offer
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-03-10T18:49:12.597548900+02:00[Europe/Riga]")

public class Offer {
  @SerializedName("monthlyPaymentAmount")
  private BigDecimal monthlyPaymentAmount = null;

  @SerializedName("totalRepaymentAmount")
  private BigDecimal totalRepaymentAmount = null;

  @SerializedName("numberOfPayments")
  private Integer numberOfPayments = null;

  @SerializedName("annualPercentageRate")
  private BigDecimal annualPercentageRate = null;

  @SerializedName("firstRepaymentDate")
  private String firstRepaymentDate = null;

  public Offer monthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
    this.monthlyPaymentAmount = monthlyPaymentAmount;
    return this;
  }

   /**
   * Get monthlyPaymentAmount
   * @return monthlyPaymentAmount
  **/
  @Schema(description = "")
  public BigDecimal getMonthlyPaymentAmount() {
    return monthlyPaymentAmount;
  }

  public void setMonthlyPaymentAmount(BigDecimal monthlyPaymentAmount) {
    this.monthlyPaymentAmount = monthlyPaymentAmount;
  }

  public Offer totalRepaymentAmount(BigDecimal totalRepaymentAmount) {
    this.totalRepaymentAmount = totalRepaymentAmount;
    return this;
  }

   /**
   * Get totalRepaymentAmount
   * @return totalRepaymentAmount
  **/
  @Schema(description = "")
  public BigDecimal getTotalRepaymentAmount() {
    return totalRepaymentAmount;
  }

  public void setTotalRepaymentAmount(BigDecimal totalRepaymentAmount) {
    this.totalRepaymentAmount = totalRepaymentAmount;
  }

  public Offer numberOfPayments(Integer numberOfPayments) {
    this.numberOfPayments = numberOfPayments;
    return this;
  }

   /**
   * Get numberOfPayments
   * @return numberOfPayments
  **/
  @Schema(description = "")
  public Integer getNumberOfPayments() {
    return numberOfPayments;
  }

  public void setNumberOfPayments(Integer numberOfPayments) {
    this.numberOfPayments = numberOfPayments;
  }

  public Offer annualPercentageRate(BigDecimal annualPercentageRate) {
    this.annualPercentageRate = annualPercentageRate;
    return this;
  }

   /**
   * Get annualPercentageRate
   * @return annualPercentageRate
  **/
  @Schema(description = "")
  public BigDecimal getAnnualPercentageRate() {
    return annualPercentageRate;
  }

  public void setAnnualPercentageRate(BigDecimal annualPercentageRate) {
    this.annualPercentageRate = annualPercentageRate;
  }

  public Offer firstRepaymentDate(String firstRepaymentDate) {
    this.firstRepaymentDate = firstRepaymentDate;
    return this;
  }

   /**
   * Get firstRepaymentDate
   * @return firstRepaymentDate
  **/
  @Schema(description = "")
  public String getFirstRepaymentDate() {
    return firstRepaymentDate;
  }

  public void setFirstRepaymentDate(String firstRepaymentDate) {
    this.firstRepaymentDate = firstRepaymentDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Offer offer = (Offer) o;
    return Objects.equals(this.monthlyPaymentAmount, offer.monthlyPaymentAmount) &&
        Objects.equals(this.totalRepaymentAmount, offer.totalRepaymentAmount) &&
        Objects.equals(this.numberOfPayments, offer.numberOfPayments) &&
        Objects.equals(this.annualPercentageRate, offer.annualPercentageRate) &&
        Objects.equals(this.firstRepaymentDate, offer.firstRepaymentDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(monthlyPaymentAmount, totalRepaymentAmount, numberOfPayments, annualPercentageRate, firstRepaymentDate);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Offer {\n");
    
    sb.append("    monthlyPaymentAmount: ").append(toIndentedString(monthlyPaymentAmount)).append("\n");
    sb.append("    totalRepaymentAmount: ").append(toIndentedString(totalRepaymentAmount)).append("\n");
    sb.append("    numberOfPayments: ").append(toIndentedString(numberOfPayments)).append("\n");
    sb.append("    annualPercentageRate: ").append(toIndentedString(annualPercentageRate)).append("\n");
    sb.append("    firstRepaymentDate: ").append(toIndentedString(firstRepaymentDate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}