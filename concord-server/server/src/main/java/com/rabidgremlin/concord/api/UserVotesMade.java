package com.rabidgremlin.concord.api;

public class UserVotesMade
{
  private String userId;

  private int votesMade;

  // Needed for Bean Mapper
  public UserVotesMade()
  {
  }

  public UserVotesMade(String userId, int votesMade)
  {
    this.userId = userId;
    this.votesMade = votesMade;
  }

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
