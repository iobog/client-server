package concurs.rpcprotocol;

import concurs.model.*;
import concurs.model.Inscriere;
import concurs.model.Participant;
import concurs.model.PersoanaOficiu;
import concurs.model.Proba;
import concurs.services.ConcursException;
import concurs.services.IConcursOberver;
import concurs.services.IConcursServices;
import grpc.ConcursServiceGrpc;
import grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.grpc.stub.StreamObserver;

import javax.net.ssl.SSLException;
import java.util.ArrayList;
import java.util.List;

public class ConcursServicesGrpcProxy implements IConcursServices {
  private final ConcursServiceGrpc.ConcursServiceBlockingStub stub;
  private final ConcursServiceGrpc.ConcursServiceStub asyncStub;
  private IConcursOberver client;

  public ConcursServicesGrpcProxy(String host, int port) throws SSLException {
    System.out.println("Connecting to server " + host + ":" + port);
    ManagedChannel channel = NettyChannelBuilder
            .forAddress(host, port)
            .sslContext(GrpcSslContexts.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build())
            .build();
    this.stub = ConcursServiceGrpc.newBlockingStub(channel);
    this.asyncStub = ConcursServiceGrpc.newStub(channel);
  }


  @Override
  public void login(PersoanaOficiu persoana, IConcursOberver client) throws ConcursException {
    this.client = client;
    try {
      LoginRequest request = LoginRequest.newBuilder()
              .setPersoana(toProto(persoana))
              .build();
      stub.login(request);
      subscribeToUpdates(persoana.getUsername());
    } catch (StatusRuntimeException e) {
      throw new ConcursException("Login failed: " + e.getMessage());
    }
  }

  @Override
  public void logout(PersoanaOficiu persoana, IConcursOberver client) throws ConcursException {
    try {
      LogoutRequest request = LogoutRequest.newBuilder()
              .setPersoana(toProto(persoana))
              .build();
      stub.logout(request);
    } catch (StatusRuntimeException e) {
      throw new ConcursException("Logout failed: " + e.getMessage());
    }
  }


  @Override
  public boolean addParticipantToProba(Integer participantId, Integer probaId) throws ConcursException {
    try {
      AddToProbaRequest request = AddToProbaRequest.newBuilder()
              .setParticipantId(participantId)
              .setProbaId(probaId)
              .build();
      BoolResponse response = stub.addParticipantToProba(request);
      return response.getSuccess();
    } catch (StatusRuntimeException e) {
      throw new ConcursException("Add to proba failed: " + e.getMessage());
    }
  }

  @Override
  public concurs.model.Participant addParticipant(concurs.model.Participant participant) throws ConcursException {
    try {
      AddParticipantRequest request = AddParticipantRequest.newBuilder()
              .setParticipant(toProto(participant))
              .build();
      ParticipantResponse response = stub.addParticipant(request);
      return fromProto(response.getParticipant());
    } catch (StatusRuntimeException e) {
      throw new ConcursException("Add participant failed: " + e.getMessage());
    }
  }

  @Override
  public Participant[] getRegisteredParticipantsForProba(Proba proba) throws ConcursException {
    try {
      grpc.Proba grpcProba = toProto(proba);  // conversia obiectului Java în proto
      GetRegisteredRequest request = GetRegisteredRequest.newBuilder()
              .setProba(grpcProba)
              .build();
      ParticipantList response = stub.getRegisteredParticipantsForProba(request);

      List<Participant> result = new ArrayList<>();
      for (grpc.Participant p : response.getParticipantsList()) {
        result.add(fromProto(p));
      }

      return result.toArray(new Participant[0]);
    } catch (StatusRuntimeException e) {
      throw new ConcursException("Failed to get registered participants: " + e.getMessage());
    }
  }

  @Override
  public Proba[] getProbele() {
      ProbaList response = stub.getProbe(grpc.Empty.newBuilder().build());
      List<Proba> result = new ArrayList<>();
      for (grpc.Proba p : response.getProbeList()) {
        result.add(fromProto(p));
      }
      return result.toArray(new Proba[0]);
  }


  private void subscribeToUpdates(String username) {
    SubscribeRequest request = SubscribeRequest.newBuilder()
            .setUsername(username)
            .build();

    asyncStub.subscribeToUpdates(request, new StreamObserver<InscriereUpdate>() {
      @Override
      public void onNext(InscriereUpdate update) {
        try {
          Participant participant = fromProto(update.getParticipant());
          Proba proba = fromProto(update.getProba());
          Inscriere inscriere = new Inscriere(participant.getId(), proba.getId());
          client.inscriereConcurs(inscriere);
        } catch (ConcursException e) {
          System.err.println("Error processing update: " + e.getMessage());
        }
      }

      @Override
      public void onError(Throwable t) {
        System.err.println("Subscription error: " + t.getMessage());
        // Attempt to resubscribe
        try {
          Thread.sleep(5000);
          subscribeToUpdates(username);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }

      @Override
      public void onCompleted() {
        System.out.println("Subscription completed");
      }
    });
  }






  // ====================
  // 🔁 Conversii Model <-> Proto
  // ====================
  private grpc.PersoanaOficiu toProto(PersoanaOficiu p) {
    return grpc.PersoanaOficiu.newBuilder()
            .setUsername(p.getUsername())
            .setParola(p.getParola())
            .build();
  }

  private grpc.Participant toProto(Participant p) {
    return grpc.Participant.newBuilder()
            .setCnp(p.getCnp())
            .setName(p.getNume())
            .setAge(p.getVarsta())
            .build();
  }

  private grpc.Proba toProto(Proba p) {
    return grpc.Proba.newBuilder()
            .setId(p.getId())
            .setName(p.getNume())
            .setCategorieVarsta(p.getCategorieVarsta())
            .setNumarParticipanti(p.getNumarParticipanti())
            .build();
  }

  private Participant fromProto(grpc.Participant p) {
    Participant p2 = new Participant(p.getCnp(), p.getName(), p.getAge());
    p2.setId(p.getId());

    return p2;
  }

  private Proba fromProto(grpc.Proba p) {
    Proba p2 = new Proba(p.getName(), p.getNumarParticipanti());
    p2.setId(p.getId());
    p2.setNume(p.getName());
    p2.setCategorieVarsta(p.getCategorieVarsta());
    p2.setNumarParticipanti(p.getNumarParticipanti());
    return p2;
  }
}
