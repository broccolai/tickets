package broccolai.tickets.api.utilities;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

public final class Either<L, R> {

    private final L left;
    private final R right;

    private Either(final @Nullable L left, final @Nullable R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Either<L, R> left(final @NonNull L left) {
        return new Either<>(left, null);
    }

    public static <L, R> Either<L, R> right(final @NonNull R right) {
        return new Either<>(null, right);
    }

    public boolean isLeft() {
        return this.left != null;
    }

    public boolean isRight() {
        return this.right != null;
    }

    public L left() {
        return this.left;
    }

    public R right() {
        return this.right;
    }

    public <O> O map(final @NonNull Function<L, O> leftMapper, final @NonNull Function<R, O> rightMapper) {
        return this.isLeft() ? leftMapper.apply(this.left) : rightMapper.apply(this.right);
    }

}
