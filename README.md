#  Electronic Voting System

This project implements an electronic voting system to ensure secure and tamper-proof voting. The system encrypts votes to maintain confidentiality and automatically detects any tampered votes, guaranteeing the integrity of the voting process.

How It Works

1. Voter Registration: Voters are registered with unique voter IDs.

   
2.Vote Casting: Registered voters can cast their votes securely. The votes are encrypted before being stored to prevent tampering.

3.Audit and Integrity Check: The system decrypts the votes and counts them. Any tampered votes are detected and flagged.

Features

Encryption: Uses AES encryption to ensure votes remain confidential.
Tamper Detection: Detects any modifications to encrypted votes.
Voter Authentication: Ensures each voter can only vote once.


How to Run
Clone the repository and navigate to the project directory.
Compile and run the DynamicVotingSystem Java class.
Follow the prompts to register voters, cast votes, and audit the results.


Extra Features
Automatic detection of tampered votes: Identifies and flags votes that have been altered.
Audit functionality: Provides a summary of voting results and identifies any inconsistencies.
Secure encryption: Uses AES encryption to ensure that vote confidentiality is maintained.


Outputs
You can view the outputs of the voting and audit processes in the outputs folder.


