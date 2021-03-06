package com.amazonaws.services.kinesis.clientlibrary.lib.worker;

import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.KinesisClientLibDependencyException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ThrottlingException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IPreparedCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.types.ExtendedSequenceNumber;

/**
 * Objects of this class are prepared to checkpoint at a specific sequence number. They use an
 * IRecordProcessorCheckpointer to do the actual checkpointing, so their checkpoint is subject to the same 'didn't go
 * backwards' validation as a normal checkpoint.
 */
public class PreparedCheckpointer implements IPreparedCheckpointer {

    private final ExtendedSequenceNumber pendingCheckpointSequenceNumber;
    private final IRecordProcessorCheckpointer checkpointer;

    /**
     * Constructor.
     *
     * @param pendingCheckpointSequenceNumber sequence number to checkpoint at
     * @param checkpointer checkpointer to use
     */
    public PreparedCheckpointer(ExtendedSequenceNumber pendingCheckpointSequenceNumber,
                                IRecordProcessorCheckpointer checkpointer) {
        this.pendingCheckpointSequenceNumber = pendingCheckpointSequenceNumber;
        this.checkpointer = checkpointer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtendedSequenceNumber getPendingCheckpoint() {
        return pendingCheckpointSequenceNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkpoint()
            throws KinesisClientLibDependencyException, InvalidStateException, ThrottlingException, ShutdownException,
            IllegalArgumentException {
        checkpointer.checkpoint(pendingCheckpointSequenceNumber.getSequenceNumber(),
                pendingCheckpointSequenceNumber.getSubSequenceNumber());
    }
}
