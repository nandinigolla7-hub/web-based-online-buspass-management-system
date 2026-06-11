package com.example.buspass_2;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Random;

import jakarta.persistence.Column;

@Entity
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String mobile;
    private String password;
    private String userType;
    private String approvalStatus;
    private String passType;
    private String previousPassFile;
    private String facultyStatus;
    private String expiryDate;
    private String facultySignature;
    private String passCategory;
    private String passNumber;
    private String issueDate;
    private String photo;
    private String applicationStatus;
    private Double fee;
    private String institutionType;
    private String institutionName;
    private String institutionArea;
    private String institutionCode;
    private Integer classLevel;
    private String dateOfBirth;
    private Integer age;
    private String otp;
    private LocalDate otpExpiry;

    public Double getFee() {
        return fee;
    }

    public void setFee(double fee2) {
        this.fee = (double) fee2;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

	public String getFacultyStatus() {
		return facultyStatus;
	}

	public void setFacultyStatus(String facultyStatus) {
		this.facultyStatus = facultyStatus;
	}

	public String getPreviousPassFile() {
		return previousPassFile;
	}

	public void setPreviousPassFile(String previousPassFile) {
		this.previousPassFile = previousPassFile;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getFacultySignature() {
		return facultySignature;
	}

	public void setFacultySignature(String facultySignature) {
		this.facultySignature = facultySignature;
	}
	private String source;

	private String destination;

	private String route;
	public String getSource() {
	    return source;
	}

	public void setSource(String source) {
	    this.source = source;
	}

	public String getDestination() {
	    return destination;
	}

	public void setDestination(String destination) {
	    this.destination = destination;
	}

	public String getRoute() {
	    return route;
	}

	public void setRoute(String route) {
	    this.route = route;
	}
	public String getPassCategory() {
		return passCategory;
	}

	public void setPassCategory(String passCategory) {
		this.passCategory = passCategory;
	}

	public String getPassNumber() {
		return passNumber;
	}

	public void setPassNumber(String passNumber) {
		this.passNumber = passNumber;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public String getInstitutionType() {
		return institutionType;
	}

	public void setInstitutionType(String institutionType) {
		this.institutionType = institutionType;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public String getInstitutionArea() {
		return institutionArea;
	}

	public void setInstitutionArea(String institutionArea) {
		this.institutionArea = institutionArea;
	}

	public String getInstitutionCode() {
		return institutionCode;
	}

	public void setInstitutionCode(String institutionCode) {
		this.institutionCode = institutionCode;
	}

	public Integer getClassLevel() {
		return classLevel;
	}

	public void setClassLevel(Integer classLevel) {
		this.classLevel = classLevel;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDate getOtpExpiry() {
		return otpExpiry;
	}

	public void setOtpExpiry(LocalDate otpExpiry) {
		this.otpExpiry = otpExpiry;
	}
   
}