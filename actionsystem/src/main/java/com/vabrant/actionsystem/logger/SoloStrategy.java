
package com.vabrant.actionsystem.logger;

public interface SoloStrategy {
	public void solo (ActionLogger action, boolean solo);

	public boolean isActive ();

	public boolean canPrint (ActionLogger logger);
}
