package com.example.data.model

enum class QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE,
    SEQUENCE_ORDER,
    STRUCTURE_IDENTIFICATION
}

enum class BiologyTopic(val title: String) {
    AMINO_ACID_PROPERTIES("Amino Acid Properties"),
    PEPTIDE_BONDS("Peptide Bonds & Synthesis"),
    PROTEIN_STRUCTURE("Protein Structure Levels"),
    PROTEIN_FOLDING("Folding & Denaturation"),
    PROTEIN_FUNCTIONS("Protein Functions & Enzymes"),
    CENTRAL_DOGMA("Transcription & Translation")
}

data class QuizQuestion(
    val id: String,
    val topic: BiologyTopic,
    val type: QuestionType,
    val question: String,
    val options: List<String>,
    val correctIndex: Int, // for MC and TF
    val correctOrder: List<String> = emptyList(), // for ordering
    val explanation: String,
    val difficulty: Int = 1 // 1=Easy, 2=Medium, 3=Hard
)

object QuestionBank {
    val questions: List<QuizQuestion> = listOf(
        // Topic 1: Amino Acid Properties
        QuizQuestion(
            id = "q_aa_1",
            topic = BiologyTopic.AMINO_ACID_PROPERTIES,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "Which amino acid possesses a sulfhydryl (-SH) group capable of forming covalent disulfide bridges?",
            options = listOf("Methionine", "Cysteine", "Serine", "Alanine"),
            correctIndex = 1,
            explanation = "Cysteine has a thiol (-SH) side chain. When two cysteines oxidize, they form a strong covalent disulfide bond (-S-S-), crucial for tertiary structure.",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q_aa_2",
            topic = BiologyTopic.AMINO_ACID_PROPERTIES,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "Which amino acid has the simplest side chain (-H) and is the only non-chiral standard amino acid?",
            options = listOf("Alanine", "Proline", "Glycine", "Valine"),
            correctIndex = 2,
            explanation = "Glycine's R-group is a single hydrogen atom. Having two hydrogens bonded to its alpha-carbon makes it achiral.",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q_aa_3",
            topic = BiologyTopic.AMINO_ACID_PROPERTIES,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "Which amino acid side chain cycles back to bond covalently with its own backbone nitrogen, introducing rigid kinks?",
            options = listOf("Proline", "Histidine", "Tryptophan", "Phenylalanine"),
            correctIndex = 0,
            explanation = "Proline is an imino acid with a cyclic pyrrolidine ring, which prevents it from fitting into standard alpha-helices, earning it the title 'helix breaker'.",
            difficulty = 2
        ),
        QuizQuestion(
            id = "q_aa_4",
            topic = BiologyTopic.AMINO_ACID_PROPERTIES,
            type = QuestionType.TRUE_FALSE,
            question = "Essential amino acids are those that the human body can synthesize on its own from other metabolites.",
            options = listOf("True", "False"),
            correctIndex = 1,
            explanation = "False! Essential amino acids cannot be synthesized de novo by the human body in sufficient amounts and must be obtained through the diet.",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q_aa_5",
            topic = BiologyTopic.AMINO_ACID_PROPERTIES,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "Which amino acid side chain has an imidazole ring with a pKa ~6.0, allowing it to act as an acid/base catalyst at physiological pH?",
            options = listOf("Lysine", "Arginine", "Histidine", "Aspartate"),
            correctIndex = 2,
            explanation = "Histidine's imidazole ring pKa is close to physiological pH (~7.4), making it extraordinarily versatile for proton donation and acceptance in enzyme active sites.",
            difficulty = 3
        ),

        // Topic 2: Peptide Bonds & Synthesis
        QuizQuestion(
            id = "q_pb_1",
            topic = BiologyTopic.PEPTIDE_BONDS,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "What chemical functional groups react to form a peptide bond between two amino acids?",
            options = listOf(
                "Carboxyl group (-COOH) and Amino group (-NH2)",
                "Amino group (-NH2) and R-group side chain",
                "Two Carboxyl groups (-COOH)",
                "Alpha-carbon and Hydroxyl group"
            ),
            correctIndex = 0,
            explanation = "A peptide bond forms between the alpha-carboxyl carbon of one residue and the alpha-amino nitrogen of the adjacent residue.",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q_pb_2",
            topic = BiologyTopic.PEPTIDE_BONDS,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "Why does a peptide bond have a planar, rigid conformation that limits rotation?",
            options = listOf(
                "It has partial double-bond character due to resonance",
                "It is held by metallic bonding",
                "It is always surrounded by water molecules",
                "The alpha-carbon is non-reactive"
            ),
            correctIndex = 0,
            explanation = "Resonance delocalization between the carbonyl oxygen, carbon, and nitrogen gives the C-N bond ~40% double bond character, preventing free rotation around the peptide bond itself.",
            difficulty = 2
        ),
        QuizQuestion(
            id = "q_pb_3",
            topic = BiologyTopic.PEPTIDE_BONDS,
            type = QuestionType.TRUE_FALSE,
            question = "Peptide bond cleavage during digestion requires the addition of a water molecule (hydrolysis).",
            options = listOf("True", "False"),
            correctIndex = 0,
            explanation = "True! Proteases perform hydrolysis (splitting by water) to break peptide bonds back into individual amino acids.",
            difficulty = 1
        ),

        // Topic 3: Protein Structure Levels
        QuizQuestion(
            id = "q_str_1",
            topic = BiologyTopic.PROTEIN_STRUCTURE,
            type = QuestionType.STRUCTURE_IDENTIFICATION,
            question = "Alpha-helices and beta-pleated sheets belong to which level of protein organization?",
            options = listOf("Primary (1°)", "Secondary (2°)", "Tertiary (3°)", "Quaternary (4°)"),
            correctIndex = 1,
            explanation = "Secondary structure describes localized conformations of the polypeptide backbone stabilized by hydrogen bonds between carbonyl oxygens and amide hydrogens.",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q_str_2",
            topic = BiologyTopic.PROTEIN_STRUCTURE,
            type = QuestionType.STRUCTURE_IDENTIFICATION,
            question = "Hemoglobin consists of two alpha-globin and two beta-globin subunits working together. What level of structure does this multi-chain assembly represent?",
            options = listOf("Primary (1°)", "Secondary (2°)", "Tertiary (3°)", "Quaternary (4°)"),
            correctIndex = 3,
            explanation = "Quaternary structure is present only in proteins composed of two or more distinct polypeptide subunits functioning as an oligomer.",
            difficulty = 2
        ),
        QuizQuestion(
            id = "q_str_3",
            topic = BiologyTopic.PROTEIN_STRUCTURE,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "What type of chemical bond primarily stabilizes the secondary structures (alpha-helices and beta-sheets)?",
            options = listOf("Hydrogen bonds", "Disulfide bridges", "Phosphodiester bonds", "Peptide bonds"),
            correctIndex = 0,
            explanation = "Hydrogen bonds between the C=O group of one peptide bond and the N-H group of another along the polypeptide backbone stabilize secondary structures.",
            difficulty = 2
        ),

        // Topic 4: Protein Folding & Denaturation
        QuizQuestion(
            id = "q_fold_1",
            topic = BiologyTopic.PROTEIN_FOLDING,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "What is the primary thermodynamic driving force for the spontaneous folding of water-soluble globular proteins?",
            options = listOf(
                "The hydrophobic effect burying non-polar side chains",
                "Gravity pulling the chain downwards",
                "Magnetic attraction between nitrogen atoms",
                "Covalent bonding to the cell membrane"
            ),
            correctIndex = 0,
            explanation = "Water molecules form ordered cages around non-polar side chains. When non-polar residues cluster into the interior core, trapped water is released, dramatically increasing entropy (the hydrophobic effect).",
            difficulty = 2
        ),
        QuizQuestion(
            id = "q_fold_2",
            topic = BiologyTopic.PROTEIN_FOLDING,
            type = QuestionType.TRUE_FALSE,
            question = "Denaturation breaks the covalent peptide bonds of the primary amino acid sequence.",
            options = listOf("True", "False"),
            correctIndex = 1,
            explanation = "False! Denaturation disrupts weak non-covalent interactions (hydrogen bonds, ionic bonds, hydrophobic forces) and tertiary/secondary shape, but leaves the primary peptide backbone intact.",
            difficulty = 2
        ),

        // Topic 5: Protein Functions & Enzymes
        QuizQuestion(
            id = "q_fn_1",
            topic = BiologyTopic.PROTEIN_FUNCTIONS,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "How do enzymes dramatically increase the rate of biological chemical reactions?",
            options = listOf(
                "By lowering the activation energy barrier of the transition state",
                "By raising the temperature of the cell",
                "By permanently binding to substrates without letting go",
                "By changing the overall chemical equilibrium"
            ),
            correctIndex = 0,
            explanation = "Enzymes stabilize the high-energy transition state, lowering the activation energy (ΔG‡) required for the reaction to proceed.",
            difficulty = 2
        ),
        QuizQuestion(
            id = "q_fn_2",
            topic = BiologyTopic.PROTEIN_FUNCTIONS,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "Which protein is the most abundant structural protein in the human body, providing tensile strength to skin, tendons, and bone?",
            options = listOf("Hemoglobin", "Collagen", "Insulin", "Myoglobin"),
            correctIndex = 1,
            explanation = "Collagen makes up about 25% to 35% of whole-body protein content, forming tough triple-helix cables.",
            difficulty = 1
        ),

        // Topic 6: Central Dogma (Transcription & Translation)
        QuizQuestion(
            id = "q_cd_1",
            topic = BiologyTopic.CENTRAL_DOGMA,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "What is the universal start codon in mRNA, and which amino acid does it encode?",
            options = listOf("AUG (Methionine)", "UAA (Stop)", "GGG (Glycine)", "CCC (Proline)"),
            correctIndex = 0,
            explanation = "AUG is the universal start codon recognized by ribosomes to initiate translation, specifying Methionine.",
            difficulty = 1
        ),
        QuizQuestion(
            id = "q_cd_2",
            topic = BiologyTopic.CENTRAL_DOGMA,
            type = QuestionType.MULTIPLE_CHOICE,
            question = "Which molecule acts as the molecular adaptor, carrying an amino acid and matching its anticodon to an mRNA codon?",
            options = listOf("tRNA (Transfer RNA)", "rRNA", "DNA Polymerase", "Histone"),
            correctIndex = 0,
            explanation = "Transfer RNA (tRNA) carries a specific cognate amino acid at its 3' CCA end and pairs its 3-nucleotide anticodon with the mRNA codon in the ribosome.",
            difficulty = 2
        )
    )
}
