package com.rabidgremlin.concord.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }

    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    UserVoteCount that = (UserVoteCount) o;

    return new EqualsBuilder()
        .append(voteCount, that.voteCount)
        .append(userId, that.userId)
        .isEquals();
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(17, 37)
        .append(userId)
        .append(voteCount)
        .toHashCode();
  }

}
