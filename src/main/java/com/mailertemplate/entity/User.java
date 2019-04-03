package com.mailertemplate.entity;

import java.util.List;

public class User {
	private String name;
	private String email;
	private String oracleId;
	private String classroomLearningHours;
	private String classroomSessionCount;
	private String onlineLearningHours;
	private String onlineCourseCount;
	private List<Upskilling> upskillingList;
	private List<String> enrollButNotAttendedList;
	private String totalLearningDays;
	private List<String> classroomSessions;
	private List<String> onlineCourses;
	private String certificationsDone;
	
	
	public User() {}
	
	public User(String oracleId) {
		super();
		this.oracleId = oracleId;
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
	public String getOracleId() {
		return oracleId;
	}
	public void setOracleId(String oracleId) {
		this.oracleId = oracleId;
	}
	public String getClassroomLearningHours() {
		return classroomLearningHours;
	}
	public void setClassroomLearningHours(String classroomLearningHours) {
		this.classroomLearningHours = classroomLearningHours;
	}
	public String getClassroomSessionCount() {
		return classroomSessionCount;
	}
	public void setClassroomSessionCount(String classroomSessionCount) {
		this.classroomSessionCount = classroomSessionCount;
	}
	public String getOnlineLearningHours() {
		return onlineLearningHours;
	}
	public void setOnlineLearningHours(String onlineLearningHours) {
		this.onlineLearningHours = onlineLearningHours;
	}
	public String getOnlineCourseCount() {
		return onlineCourseCount;
	}
	public void setOnlineCourseCount(String onlineCourseCount) {
		this.onlineCourseCount = onlineCourseCount;
	}
	public List<Upskilling> getUpskillingList() {
		return upskillingList;
	}
	public void setUpskillingList(List<Upskilling> upskillingList) {
		this.upskillingList = upskillingList;
	}
	public List<String> getEnrollButNotAttendedList() {
		return enrollButNotAttendedList;
	}
	public void setEnrollButNotAttendedList(List<String> enrollButNotAttendedList) {
		this.enrollButNotAttendedList = enrollButNotAttendedList;
	}
	public String getTotalLearningDays() {
		return totalLearningDays;
	}
	public void setTotalLearningDays(String totalLearningDays) {
		this.totalLearningDays = totalLearningDays;
	}
	public List<String> getClassroomSessions() {
		return classroomSessions;
	}
	public void setClassroomSessions(List<String> classroomSessions) {
		this.classroomSessions = classroomSessions;
	}
	public List<String> getOnlineCourses() {
		return onlineCourses;
	}
	public void setOnlineCourses(List<String> onlineCourses) {
		this.onlineCourses = onlineCourses;
	}
	
	@Override
    public boolean equals(Object object) {
        if(object instanceof User) {
        	User u = (User) object;
            return this.oracleId.equals(u.oracleId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return oracleId.hashCode();
    }

	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", oracleId=" + oracleId + ", classroomLearningHours="
				+ classroomLearningHours + ", classroomSessionCount=" + classroomSessionCount + ", onlineLearningHours="
				+ onlineLearningHours + ", onlineCourseCount=" + onlineCourseCount + ", upskillingList="
				+ upskillingList + ", enrollButNotAttendedList=" + enrollButNotAttendedList + ", totalLearningDays="
				+ totalLearningDays + ", classroomSessions=" + classroomSessions + ", onlineCourses=" + onlineCourses
				+ ", certificationsDone=" + certificationsDone + "]";
	}

	public String getCertificationsDone() {
		return certificationsDone;
	}

	public void setCertificationsDone(String certificationsDone) {
		this.certificationsDone = certificationsDone;
	}
    
    
}
