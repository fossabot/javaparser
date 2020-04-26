package org.tensorflow.op;

public final class Ops {
    public <T extends TType, U extends TNumber> BatchToSpace<T> batchToSpace(Operand<T> input, Operand<U> crops, Long blockSize) {
        return BatchToSpace.create(scope, input, crops, blockSize);
    }

    /**
     *
     * @param input      N-D with shape `input_shape = [batch] + spatial_shape + remaining_shape`,
     *                   where spatial_shape has M dimensions.
     * @param blockShape 1-D with shape `[M]`, all values must be >= 1.
     * @param crops      2-D with shape `[M, 2]`, all values must be >= 0.
     *                   input_shape[1], ..., input_shape[N-1]]
     *                   <p>
     *                   2. Permute dimensions of `reshaped` to produce `permuted` of shape
     *                   [batch / prod(block_shape),
     *                   <p>
     *                   input_shape[1], block_shape[0],
     *                   ...,
     *                   input_shape[M], block_shape[M-1],
     *                   <p>
     *                   input_shape[M+1], ..., input_shape[N-1]]
     *                   <p>
     *                   3. Reshape `permuted` to produce `reshaped_permuted` of shape
     *                   [batch / prod(block_shape),
     *                   <p>
     *                   input_shape[1] * block_shape[0],
     *                   ...,
     *                   input_shape[M] * block_shape[M-1],
     *                   <p>
     *                   input_shape[M+1],
     *                   ...,
     *                   input_shape[N-1]]
     *                   <p>
     *                   4. Crop the start and end of dimensions `[1, ..., M]` of
     *                   `reshaped_permuted` according to `crops` to produce the output of shape:
     *                   [batch / prod(block_shape),
     *                   <p>
     *                   input_shape[1] * block_shape[0] - crops[0,0] - crops[0,1],
     *                   ...,
     *                   input_shape[M] * block_shape[M-1] - crops[M-1,0] - crops[M-1,1],
     *                   <p>
     *                   input_shape[M+1], ..., input_shape[N-1]]
     *                   <p>
     *                   Some examples:
     *                   <p>
     *                   (1) For the following input of shape `[4, 1, 1, 1]`, `block_shape = [2, 2]`, and
     *                   `crops = [[0, 0], [0, 0]]`:
     *                   <pre>{@code
     *                                                        [[[[1]]], [[[2]]], [[[3]]], [[[4]]]]
     *                                                        }</pre>
     *                   The output tensor has shape `[1, 2, 2, 1]` and value:
     *                   <pre>{@code
     *                                                        x = [[[[1], [2]], [[3], [4]]]]
     *                                                        }</pre>
     *                   (2) For the following input of shape `[4, 1, 1, 3]`, `block_shape = [2, 2]`, and
     *                   `crops = [[0, 0], [0, 0]]`:
     *                   <pre>{@code
     *                                                        [[[[1, 2, 3]]], [[[4, 5, 6]]], [[[7, 8, 9]]], [[[10, 11, 12]]]]
     *                                                        }</pre>
     *                   The output tensor has shape `[1, 2, 2, 3]` and value:
     *                   <pre>{@code
     *                                                        x = [[[[1, 2, 3], [4, 5, 6]],
     *                                                              [[7, 8, 9], [10, 11, 12]]]]
     *                                                        }</pre>
     *                   (3) For the following input of shape `[4, 2, 2, 1]`, `block_shape = [2, 2]`, and
     *                   `crops = [[0, 0], [0, 0]]`:
     *                   <pre>{@code
     *                                                        x = [[[[1], [3]], [[9], [11]]],
     *                                                             [[[2], [4]], [[10], [12]]],
     *                                                             [[[5], [7]], [[13], [15]]],
     *                                                             [[[6], [8]], [[14], [16]]]]
     *                                                        }</pre>
     *                   The output tensor has shape `[1, 4, 4, 1]` and value:
     *                   <pre>{@code
     *                                                        x = [[[[1],   [2],  [3],  [4]],
     *                                                             [[5],   [6],  [7],  [8]],
     *                                                             [[9],  [10], [11],  [12]],
     *                                                             [[13], [14], [15],  [16]]]]
     *                                                        }</pre>
     *                   (4) For the following input of shape `[8, 1, 3, 1]`, `block_shape = [2, 2]`, and
     *                   `crops = [[0, 0], [2, 0]]`:
     *                   <pre>{@code
     *                                                        x = [[[[0], [1], [3]]], [[[0], [9], [11]]],
     *                                                             [[[0], [2], [4]]], [[[0], [10], [12]]],
     *                                                             [[[0], [5], [7]]], [[[0], [13], [15]]],
     *                                                             [[[0], [6], [8]]], [[[0], [14], [16]]]]
     *                                                        }</pre>
     *                   The output tensor has shape `[2, 2, 4, 1]` and value:
     *                   <pre>{@code
     *                                                        x = [[[[1],   [2],  [3],  [4]],
     *                                                              [[5],   [6],  [7],  [8]]],
     *                                                             [[[9],  [10], [11],  [12]],
     *                                                              [[13], [14], [15],  [16]]]]
     *                                                        }</pre>
     */
    public <T extends TType, U extends TNumber, V extends TNumber> BatchToSpaceNd<T> batchToSpaceNd(Operand<T> input, Operand<U> blockShape, Operand<V> crops) {
        return BatchToSpaceNd.create(scope, input, blockShape, crops);
    }

}
