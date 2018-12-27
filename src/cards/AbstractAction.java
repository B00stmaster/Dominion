package cards;

public abstract class AbstractAction extends AbstractCard {

	public AbstractAction() {
		super();
		this.types.add(Type.ACTION);
	}
}
