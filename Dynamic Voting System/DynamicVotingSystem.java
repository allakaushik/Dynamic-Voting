import java.util.*;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

class Voter {
    private final String voterId;
    private boolean hasVoted;

    public Voter(String voterId) {
        this.voterId = voterId;
        this.hasVoted = false;
    }

    public String getVoterId() {
        return voterId;
    }

    public boolean hasVoted() {
        return hasVoted;
    }

    public void vote() {
        this.hasVoted = true;
    }
}

class Vote {
    private final byte[] encryptedVote;
    private final String voterId;

    public Vote(String voterId, byte[] encryptedVote) {
        this.voterId = voterId;
        this.encryptedVote = encryptedVote;
    }

    public byte[] getEncryptedVote() {
        return encryptedVote;
    }

    public String getVoterId() {
        return voterId;
    }
}

class VotingSystem {
    private final List<Voter> voters = new ArrayList<>();
    private final List<Vote> voteLedger = new ArrayList<>();
    private final SecretKey encryptionKey;
    private final Set<String> tamperedVotes = new HashSet<>();

    public VotingSystem() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        this.encryptionKey = keyGen.generateKey();
    }

    public void registerVoter(String voterId) {
        voters.add(new Voter(voterId));
    }

    public Voter authenticateVoter(String voterId) {
        for (Voter voter : voters) {
            if (voter.getVoterId().equals(voterId)) {
                return voter;
            }
        }
        return null;
    }

    private byte[] encryptVote(String vote) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
        return cipher.doFinal(vote.getBytes());
    }

    private String decryptVote(byte[] encryptedVote) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
            return new String(cipher.doFinal(encryptedVote));
        } catch (Exception e) {
            return "Error: Tampered vote detected.";
        }
    }

    public void castVote(String voterId, String vote) throws Exception {
        Voter voter = authenticateVoter(voterId);
        if (voter == null || voter.hasVoted()) {
            throw new Exception("Invalid voter or voter has already voted.");
        }

        byte[] encryptedVote = encryptVote(vote);
        voteLedger.add(new Vote(voterId, encryptedVote));
        voter.vote();
    }

    public Map<String, Integer> auditVotes() {
        Map<String, Integer> voteCount = new HashMap<>();
        for (Vote vote : voteLedger) {
            String decryptedVote = decryptVote(vote.getEncryptedVote());
            if (decryptedVote.equals("Error: Tampered vote detected.")) {
                tamperedVotes.add(vote.getVoterId());
            } else {
                voteCount.put(decryptedVote, voteCount.getOrDefault(decryptedVote, 0) + 1);
            }
        }
        return voteCount;
    }

    public Set<String> getTamperedVotes() {
        return tamperedVotes;
    }
}

public class DynamicVotingSystem {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            VotingSystem system = new VotingSystem();

            System.out.print("Enter number of voters: ");
            int voterCount = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < voterCount; i++) {
                System.out.print("Enter voter ID for voter " + (i + 1) + ": ");
                String voterId = scanner.nextLine();
                system.registerVoter(voterId);
            }

            for (int i = 0; i < voterCount; i++) {
                System.out.print("Enter voter ID to cast vote: ");
                String voterId = scanner.nextLine();
                System.out.print("Enter vote (e.g., CandidateA, CandidateB): ");
                String vote = scanner.nextLine();
                try {
                    system.castVote(voterId, vote);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            Map<String, Integer> auditResults = system.auditVotes();
            System.out.println("Audit Results: " + auditResults);

            Set<String> tamperedVotes = system.getTamperedVotes();
            if (!tamperedVotes.isEmpty()) {
                System.out.println("Tampered votes detected from voter IDs: " + tamperedVotes);
            } else {
                System.out.println("No tampered votes detected.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
