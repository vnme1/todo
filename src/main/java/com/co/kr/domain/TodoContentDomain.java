package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName="builder")
public class TodoContentDomain {
	
	private Integer bdSeq;
	private String mbId;

	//private String bdTitle;
	private String bdDone;
	private String bdContent;

}
