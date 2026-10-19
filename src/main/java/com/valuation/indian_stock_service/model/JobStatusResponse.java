package com.valuation.indian_stock_service.model;

import lombok.Getter;
import lombok.Setter;

public class JobStatusResponse {
  @Getter @Setter
  private String jobId;
  @Getter @Setter
  private String status;
  @Getter @Setter
  private String message;
  @Getter @Setter
  private Object data;

  public JobStatusResponse(String jobId, String status, String message, Object data) {
    this.jobId = jobId;
    this.status = status;
    this.message = message;
    this.data = data;
  }

}
