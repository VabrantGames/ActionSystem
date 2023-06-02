
package com.vabrant.actionsystem.logger;

public class SingleSoloStrategy implements SoloStrategy {

	private ActionLogger logger;

	@Override
	public void solo (ActionLogger logger, boolean solo) {
		if (this.logger != null && !solo) {
			if (this.logger.equals(logger)) {
				this.logger = null;
			}
		} else if (solo) {
			this.logger = logger;
		}
	}

	@Override
	public boolean isActive () {
		return logger != null;
	}

	@Override
	public boolean canPrint (ActionLogger logger) {
		return (this.logger != null && this.logger.equals(logger));
	}
}
