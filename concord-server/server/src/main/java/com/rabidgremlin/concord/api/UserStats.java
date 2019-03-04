package com.rabidgremlin.concord.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserStats
{

  private final String userId;

  private final int totalVotes;

  private final int completedVotes;

  private final int trashVotes;

  private final float completedSuccessRatio;

  private final float trashRatio;

  public UserStats(String userId, int totalVotes, int completedVotes, int trashVotes, float completedSuccessRatio, float trashRatio)
  {
    this.userId = userId;
    this.totalVotes = totalVotes;
    this.completedVotes = completedVotes;
    this.trashVotes = trashVotes;
    this.completedSuccessRatio = completedSuccessRatio;
    this.trashRatio = trashRatio;
  }

  public String getUserId()
  {
    return userId;
  }

  public int getTotalVotes()
  {
    return totalVotes;
  }

  public int getCompletedVotes()
  {
    return completedVotes;
  }

  public int getTrashVotes()
  {
    return trashVotes;
  }

  public float getCompletedSuccessRatio()
  {
    return completedSuccessRatio;
  }

  public float getTrashRatio()
  {
    return trashRatio;
  }

  @Override
  public String toString()
  {
    return "UserStats{" +
        "userId='" + userId + '\'' +
        ", totalVotes=" + totalVotes +
        ", completedVotes=" + completedVotes +
        ", trashVotes=" + trashVotes +
        ", completedSuccessRatio=" + completedSuccessRatio +
        ", trashRatio=" + trashRatio +
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

    UserStats userStats = (UserStats) o;

    return new EqualsBuilder()
        .append(totalVotes, userStats.totalVotes)
        .append(completedVotes, userStats.completedVotes)
        .append(trashVotes, userStats.trashVotes)
        .append(completedSuccessRatio, userStats.completedSuccessRatio)
        .append(trashRatio, userStats.trashRatio)
        .append(userId, userStats.userId)
        .isEquals();
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder(17, 37)
        .append(userId)
        .append(totalVotes)
        .append(completedVotes)
        .append(trashVotes)
        .append(completedSuccessRatio)
        .append(trashRatio)
        .toHashCode();
  }

}
