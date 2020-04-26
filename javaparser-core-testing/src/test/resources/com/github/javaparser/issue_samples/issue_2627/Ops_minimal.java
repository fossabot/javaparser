// Copyright 2020 The TensorFlow Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ==============================================================================
//
// This class has been generated, DO NOT EDIT!
//
package org.tensorflow.op;

import java.nio.charset.Charset;
import java.util.List;
import org.tensorflow.DataType;
import org.tensorflow.EagerSession;
import org.tensorflow.ExecutionEnvironment;
import org.tensorflow.Operand;
import org.tensorflow.Tensor;
import org.tensorflow.op.core.Abort;
import org.tensorflow.op.core.All;
import org.tensorflow.op.core.Any;
import org.tensorflow.op.core.AssertThat;
import org.tensorflow.op.core.Assign;
import org.tensorflow.op.core.AssignAdd;
import org.tensorflow.op.core.AssignAddVariableOp;
import org.tensorflow.op.core.AssignSub;
import org.tensorflow.op.core.AssignSubVariableOp;
import org.tensorflow.op.core.AssignVariableOp;
import org.tensorflow.op.core.Barrier;
import org.tensorflow.op.core.BarrierClose;
import org.tensorflow.op.core.BarrierIncompleteSize;
import org.tensorflow.op.core.BarrierInsertMany;
import org.tensorflow.op.core.BarrierReadySize;
import org.tensorflow.op.core.BarrierTakeMany;
import org.tensorflow.op.core.Batch;
import org.tensorflow.op.core.BatchToSpace;
import org.tensorflow.op.core.BatchToSpaceNd;
import org.tensorflow.op.core.Bitcast;
import org.tensorflow.op.core.BroadcastDynamicShape;
import org.tensorflow.op.core.BroadcastTo;
import org.tensorflow.op.core.Bucketize;
import org.tensorflow.op.core.ClipByValue;
import org.tensorflow.op.core.Concat;
import org.tensorflow.op.core.Constant;
import org.tensorflow.op.core.ConsumeMutexLock;
import org.tensorflow.op.core.ControlTrigger;
import org.tensorflow.op.core.CountUpTo;
import org.tensorflow.op.core.DeepCopy;
import org.tensorflow.op.core.DeleteSessionTensor;
import org.tensorflow.op.core.DestroyResourceOp;
import org.tensorflow.op.core.DestroyTemporaryVariable;
import org.tensorflow.op.core.DynamicPartition;
import org.tensorflow.op.core.DynamicStitch;
import org.tensorflow.op.core.EditDistance;
import org.tensorflow.op.core.Empty;
import org.tensorflow.op.core.EmptyTensorList;
import org.tensorflow.op.core.EnsureShape;
import org.tensorflow.op.core.ExpandDims;
import org.tensorflow.op.core.ExtractVolumePatches;
import org.tensorflow.op.core.Fill;
import org.tensorflow.op.core.Fingerprint;
import org.tensorflow.op.core.Gather;
import org.tensorflow.op.core.GatherNd;
import org.tensorflow.op.core.GetSessionHandle;
import org.tensorflow.op.core.GetSessionTensor;
import org.tensorflow.op.core.Gradients;
import org.tensorflow.op.core.GuaranteeConst;
import org.tensorflow.op.core.HashTable;
import org.tensorflow.op.core.Helpers;
import org.tensorflow.op.core.HistogramFixedWidth;
import org.tensorflow.op.core.Identity;
import org.tensorflow.op.core.IdentityN;
import org.tensorflow.op.core.ImmutableConst;
import org.tensorflow.op.core.Init;
import org.tensorflow.op.core.InitializeTable;
import org.tensorflow.op.core.InitializeTableFromTextFile;
import org.tensorflow.op.core.InplaceAdd;
import org.tensorflow.op.core.InplaceSub;
import org.tensorflow.op.core.InplaceUpdate;
import org.tensorflow.op.core.IsVariableInitialized;
import org.tensorflow.op.core.LinSpace;
import org.tensorflow.op.core.LookupTableExport;
import org.tensorflow.op.core.LookupTableFind;
import org.tensorflow.op.core.LookupTableImport;
import org.tensorflow.op.core.LookupTableInsert;
import org.tensorflow.op.core.LookupTableSize;
import org.tensorflow.op.core.LoopCond;
import org.tensorflow.op.core.MapClear;
import org.tensorflow.op.core.MapIncompleteSize;
import org.tensorflow.op.core.MapPeek;
import org.tensorflow.op.core.MapSize;
import org.tensorflow.op.core.MapStage;
import org.tensorflow.op.core.MapUnstage;
import org.tensorflow.op.core.MapUnstageNoKey;
import org.tensorflow.op.core.Max;
import org.tensorflow.op.core.Merge;
import org.tensorflow.op.core.Min;
import org.tensorflow.op.core.MirrorPad;
import org.tensorflow.op.core.MlirPassthroughOp;
import org.tensorflow.op.core.MutableDenseHashTable;
import org.tensorflow.op.core.MutableHashTable;
import org.tensorflow.op.core.MutableHashTableOfTensors;
import org.tensorflow.op.core.Mutex;
import org.tensorflow.op.core.MutexLock;
import org.tensorflow.op.core.NextIteration;
import org.tensorflow.op.core.NoOp;
import org.tensorflow.op.core.OneHot;
import org.tensorflow.op.core.OnesLike;
import org.tensorflow.op.core.OrderedMapClear;
import org.tensorflow.op.core.OrderedMapIncompleteSize;
import org.tensorflow.op.core.OrderedMapPeek;
import org.tensorflow.op.core.OrderedMapSize;
import org.tensorflow.op.core.OrderedMapStage;
import org.tensorflow.op.core.OrderedMapUnstage;
import org.tensorflow.op.core.OrderedMapUnstageNoKey;
import org.tensorflow.op.core.Pad;
import org.tensorflow.op.core.ParallelConcat;
import org.tensorflow.op.core.ParallelDynamicStitch;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.core.PlaceholderWithDefault;
import org.tensorflow.op.core.Print;
import org.tensorflow.op.core.Prod;
import org.tensorflow.op.core.QuantizedReshape;
import org.tensorflow.op.core.Range;
import org.tensorflow.op.core.Rank;
import org.tensorflow.op.core.ReadVariableOp;
import org.tensorflow.op.core.ReduceAll;
import org.tensorflow.op.core.ReduceAny;
import org.tensorflow.op.core.ReduceMax;
import org.tensorflow.op.core.ReduceMin;
import org.tensorflow.op.core.ReduceProd;
import org.tensorflow.op.core.ReduceSum;
import org.tensorflow.op.core.RefNextIteration;
import org.tensorflow.op.core.RefSelect;
import org.tensorflow.op.core.RefSwitch;
import org.tensorflow.op.core.RemoteFusedGraphExecute;
import org.tensorflow.op.core.Reshape;
import org.tensorflow.op.core.ResourceCountUpTo;
import org.tensorflow.op.core.ResourceGather;
import org.tensorflow.op.core.ResourceGatherNd;
import org.tensorflow.op.core.ResourceScatterAdd;
import org.tensorflow.op.core.ResourceScatterDiv;
import org.tensorflow.op.core.ResourceScatterMax;
import org.tensorflow.op.core.ResourceScatterMin;
import org.tensorflow.op.core.ResourceScatterMul;
import org.tensorflow.op.core.ResourceScatterNdAdd;
import org.tensorflow.op.core.ResourceScatterNdSub;
import org.tensorflow.op.core.ResourceScatterNdUpdate;
import org.tensorflow.op.core.ResourceScatterSub;
import org.tensorflow.op.core.ResourceScatterUpdate;
import org.tensorflow.op.core.ResourceStridedSliceAssign;
import org.tensorflow.op.core.Reverse;
import org.tensorflow.op.core.ReverseSequence;
import org.tensorflow.op.core.Roll;
import org.tensorflow.op.core.Rpc;
import org.tensorflow.op.core.ScatterAdd;
import org.tensorflow.op.core.ScatterDiv;
import org.tensorflow.op.core.ScatterMax;
import org.tensorflow.op.core.ScatterMin;
import org.tensorflow.op.core.ScatterMul;
import org.tensorflow.op.core.ScatterNd;
import org.tensorflow.op.core.ScatterNdAdd;
import org.tensorflow.op.core.ScatterNdNonAliasingAdd;
import org.tensorflow.op.core.ScatterNdSub;
import org.tensorflow.op.core.ScatterNdUpdate;
import org.tensorflow.op.core.ScatterSub;
import org.tensorflow.op.core.ScatterUpdate;
import org.tensorflow.op.core.Select;
import org.tensorflow.op.core.SetDiff1d;
import org.tensorflow.op.core.SetSize;
import org.tensorflow.op.core.ShapeN;
import org.tensorflow.op.core.Size;
import org.tensorflow.op.core.Skipgram;
import org.tensorflow.op.core.Slice;
import org.tensorflow.op.core.Snapshot;
import org.tensorflow.op.core.SpaceToBatchNd;
import org.tensorflow.op.core.Split;
import org.tensorflow.op.core.SplitV;
import org.tensorflow.op.core.Squeeze;
import org.tensorflow.op.core.Stack;
import org.tensorflow.op.core.Stage;
import org.tensorflow.op.core.StageClear;
import org.tensorflow.op.core.StagePeek;
import org.tensorflow.op.core.StageSize;
import org.tensorflow.op.core.StopGradient;
import org.tensorflow.op.core.StridedSlice;
import org.tensorflow.op.core.StridedSliceAssign;
import org.tensorflow.op.core.StridedSliceGrad;
import org.tensorflow.op.core.Sum;
import org.tensorflow.op.core.SwitchCond;
import org.tensorflow.op.core.TemporaryVariable;
import org.tensorflow.op.core.TensorArray;
import org.tensorflow.op.core.TensorArrayClose;
import org.tensorflow.op.core.TensorArrayConcat;
import org.tensorflow.op.core.TensorArrayGather;
import org.tensorflow.op.core.TensorArrayGrad;
import org.tensorflow.op.core.TensorArrayGradWithShape;
import org.tensorflow.op.core.TensorArrayPack;
import org.tensorflow.op.core.TensorArrayRead;
import org.tensorflow.op.core.TensorArrayScatter;
import org.tensorflow.op.core.TensorArraySize;
import org.tensorflow.op.core.TensorArraySplit;
import org.tensorflow.op.core.TensorArrayUnpack;
import org.tensorflow.op.core.TensorArrayWrite;
import org.tensorflow.op.core.TensorListConcat;
import org.tensorflow.op.core.TensorListConcatLists;
import org.tensorflow.op.core.TensorListElementShape;
import org.tensorflow.op.core.TensorListFromTensor;
import org.tensorflow.op.core.TensorListGather;
import org.tensorflow.op.core.TensorListGetItem;
import org.tensorflow.op.core.TensorListLength;
import org.tensorflow.op.core.TensorListPopBack;
import org.tensorflow.op.core.TensorListPushBack;
import org.tensorflow.op.core.TensorListPushBackBatch;
import org.tensorflow.op.core.TensorListReserve;
import org.tensorflow.op.core.TensorListResize;
import org.tensorflow.op.core.TensorListScatter;
import org.tensorflow.op.core.TensorListScatterIntoExistingList;
import org.tensorflow.op.core.TensorListSetItem;
import org.tensorflow.op.core.TensorListSplit;
import org.tensorflow.op.core.TensorListStack;
import org.tensorflow.op.core.TensorScatterNdAdd;
import org.tensorflow.op.core.TensorScatterNdSub;
import org.tensorflow.op.core.TensorScatterNdUpdate;
import org.tensorflow.op.core.TensorStridedSliceUpdate;
import org.tensorflow.op.core.Tile;
import org.tensorflow.op.core.Timestamp;
import org.tensorflow.op.core.TryRpc;
import org.tensorflow.op.core.Unbatch;
import org.tensorflow.op.core.UnbatchGrad;
import org.tensorflow.op.core.Unique;
import org.tensorflow.op.core.UniqueWithCounts;
import org.tensorflow.op.core.UnravelIndex;
import org.tensorflow.op.core.Unstack;
import org.tensorflow.op.core.Unstage;
import org.tensorflow.op.core.VarHandleOp;
import org.tensorflow.op.core.VarIsInitializedOp;
import org.tensorflow.op.core.Variable;
import org.tensorflow.op.core.VariableShape;
import org.tensorflow.op.core.Where;
import org.tensorflow.op.core.Zeros;
import org.tensorflow.op.core.ZerosLike;
import org.tensorflow.tools.Shape;
import org.tensorflow.tools.buffer.BooleanDataBuffer;
import org.tensorflow.tools.buffer.ByteDataBuffer;
import org.tensorflow.tools.buffer.DataBuffer;
import org.tensorflow.tools.buffer.DoubleDataBuffer;
import org.tensorflow.tools.buffer.FloatDataBuffer;
import org.tensorflow.tools.buffer.IntDataBuffer;
import org.tensorflow.tools.buffer.LongDataBuffer;
import org.tensorflow.tools.ndarray.BooleanNdArray;
import org.tensorflow.tools.ndarray.ByteNdArray;
import org.tensorflow.tools.ndarray.DoubleNdArray;
import org.tensorflow.tools.ndarray.FloatNdArray;
import org.tensorflow.tools.ndarray.IntNdArray;
import org.tensorflow.tools.ndarray.LongNdArray;
import org.tensorflow.tools.ndarray.NdArray;
import org.tensorflow.types.TBool;
import org.tensorflow.types.TFloat32;
import org.tensorflow.types.TFloat64;
import org.tensorflow.types.TInt32;
import org.tensorflow.types.TInt64;
import org.tensorflow.types.TString;
import org.tensorflow.types.TUint8;
import org.tensorflow.types.family.TNumber;
import org.tensorflow.types.family.TType;

/**
 * An API for building operations as {@link Op Op}s
 * <p>
 * Any operation wrapper found in the classpath properly annotated as an{@link org.tensorflow.op.annotation.Operator @Operator} is exposed
 * by this API or one of its subgroup.
 * <p>Example usage:
 * <pre>{@code
 * try (Graph g = new Graph()) {
 *   Ops tf = Ops.create(g);
 *   // Operations are typed classes with convenience
 *   // builders in Ops.
 *   Constant<TInt32> three = tf.constant(3);
 *   // Single-result operations implement the Operand
 *   // interface, so this works too.
 *   Operand<TInt32> four = tf.constant(4);
 *   // Most builders are found within a group, and accept
 *   // Operand types as operands
 *   Operand<TInt32> nine = tf.math.add(four, tf.constant(5));
 *   // Multi-result operations however offer methods to
 *   // select a particular result for use.
 *   Operand<TInt32> result = 
 *       tf.math.add(tf.unique(s, a).y(), b);
 *   // Optional attributes
 *   tf.linalg.matMul(a, b, MatMul.transposeA(true));
 *   // Naming operators
 *   tf.withName("foo").constant(5); // name "foo"
 *   // Names can exist in a hierarchy
 *   Ops sub = tf.withSubScope("sub");
 *   sub.withName("bar").constant(4); // "sub/bar"
 * }
 * }</pre>
 */
public final class Ops {
  public final NnOps nn;

  public final SummaryOps summary;

  public final ImageOps image;

  public final DataOps data;

  public final IoOps io;

  public final DtypesOps dtypes;

  public final LinalgOps linalg;

  public final RandomOps random;

  public final StringsOps strings;

  public final SparseOps sparse;

  public final BitwiseOps bitwise;

  public final MathOps math;

  public final AudioOps audio;

  public final SignalOps signal;

  public final TrainOps train;

  public final QuantizationOps quantization;

  private final Scope scope;

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
   * Raise a exception to abort the process when called.
   *  <p>
   *  If exit_without_error is true, the process will exit normally,
   *  otherwise it will exit with a SIGABORT signal.
   *  <p>
   *  Returns nothing but an exception.
   *
   * @param options carries optional attributes values
   * @return a new instance of Abort
   */
  public Abort abort(Abort.Options... options) {
    return Abort.create(scope, options);
  }

  /**
   * Computes the "logical and" of elements across dimensions of a tensor.
   *  <p>
   *  Reduces `input` along the dimensions given in `axis`. Unless
   *  `keep_dims` is true, the rank of the tensor is reduced by 1 for each entry in
   *  `axis`. If `keep_dims` is true, the reduced dimensions are
   *  retained with length 1.
   *
   * @param input The tensor to reduce.
   * @param axis The dimensions to reduce. Must be in the range
   *  `[-rank(input), rank(input))`.
   * @param options carries optional attributes values
   * @return a new instance of All
   */
  public <T extends TNumber> All all(Operand<TBool> input, Operand<T> axis,
      All.Options... options) {
    return All.create(scope, input, axis, options);
  }

  /**
   * Computes the "logical or" of elements across dimensions of a tensor.
   *  <p>
   *  Reduces `input` along the dimensions given in `axis`. Unless
   *  `keep_dims` is true, the rank of the tensor is reduced by 1 for each entry in
   *  `axis`. If `keep_dims` is true, the reduced dimensions are
   *  retained with length 1.
   *
   * @param input The tensor to reduce.
   * @param axis The dimensions to reduce. Must be in the range
   *  `[-rank(input), rank(input))`.
   * @param options carries optional attributes values
   * @return a new instance of Any
   */
  public <T extends TNumber> Any any(Operand<TBool> input, Operand<T> axis,
      Any.Options... options) {
    return Any.create(scope, input, axis, options);
  }

  /**
   * Creates a constant of {@code int} elements.
   *
   * @param scope is a scope used to add the underlying operation.
   * @param data An array containing the values to put into the new constant.
   * @return a float constant
   */
  public Constant<TInt32> array(int... data) {
    return Constant.arrayOf(scope, data);
  }

  /**
   * Creates a constant of {@code String} elements, using the default UTF-8 charset.
   *
   * @param scope is a scope used to add the underlying operation.
   * @param data An array containing the values to put into the new constant.
   * @return the {@code String} constant
   */
  public Constant<TString> array(String... data) {
    return Constant.arrayOf(scope, data);
  }

  /**
   * Creates a constant of {@code boolean} elements.
   *
   * @param scope is a scope used to add the underlying operation.
   * @param data An array containing the values to put into the new constant.
   * @return a boolean constant
   */
  public Constant<TBool> array(boolean... data) {
    return Constant.arrayOf(scope, data);
  }

  /**
   * Creates a constant of {@code long} elements.
   *
   * @param scope is a scope used to add the underlying operation.
   * @param data An array containing the values to put into the new constant.
   * @return a long constant
   */
  public Constant<TInt64> array(long... data) {
    return Constant.arrayOf(scope, data);
  }

  /**
   * Creates a constant of {@code float} elements.
   *
   * @param scope is a scope used to add the underlying operation.
   * @param data An array containing the values to put into the new constant.
   * @return a float constant
   */
  public Constant<TFloat32> array(float... data) {
    return Constant.arrayOf(scope, data);
  }

  /**
   * Creates a constant of {@code double} elements.
   *
   * @param scope is a scope used to add the underlying operation.
   * @param data An array containing the values to put into the new constant.
   * @return a double constant
   */
  public Constant<TFloat64> array(double... data) {
    return Constant.arrayOf(scope, data);
  }

  /**
   * Creates a constant of {@code byte} elements.
   *
   * @param scope is a scope used to add the underlying operation.
   * @param data An array containing the values to put into the new constant.
   * @return a byte constant
   */
  public Constant<TUint8> array(byte... data) {
    return Constant.arrayOf(scope, data);
  }

  /**
   * Creates a constant of {@code String} elements, using the given charset.
   *
   * @param scope is a scope used to add the underlying operation.
   * @param charset charset for encoding/decoding strings bytes.
   * @param data An array containing the values to put into the new constant. String elements are
   *      sequences of bytes from the last array dimension.
   * @return the {@code String} constant
   */
  public Constant<TString> array(Charset charset, String... data) {
    return Constant.arrayOf(scope, charset, data);
  }

  /**
   * Asserts that the given condition is true.
   *  <p>
   *  If `condition` evaluates to false, print the list of tensors in `data`.
   *  `summarize` determines how many entries of the tensors to print.
   *
   * @param condition The condition to evaluate.
   * @param data The tensors to print out when condition is false.
   * @param options carries optional attributes values
   * @return a new instance of AssertThat
   */
  public AssertThat assertThat(Operand<TBool> condition, Iterable<Operand<?>> data,
      AssertThat.Options... options) {
    return AssertThat.create(scope, condition, data, options);
  }

  /**
   * Update 'ref' by assigning 'value' to it.
   *  <p>
   *  This operation outputs "ref" after the assignment is done.
   *  This makes it easier to chain operations that need to use the reset value.
   *
   * @param <T> data type for {@code outputRef()} output
   * @param ref Should be from a `Variable` node. May be uninitialized.
   * @param value The value to be assigned to the variable.
   * @param options carries optional attributes values
   * @return a new instance of Assign
   */
  public <T extends TType> Assign<T> assign(Operand<T> ref, Operand<T> value,
      Assign.Options... options) {
    return Assign.create(scope, ref, value, options);
  }

  /**
   * Update 'ref' by adding 'value' to it.
   *  <p>
   *  This operation outputs "ref" after the update is done.
   *  This makes it easier to chain operations that need to use the reset value.
   *
   * @param <T> data type for {@code outputRef()} output
   * @param ref Should be from a `Variable` node.
   * @param value The value to be added to the variable.
   * @param options carries optional attributes values
   * @return a new instance of AssignAdd
   */
  public <T extends TType> AssignAdd<T> assignAdd(Operand<T> ref, Operand<T> value,
      AssignAdd.Options... options) {
    return AssignAdd.create(scope, ref, value, options);
  }

  /**
   * Adds a value to the current value of a variable.
   *  <p>
   *  Any ReadVariableOp with a control dependency on this op is guaranteed to
   *  see the incremented value or a subsequent newer one.
   *
   * @param resource handle to the resource in which to store the variable.
   * @param value the value by which the variable will be incremented.
   * @return a new instance of AssignAddVariableOp
   */
  public <T extends TType> AssignAddVariableOp assignAddVariableOp(Operand<?> resource,
      Operand<T> value) {
    return AssignAddVariableOp.create(scope, resource, value);
  }

  /**
   * Update 'ref' by subtracting 'value' from it.
   *  <p>
   *  This operation outputs "ref" after the update is done.
   *  This makes it easier to chain operations that need to use the reset value.
   *
   * @param <T> data type for {@code outputRef()} output
   * @param ref Should be from a `Variable` node.
   * @param value The value to be subtracted to the variable.
   * @param options carries optional attributes values
   * @return a new instance of AssignSub
   */
  public <T extends TType> AssignSub<T> assignSub(Operand<T> ref, Operand<T> value,
      AssignSub.Options... options) {
    return AssignSub.create(scope, ref, value, options);
  }

  /**
   * Subtracts a value from the current value of a variable.
   *  <p>
   *  Any ReadVariableOp with a control dependency on this op is guaranteed to
   *  see the decremented value or a subsequent newer one.
   *
   * @param resource handle to the resource in which to store the variable.
   * @param value the value by which the variable will be incremented.
   * @return a new instance of AssignSubVariableOp
   */
  public <T extends TType> AssignSubVariableOp assignSubVariableOp(Operand<?> resource,
      Operand<T> value) {
    return AssignSubVariableOp.create(scope, resource, value);
  }

  /**
   * Assigns a new value to a variable.
   *  <p>
   *  Any ReadVariableOp with a control dependency on this op is guaranteed to return
   *  this value or a subsequent newer value of the variable.
   *
   * @param resource handle to the resource in which to store the variable.
   * @param value the value to set the new tensor to use.
   * @return a new instance of AssignVariableOp
   */
  public <T extends TType> AssignVariableOp assignVariableOp(Operand<?> resource,
      Operand<T> value) {
    return AssignVariableOp.create(scope, resource, value);
  }

  /**
   * Defines a barrier that persists across different graph executions.
   *  <p>
   *  A barrier represents a key-value map, where each key is a string, and
   *  each value is a tuple of tensors.
   *  <p>
   *  At runtime, the barrier contains 'complete' and 'incomplete'
   *  elements. A complete element has defined tensors for all components of
   *  its value tuple, and may be accessed using BarrierTakeMany. An
   *  incomplete element has some undefined components in its value tuple,
   *  and may be updated using BarrierInsertMany.
   *
   * @param componentTypes The type of each component in a value.
   * @param options carries optional attributes values
   * @return a new instance of Barrier
   */
  public Barrier barrier(List<DataType<?>> componentTypes, Barrier.Options... options) {
    return Barrier.create(scope, componentTypes, options);
  }

  /**
   * Closes the given barrier.
   *  <p>
   *  This operation signals that no more new elements will be inserted in the
   *  given barrier. Subsequent InsertMany that try to introduce a new key will fail.
   *  Subsequent InsertMany operations that just add missing components to already
   *  existing elements will continue to succeed. Subsequent TakeMany operations will
   *  continue to succeed if sufficient completed elements remain in the barrier.
   *  Subsequent TakeMany operations that would block will fail immediately.
   *
   * @param handle The handle to a barrier.
   * @param options carries optional attributes values
   * @return a new instance of BarrierClose
   */
  public BarrierClose barrierClose(Operand<TString> handle, BarrierClose.Options... options) {
    return BarrierClose.create(scope, handle, options);
  }

  /**
   * Computes the number of incomplete elements in the given barrier.
   *
   * @param handle The handle to a barrier.
   * @return a new instance of BarrierIncompleteSize
   */
  public BarrierIncompleteSize barrierIncompleteSize(Operand<TString> handle) {
    return BarrierIncompleteSize.create(scope, handle);
  }

  /**
   * For each key, assigns the respective value to the specified component.
   *  <p>
   *  If a key is not found in the barrier, this operation will create a new
   *  incomplete element. If a key is found in the barrier, and the element
   *  already has a value at component_index, this operation will fail with
   *  INVALID_ARGUMENT, and leave the barrier in an undefined state.
   *
   * @param handle The handle to a barrier.
   * @param keys A one-dimensional tensor of keys, with length n.
   * @param values An any-dimensional tensor of values, which are associated with the
   *  respective keys. The 0th dimension must have length n.
   * @param componentIndex The component of the barrier elements that is being assigned.
   * @return a new instance of BarrierInsertMany
   */
  public <T extends TType> BarrierInsertMany barrierInsertMany(Operand<TString> handle,
      Operand<TString> keys, Operand<T> values, Long componentIndex) {
    return BarrierInsertMany.create(scope, handle, keys, values, componentIndex);
  }

  /**
   * Computes the number of complete elements in the given barrier.
   *
   * @param handle The handle to a barrier.
   * @return a new instance of BarrierReadySize
   */
  public BarrierReadySize barrierReadySize(Operand<TString> handle) {
    return BarrierReadySize.create(scope, handle);
  }

  /**
   * Takes the given number of completed elements from a barrier.
   *  <p>
   *  This operation concatenates completed-element component tensors along
   *  the 0th dimension to make a single component tensor.
   *  <p>
   *  Elements come out of the barrier when they are complete, and in the order
   *  in which they were placed into the barrier.  The indices output provides
   *  information about the batch in which each element was originally inserted
   *  into the barrier.
   *
   * @param handle The handle to a barrier.
   * @param numElements A single-element tensor containing the number of elements to
   *  take.
   * @param componentTypes The type of each component in a value.
   * @param options carries optional attributes values
   * @return a new instance of BarrierTakeMany
   */
  public BarrierTakeMany barrierTakeMany(Operand<TString> handle, Operand<TInt32> numElements,
      List<DataType<?>> componentTypes, BarrierTakeMany.Options... options) {
    return BarrierTakeMany.create(scope, handle, numElements, componentTypes, options);
  }

  /**
   * Batches all input tensors nondeterministically.
   *  <p>
   *  When many instances of this Op are being run concurrently with the same
   *  container/shared_name in the same device, some will output zero-shaped Tensors
   *  and others will output Tensors of size up to max_batch_size.
   *  <p>
   *  All Tensors in in_tensors are batched together (so, for example, labels and
   *  features should be batched with a single instance of this operation.
   *  <p>
   *  Each invocation of batch emits an `id` scalar which will be used to identify
   *  this particular invocation when doing unbatch or its gradient.
   *  <p>
   *  Each op which emits a non-empty batch will also emit a non-empty batch_index
   *  Tensor, which, is a [K, 3] matrix where each row contains the invocation's id,
   *  start, and length of elements of each set of Tensors present in batched_tensors.
   *  <p>
   *  Batched tensors are concatenated along the first dimension, and all tensors in
   *  in_tensors must have the first dimension of the same size.
   *  <p>
   *  in_tensors: The tensors to be batched.
   *  num_batch_threads: Number of scheduling threads for processing batches of work.
   *   Determines the number of batches processed in parallel.
   *  max_batch_size: Batch sizes will never be bigger than this.
   *  batch_timeout_micros: Maximum number of microseconds to wait before outputting
   *   an incomplete batch.
   *  allowed_batch_sizes: Optional list of allowed batch sizes. If left empty, does
   *   nothing. Otherwise, supplies a list of batch sizes, causing the op to pad
   *   batches up to one of those sizes. The entries must increase monotonically, and
   *   the final entry must equal max_batch_size.
   *  grad_timeout_micros: The timeout to use for the gradient. See Unbatch.
   *  batched_tensors: Either empty tensors or a batch of concatenated Tensors.
   *  batch_index: If out_tensors is non-empty, has information to invert it.
   *  container: Controls the scope of sharing of this batch.
   *  id: always contains a scalar with a unique ID for this invocation of Batch.
   *  shared_name: Concurrently running instances of batch in the same device with the
   *   same container and shared_name will batch their elements together. If left
   *   empty, the op name will be used as the shared name.
   *  T: the types of tensors to be batched.
   *
   * @param inTensors
   * @param numBatchThreads
   * @param maxBatchSize
   * @param batchTimeoutMicros
   * @param gradTimeoutMicros
   * @param options carries optional attributes values
   * @return a new instance of Batch
   */
  public Batch batch(Iterable<Operand<?>> inTensors, Long numBatchThreads, Long maxBatchSize,
      Long batchTimeoutMicros, Long gradTimeoutMicros, Batch.Options... options) {
    return Batch.create(scope, inTensors, numBatchThreads, maxBatchSize, batchTimeoutMicros, gradTimeoutMicros, options);
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
