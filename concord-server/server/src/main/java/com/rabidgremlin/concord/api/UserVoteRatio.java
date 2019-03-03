package com.rabidgremlin.concord.api;

public class UserVoteRatio
{
    private final String userId;

    private final double voteRatio;

    public UserVoteRatio(String userId, double voteRatio)
    {
        this.userId = userId;
        this.voteRatio = voteRatio;
    }

    public double getVoteRatio()
    {
        return voteRatio;
    }

    public String getUserId()
    {
        return userId;
    }

    @Override
    public String toString()
    {
        return "UserVoteRatio{" +
                "userId='" + userId + '\'' +
                ", voteRatio=" + voteRatio +
                '}';
    }

}
