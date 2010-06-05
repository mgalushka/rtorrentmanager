package dialog;

/**
 * User: welvet
 * Date: 03.06.2010
 * Time: 22:47:47
 */
public interface Input {
    /**
     * @return �� ����
     */
    public String getFieldName();

    /**
     * @return ����� � ��������� ����
     */
    public String getFieldText();

    /**
     * @return ����� ���������
     */
    public String getFieldDescription();

    /**
     * @return html pattern
     */
    public String getHtml();

    /**
     * @return �������� ����
     */
    public String getFieldValue();
}
