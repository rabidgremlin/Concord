package com.rabidgremlin.concord.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    UserVoteRatio that = (UserVoteRatio) o;

    return new EqualsBuilder()
        .append(voteRatio, that.voteRatio)
        .append(userId, that.userId)
        .isEquals();
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(17, 37)
        .append(userId)
        .append(voteRatio)
        .toHashCode();
  }
}
