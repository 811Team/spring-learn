package org.lucas;

/**
 * @Author: shaw
 * @Date: 2019/5/21 12:45
 */
public class EventPoller<T> {

    private final DataProvider<T> dataProvider;

    private final Sequencer sequencer;

    private final Sequence sequence;

    private final Sequence gatingSequence;

    public EventPoller(final DataProvider<T> dataProvider, final Sequencer sequencer,
                       final Sequence sequence, final Sequence gatingSequence) {
        this.dataProvider = dataProvider;
        this.sequencer = sequencer;
        this.sequence = sequence;
        this.gatingSequence = gatingSequence;
    }
}
