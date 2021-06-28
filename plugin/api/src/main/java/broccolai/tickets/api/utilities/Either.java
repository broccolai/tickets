package broccolai.tickets.api.utilities;

import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public record Either<L, R>(L left, R right) {

    public Either(final @Nullable L left, final @Nullable R right) {
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

    public <O> O map(final @NonNull Function<L, O> leftMapper, final @NonNull Function<R, O> rightMapper) {
        return this.isLeft() ? leftMapper.apply(this.left) : rightMapper.apply(this.right);
    }

}
