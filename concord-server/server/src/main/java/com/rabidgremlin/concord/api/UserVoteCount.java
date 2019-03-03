package com.rabidgremlin.concord.api;

public class UserVoteCount
{
  private String userId;

  private int voteCount;

  // Needed for Bean Mapper
  public UserVoteCount()
  {
  }

  public UserVoteCount(String userId, int voteCount)
  {
    this.userId = userId;
    this.voteCount = voteCount;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  public int getVoteCount()
  {
    return voteCount;
  }

  public void setVoteCount(int voteCount)
  {
    this.voteCount = voteCount;
  }

  @Override
  public String toString()
  {
    return "UserVoteCount{" +
        "userId='" + userId + '\'' +
        ", voteCount=" + voteCount +
        '}';
  }

}
