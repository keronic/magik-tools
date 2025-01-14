package nl.ramsolutions.sw.magik.analysis.typing;

import edu.umd.cs.findbugs.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Container to hold resulting {@link TypeString}s. */
public class ExpressionResultString {

  /** Stream collector. */
  public static final Collector<TypeString, ?, ExpressionResultString> COLLECTOR =
      Collector.of(
          ArrayList<TypeString>::new,
          List::add,
          (list, values) -> {
            list.addAll(values);
            return list;
          },
          ExpressionResultString::new);

  /** Max number of items in a {@link ExpressionResultString}. */
  public static final int MAX_ITEMS = 1024; // 1024 is max for _scatter

  /**
   * Instance of {@link ExpressionResultString} to be used in all cases of undefined expression
   * results.
   */
  public static final ExpressionResultString UNDEFINED =
      new ExpressionResultString(Collections.nCopies(MAX_ITEMS, TypeString.UNDEFINED));

  /** Serialized name of {@link ExpressionResultString.UNDEFINED}. */
  public static final String UNDEFINED_SERIALIZED_NAME = "__UNDEFINED_RESULT__";

  /** Instance of an empty {@link ExpressionResultString}. */
  public static final ExpressionResultString EMPTY = new ExpressionResultString();

  private final List<TypeString> types;

  /** Empty result constructor. */
  public ExpressionResultString() {
    this(Collections.emptyList());
  }

  /** Array/utility constructor. */
  public ExpressionResultString(final TypeString... types) {
    this(List.of(types));
  }

  /**
   * List constructor.
   *
   * @param types Types this {@link ExpressionResult} represents.
   */
  public ExpressionResultString(final List<TypeString> types) {
    this.types = Collections.unmodifiableList(types);
  }

  /** Combine constructor. */
  public ExpressionResultString(
      final ExpressionResultString result1, final @Nullable ExpressionResultString result2) {
    if (result2 == null) {
      this.types = result1.getTypes();
    } else {
      final int size = Math.max(result1.size(), result2.size());
      final List<TypeString> combinedTypes = new ArrayList<>(size);
      for (int i = 0; i < size; ++i) {
        final TypeString type1 = result1.get(i, TypeString.SW_UNSET);
        final TypeString type2 = result2.get(i, TypeString.SW_UNSET);
        final TypeString combinedType = TypeString.combine(type1, type2);
        combinedTypes.add(combinedType);
      }

      this.types = Collections.unmodifiableList(combinedTypes);
    }
  }

  /**
   * Test if is empty.
   *
   * @return True if empty, false otherwise.
   */
  public boolean isEmpty() {
    return this.types.isEmpty();
  }

  /**
   * Get types.
   *
   * @return Type strings.
   */
  public List<TypeString> getTypes() {
    return Collections.unmodifiableList(this.types);
  }

  /**
   * Get type at index.
   *
   * @param index Index of type.
   * @return Type at index.
   */
  public TypeString get(final int index, final @Nullable TypeString unsetType) {
    if (this.types.isEmpty() || index >= this.types.size()) {
      return unsetType;
    }

    return this.types.get(index);
  }

  public int size() {
    return this.types.size();
  }

  /**
   * Substitue {@code from} by {@code to} in a copy of self.
   *
   * @param from From type.
   * @param to To type.
   * @return New {@link ExpressionResultString}.
   */
  public ExpressionResultString substituteType(final TypeString from, final TypeString to) {
    return this.types.stream()
        .map(typeString -> typeString.substituteType(from, to))
        .collect(ExpressionResultString.COLLECTOR);
  }

  /**
   * Get type names of all items of the result.
   *
   * @return Type names of items of the result.
   */
  public String getTypeNames(final String separator) {
    if (this == ExpressionResultString.UNDEFINED) {
      return "UNDEFINED...";
    }

    // Determine first index of trailing homogenous sequence.
    int firstRepeatingIndex = MAX_ITEMS;
    TypeString lastType = null;
    if (this.types.size() == MAX_ITEMS) {
      lastType = this.get(firstRepeatingIndex - 1, null);
      for (int i = this.types.size() - 1; i > -1; --i) {
        final TypeString type = this.types.get(i);
        if (type.equals(lastType)) {
          firstRepeatingIndex = i;
        } else {
          break;
        }
      }
    }

    final StringBuilder builder = new StringBuilder();
    final String typesStr =
        this.types.stream()
            .limit(firstRepeatingIndex)
            .map(TypeString::getFullString)
            .collect(Collectors.joining(separator));
    builder.append(typesStr);

    // If a trailing sequence was found, append one with three dots.
    if (lastType != null) {
      if (!typesStr.isEmpty()) {
        builder.append(separator);
      }

      builder.append(lastType).append("...");
    }
    return builder.toString();
  }

  /**
   * Get stream of types contained by this {@link ExpressionResult}.
   *
   * @return Stream of types.
   */
  public Stream<TypeString> stream() {
    return this.types.stream();
  }

  /**
   * Get full string.
   *
   * @return Full string.
   */
  public String getFullString() {
    if (this == UNDEFINED) {
      return UNDEFINED_SERIALIZED_NAME;
    }

    return this.types.stream().map(TypeString::getFullString).collect(Collectors.joining(","));
  }

  @Override
  @SuppressWarnings("checkstyle:NestedIfDepth")
  public String toString() {
    return String.format(
        "%s@%s(%s)",
        this.getClass().getName(), Integer.toHexString(this.hashCode()), this.getTypeNames(","));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.types.toArray());
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (this.getClass() != obj.getClass()) {
      return false;
    }

    final ExpressionResultString other = (ExpressionResultString) obj;
    return Objects.equals(this.types, other.types);
  }
}
