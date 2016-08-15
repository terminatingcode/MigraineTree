package com.terminatingcode.android.migrainetree;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class MigraineRecordItems {
    public static class RecordItem {
        public int id;
        public final String content;
        public final String details;
        public final long startHour;

        public RecordItem(int id, String content, String details, long startHour) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.startHour = startHour;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
