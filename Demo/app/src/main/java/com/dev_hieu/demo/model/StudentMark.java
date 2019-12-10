package com.dev_hieu.demo.model;

public class StudentMark {
    //Mã sinh viên
    private int studentID;
    //Mã lớp học
    private String subjectID;
    //Phòng học
    private String classRoom;
    //Ngày điểm danh
    private String markDate;

    //Trạng thái
    private String status;


    public StudentMark(int studentID, String subjectID, String classRoom, String markDate, String status) {
        this.studentID = studentID;
        this.subjectID = subjectID;
        this.classRoom = classRoom;
        this.markDate = markDate;
        this.status = status;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getMarkDate() {
        return markDate;
    }

    public void setMarkDate(String markDate) {
        this.markDate = markDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
