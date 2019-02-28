package com.rabidgremlin.concord.api;

public class UserVotesMade
{
    private String userId;

    private int votesMade;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getVotesMade()
    {
        return votesMade;
    }

    public void setVotesMade(int votesMade)
    {
        this.votesMade = votesMade;
    }

    @Override
    public String toString()
    {
        return "UserVotesMade{" +
                "userId='" + userId + '\'' +
                ", votesMade=" + votesMade +
                '}';
    }

}
