package com.rabidgremlin.concord.plugin;

import java.util.List;

/**
 * Responsible for returning a list of all phrase/intent labels from the system.
 * E.g. from a database or txt file.
 */
@FunctionalInterface
public interface SystemLabelStore
{
  List<SystemLabel> getSystemLabels();
}
