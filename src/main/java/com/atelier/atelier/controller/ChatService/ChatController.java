package com.atelier.atelier.controller.ChatService;

import com.atelier.atelier.context.*;
import com.atelier.atelier.entity.MessagingSystem.*;
import com.atelier.atelier.entity.UserPortalManagment.User;
import com.atelier.atelier.entity.UserPortalManagment.UserChatterConnection;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshop;
import com.atelier.atelier.entity.WorkshopManagment.OfferedWorkshopChatroom;
import com.atelier.atelier.repository.ChatService.ChatroomRepository;
import com.atelier.atelier.repository.ChatService.ChatterMessageRelationRepository;
import com.atelier.atelier.repository.ChatService.ChatterRepository;
import com.atelier.atelier.repository.ChatService.MessageRepository;
import com.atelier.atelier.repository.user.UserRepository;
import com.atelier.atelier.repository.workshop.OfferingWorkshopRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private ChatroomRepository chatroomRepository;
    private ChatterRepository chatterRepository;
    private ChatterMessageRelationRepository chatterMessageRelationRepository;
    private OfferingWorkshopRepository offeringWorkshopRepository;
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    public ChatController(MessageRepository messageRepository, UserRepository userRepository, OfferingWorkshopRepository offeringWorkshopRepository, ChatterMessageRelationRepository chatterMessageRelationRepository, ChatterRepository chatterRepository, ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.offeringWorkshopRepository = offeringWorkshopRepository;
        this.chatterRepository = chatterRepository;
        this.chatterMessageRelationRepository = chatterMessageRelationRepository;
    }


    @GetMapping("/offeringWorkshop/{offeringWorkshopId}/chatrooms")
    public ResponseEntity<Object> getOfferingWorkshopAttAndGraderChatroomIds(@PathVariable long offeringWorkshopId){
        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        OfferingWorkshopChatroomsIdContext offeringWorkshopChatroomsIdContext = new OfferingWorkshopChatroomsIdContext();

        offeringWorkshopChatroomsIdContext.setAttendeeChatroomId(offeredWorkshop.getAttendeesChatroom().getId());
        offeringWorkshopChatroomsIdContext.setGraderChatroomId(offeredWorkshop.getGradersChatroom().getId());

        return new ResponseEntity<>(offeringWorkshopChatroomsIdContext, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<Object> getChat(@PathVariable long chatId){

        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(chatId);

        if (!optionalChatroom.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Chatroom chatroom = optionalChatroom.get();

        return new ResponseEntity<>(chatroom, HttpStatus.OK);
    }

    @GetMapping("/chatter/{chatterId}")
    public ResponseEntity<Object> getAChattersChatIds(@PathVariable long chatterId){

        Optional<Chatter> optionalChatter = chatterRepository.findById(chatterId);

        if (!optionalChatter.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Chatter chatter = optionalChatter.get();

        ChatterChatroomIdsContext chatterChatroomIdsContext = new ChatterChatroomIdsContext();

        List<Long> chatroomsId = new ArrayList<Long>();

        for (Chatroom chatroom : chatter.getChatrooms()){
            chatroomsId.add(chatroom.getId());
        }

        chatterChatroomIdsContext.setChatroomIds(chatroomsId);

        return new ResponseEntity<>(chatterChatroomIdsContext, HttpStatus.OK);
    }


    @GetMapping("/offeringWorkshop/{offeringWorkshopId}/chatter/{chatterId}")
    public ResponseEntity<Object> getAChattersChatroomsForOfferingWorkshop(@PathVariable long offeringWorkshopId, @PathVariable long chatterId){

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        Optional<Chatter> optionalChatter = chatterRepository.findById(chatterId);


        if (!optionalChatter.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Chatter chatter = optionalChatter.get();
        List<OfferedWorkshopChatroom> offeredWorkshopChatrooms = new ArrayList<>();

        for (OfferedWorkshopChatroom offeredWorkshopChatroom: offeredWorkshop.getOfferedWorkshopChatrooms()){

            if (offeredWorkshopChatroom.getChatters().contains(chatter)){
                offeredWorkshopChatrooms.add(offeredWorkshopChatroom);
            }

        }


        return new ResponseEntity<>(offeredWorkshopChatrooms, HttpStatus.OK);
    }


    @PostMapping("/offeringWorkshop/{offeringWorkshopId}/chatrooms")
    public ResponseEntity<Object> createChatroomForOfferingWorkshop(@PathVariable long offeringWorkshopId, @RequestBody CreateChatroomContext createChatroomContext){

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        OfferedWorkshopChatroom offeredWorkshopChatroom = new OfferedWorkshopChatroom();

        offeredWorkshopChatroom.setName(createChatroomContext.getName());

        offeredWorkshopChatroom.setOfferedWorkshop(offeredWorkshop);

        chatroomRepository.save(offeredWorkshopChatroom);

        for (Long userId : createChatroomContext.getUserIds()){
            Optional<User> optionalUser = userRepository.findById(userId);

            if (!optionalUser.isPresent()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            User user = optionalUser.get();

            Chatter chatter = user.getUserChatterConnection();

            offeredWorkshopChatroom.addChatter(chatter);
            chatter.addChatroom(offeredWorkshopChatroom);
            chatterRepository.save(chatter);

        }

        offeredWorkshop.addToWorkshopChatrooms(offeredWorkshopChatroom);

        chatroomRepository.save(offeredWorkshopChatroom);
        offeringWorkshopRepository.save(offeredWorkshop);

        return new ResponseEntity<>(offeredWorkshopChatroom.getId(), HttpStatus.CREATED);
    }


    @GetMapping("/offeringWorkshop/{offeringWorkshopId}/allChatrooms")
    public ResponseEntity<Object> getAllChatroomForOfferingWorkshop(@PathVariable long offeringWorkshopId){

        Optional<OfferedWorkshop> optionalOfferedWorkshop = offeringWorkshopRepository.findById(offeringWorkshopId);

        if (!optionalOfferedWorkshop.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshop offeredWorkshop = optionalOfferedWorkshop.get();

        OfferedWorkshopChatroomContext chatroomContext = new OfferedWorkshopChatroomContext();

        List<OfferedWorkshopChatroom> offeredWorkshopChatrooms = offeredWorkshop.getOfferedWorkshopChatrooms();

        chatroomContext.setOfferedWorkshopChatrooms(offeredWorkshopChatrooms);

        return new ResponseEntity<>(chatroomContext, HttpStatus.OK);

    }


    @PostMapping("/chat/{chatId}")
    public ResponseEntity<Object> addUserToChatroom(@PathVariable long chatId, @RequestBody UserIdContext userIdContext){

        Optional<Chatroom> optionalOfferedWorkshopChatroom = chatroomRepository.findById(chatId);

        if (!optionalOfferedWorkshopChatroom.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshopChatroom offeredWorkshopChatroom = (OfferedWorkshopChatroom) optionalOfferedWorkshopChatroom.get();

        Optional<User> optionalUser = userRepository.findById(userIdContext.getUserId());

        if (!optionalUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        User user = optionalUser.get();


        Chatter chatter = user.getUserChatterConnection();

        for (Chatter chatter1 : offeredWorkshopChatroom.getChatters()){

            if (chatter1.getId() == chatter.getId()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        offeredWorkshopChatroom.addChatter(chatter);
        chatter.addChatroom(offeredWorkshopChatroom);
        chatterRepository.save(chatter);
        chatroomRepository.save(offeredWorkshopChatroom);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/chat/{chatId}/message")
    public ResponseEntity<Object> sendMessageToAChatroom(@PathVariable long chatId, @RequestBody MessageContext messageContext, Authentication authentication) throws ParseException {

        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(chatId);

        if (!optionalChatroom.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshopChatroom offeredWorkshopChatroom = (OfferedWorkshopChatroom) optionalChatroom.get();

        User user = User.getUser(authentication, userRepository);

        Chatter chatter = user.getUserChatterConnection();

        if (!offeredWorkshopChatroom.getChatters().contains(chatter)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Message message = new Message();

        message.setText(messageContext.getText());

        TimeZone tz = TimeZone.getTimeZone("GMT");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(nowAsISO));

        message.setTime(cal);

        message.setChatroom(offeredWorkshopChatroom);

        messageRepository.save(message);

        ChatterMessageRelation chatterMessageRelation = new ChatterMessageRelation();
        chatterMessageRelation.setChatter(chatter);
        chatterMessageRelation.setMessageRelation(MessageRelation.Sender);
        chatterMessageRelation.setMessageReadStatus(MessageReadStatus.Seen);
        chatterMessageRelation.setMessage(message);

        chatterMessageRelationRepository.save(chatterMessageRelation);

        chatter.addChatterMessageRelation(chatterMessageRelation);
        chatterRepository.save(chatter);

        message.addChatterMessageRelation(chatterMessageRelation);
        messageRepository.save(message);

        for (Chatter receiverChatter: offeredWorkshopChatroom.getChatters()){

            if (receiverChatter.getId() == chatter.getId()){
                continue;
            }
            else{
                ChatterMessageRelation chatterMessageRelation1 = new ChatterMessageRelation();
                chatterMessageRelation1.setChatter(receiverChatter);
                chatterMessageRelation1.setMessageRelation(MessageRelation.Receiver);
                chatterMessageRelation1.setMessageReadStatus(MessageReadStatus.Unseen);
                chatterMessageRelation1.setMessage(message);

                chatterMessageRelationRepository.save(chatterMessageRelation1);

                receiverChatter.addChatterMessageRelation(chatterMessageRelation1);
                chatterRepository.save(receiverChatter);

                message.addChatterMessageRelation(chatterMessageRelation1);
                messageRepository.save(message);
            }
        }

        offeredWorkshopChatroom.addMessage(message);

        chatroomRepository.save(offeredWorkshopChatroom);

        return new ResponseEntity<>(message.getId(), HttpStatus.CREATED);

    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<Object> getMessage(@PathVariable long messageId){

        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (!optionalMessage.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Message message = optionalMessage.get();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @GetMapping("/message/{messageId}/sender")
    public ResponseEntity<Object> getMessageSenderUser(@PathVariable long messageId){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);

        if (!optionalMessage.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Message message = optionalMessage.get();

        for (ChatterMessageRelation chatterMessageRelation : message.getChatterMessageRelations()){

            if (chatterMessageRelation.getMessageRelation().equals(MessageRelation.Sender)){

                Chatter chatter = chatterMessageRelation.getChatter();

                List<User> users = userRepository.findAll();

                for (User user : users){

                    if (user.getUserChatterConnection().getId() == chatter.getId()){

                        return new ResponseEntity<>(user, HttpStatus.OK);
                    }
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/chat/{chatId}/messages")
    public ResponseEntity<Object> getChatroomMessagesWithSenderName(@PathVariable long chatId){

        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(chatId);

        if (!optionalChatroom.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshopChatroom offeredWorkshopChatroom = (OfferedWorkshopChatroom) optionalChatroom.get();

        List<MessageNameContext> messageNameContexts = new ArrayList<>();
        List<User> users = userRepository.findAll();

        for (Message message : offeredWorkshopChatroom.getMessages()){

            MessageNameContext messageNameContext = new MessageNameContext();

            messageNameContext.setContent(message.getText());
            User user = findMessageSender(message);
            messageNameContext.setParticipantId(user.getUserChatterConnection().getId());
            MessageTimestampContext messageTimestampContext = new MessageTimestampContext(message.getTime());
            messageNameContext.setTimestamp(messageTimestampContext);

//            for (ChatterMessageRelation chatterMessageRelation : message.getChatterMessageRelations()){
//
//                if (chatterMessageRelation.getMessageRelation().equals(MessageRelation.Sender)){
//
//                    Chatter chatter = chatterMessageRelation.getChatter();
//
//                    for (User user: users){
//
//                        if (user.getUserChatterConnection().getId() == chatter.getId()){
//                            messageNameContext.setSenderName(user.getName());
//                            break;
//                        }
//
//                    }
//
//                    break;
//                }
//            }

            messageNameContexts.add(messageNameContext);
        }

        return new ResponseEntity<>(messageNameContexts, HttpStatus.OK);
    }


    @GetMapping("/chat/{chatId}/participants")
    public ResponseEntity<Object> getParticipantNamesWithChatterId(@PathVariable long chatId){

        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(chatId);

        if (!optionalChatroom.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshopChatroom offeredWorkshopChatroom = (OfferedWorkshopChatroom) optionalChatroom.get();

        List<NameChatterContext> nameChatterContexts = new ArrayList<>();

        for (Chatter chatter : offeredWorkshopChatroom.getChatters()){

            UserChatterConnection userChatterConnection = (UserChatterConnection) chatter;

            NameChatterContext nameChatterContext = new NameChatterContext();

            nameChatterContext.setName(userChatterConnection.getUser().getName());
            nameChatterContext.setId(chatter.getId());

            nameChatterContexts.add(nameChatterContext);
        }

        return new ResponseEntity<>(nameChatterContexts, HttpStatus.OK);
    }


    @GetMapping("/chat/{chatId}/unread/{userId}")
    public ResponseEntity<Object> getUnreadMessagesForUserInAChatroom(@PathVariable long chatId, @PathVariable long userId){

        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(chatId);

        if (!optionalChatroom.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshopChatroom offeredWorkshopChatroom = (OfferedWorkshopChatroom) optionalChatroom.get();

        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        User user = optionalUser.get();

        Chatter chatter = user.getUserChatterConnection();

        List<MessageNameContext> messageNameContexts = new ArrayList<>();


        for (Chatter chatter1 : offeredWorkshopChatroom.getChatters()){

            if (chatter1.getId() == chatter.getId()){

                for (Message message : offeredWorkshopChatroom.getMessages()){

                    for (ChatterMessageRelation chatterMessageRelation : message.getChatterMessageRelations()){

                        UserChatterConnection relatedChatter = (UserChatterConnection) chatterMessageRelation.getChatter();

                        if ((relatedChatter.getId() == chatter.getId()) && (chatterMessageRelation.getMessageReadStatus().equals(MessageReadStatus.Unseen))){

                            chatterMessageRelation.setMessageReadStatus(MessageReadStatus.Seen);

                            chatterMessageRelationRepository.save(chatterMessageRelation);

                            MessageNameContext messageNameContext = new MessageNameContext();

                            MessageTimestampContext messageTimestampContext = new MessageTimestampContext(message.getTime());
                            messageNameContext.setTimestamp(messageTimestampContext);
                            User sender = findMessageSender(message);
                            messageNameContext.setParticipantId(sender.getUserChatterConnection().getId());
                            messageNameContext.setContent(" * " + message.getText());

                            messageNameContexts.add(messageNameContext);

                            break;
                        }
                    }
                }

                return new ResponseEntity<>(messageNameContexts, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //Also returns messages sent by this person
    @GetMapping("/chat/{chatId}/read/{userId}")
    public ResponseEntity<Object> getReadMessagesForUserInChatroom(@PathVariable long chatId, @PathVariable long userId){

        Optional<Chatroom> optionalChatroom = chatroomRepository.findById(chatId);

        if (!optionalChatroom.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        OfferedWorkshopChatroom offeredWorkshopChatroom = (OfferedWorkshopChatroom) optionalChatroom.get();

        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        User user = optionalUser.get();

        Chatter chatter = user.getUserChatterConnection();

        List<MessageNameContext> messageNameContexts = new ArrayList<>();

        for (Chatter chatter1 : offeredWorkshopChatroom.getChatters()){

            if (chatter1.getId() == chatter.getId()){

                for (Message message : offeredWorkshopChatroom.getMessages()){

                    for (ChatterMessageRelation chatterMessageRelation : message.getChatterMessageRelations()){

                        UserChatterConnection relatedChatter = (UserChatterConnection) chatterMessageRelation.getChatter();

                        if ((relatedChatter.getId() == chatter.getId()) && (chatterMessageRelation.getMessageReadStatus().equals(MessageReadStatus.Seen))){

                            MessageNameContext messageNameContext = new MessageNameContext();

                            MessageTimestampContext messageTimestampContext = new MessageTimestampContext(message.getTime());
                            messageNameContext.setTimestamp(messageTimestampContext);
                            User sender = findMessageSender(message);
                            messageNameContext.setParticipantId(sender.getUserChatterConnection().getId());
                            messageNameContext.setContent(message.getText());

                            messageNameContexts.add(messageNameContext);

                            break;
                        }
                    }
                }

                return new ResponseEntity<>(messageNameContexts, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }



    public User findMessageSender(Message message){

        List<User> users = userRepository.findAll();

        for (ChatterMessageRelation chatterMessageRelation : message.getChatterMessageRelations()){

            if (chatterMessageRelation.getMessageRelation().equals(MessageRelation.Sender)){

                Chatter chatter = chatterMessageRelation.getChatter();

                for (User user : users){

                    if (user.getUserChatterConnection().getId() == chatter.getId()){
                        return user;
                    }
                }
            }
        }

        return null;
    }




}
