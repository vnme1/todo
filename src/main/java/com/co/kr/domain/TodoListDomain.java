package com.co.kr.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName="builder")
public class TodoListDomain {

	private String bdSeq;
	private String mbId;
	//private String bdTitle;
	private String bdContent;
	private String bdDone;
	private String bdCreateAt;
	private String bdUpdateAt;

}