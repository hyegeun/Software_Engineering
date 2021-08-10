package com.example.sogong_project;

public class BoardInfo {
    private String boardName;
    private String boardPurpose;

    public BoardInfo() {
    }

    public BoardInfo(String boardName, String boardPurpose) {
        this.boardName = boardName;
        this.boardPurpose = boardPurpose;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardPurpose() {
        return boardPurpose;
    }

    public void setBoardPurpose(String boardPurpose) {
        this.boardPurpose = boardPurpose;
    }
}
