package com.resumebuilder.DTO;

public class ForgotPassword {

    private String newPassword;

    private String confirmPassword;


    public ForgotPassword() {
        super();
    }


    public ForgotPassword(String newPassword, String confirmPassword) {
		super();
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}


	public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


	public String getConfirmPassword() {
		return confirmPassword;
	}


	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}


	@Override
	public String toString() {
		return "ForgotPassword [newPassword=" + newPassword + ", confirmPassword=" + confirmPassword + "]";
	}

    

}