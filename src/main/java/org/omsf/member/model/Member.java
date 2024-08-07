package org.omsf.member.model;

import java.sql.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
* @packageName    : org.omsf.member.model
* @fileName       : Member.java
* @author         : leeyunbin
* @date           : 2024.06.18
* @description    :
* ===========================================================
* DATE              AUTHOR             NOTE
* -----------------------------------------------------------
* 2024.06.18        leeyunbin       최초 생성
*/

@ToString
@Getter
@Setter
@NoArgsConstructor
public class Member { // yunbin
	
	@NotEmpty(message = "아이디(이메일) 입력은 필수입니다.")
	@Email(message = "유효한 이메일 주소를 입력하세요.")
	private String username;
	
	@Pattern(regexp = "^(?!.*\\|)(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "비밀번호는 8~16자의 영문 대/소문자, 숫자, 특수문자를 포함해야 합니다.")
	@NotEmpty(message = "비밀번호 입력은 필수입니다.")
	private String password;
	
	@NotEmpty(message = "비밀번호 확인은 필수입니다.")
	private String passwordConfirm;
	
	private String memberType; 
	private Date createdAt;
	private Date modifiedAt;
	private String profileImage;
	
	public Member(String memberType) {
		this.memberType=memberType;
	}
	
	public Member(String username, String password, String memberType, Date createdAt, Date modifiedAt) {
		this.username = username;
		this.password = password;
		this.memberType = memberType;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
	
}
