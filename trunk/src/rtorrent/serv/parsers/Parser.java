package rtorrent.serv.parsers;

/**
 * User: welvet
 * Date: 18.05.2010
 * Time: 20:28:00
 */
public interface Parser {
    /**
     * ������ ������������� �������
     * @return
     */
    public String getKey();

    /**
     * ������
     * @param request ������� ��� ����� �������
     * @return
     * @throws ParserException
     */
    public String doParse(String request) throws ParserException;
}
