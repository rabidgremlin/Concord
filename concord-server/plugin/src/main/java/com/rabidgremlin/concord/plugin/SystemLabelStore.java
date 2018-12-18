package com.rabidgremlin.concord.plugin;

import java.util.List;

/**
 * Responsible for returning list of all phrase/intent labels from the system.
 */
public interface SystemLabelStore
{
  List<SystemLabel> getSystemLabels();
}
