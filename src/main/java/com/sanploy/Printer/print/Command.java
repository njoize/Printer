package com.sanploy.Printer.print;

public class Command {
	/**�����ϻ��� 1B 2B n ��n=0ʱ����ֹ�ϻ��ߴ�ӡ����n=1ʱ�������ϻ��ߴ�ӡ�����������ַ�ո��Լ��ַ�������»��ߣ�Ĭ��n=0x00*/
	public static final byte[] OVERLINE = new byte[] { 0x1B,0x2D,0x01 };
	/**�����»��� 1B 2D n ��n=0ʱ����ֹ�»��ߴ�ӡ����n=1ʱ�������»��ߴ�ӡ�����������ַ�ո��Լ��ַ�������»��ߣ�Ĭ��n=0x00*/
	public static final byte[] UNDERLINE = new byte[] { 0x1B,0x2D,0x01 };	
	/**�����м�� 1B 31 n  Ϊ����Ļ�����������n���м�࣬��ֵ��ʾ�����ַ���֮��Ŀհ׵�����Ĭ��n=0x06*/
	public static final byte[] ROW_SPACE = new byte[] { 0x1B, 0x31, 0x06 };	
	/**����Ĭ���о� 1B 32 Ĭ���о�Ϊ30����*/
	public static final byte[] ROW_DEFAULT = new byte[] { 0x1B, 0x32 };	
	/**�����о� 1B 33 nΪ����Ļ�����������n���о࣬��ֵ��ʾ����ַ�����ռ�ĵ�����*/
	public static final byte[] ROW = new byte[] { 0x1B, 0x33,0x00 };	
	/**��ӡ���ʼ��*/
	public static final byte[] INIT = new byte[] { 0x1B, 0x40 };		
	/**ɾ���ӡ������*/
	public static final byte[] CLEAN = new byte[] { 0x18 };	
	/**���У���ӡ������*/
	public static final byte[] LF = new byte[] { 0x0A };					        
	/**�س�����ӡ������*/
	public static final byte[] CR = new byte[] { 0x0D };					        
	/**���ʹ�ӡ��״̬*/
	public static final byte[] DLE_EOT_1 = new byte[] { 0x10,0x04,0x01 };		        
	/**����̧��״̬*/
	public static final byte[] DLE_EOT_2 = new byte[] { 0x10,0x04,0x02 };		        
	/**�����е�״̬*/
	public static final byte[] DLE_EOT_3 = new byte[] { 0x10,0x04,0x03 };		        
	/**���;�ֽ״̬*/
	public static final byte[] DLE_EOT_4 = new byte[] { 0x10,0x04,0x04 };		        
	/**�ַ���ӡ  0x1B,n  0���?14ȡ��? */
	public static final byte[] DOUBLE_WIDTH = new byte[] { 0x1B,0x0E };		        
	public static final byte[] CANCEL_DOUBLE_WIDTH = new byte[] { 0x1B,0x14 };		
	/**���ô����ӡ:01���?00��ֹ*/
	public static final byte[] BOLD = new byte[] { 0x1B,0x45,0x01 };
	public static final byte[] CANCEL_BOLD = new byte[] { 0x1B,0x45,0x00 };
	/**��ӡ����ֽn����:1B 4A n ����ǰ��������δ��ӡ�ַ����ӡ���ַ�ִ��n���пհ���ֽ������ǰ������û�д�ӡ�ַ���ֱ��ִ��n���пհ���ֽ*/
	public static final byte[] MOVE_POINT = new byte[] { 0x1B,0x4A,0x00 };	
	/**ѡ������:1B 4D n ��n=0x00ʱ��ASCLL�ַ����12*24�����ӡ����n=0x01ʱ��ASCLL�ַ����8*16�����ӡ��*/
	public static final byte[] FONT = new byte[] { 0x1B,0x4D,0x00 };	
	/**�����ұ߾�:1B 51 n �ұ߾�Ϊ��ӡֽ�Ҳ�հ׵��ַ���������ָ�ַ�Ϊ8��?*/
	public static final byte[] RINGHTMARGIN = new byte[] { 0x1B,0x51,0x05 };	
	/**�����ַ����Ŵ�:1b 55 n ����n��ʾ������ȵ�n��*/
	public static final byte[] TRANSVERSE = new byte[] { 0x1B,0x55,0x03 };	
	/**�����ַ�����Ŵ�:1b 56 n ����n��ʾ�������ȵ�n��*/
	public static final byte[] LONGITUDINAL = new byte[] { 0x1B,0x56,0x01 };	
	/**�����ַ���뷽ʽ:1b 61 n ����n����n=0ʱ���ַ���ִ��������ӡ����n=1ʱ���ַ���ִ�о��д�ӡ����n=2ʱ���ַ���ִ�Ҷ����ӡ��Ĭ��Ϊ0*/
	public static final byte[] ALIGN_LEFT = new byte[] { 0x1B,0x61,0x00 };
	public static final byte[] ALIGN_CENTER = new byte[] { 0x1B,0x61,0x01 };
	public static final byte[] ALIGN_RIGHT = new byte[] { 0x1B,0x61,0x00 };
	/**��ӡ����ֽn�ַ���:1B 63 n  ��n=0Ϊ�����ӡ���ַ������������򣻵�n=1ʱ��Ϊ�����ӡ���ַ������������������ַ�Ҳ����ת��Ĭ��n=0x00;*/
	public static final byte[] DIRECTION = new byte[] { 0x1B,0x63,0x00 };
	/**��ӡ����ֽn�ַ���:1B 64 n  ��ǰ��������δ��ӡ�ַ����ӡ���ַ�ִ��n�ַ��пհ���ֽ����ǰ������û�д�ӡ�ַ���ֱ��ִ��n�ַ��пհ���ֽ*/
	public static final byte[] MOVE_LINE = new byte[] { 0x1B,0x64,0x08 };
	/**��ӡ�ո�����:1B 66 m n  ����m=0����ӡ���ӡn�����ַ�m=1����ӡ���ӡn���ַ����*/
	public static final byte[] BLANK_LINE = new byte[] { 0x1B,0x66,0x00,0x02 };
	/**������߾�:1B 6C n ����n����߾�Ϊ��ӡֽ���հ׵��ַ����ַ�Ϊ8��?Ĭ��Ϊ0x00*/
	public static final byte[] LEFTMARGIN = new byte[] { 0x1B,0x61,0x01 };
	/**�����ַ���ת����:1C 49 n ����n����n=0ʱ���ַ���ת����n=1ʱ���ַ�˳ʱ��90�ȣ���n=2ʱ���ַ�˳ʱ��180�ȣ�Ĭ��Ϊ0*/
	public static final byte[] ROTATION = new byte[] { 0x1B,0x49,0x00 };
	
}
