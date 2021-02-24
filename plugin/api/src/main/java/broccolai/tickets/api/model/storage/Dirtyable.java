package broccolai.tickets.api.model.storage;

public interface Dirtyable {

    /**
     * Check if the object is dirty
     *
     * @return {@code true} if the object is dirty
     */
    boolean dirty();

    abstract class Impl implements Dirtyable {
        private boolean dirty = false;

        @Override
        public boolean dirty() {
            return this.dirty;
        }

        public void dirty(final boolean dirty) {
            this.dirty = dirty;
        }

    }

}
