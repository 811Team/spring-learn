package org.lucas.disruptor;

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

    /**
     * 创建新的事件轮询器
     *
     * @param dataProvider    用户数据源
     * @param sequencer       序列器
     * @param sequence        序列
     * @param cursorSequence  游标序列
     * @param gatingSequences
     * @return 事件轮询器
     */
    public static <T> EventPoller<T> newInstance(final DataProvider<T> dataProvider, final Sequencer sequencer,
                                                 final Sequence sequence, final Sequence cursorSequence, final Sequence... gatingSequences) {
        Sequence gatingSequence;
        if (gatingSequences.length == 0) {
            gatingSequence = cursorSequence;
        } else if (gatingSequences.length == 1) {
            gatingSequence = gatingSequences[0];
        } else {
            gatingSequence = new FixedSequenceGroup(gatingSequences);
        }
        return new EventPoller<T>(dataProvider, sequencer, sequence, gatingSequence);
    }
}
