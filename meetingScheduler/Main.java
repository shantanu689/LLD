package meetingScheduler;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

// Strategy Pattern for Multiple Notification Channels
interface NotificationStrategy
{
    void send(User user, String message);
}

class EmailNotification implements NotificationStrategy
{
    public void send(User user, String message)
    {
        System.out.println("[Email to " + user.name + "] " + message);
    }
}

class SMSNotification implements NotificationStrategy
{
    public void send(User user, String message)
    {
        System.out.println("[SMS to " + user.name + "] " + message);
    }
}

enum NotificationType
{
    MEETING_SCHEDULED,
    MEETING_CANCELLED,
    MEETING_UPDATED
}

// User
//  -Id, Name

// Meeting
//  - Id, Name, List<User> participants, long startTime, long endTime, MeetingRoom meetingRoom

// MeetingRoom
// - Id, Name, Capacity, List<Meeting> (Replace with TreeMap<long, Booking>)

// 

// MeetingRoomScheduler
//  - getAvailableRooms()
//  - scheduleMeeting()

class MeetingRoom
{
    public String id;
    public String name;
    public int capacity;
    public List<Meeting> meetings;
    private final ReentrantLock lock = new ReentrantLock(); // ReentrantLock for thread-safe operations

    public MeetingRoom(String name, int capacity)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.capacity = capacity;
        this.meetings = new ArrayList<>();
    }

    public ReentrantLock getLock()
    {
        return lock;
    }

    @Override
    public String toString()
    {
        return "MeetingRoom=> Name :" + this.name + ", Capacity: " + this.capacity;
    }
}

class User
{
    public String id;
    public String name;
    public List<NotificationStrategy> preferredChannels;

    public User(String name)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.preferredChannels = new ArrayList<>();
    }

    public void addNotificationChannel(NotificationStrategy strategy)
    {
        preferredChannels.add(strategy);
    }
}

class Meeting
{
    public String id;
    public String name;
    public long startTime;
    public long endTime;
    public List<User> participants;
    public MeetingRoom meetingRoom;

    public Meeting(String name, long startTime, long endTime, List<User> participants, MeetingRoom meetingRoom)
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = new ArrayList<>();
        for(var user: participants)
        {
            this.participants.add(user);
        }

        this.meetingRoom = meetingRoom;
    }

    @Override
    public String toString()
    {
        return "Meeting " + name + " scheduled from " + startTime + " to " + endTime + " in room: " + meetingRoom.name;
    }
}

class MeetingService
{
    HashMap<String, Meeting> meetings;
    MeetingRoomService meetingRoomService;

    public MeetingService(MeetingRoomService meetingRoomService)
    {
        this.meetings = new HashMap<>();
        this.meetingRoomService = meetingRoomService;
    }

    public void addMeeting(Meeting meeting)
    {
        meetings.put(meeting.id, meeting);
        notifyUsers(meeting.participants, NotificationType.MEETING_SCHEDULED, meeting);
    }

    public boolean cancelMeeting(String meetingId)
    {
        if(meetings.containsKey(meetingId))
        {
            Meeting meeting = meetings.remove(meetingId);
            if(meeting != null && meeting.meetingRoom != null)
            {
                meetingRoomService.cancelMeetingFromRoom(meeting.meetingRoom, meeting);
                notifyUsers(meeting.participants, NotificationType.MEETING_CANCELLED, meeting);
                return true;
            }
        }
        return false;
    }

    private void notifyUsers(List<User> users, NotificationType type, Meeting meeting)
    {
        String message = buildNotificationMessage(type, meeting);
        for(User user : users)
        {
            // Send notification via all user's preferred channels
            for(NotificationStrategy strategy : user.preferredChannels)
            {
                strategy.send(user, message);
            }
        }
    }

    private String buildNotificationMessage(NotificationType type, Meeting meeting)
    {
        switch(type)
        {
            case MEETING_SCHEDULED:
                return "Meeting '" + meeting.name + "' has been scheduled in " + meeting.meetingRoom.name;
            case MEETING_CANCELLED:
                return "Meeting '" + meeting.name + "' has been cancelled";
            case MEETING_UPDATED:
                return "Meeting '" + meeting.name + "' has been updated";
            default:
                return "Meeting notification";
        }
    }
}

class MeetingRoomService
{
    HashMap<String, MeetingRoom> meetingRooms;

    public MeetingRoomService()
    {
        this.meetingRooms = new HashMap<>();
    }

    public MeetingRoom addRoom(String name, int capacity)
    {
        var room = new MeetingRoom(name, capacity);
        meetingRooms.put(room.id, room);
        return room;
    }

    public List<MeetingRoom> getAvailableRooms(long startTime, long endTime, int requiredCapacity)
    {
        List<MeetingRoom> result = new ArrayList<>();
        for(var entry: meetingRooms.entrySet())
        {
            var room = entry.getValue();
            room.getLock().lock();
            try
            {
                if(isRoomAvailable(room, startTime, endTime) && room.capacity >= requiredCapacity)
                {
                    result.add(room);
                }
            }
            finally
            {
                room.getLock().unlock();
            }
        }

        return result;
    }

    private boolean isRoomAvailable(MeetingRoom room, long startTime, long endTime)
    {
        for(var meeting : room.meetings)
        {
            if(startTime<meeting.endTime && endTime>meeting.startTime)
            {
                return false;
            }
        }

        return true;
    }

    public boolean bookMeeting(MeetingRoom room, Meeting meeting)
    {
        // Use ReentrantLock to prevent concurrent double-booking
        room.getLock().lock();
        try
        {
            if(!isRoomAvailable(room, meeting.startTime, meeting.endTime))
            {
                return false;
            }
            room.meetings.add(meeting);
            return true;
        }
        finally
        {
            // Re-entrant lock should be released explicitly 
            // try finally is needed to release lock if exception occurs(This is not required in synchronized)
            room.getLock().unlock();
        }
    }

    public void cancelMeetingFromRoom(MeetingRoom room, Meeting meeting)
    {
        // Use ReentrantLock when modifying room's meeting list
        room.getLock().lock();
        try
        {
            room.meetings.remove(meeting);
        }
        finally
        {
            room.getLock().unlock();
        }
    }
}

class MeetingScheduler
{
    private static MeetingScheduler instance = null;
    private MeetingRoomService meetingRoomService;
    private MeetingService meetingService;

    private MeetingScheduler(MeetingRoomService meetingRoomService, MeetingService meetingService) {
        this.meetingRoomService = meetingRoomService;
        this.meetingService = meetingService;
    }

    public static MeetingScheduler getInstance(MeetingRoomService meetingRoomService, MeetingService meetingService)
    {
        if(instance == null)
        {
            synchronized(MeetingScheduler.class)
            {
                if(instance == null)
                {
                    instance = new MeetingScheduler(meetingRoomService, meetingService);
                }
            }
        }

        return instance;
    }

    public List<MeetingRoom> getAvailableRooms(long startTime, long endTime, int requiredCapacity)
    {
        return meetingRoomService.getAvailableRooms(startTime, endTime, requiredCapacity);
    }

    public Meeting scheduleMeeting(String name, long startTime, long endTime, List<User> participants, MeetingRoom meetingRoom)
    {
        // Create meeting
        Meeting meeting = new Meeting(name, startTime, endTime, participants, meetingRoom);
        
        // Book the room
        if(!meetingRoomService.bookMeeting(meetingRoom, meeting))
        {
            return null;
        }

        meetingService.addMeeting(meeting);
        return meeting;
    }

    public boolean cancelMeeting(String meetingId)
    {
        return meetingService.cancelMeeting(meetingId);
    }
}

public class Main {
    public static void main(String args[])
    {
        try {
            // Initialize services
            MeetingRoomService roomService = new MeetingRoomService();
            MeetingService meetingService = new MeetingService(roomService);
            MeetingScheduler scheduler = MeetingScheduler.getInstance(roomService, meetingService);

            // Add meeting rooms
            MeetingRoom room1 = roomService.addRoom("Conference Room", 2);
            MeetingRoom room3 = roomService.addRoom("Board Room", 5);

            EmailNotification emailNotif = new EmailNotification();
            SMSNotification smsNotif = new SMSNotification();

            // Create users with email, phone, and notification preferences
            User user1 = new User("Alice");
            user1.addNotificationChannel(emailNotif);   // Alice wants email
            user1.addNotificationChannel(smsNotif);     // and SMS

            User user2 = new User("Bob");
            user2.addNotificationChannel(emailNotif);

            List<User> meeting1Participants = new ArrayList<>(Arrays.asList(user1, user2));

            List<MeetingRoom> rooms = scheduler.getAvailableRooms(10, 11, 2);
            System.out.println("Available rooms:");
            for(var room: rooms)
            {
                System.out.println(room);
            }
            Meeting meeting1 = scheduler.scheduleMeeting(
                "Sprint Planning", 
                10, 
                11, 
                meeting1Participants, 
                room1
            );
            System.out.println(meeting1);

            rooms = scheduler.getAvailableRooms(10, 11, 2);
            System.out.println("Available rooms:");
            for(var room: rooms)
            {
                System.out.println(room);
            }
            Meeting meeting2 = scheduler.scheduleMeeting(
                "Team Meeting", 
                10, 
                11, 
                meeting1Participants, 
                room3
            );
            System.out.println(meeting2);


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
