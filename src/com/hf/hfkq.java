package com.hf;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map; 
import java.applet.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.filechooser.FileSystemView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.eltima.components.ui.DatePicker;
import java.util.Date;
import java.util.Locale;

import com.hf.ZkemSDK;
import com.hf.ConfigReader;

public class hfkq extends JFrame{
	//�����ͼ�����������������á�  
    private JFrame f;  
    private Button btnConn;
    private Button btnChoose;
    private Button btnDownload;
    private Button btnUsers;
    private Button btnQuit;
    
    private JLabel jl;
    private TextField tf;
    
    private JLabel jl_kq;
    private DatePicker datepick;
    
    private JComboBox comboBox;
    private Button btnGetResult;
    private Button btnAnalyseLog;
    
    private Dialog dlgError;
    
    private String DownloadPath="";
    
	public static void main(String[] args)  
    {  
        new hfkq();  
    }
	
	public hfkq() {
		init();
		
//        ConfigReader cr = new  ConfigReader("E:/test.ini");
//        System.out.println(cr.get("config", "IP"));
        
//		ZkemSDK sdk = new ZkemSDK();  
//        boolean  connFlag = sdk.connect("192.168.1.100", 4370);  
//        System.out.println("conn:"+connFlag);  
	}
	
	
        
      
    public void init()  
    {  
        f = new JFrame("���ڼ�¼�������");  
          
        //��frame���л�������  
        f.setBounds(300, 100, 600, 500);  
        //f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.setResizable(false);
        //f.setLayout(new FlowLayout());  
        JPanel panel = new JPanel();
        //��ʼ��������ӵ�frame��
        
        //jl_title.setHorizontalAlignment(JLabel.CENTER);
        jl = new JLabel("�������Ŀ¼��");
        tf = new TextField(30);// ���������ı�����60���ȴ�С�ַ�
        tf.setEditable(false);
        
        btnChoose = new Button("ѡ��Ŀ¼");  
        btnConn = new Button("���Կ��ڻ�����");  
        btnDownload = new Button("����ȫ�����ڼ�¼");  
        btnUsers = new Button("�������п�����Ա");
        jl_kq = new JLabel("������������ѡ��");
        datepick = getDatePicker();
        
        comboBox = new JComboBox();
        comboBox.addItem("ǩ��");
        comboBox.addItem("ǩ��");
        btnGetResult = new Button("��ȡָ��ʱ�����俼�ڼ�¼");
        btnAnalyseLog = new Button("������������");
        btnAnalyseLog.setEnabled(false);
        btnQuit = new Button("�˳�");
        //Ϊָ����Container����GroupLayout
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setLayout(layout);
        //����GroupLayout��ˮƽ�����飬Խ�ȼ����ParalleGroupLayout���ȼ�Խ��
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup().addComponent(jl).addComponent(btnConn).addComponent(jl_kq).addComponent(btnGetResult));        
        hGroup.addGroup(layout.createParallelGroup().addComponent(tf).addComponent(btnDownload).addComponent(datepick).addComponent(btnAnalyseLog));        
        hGroup.addGroup(layout.createParallelGroup().addComponent(btnChoose).addComponent(btnUsers).addComponent(comboBox).addComponent(btnQuit));
        
        layout.setHorizontalGroup(hGroup);
        //����GroupLayout�Ĵ�ֱ�����飬Խ�߼����ParallelGroup�����ȼ�Խ��
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup().addComponent(jl).addComponent(tf).addComponent(btnChoose));
        vGroup.addGroup(layout.createParallelGroup().addComponent(btnConn).addComponent(btnDownload).addComponent(btnUsers));
        vGroup.addGroup(layout.createParallelGroup().addComponent(jl_kq).addComponent(datepick).addComponent(comboBox));
        vGroup.addGroup(layout.createParallelGroup().addComponent(btnGetResult).addComponent(btnAnalyseLog).addComponent(btnQuit));        
        //���ô�ֱ��
        layout.setVerticalGroup(vGroup);
        //����һ�´����ϵ��¼�  
        myEvent();  
        
        f.setContentPane(panel);
        f.pack();
        f.setLocationRelativeTo(null);
        //��ʾ����  
        f.setVisible(true);  
    }  
      
    //�¼���Ӧ  
    private void myEvent()  
    {  
        f.addWindowListener(new WindowAdapter(){  
            public void windowClosing(WindowEvent e)  
            {  
                System.exit(0);  
            }  
        }); 
        //��������  
        btnConn.addActionListener(new ActionListener() {  
        	@Override  
        	public void actionPerformed(ActionEvent e) {  
        	// TODO Auto-generated method stub   		
        		String iniPath = System.getProperty("user.dir");
        		//JOptionPane.showMessageDialog(null, "��ǰ·���ǣ�"+iniPath, "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
        		try{
        			ZkemSDK sdk = new ZkemSDK();
            		boolean  connFlag = true;  
            		
            		String strError="";
            		
            		ConfigReader cr = new  ConfigReader(iniPath+"\\config.ini");
                    System.out.println(cr.get("config", "IP"));
                    
                    String strIPs = cr.get("config", "IP").get(0);
                    String[] strIP = strIPs.split(",");
                    for(int i=0;i<strIP.length;i++){
                    	System.out.println(strIP[i]);
                    	if(!sdk.connect(strIP[i], 4370)){
                    		connFlag = false;
                    		strError = strError + strIP[i]+" ";
                    	}else{
                    		sdk.disConnect();
                    	}
                    }
                    if(connFlag){
                    	JOptionPane.showMessageDialog(null, "���п��ڻ����ӳɹ���", "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
                    }else{
                    	JOptionPane.showMessageDialog(null, strError+"���ڻ�����ʧ�ܣ�", "����", JOptionPane.ERROR_MESSAGE); 
                    }
        		}catch(Exception err){
        			JOptionPane.showMessageDialog(null, err.toString(), "����", JOptionPane.ERROR_MESSAGE);
        		}
        		
        		
        	}  
        }); 
        //ѡ�񱣴�Ŀ¼
        btnChoose.addActionListener(new ActionListener() {  
        	@Override  
        	public void actionPerformed(ActionEvent e) {  
        	// TODO Auto-generated method stub  
        		JFileChooser fileChooser = new JFileChooser("C:\\");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fileChooser.showOpenDialog(fileChooser);
                if(returnVal == JFileChooser.APPROVE_OPTION){       
                	DownloadPath= fileChooser.getSelectedFile().getAbsolutePath();//���������ѡ����ļ��е�·��
                    System.out.println(DownloadPath);
                    tf.setText(DownloadPath);
                }
                
        	}  
        });
        //���ؿ��ڼ�¼
        btnDownload.addActionListener(new ActionListener() {  
        	@Override  
        	public void actionPerformed(ActionEvent e) {  
        	// TODO Auto-generated method stub  
        		if(DownloadPath.equals("")){
        			JOptionPane.showMessageDialog(null, "δѡ�����Ŀ¼����֪���ص��δ���", "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
        		}else{
        			System.out.println("���ؿ��ڼ�¼����"+DownloadPath);
        			ZkemSDK sdk = new ZkemSDK();             		
            		String strError=""; 
            		String iniPath = System.getProperty("user.dir");
            		ConfigReader cr = new  ConfigReader(iniPath+"\\config.ini");
                    System.out.println(cr.get("config", "IP"));
                    String strIPs = cr.get("config", "IP").get(0);
                    String[] strIP = strIPs.split(",");
                                     
                    int iMachineNumber=0;
                    for(int i=0;i<strIP.length;i++){
                    	System.out.println(strIP[i]);
                    	if(!sdk.connect(strIP[i], 4370)){
                    		strError = strError + strIP[i]+" ";
                    	}else{
                    		sdk.readGeneralLogData(iMachineNumber);
                    		saveFileItemsToTxt(iMachineNumber,sdk.getGeneralLogData(),DownloadPath+"output.txt");
                    	}
                    	iMachineNumber++;
                    }
                    if(strError.equals("")){
                    	JOptionPane.showMessageDialog(null, "������ɣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
                    }else{
                    	JOptionPane.showMessageDialog(null, strError+"������������ʧ�ܣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
                    }
                    
        		}
        	}  
        });
        //��ȡ���п�����Ա
        btnUsers.addActionListener(new ActionListener() {
        	@Override  
        	public void actionPerformed(ActionEvent e) {  
        	// TODO Auto-generated method stub  
        		System.out.println("������Ա��Ϣ����"+DownloadPath);
        		ZkemSDK sdk = new ZkemSDK();             		
            	String strError=""; 
            	String iniPath = System.getProperty("user.dir");
            	ConfigReader cr = new  ConfigReader(iniPath+"\\config.ini");
                System.out.println(cr.get("config", "IP"));
                String strIPs = cr.get("config", "IP").get(0);
                String[] strIP = strIPs.split(",");
                
                System.out.println(strIP[0]);
                if(!sdk.connect(strIP[0], 4370)){
                    strError = strError + strIP[0]+" ";
                }else{
                    sdk.getUserInfo();
                    downUsers(sdk.getUserInfo());
                }
                    
                if(strError.equals("")){
                    JOptionPane.showMessageDialog(null, "������ɣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                    btnUsers.setEnabled(false);
                }else{
                    JOptionPane.showMessageDialog(null, strError+"������Ա��Ϣʧ�ܣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
                }
        	}
        });
        //��ȡָ��ʱ�����俼�ڼ�¼
        btnGetResult.addActionListener(new ActionListener() {  
        	@Override  
        	public void actionPerformed(ActionEvent e) {  
        	// TODO Auto-generated method stub  
        		String strDT = datepick.getText();
        		String strAP = comboBox.getSelectedItem().toString();
        		System.out.print(strDT+" "+strAP);
        		
        		ZkemSDK sdk = new ZkemSDK();             		
        		String strError=""; 
        		String iniPath = System.getProperty("user.dir");
        		ConfigReader cr = new  ConfigReader(iniPath+"\\config.ini");
                System.out.println(cr.get("config", "IP"));
                String strIPs = cr.get("config", "IP").get(0);
                String[] strIP = strIPs.split(",");
                                 
                int iMachineNumber=0;
                for(int i=0;i<strIP.length;i++){
                	System.out.println(strIP[i]);
                	if(!sdk.connect(strIP[i], 4370)){
                		strError = strError + strIP[i]+" ";
                	}else{
                		sdk.readGeneralLogData(iMachineNumber);
                		downLogs(iMachineNumber,sdk.getGeneralLogData(),strDT,strAP);
                	}
                	iMachineNumber++;
                }
                if(strError.equals("")){
                    JOptionPane.showMessageDialog(null, "��ȡ"+strDT+strAP+"��¼�ɹ���", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
                    btnAnalyseLog.setEnabled(true);
                }else{
                    JOptionPane.showMessageDialog(null, strError+"��ȡ���ڼ�¼ʧ�ܣ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
                    btnAnalyseLog.setEnabled(false);
                }
        	}
        });
        //�������ڼ�¼ ---δ�򿨡��ٵ������ˡ��Ӱ��
        btnAnalyseLog.addActionListener(new ActionListener() {  
        	@Override  
        	public void actionPerformed(ActionEvent e) {  
        	// TODO Auto-generated method stub  
        		if(DownloadPath.equals("")){
        			JOptionPane.showMessageDialog(null, "δѡ�����Ŀ¼����������޷������", "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
        		}else{
        			String strAP = comboBox.getSelectedItem().toString();
        			AnalyseLog(strAP,DownloadPath+"���ڷ������.txt");
        			btnAnalyseLog.setEnabled(false);
        		}
        	}
        });
        //�˳�����
        btnQuit.addActionListener(new ActionListener() {  
        	@Override  
        	public void actionPerformed(ActionEvent e) {  
        	// TODO Auto-generated method stub  
        		Object[] options = { "�˳�����", "ȡ��" }; 
        		int result = JOptionPane.showOptionDialog(null, "ȷ���˳���", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]); 
        		if(result == 0){
        			f.setVisible(false);
                    f.dispose();
                    System.exit(0); 
        		}          
        	}  
        }); 
    }
    
    public static void saveFileItemsToTxt(int machineNo,List<Map<String,Object>> list,String strFileName){  
        
        OutputStreamWriter outFile = null;  
        FileOutputStream fileName;  
        String strItems = null;  
        try{
        	//����׷�Ӵ洢
            //fileName = new FileOutputStream(strFileName,true); 
            //������׷�Ӵ洢
            fileName = new FileOutputStream(strFileName); 
              
            outFile = new OutputStreamWriter(fileName);  
            
            for(Map<String,Object> map:list){  
                for (String key : map.keySet()) { 
                    strItems = Integer.toString(machineNo)+"|"+map.get("EnrollNumber")+"|"+map.get("Time")+"|"+map.get("VerifyMode")+"|"+map.get("InOutMode")+"\n";
                }  
                outFile.write(strItems);  
            } 
        }  
        catch(Exception e)  
        {  
            e.printStackTrace();  
        }finally{  
            try {  
                outFile.flush();  
                outFile.close();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }
    
    //���濼����Ա��Ϣ
    public static void downUsers(List<Map<String,Object>> list){ 
    	String iniPath = System.getProperty("user.dir");
        String strurl = "jdbc:Access:///"+iniPath+"/users.mdb";
    	Connection conn = null;
    	//Statement stmt = null;
    	String strsql = "";
    	//ResultSet rs = null;
        try{
            Class.forName("com.hxtt.sql.access.AccessDriver");
            conn = DriverManager.getConnection(strurl);
            strsql = "delete from Users;";
            //stmt = conn.createStatement();
            //stmt.execute("delete from Users");
            for(Map<String,Object> map:list){
            	//System.out.print(map.get("Enabled").toString());
            	if("true".equals(map.get("Enabled").toString())){
            		String abc = "insert into Users(EnrollNumber,UserName,Privilege,Enabled) values('"+map.get("EnrollNumber")+"','"+map.get("Name")+"','"+map.get("Privilege")+"',"+map.get("Enabled")+");";
                    strsql+=abc;
                    //stmt.execute(abc);
            	}
            } 
            PreparedStatement ps = conn.prepareStatement(strsql);
            ps.execute();
        }  
        catch(Exception e)  
        {  
            e.printStackTrace();  
        }
    }
    
    //����ָ�����ڵĿ��ڼ�¼
    public static void downLogs(int machineNo,List<Map<String,Object>> list,String strDT,String strAP){ 
    	String iniPath = System.getProperty("user.dir");
        String strurl = "jdbc:Access:///"+iniPath+"/users.mdb";
    	Connection conn = null;
    	//Statement stmt = null;
    	//ResultSet rs = null;
    	String strsql = "";
    	DateFormat df = new SimpleDateFormat("yyyy-m-d HH:mm:ss");
        try{
            Class.forName("com.hxtt.sql.access.AccessDriver");
            conn = DriverManager.getConnection(strurl);
            //stmt = conn.createStatement();
            //stmt.execute("delete from LogTmp");
            strsql = "delete from LogTmp;";
            for(Map<String,Object> map:list){
            	if("ǩ��".equals(strAP)){
	            	if(strDT.equals(map.get("Time").toString().substring(0,strDT.length()))&(df.parse(map.get("Time").toString()).compareTo(df.parse(strDT+" 12:00:00")))<0){
	            		String abc = "insert into LogTmp(machineNo,EnrollNumber,LogTime,VerifyMode,InOutMode) values('"+Integer.toString(machineNo)+"','"+map.get("EnrollNumber")+"','"+map.get("Time")+"','"+map.get("VerifyMode")+"',"+map.get("InOutMode")+");";
	            		//stmt.execute(abc);
	            		strsql+=abc;
	            	}
            	}else if("ǩ��".equals(strAP)){
            		if(strDT.equals(map.get("Time").toString().substring(0,strDT.length()))&(df.parse(map.get("Time").toString()).compareTo(df.parse(strDT+" 12:00:00")))>0){
	            		String abc = "insert into LogTmp(machineNo,EnrollNumber,LogTime,VerifyMode,InOutMode) values('"+Integer.toString(machineNo)+"','"+map.get("EnrollNumber")+"','"+map.get("Time")+"','"+map.get("VerifyMode")+"',"+map.get("InOutMode")+");";
	            		//stmt.execute(abc);
	            		strsql+=abc;
	            	}
            	}
            }
            PreparedStatement ps = conn.prepareStatement(strsql);
            ps.execute();
        }  
        catch(Exception e)  
        {  
            e.printStackTrace();  
        }
    }
    
    //�������ڼ�¼
    public static void AnalyseLog(String strAP,String strFileName){ 
    	String iniPath = System.getProperty("user.dir");
        String strurl = "jdbc:Access:///"+iniPath+"/users.mdb";
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	OutputStreamWriter outFile = null;  
        FileOutputStream fileName;  
        String strItems = null;  
        try{
            Class.forName("com.hxtt.sql.access.AccessDriver");
            conn = DriverManager.getConnection(strurl);
            stmt = conn.createStatement();
            String strsql = "select a.EnrollNumber,a.UserName from Users a left join LogTmp b on a.EnrollNumber=b.EnrollNumber where b.LogTime is null";
            rs=stmt.executeQuery(strsql);
            //����׷�Ӵ洢
            //fileName = new FileOutputStream(strFileName,true);
            //������׷�Ӵ洢
            fileName = new FileOutputStream(strFileName);
            outFile = new OutputStreamWriter(fileName); 
            outFile.write("δ����Ա������\n");
            while(rs.next()){
            	String strItem = rs.getString(1)+" "+rs.getString(2)+"\n";
            	outFile.write(strItem);
        	}
            outFile.write("���ڼ�¼������\n");
            strsql  = "select a.EnrollNumber,a.UserName,b.LogTime from Users a left join LogTmp b on a.EnrollNumber=b.EnrollNumber where b.LogTime is not null order by b.LogTime";
            rs = null;
            rs = stmt.executeQuery(strsql);
            while(rs.next()){
            	String strItem = rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+"\n";
            	outFile.write(strItem);
            }
        }  
        catch(Exception e)  
        {  
            e.printStackTrace();  
        }finally{  
            try {  
                outFile.flush();  
                outFile.close();  
                JOptionPane.showMessageDialog(null, "���ڽ���������Ŀ¼"+strFileName, "��ʾ", JOptionPane.INFORMATION_MESSAGE); 
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }
    }
    
    private static DatePicker getDatePicker() {
        final DatePicker datepick;
        // ��ʽ
        String DefaultFormat = "yyyy-M-d";
        // ��ǰʱ��
        Date date = new Date();
        // ����
        Font font = new Font("Times New Roman", Font.BOLD, 14);

        Dimension dimension = new Dimension(177, 24);
        //���췽������ʼʱ�䣬ʱ����ʾ��ʽ�����壬�ؼ���С��
        datepick = new DatePicker(date, DefaultFormat, font, dimension);
        
        datepick.setLocation(137, 83);//������ʼλ��
        /*
        //Ҳ����setBounds()ֱ�����ô�С��λ��
        datepick.setBounds(137, 83, 177, 24);
        */
        // ����һ���·�����Ҫ������ʾ������
        //datepick.setHightlightdays(hilightDays, Color.red);
        // ����һ���·��в���Ҫ�����ӣ��ʻ�ɫ��ʾ
        //datepick.setDisableddays(disabledDays);
        // ���ù���
        datepick.setLocale(Locale.CHINA);
        // ����ʱ�����ɼ�
        datepick.setTimePanleVisible(false);
        return datepick;
    }
}