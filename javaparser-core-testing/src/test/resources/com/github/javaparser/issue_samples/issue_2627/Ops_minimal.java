package org.tensorflow.op;

public final class Ops {

  private Ops(Scope scope) {
    this.scope = scope;
    nn = new NnOps(scope);
    summary = new SummaryOps(scope);
    image = new ImageOps(scope);
    data = new DataOps(scope);
    io = new IoOps(scope);
    dtypes = new DtypesOps(scope);
    linalg = new LinalgOps(scope);
    random = new RandomOps(scope);
    strings = new StringsOps(scope);
    sparse = new SparseOps(scope);
    bitwise = new BitwiseOps(scope);
    math = new MathOps(scope);
    audio = new AudioOps(scope);
    signal = new SignalOps(scope);
    train = new TrainOps(scope);
    quantization = new QuantizationOps(scope);
  }


  /**
   * BatchToSpace for 4-D tensors of type T.
   *  <p>
   *  This is a legacy version of the more general BatchToSpaceND.
   *  <p>
   *  Rearranges (permutes) data from batch into blocks of spatial data, followed by
   *  cropping. This is the reverse transformation of SpaceToBatch. More specifically,
   *  this op outputs a copy of the input tensor where values from the `batch`
   *  dimension are moved in spatial blocks to the `height` and `width` dimensions,
   *  followed by cropping along the `height` and `width` dimensions.
   *
   * @param <T> data type for {@code output()} output
   * @param input 4-D tensor with shape
   *  `[batch<i>block_size</i>block_size, height_pad/block_size, width_pad/block_size,
   *    depth]`. Note that the batch size of the input tensor must be divisible by
   *  `block_size * block_size`.
   * @param crops 2-D tensor of non-negative integers with shape `[2, 2]`. It specifies
   *  how many elements to crop from the intermediate result across the spatial
   *  dimensions as follows:
   *  <p>
   *      crops = [[crop_top, crop_bottom], [crop_left, crop_right]]
   * @param blockSize
   * @return a new instance of BatchToSpace
   */
  public <T extends TType, U extends TNumber> BatchToSpace<T> batchToSpace(Operand<T> input,
                                                                           Operand<U> crops, Long blockSize) {
    return BatchToSpace.create(scope, input, crops, blockSize);
  }

  /**
   *  <p>
   *  `block_shape + [batch]`, interleaves these blocks back into the grid defined by
   *  the spatial dimensions `[1, ..., M]`, to obtain a result with the same rank as
   *  the input.  The spatial dimensions of this intermediate result are then
   *  optionally cropped according to `crops` to produce the output.  This is the
   *  reverse of SpaceToBatch.  See below for a precise description.
   *
   * @param <T> data type for {@code output()} output
   * @param input N-D with shape `input_shape = [batch] + spatial_shape + remaining_shape`,
   *  where spatial_shape has M dimensions.
   * @param blockShape 1-D with shape `[M]`, all values must be >= 1.
   * @param crops 2-D with shape `[M, 2]`, all values must be >= 0.
   *    `crops[i] = [crop_start, crop_end]` specifies the amount to crop from input
   *    dimension `i + 1`, which corresponds to spatial dimension `i`.  It is
   *    required that
   *    `crop_start[i] + crop_end[i] <= block_shape[i] * input_shape[i + 1]`.
   *  <p>
   *  This operation is equivalent to the following steps:
   *  <p>
   *  1. Reshape `input` to `reshaped` of shape:
   *       [block_shape[0], ..., block_shape[M-1],
   *        batch / prod(block_shape),
   *        input_shape[1], ..., input_shape[N-1]]
   *  <p>
   *  2. Permute dimensions of `reshaped` to produce `permuted` of shape
   *       [batch / prod(block_shape),
   *  <p>
   *        input_shape[1], block_shape[0],
   *        ...,
   *        input_shape[M], block_shape[M-1],
   *  <p>
   *        input_shape[M+1], ..., input_shape[N-1]]
   *  <p>
   *  3. Reshape `permuted` to produce `reshaped_permuted` of shape
   *       [batch / prod(block_shape),
   *  <p>
   *        input_shape[1] * block_shape[0],
   *        ...,
   *        input_shape[M] * block_shape[M-1],
   *  <p>
   *        input_shape[M+1],
   *        ...,
   *        input_shape[N-1]]
   *  <p>
   *  4. Crop the start and end of dimensions `[1, ..., M]` of
   *     `reshaped_permuted` according to `crops` to produce the output of shape:
   *       [batch / prod(block_shape),
   *  <p>
   *        input_shape[1] * block_shape[0] - crops[0,0] - crops[0,1],
   *        ...,
   *        input_shape[M] * block_shape[M-1] - crops[M-1,0] - crops[M-1,1],
   *  <p>
   *        input_shape[M+1], ..., input_shape[N-1]]
   *  <p>
   *  Some examples:
   *  <p>
   *  (1) For the following input of shape `[4, 1, 1, 1]`, `block_shape = [2, 2]`, and
   *      `crops = [[0, 0], [0, 0]]`:
   *  <pre>{@code
   *  [[[[1]]], [[[2]]], [[[3]]], [[[4]]]]
   *  }</pre>
   *  The output tensor has shape `[1, 2, 2, 1]` and value:
   *  <pre>{@code
   *  x = [[[[1], [2]], [[3], [4]]]]
   *  }</pre>
   *  (2) For the following input of shape `[4, 1, 1, 3]`, `block_shape = [2, 2]`, and
   *      `crops = [[0, 0], [0, 0]]`:
   *  <pre>{@code
   *  [[[[1, 2, 3]]], [[[4, 5, 6]]], [[[7, 8, 9]]], [[[10, 11, 12]]]]
   *  }</pre>
   *  The output tensor has shape `[1, 2, 2, 3]` and value:
   *  <pre>{@code
   *  x = [[[[1, 2, 3], [4, 5, 6]],
   *        [[7, 8, 9], [10, 11, 12]]]]
   *  }</pre>
   *  (3) For the following input of shape `[4, 2, 2, 1]`, `block_shape = [2, 2]`, and
   *      `crops = [[0, 0], [0, 0]]`:
   *  <pre>{@code
   *  x = [[[[1], [3]], [[9], [11]]],
   *       [[[2], [4]], [[10], [12]]],
   *       [[[5], [7]], [[13], [15]]],
   *       [[[6], [8]], [[14], [16]]]]
   *  }</pre>
   *  The output tensor has shape `[1, 4, 4, 1]` and value:
   *  <pre>{@code
   *  x = [[[[1],   [2],  [3],  [4]],
   *       [[5],   [6],  [7],  [8]],
   *       [[9],  [10], [11],  [12]],
   *       [[13], [14], [15],  [16]]]]
   *  }</pre>
   *  (4) For the following input of shape `[8, 1, 3, 1]`, `block_shape = [2, 2]`, and
   *      `crops = [[0, 0], [2, 0]]`:
   *  <pre>{@code
   *  x = [[[[0], [1], [3]]], [[[0], [9], [11]]],
   *       [[[0], [2], [4]]], [[[0], [10], [12]]],
   *       [[[0], [5], [7]]], [[[0], [13], [15]]],
   *       [[[0], [6], [8]]], [[[0], [14], [16]]]]
   *  }</pre>
   *  The output tensor has shape `[2, 2, 4, 1]` and value:
   *  <pre>{@code
   *  x = [[[[1],   [2],  [3],  [4]],
   *        [[5],   [6],  [7],  [8]]],
   *       [[[9],  [10], [11],  [12]],
   *        [[13], [14], [15],  [16]]]]
   *  }</pre>
   */
  public <T extends TType, U extends TNumber, V extends TNumber> BatchToSpaceNd<T> batchToSpaceNd(
          Operand<T> input, Operand<U> blockShape, Operand<V> crops) {
    return BatchToSpaceNd.create(scope, input, blockShape, crops);
  }

}
