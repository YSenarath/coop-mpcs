package util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class CharactorLimitDocument extends PlainDocument {

    private final int limit;

    public CharactorLimitDocument(int limit) {
        super();
        this.limit = limit;
        setDocumentFilter(new IntegerFilter());
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
