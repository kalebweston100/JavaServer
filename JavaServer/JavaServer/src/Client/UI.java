package Client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import DataObjects.ClientMessage;
import DataObjects.Data;
import DataObjects.Message;
import DataObjects.ServerMessage;
import DataObjects.User;
import DataObjects.UserGroup;


public class UI extends AbstractUI
{	
	//top level UI components
	JFrame frame;
	CardLayout cardLayout;
	JPanel mainPanel;
	JPanel createUser;
	JPanel loginUser;
	JPanel viewGroups;
	JPanel createGroup;
	JPanel messageGroup;
	JPanel joinGroups;
	JPanel chooseUser;
	JPanel currentMessages;
	JPanel messageUser;
	JLabel pageName;
	
	//components for create user page
	JLabel createUserTitle;
	JButton toLogin;
	JTextField createUsername;
	JTextField createPassword;
	JButton submitCreateUser;
	JLabel createUserMessage;
	
	JPanel createUserTitleContainer;
	JPanel createUserInputContainer;
	
	//components for login page
	JLabel loginUserTitle;
	JButton toCreate;
	JTextField loginUsername;
	JTextField loginPassword;
	JButton submitLoginUser;
	JLabel loginUserMessage;
	
	JPanel loginTitleContainer;
	JPanel loginInputContainer;
	
	//the user's joined groups
	//components for view groups page
	JLabel viewGroupsTitle;
	JButton toCreateGroup;
	JButton toJoinGroup;
	JButton toCurrentMessages;
	JButton selectViewGroup;
	ButtonGroup groupList;
	JLabel viewGroupsMessage;
	JPanel viewGroupsTitleContainer;
	JPanel groupContainer;
	JPanel viewGroupsFooter;
	JScrollPane viewGroupsScroll;
	
	//page to show all groups so the user can join them
	JLabel joinGroupsTitle;
	JButton toJoinedGroups;
	JButton submitJoinGroup;
	JLabel joinGroupMessage;
	ButtonGroup allGroupsList;
	JPanel allGroupsContainer;
	
	//create group components
	JLabel createGroupTitle;
	JButton toViewGroups;
	JTextField groupName;
	JButton submitCreateGroup;
	JLabel createGroupMessage;
	
	//components for message group page
	JLabel messageGroupName;
	JTextField messageContent;
	JButton submitMessage;
	JButton backToGroups;
	JPanel messageContainer;
	JScrollPane messageScroll;
	JLabel messageSent;
	JButton refreshMessages;
	
	//initialize components for chooseUser
	//allows the client to choose another user
	//to private message
	JButton backToCurrentMessages;
	JLabel chooseUserTitle;
	JPanel chooseUserTitleContainer;
	JPanel userContainer;
	JScrollPane chooseUserScroll;
	ButtonGroup chooseUserButtons;
	JButton selectUser;
	JPanel chooseUserFooter;
	
	//initialize components for currentMessages
	//allows the client to view sent and 
	//received private messages
	JButton currentMessagesToViewGroups;
	JButton toChooseUser;
	JLabel currentMessagesTitle;
	JPanel currentMessagesTitleContainer;
	JPanel currentUserContainer;
	JScrollPane currentUserScroll;
	ButtonGroup currentUserButtons;
	JButton selectMessageUser;
	JPanel currentMessageFooter;
	
	//initializes components for messageUser
	//the inside page for messaging another user
	JButton backFromMessageUser;
	JLabel messageUserTitle;
	JPanel messageUserTitleContainer;
	JPanel messageUserContainer;
	JScrollPane messageUserScroll;
	ButtonGroup messageUserButtons;
	JPanel messageUserFooter;
	JTextField messageInput;
	JButton sendUserMessage;
	
	public UI()
	{	
		//call AbstractUI constructor
		super();
		
		//initialize top level components
		frame = new JFrame();
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		createUser = new JPanel();
		createUser.setLayout(new GridBagLayout());
		loginUser = new JPanel();
		loginUser.setLayout(new GridBagLayout());
		viewGroups = new JPanel(new GridBagLayout());
		createGroup = new JPanel();
		messageGroup = new JPanel();
		joinGroups = new JPanel();
		chooseUser = new JPanel(new GridBagLayout());
		currentMessages = new JPanel(new GridBagLayout());
		messageUser = new JPanel(new GridBagLayout());
		pageName = new JLabel();
		mainPanel.add(pageName);
		
		//constraints used to style components
		GridBagConstraints pageTitleContainer = new GridBagConstraints();
		pageTitleContainer.gridx = 1;
		pageTitleContainer.gridy = 0;
		pageTitleContainer.anchor = GridBagConstraints.PAGE_START;
		pageTitleContainer.fill = GridBagConstraints.HORIZONTAL;
		pageTitleContainer.weightx = 0.5;
		pageTitleContainer.weighty = 0.5;
		
		GridBagConstraints contentContainer = new GridBagConstraints();
		contentContainer.gridx = 1;
		contentContainer.gridy = 1;
		contentContainer.anchor = GridBagConstraints.CENTER;
		contentContainer.fill = GridBagConstraints.BOTH;
		
		
		GridBagConstraints inputContainer = new GridBagConstraints();
		inputContainer.gridx = 1;
		inputContainer.gridy = 1;
		inputContainer.anchor = GridBagConstraints.PAGE_START;
		
		GridBagConstraints pageFooter = new GridBagConstraints();
		pageFooter.gridx = 1;
		pageFooter.gridy = 2;
		pageFooter.weightx = 1;
		pageFooter.weighty = 1;
		
		//initialize create user page 
		createUserTitle = new JLabel("Create A Chat Account");
		createUserTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		toLogin = new JButton("To Login");
		toLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		createUsername = new JTextField("Username", 20);
		createPassword = new JTextField("Password", 20);
		submitCreateUser = new JButton("Create Account");
		submitCreateUser.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		createUserMessage = new JLabel();
		
		createUserTitleContainer = new JPanel();
		createUserTitleContainer.setLayout(new BoxLayout(createUserTitleContainer, BoxLayout.PAGE_AXIS));
		createUserTitleContainer.add(toLogin);
		createUserTitleContainer.add(createUserTitle);
		
		createUserInputContainer = new JPanel();
		createUserInputContainer.setLayout(new BoxLayout(createUserInputContainer, BoxLayout.PAGE_AXIS));
		createUserInputContainer.add(createUsername);
		createUserInputContainer.add(createPassword);
		createUserInputContainer.add(submitCreateUser);
		
		createUser.add(createUserTitleContainer, pageTitleContainer);
		createUser.add(createUserInputContainer, inputContainer);
		createUser.add(createUserMessage, pageFooter);
		
		//add action listener for creating a user
		submitCreateUser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				UI.super.sendUserData(createUsername.getText(), createPassword.getText());
			}
		});
		
		//initialize login page 
		loginUserTitle = new JLabel("Log In To Your Account");
		loginUserTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		toCreate = new JButton("To Create Account");
		toCreate.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		loginUsername = new JTextField("Username", 20);
		loginPassword = new JTextField("Password", 20);
		submitLoginUser = new JButton("Log in");
		submitLoginUser.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		loginUserMessage = new JLabel();
		
		loginTitleContainer = new JPanel();
		loginTitleContainer.setLayout(new BoxLayout(loginTitleContainer, BoxLayout.PAGE_AXIS));
		loginTitleContainer.add(toCreate);
		loginTitleContainer.add(loginUserTitle);
		
		loginInputContainer = new JPanel();
		loginInputContainer.setLayout(new BoxLayout(loginInputContainer, BoxLayout.PAGE_AXIS));
		loginInputContainer.add(loginUsername);
		loginInputContainer.add(loginPassword);
		loginInputContainer.add(submitLoginUser);
		
		loginUser.add(loginTitleContainer, pageTitleContainer);
		loginUser.add(loginInputContainer, inputContainer);
		loginUser.add(loginUserMessage, pageFooter);
		
		//add action listener for logging a user in
		submitLoginUser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				UI.super.sendLoginData(loginUsername.getText(), loginPassword.getText());
			}
		});
		
		//initialize join groups page
		joinGroupsTitle = new JLabel("Join A Group");
		toJoinedGroups = new JButton("Your Groups");
		submitJoinGroup = new JButton("Select Group To Join");
		joinGroupMessage = new JLabel();
		allGroupsList = new ButtonGroup();
		allGroupsContainer = new JPanel();
		
		joinGroups.add(joinGroupsTitle);
		joinGroups.add(toJoinedGroups);
		joinGroups.add(submitJoinGroup);
		joinGroups.add(joinGroupMessage);
		joinGroups.add(allGroupsContainer);
		
		//add action listener to join a group
		submitJoinGroup.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				selectJoinGroup();
			}
		});
		
		//initialize view groups page
		viewGroupsTitle = new JLabel("Your Groups");
		toCreateGroup = new JButton("Create A Group");
		toJoinGroup = new JButton("Join A New Group");
		selectViewGroup = new JButton("Select Group");
		toCurrentMessages = new JButton("Private Message");
		groupList = new ButtonGroup();
		viewGroupsMessage = new JLabel();
		
		viewGroupsTitleContainer = new JPanel();
		viewGroupsTitleContainer.setLayout(new FlowLayout());
		viewGroupsTitleContainer.add(toCreateGroup);
		viewGroupsTitleContainer.add(viewGroupsTitle);
		viewGroupsTitleContainer.add(toJoinGroup);
		viewGroupsTitleContainer.add(toCurrentMessages);
		
		groupContainer = new JPanel();
		groupContainer.setLayout(new BoxLayout(groupContainer, BoxLayout.PAGE_AXIS));
		viewGroupsScroll = new JScrollPane(groupContainer);
		viewGroupsScroll.setPreferredSize(new Dimension(300, 300));
		
		viewGroupsFooter = new JPanel();
		viewGroupsFooter.setLayout(new BoxLayout(viewGroupsFooter, BoxLayout.PAGE_AXIS));
		viewGroupsFooter.add(selectViewGroup);
		viewGroupsFooter.add(viewGroupsMessage);
		
		viewGroups.add(viewGroupsTitleContainer, pageTitleContainer);
		viewGroups.add(viewGroupsScroll, contentContainer);
		viewGroups.add(viewGroupsFooter, pageFooter);
		
		//add action listener to select a message group to view
		selectViewGroup.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				selectMessageGroup();
			}
		});
		
		//initialize create group page 
		createGroupTitle = new JLabel("Create Your Own Group");
		toViewGroups = new JButton("Your Groups");
		groupName = new JTextField("Group Name", 20);
		submitCreateGroup = new JButton("Create Group");
		createGroupMessage = new JLabel();
		
		createGroup.add(createGroupTitle);
		createGroup.add(toViewGroups);
		createGroup.add(groupName);
		createGroup.add(submitCreateGroup);
		
		//add action listener to create a group
		submitCreateGroup.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				UI.super.createGroup(groupName.getText());
			}
		});
		
		//initialize message group page
		messageGroupName = new JLabel();
		messageContent = new JTextField("Message", 20);
		submitMessage = new JButton("Send Message");
		backToGroups = new JButton("Back To Groups");
		messageContainer = new JPanel();
		messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.PAGE_AXIS));
		messageScroll = new JScrollPane(messageContainer);
		messageScroll.setPreferredSize(new Dimension(300, 200));
		messageSent = new JLabel();
		
		messageGroup.add(messageGroupName);
		messageGroup.add(messageContent);
		messageGroup.add(submitMessage);
		messageGroup.add(backToGroups);
		messageGroup.add(messageScroll, BorderLayout.CENTER);
		messageGroup.add(messageSent);
		
		//add action listener to save a message
		//and then update your and other users' UIs
		submitMessage.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{	
				UI.super.saveMessage(selectedGroupId, messageContent.getText());
				UI.super.refreshMessages(selectedGroupId);
			}
		});
		
		backToCurrentMessages = new JButton("Back To Current Messages");
		backToCurrentMessages.setAlignmentX(Component.CENTER_ALIGNMENT);
		chooseUserTitle = new JLabel("Private Message A User");
		chooseUserTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		chooseUserTitleContainer = new JPanel();
		chooseUserTitleContainer.setLayout(new BoxLayout(chooseUserTitleContainer, BoxLayout.PAGE_AXIS));
		
		userContainer = new JPanel();
		userContainer.setLayout(new BoxLayout(userContainer, BoxLayout.PAGE_AXIS));
		chooseUserScroll = new JScrollPane(userContainer);
		chooseUserScroll.setPreferredSize(new Dimension(300, 300));
		chooseUserButtons = new ButtonGroup();
		
		selectUser = new JButton("Message User");
		
		chooseUserTitleContainer.add(backToCurrentMessages);
		chooseUserTitleContainer.add(chooseUserTitle);
		
		chooseUser.add(chooseUserTitleContainer, pageTitleContainer);
		chooseUser.add(chooseUserScroll, contentContainer);
		chooseUser.add(selectUser, pageFooter);
		
		//add action listener to select and message a new user
		selectUser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				selectMessageNewUser();
			}
		});
		
		//initialize components for currentMessages
		//allows the client to view sent and 
		//received private messages
		currentMessagesToViewGroups = new JButton("Back To Your Groups");
		currentMessagesTitle = new JLabel("Private Message Conversations");
		toChooseUser = new JButton("View All Users");
		currentMessagesTitleContainer = new JPanel();
		currentMessagesTitleContainer.setLayout(new FlowLayout());
		
		currentUserContainer = new JPanel();
		currentUserContainer.setLayout(new BoxLayout(currentUserContainer, BoxLayout.PAGE_AXIS));
		currentUserScroll = new JScrollPane(currentUserContainer);
		currentUserScroll.setPreferredSize(new Dimension(300, 300));
		currentUserButtons = new ButtonGroup();
		
		selectMessageUser = new JButton("Message User");
		
		currentMessagesTitleContainer.add(currentMessagesToViewGroups);
		currentMessagesTitleContainer.add(currentMessagesTitle);
		currentMessagesTitleContainer.add(toChooseUser);
		
		currentMessages.add(currentMessagesTitleContainer, pageTitleContainer);
		currentMessages.add(currentUserScroll, contentContainer);
		currentMessages.add(selectMessageUser, pageFooter);
		
		//add action listener to select a user to message
		//and refresh past messages to and from that user
		selectMessageUser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				selectMessageUser();
				UI.super.refreshUserMessages(selectedGroupId);
				cardLayout.show(mainPanel, "messageUser");
			}
		});
		
		//initializes components for messageUser
		//the inside page for messaging another user
		backFromMessageUser = new JButton("Back To All Messages");
		messageUserTitle = new JLabel();
		messageUserTitleContainer = new JPanel();
		messageUserTitleContainer.setLayout(new BoxLayout(messageUserTitleContainer, BoxLayout.PAGE_AXIS));
		
		messageUserContainer = new JPanel();
		messageUserContainer.setLayout(new BoxLayout(messageUserContainer, BoxLayout.PAGE_AXIS));
		messageUserScroll = new JScrollPane(messageUserContainer);
		messageUserScroll.setPreferredSize(new Dimension(300, 300));
		messageUserButtons = new ButtonGroup();

		messageUserFooter = new JPanel();
		messageUserFooter.setLayout(new BoxLayout(messageUserFooter, BoxLayout.PAGE_AXIS));
		messageInput = new JTextField("Message", 20);
		sendUserMessage = new JButton("Send Message");
		
		messageUserTitleContainer.add(backFromMessageUser);
		messageUserTitleContainer.add(messageUserTitle);
		
		messageUserFooter.add(messageInput);
		messageUserFooter.add(sendUserMessage);
		
		messageUser.add(messageUserTitleContainer, pageTitleContainer);
		messageUser.add(messageUserScroll, contentContainer);
		messageUser.add(messageUserFooter, pageFooter);
		
		//add action listener to save a message to another user
		//and refresh both your and their UI
		sendUserMessage.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				UI.super.saveUserMessage(selectedGroupId, messageInput.getText());
				UI.super.refreshUserMessages(selectedGroupId);
			}
		});
		
		//add panels to main panel with name
		//that will be used for navigation
		mainPanel.add(createUser, "createUser");
		mainPanel.add(loginUser, "loginUser");
		mainPanel.add(viewGroups, "viewGroups");
		mainPanel.add(joinGroups, "joinGroups");
		mainPanel.add(createGroup, "createGroup");
		mainPanel.add(messageGroup, "messageGroup");
		mainPanel.add(chooseUser, "chooseUser");
		mainPanel.add(currentMessages, "currentMessages");
		mainPanel.add(messageUser, "messageUser");
		
		//add action listeners for navigation
		//to login from create user
		toLogin.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				pageName.setText("Login");
				cardLayout.show(mainPanel, "loginUser");
			}
		});
		
		//to create from login
		toCreate.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				pageName.setText("Create Account");
				cardLayout.show(mainPanel, "createUser");
			}
		});
		
		//to create group from view groups
		toCreateGroup.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				pageName.setText("Create A Group");
				cardLayout.show(mainPanel, "createGroup");
			}
		});
		
		//back to view groups from create group
		toViewGroups.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				pageName.setText("Your Groups");
				cardLayout.show(mainPanel, "viewGroups");
				UI.super.refreshYourGroups();
			}
		});
		
		//to all groups from your groups
		toJoinGroup.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				pageName.setText("Join A Group");
				cardLayout.show(mainPanel, "joinGroups");
				UI.super.refreshAllGroups();
			}
		});
		
		//back to your groups from all groups
		toJoinedGroups.addActionListener(new ActionListener()
		{
			@Override 
			public void actionPerformed(ActionEvent ae)
			{
				pageName.setText("Your Groups");
				cardLayout.show(mainPanel, "viewGroups");
				UI.super.refreshYourGroups();
			}
		});
		
		//back to joined groups from message page
		backToGroups.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				cardLayout.show(mainPanel, "viewGroups");
				UI.super.refreshYourGroups();
			}
		});
		
		//from your groups to your current private messages
		toCurrentMessages.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				cardLayout.show(mainPanel, "currentMessages");
				UI.super.refreshMessagedUsers();
			}
		});
		
		//from current messages back to your groups
		currentMessagesToViewGroups.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				cardLayout.show(mainPanel, "viewGroups");
				UI.super.refreshYourGroups();
			}
		});
		
		//from current messages to all users
		toChooseUser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				cardLayout.show(mainPanel, "chooseUser");
				UI.super.refreshAllUsers();
			}
		});
		
		//from all users back to current messages
		backToCurrentMessages.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				cardLayout.show(mainPanel, "currentMessages");
				UI.super.refreshMessagedUsers();
			}
		});
	
		//from user message page back to current messages	
		backFromMessageUser.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				cardLayout.show(mainPanel, "currentMessages");
				UI.super.refreshMessagedUsers();
			}
		});
		
		//add top level components to frame
		frame.add(mainPanel);
		frame.setSize(600, 600);
		frame.setVisible(true);
		
		//display login page to user first
		cardLayout.show(mainPanel, "loginUser");
	}
	
	
	/* 
	 * There are two types of methods below.
	 * 
	 * The first type of method is a response method.
	 * This type of method is called by the ClientProtocol
	 * when the corresponding response was returned by
	 * the server. The response methods take the returned
	 * server data passed to them and update the UI.
	 * 
	 * The second type of method is a display method.
	 * These methods are used to display the data in 
	 * the data Lists initialized at the top of the class.
	 * Display methods are necessary because multiple 
	 * response methods will change the data Lists. 
	 * Then when each response method that changes
	 * the data Lists is called, it can call the
	 * display method to update the UI.
	 */
	
	//validates that a create user request was successful
	protected void validateCreateUser(ServerMessage serverMessage)
	{	
		if (serverMessage.getMessage().equals("userCreated"))
		{
			createUserMessage.setText("Account Created");
		}
		else
		{
			createUserMessage.setText("Error Creating Account");
		}
	}
	
	//validates that a login was successful
	//and sends the user to the main UI if validated
	protected void validateLogin(ServerMessage serverMessage)
	{	
		if (serverMessage.getMessage().equals("userValidated"))
		{
			refreshYourGroups();
			cardLayout.show(mainPanel, "viewGroups");
		}
		else
		{
			loginUserMessage.setText("Error Logging In");
		}
	}
	
	//validates that a UserGroup was created successfully
	protected void validateCreateGroup(ServerMessage serverMessage)
	{	
		if (serverMessage.getMessage().equals("groupCreated"))
		{
			createGroupMessage.setText("Group Created");
		}
		else
		{
			createGroupMessage.setText("Error Creating Group");
		}
	}
	
	//receives new UserGroup data and updates the UI
	protected void handleRefreshAllGroups(List<UserGroup> groups)
	{	
		allGroups = groups;
		displayAllGroups();
	}
	
	//displays new UserGroup data when received
	private void displayAllGroups()
	{
		allGroupsContainer.removeAll();
		
		for (UserGroup group : allGroups)
		{
			String groupId = Integer.toString(group.getGroupId());
			String groupName = group.getGroupName();
			
			JRadioButton button = new JRadioButton(groupName);
			button.setActionCommand(groupId);
			allGroupsList.add(button);
			allGroupsContainer.add(button);
		}
		
		allGroupsContainer.revalidate();
		allGroupsContainer.repaint();
	}
	
	//gets the currently selected button on the join group page
	//and calls the joinGroup method with its groupId
	private void selectJoinGroup()
	{
		JRadioButton selected = null;
		
		for (Enumeration<AbstractButton> buttons = allGroupsList.getElements(); buttons.hasMoreElements();)
		{
			JRadioButton button = (JRadioButton) buttons.nextElement();
			if (button.isSelected())
			{
				selected = button;
			}
		}
		
		if (selected != null)
		{
			int groupId = Integer.parseInt(selected.getActionCommand());
			joinGroup(groupId);
		}
		else
		{
			viewGroupsMessage.setText("Select A Group");
		}
		
	}
	
	//validates that a group was joined successfully
	protected void validateJoinGroup(ServerMessage serverMessage)
	{	
		if (serverMessage.getMessage().equals("groupJoined"))
		{
			joinGroupMessage.setText(groupName + " joined");
			refreshAllGroups();
		}
		else
		{
			joinGroupMessage.setText("Error Joining Group");
		}
	}
	
	//receives new data for UserGroups that a User is a member of
	protected void handleJoinedGroupRefresh(List<UserGroup> groups)
	{
		currentGroups = groups;
		displayJoinedGroups();
	}
	
	//displays new joined UserGroup data
	private void displayJoinedGroups()
	{
		groupContainer.removeAll();
		
		for (UserGroup group : currentGroups)
		{
			String groupId = Integer.toString(group.getGroupId());
			String groupName = group.getGroupName();
			
			JRadioButton button = new JRadioButton(groupName);
			button.setActionCommand(groupId);
			groupList.add(button);
			groupContainer.add(button);
		}
		
		groupContainer.revalidate();
		groupContainer.repaint();
	}

	//selects a UserGroup to view, changes the selectedGroupId
	//to its groupId, and refreshes the Messages for that id
	private void selectMessageGroup()
	{
		JRadioButton selected = null;
		
		for (Enumeration<AbstractButton> buttons = groupList.getElements(); buttons.hasMoreElements();)
		{
			JRadioButton button = (JRadioButton) buttons.nextElement();
			if (button.isSelected())
			{
				selected = button;
			}
		}
		
		if (selected != null)
		{
			String groupName = selected.getText();
			int groupId = Integer.parseInt(selected.getActionCommand());
			selectedGroupId = groupId;
			messageGroupName.setText(groupName);
			refreshMessages(selectedGroupId);
			cardLayout.show(mainPanel, "messageGroup");
		}
		else
		{
			viewGroupsMessage.setText("Select A Group");
		}
		
	}
	
	//receives new Message objects, updates messages,
	//and displays thoses Messages
	protected void handleMessageRefresh(List<Message> newMessages)
	{
		messages = newMessages;
		displayGroupMessages();
	}
	
	//displays new Messages for non-private UserGroups
	private void displayGroupMessages()
	{
		messageContainer.removeAll();
		
		for (Message message : messages)
		{
			String senderUsername = message.getSenderUsername();
			String content = message.getMessageContent();
			
			JLabel displayMessage = new JLabel(senderUsername + " : " + content);
			messageContainer.add(displayMessage);
		}
		
		messageContainer.revalidate();
		messageContainer.repaint();
	}
	
	//validates that a Message was saved successfully
	protected void validateSaveMessage(ServerMessage serverMessage)
	{
		if (serverMessage.getMessage().equals("messageSaved"))
		{
			messageSent.setText("Message Sent");
		}
		else
		{
			messageSent.setText("Error Sending Message");
		}
	}
	
	//displays all User objects that the current User
	//does not already have a private UserGroup with
	private void displayAllUsers()
	{
		userContainer.removeAll();
		
		for (User user : allUsers)
		{
			String username = user.getUsername();
			String userId = Integer.toString(user.getUserId());
			
			JRadioButton button = new JRadioButton(username);
			button.setActionCommand(userId);
			chooseUserButtons.add(button);
			userContainer.add(button);
		}
		
		userContainer.revalidate();
		userContainer.repaint();
	}
	
	//selects a User to create a new private UserGroup with 
	private void selectMessageNewUser()
	{
		JRadioButton selected = null;
		
		for (Enumeration<AbstractButton> buttons = chooseUserButtons.getElements(); buttons.hasMoreElements();)
		{
			JRadioButton button = (JRadioButton) buttons.nextElement();
			
			if (button.isSelected())
			{
				selected = button;
			}
		}
		
		if (selected != null)
		{
			String username = selected.getText();
			int userId = Integer.parseInt(selected.getActionCommand());
			messageNewUser(userId, username);
		}
		else
		{
			//display error
		}
	}
	
	//on return of messageNewUser data, sets selectedGroupId
	//to the newly created groupId and sends to the messageUser page
	//so the current User can message the new User they selected
	protected void handleMessageNewUser(ServerMessage serverMessage)
	{
		int groupId = Integer.parseInt(serverMessage.getMessage());
		selectedGroupId = groupId;
		refreshUserMessages(selectedGroupId);
		cardLayout.show(mainPanel, "messageUser");
	}
	
	//sets the selectedGroupId to the selected
	//private UserGroup
	private void selectMessageUser()
	{
		//currentUserContainer
		//currentUserButtons
		JRadioButton selected = null;
		for (Enumeration<AbstractButton> buttons = currentUserButtons.getElements(); buttons.hasMoreElements();)
		{
			JRadioButton button = (JRadioButton) buttons.nextElement();
			
			if (button.isSelected())
			{
				selected = button;
			}
		}
		
		if (selected != null)
		{
			int groupId = Integer.parseInt(selected.getActionCommand());
			selectedGroupId = groupId;
		}
	}
	
	//displays all the Users in the
	//currentMessagedUsers ArrayList
	private void displayMessagedUsers()
	{
		currentUserContainer.removeAll();
		
		for (UserGroup group : messagedUserGroups)
		{
			String groupName = group.getGroupName();
			String groupId = Integer.toString(group.getGroupId());
			
			JRadioButton button = new JRadioButton(groupName);
			button.setActionCommand(groupId);
			currentUserButtons.add(button);
			currentUserContainer.add(button);
		}
		
		currentUserContainer.revalidate();
		currentUserContainer.repaint();
	}
	
	//receives a new Message data and updates the current data
	protected void handleUserMessageRefresh(List<Message> newMessages)
	{
		userMessages = newMessages;
		displayUserMessages();
	}
	
	//displays all the Messages in the
	//userMessages ArrayList
	private void displayUserMessages()
	{
		messageUserContainer.removeAll();
		
		for (Message message : userMessages)
		{
			String senderUsername = message.getSenderUsername();
			String content = message.getMessageContent();
			
			JLabel displayMessage = new JLabel(senderUsername + " : " + content);
			messageUserContainer.add(displayMessage);
		}
		
		messageUserContainer.revalidate();
		messageUserContainer.repaint();
	}
	
	//validates that a private UserGroup message was saved
	protected void validateSaveUserMessage(ServerMessage serverMessage)
	{
		if (serverMessage.getMessage().equals("messageSaved"))
		{
			//display that message was sent
		}
	}
	
	//refreshes the list of all Users
	protected void handleAllUserRefresh(List<User> users)
	{
		allUsers = users;
		displayAllUsers();
	}
	
	//refreshes the list of private UserGroups
	protected void handleMessagedUserRefresh(List<UserGroup> groups)
	{
		messagedUserGroups = groups;
		displayMessagedUsers();
	}
		
}

