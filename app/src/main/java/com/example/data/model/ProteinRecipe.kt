package com.example.data.model

enum class BuildingDifficulty(val displayName: String, val basePoints: Int, val timeLimitSec: Int?) {
    EASY("Easy", 100, null),
    MEDIUM("Medium", 200, null),
    HARD("Hard", 350, 45)
}

data class ProteinRecipe(
    val id: String,
    val name: String,
    val scientificName: String,
    val difficulty: BuildingDifficulty,
    val sequence: List<String>, // 3-letter codes
    val biologyHint: String,
    val biologicalFunction: String,
    val structureLevelTested: String,
    val educationalFact: String
)

object ProteinRecipeData {
    val recipes: List<ProteinRecipe> = listOf(
        // EASY (3-4 residues, fundamental intro to peptide bonds)
        ProteinRecipe(
            id = "easy_1",
            name = "Glutathione Segment",
            scientificName = "γ-L-Glutamyl-L-cysteinylglycine Fragment",
            difficulty = BuildingDifficulty.EASY,
            sequence = listOf("Glu", "Cys", "Gly"),
            biologyHint = "Starts with an acidic residue, followed by sulfur-bearing Cysteine and flexible Glycine.",
            biologicalFunction = "The master antioxidant protecting human cells from reactive oxidative stress.",
            structureLevelTested = "Primary Structure (Oligopeptide)",
            educationalFact = "Glutathione uses Cysteine's sulfhydryl (-SH) group to quench harmful free radicals in cells."
        ),
        ProteinRecipe(
            id = "easy_2",
            name = "Thyrotropin-Releasing Bit",
            scientificName = "TRH Tripeptide Precursor",
            difficulty = BuildingDifficulty.EASY,
            sequence = listOf("Glu", "His", "Pro"),
            biologyHint = "Acidic Glutamate links to basic Histidine, ending with rigid ring Proline.",
            biologicalFunction = "Stimulates the pituitary gland to release thyroid-stimulating hormone (TSH).",
            structureLevelTested = "Primary Peptide Sequence",
            educationalFact = "Even a tiny 3-amino-acid chain acts as a powerful neurohormone regulating whole-body metabolism!"
        ),
        ProteinRecipe(
            id = "easy_3",
            name = "Collagen Repeat",
            scientificName = "Gly-Pro-Ala Structural Motif",
            difficulty = BuildingDifficulty.EASY,
            sequence = listOf("Gly", "Pro", "Ala", "Gly"),
            biologyHint = "Glycine appears every 3rd residue, alternating with rigid Proline and small Alanine.",
            biologicalFunction = "The tight repeating triplet that lets collagen wrap into its signature triple-helix cable.",
            structureLevelTested = "Secondary Structural Motif",
            educationalFact = "Because Glycine has only a hydrogen atom as its side chain, it can pack into the crowded center of the collagen helix."
        ),
        ProteinRecipe(
            id = "easy_4",
            name = "Start Peptide",
            scientificName = "Universal Translation Initiator",
            difficulty = BuildingDifficulty.EASY,
            sequence = listOf("Met", "Ala", "Ser", "Val"),
            biologyHint = "Every protein begins with Methionine (AUG codon), followed by common cytosolic amino acids.",
            biologicalFunction = "The opening sequence transcribed from messenger RNA at the ribosome.",
            structureLevelTested = "Translation & N-Terminus",
            educationalFact = "In eukaryotes, the first amino acid translated by the ribosome is always Methionine."
        ),

        // MEDIUM (5-7 residues, complex sequences, hints & questions)
        ProteinRecipe(
            id = "med_1",
            name = "Met-Enkephalin",
            scientificName = "Endogenous Opioid Pentapeptide",
            difficulty = BuildingDifficulty.MEDIUM,
            sequence = listOf("Tyr", "Gly", "Gly", "Phe", "Met"),
            biologyHint = "Aromatic Tyrosine -> two flexible Glycines -> aromatic Phenylalanine -> sulfur Methionine.",
            biologicalFunction = "Natural pain-relieving neuropeptide that binds to opioid receptors in the brain.",
            structureLevelTested = "Tertiary Receptor Binding Loop",
            educationalFact = "Enkephalins are your body's natural endorphins, dampening pain signals after physical exercise."
        ),
        ProteinRecipe(
            id = "med_2",
            name = "Insulin Chain Fragment",
            scientificName = "Human Insulin A-Chain Terminal",
            difficulty = BuildingDifficulty.MEDIUM,
            sequence = listOf("Gly", "Ile", "Val", "Glu", "Gln", "Cys"),
            biologyHint = "Hydrophobic cluster (Gly-Ile-Val) followed by acidic Glu, polar Gln, and disulfide-forming Cys.",
            biologicalFunction = "Regulates cellular glucose uptake from the bloodstream into liver and muscle cells.",
            structureLevelTested = "Disulfide Bond Anchor",
            educationalFact = "Insulin was the very first protein ever sequenced, earning Frederick Sanger the 1958 Nobel Prize!"
        ),
        ProteinRecipe(
            id = "med_3",
            name = "Oxytocin Core",
            scientificName = "Nonapeptide Hormone Segment",
            difficulty = BuildingDifficulty.MEDIUM,
            sequence = listOf("Cys", "Tyr", "Ile", "Gln", "Asn", "Cys"),
            biologyHint = "A ring-forming peptide bounded by two Cysteine residues that bridge with a disulfide bond.",
            biologicalFunction = "Mediates social bonding, maternal bonding, lactation, and uterine contractions.",
            structureLevelTested = "Cyclic Peptide & Disulfide Bridge",
            educationalFact = "The two Cysteines at the ends oxidize to form a covalent -S-S- disulfide bridge, forming a ring essential for receptor activation."
        ),

        // HARD (7-9 residues, time limit, complex folding criteria, fewer hints)
        ProteinRecipe(
            id = "hard_1",
            name = "Glucagon Active Loop",
            scientificName = "Pancreatic Alpha-Cell Hormone",
            difficulty = BuildingDifficulty.HARD,
            sequence = listOf("His", "Ser", "Gln", "Gly", "Thr", "Phe", "Thr", "Ser"),
            biologyHint = "Starts with basic His, heavily phosphorylated Ser/Thr residues, polar Gln, and aromatic Phe.",
            biologicalFunction = "Counters insulin by prompting the liver to break down glycogen stores into glucose.",
            structureLevelTested = "Alpha-Helical Receptor Trigger",
            educationalFact = "Glucagon assumes a nearly complete alpha-helical conformation when it binds its receptor on hepatocytes."
        ),
        ProteinRecipe(
            id = "hard_2",
            name = "Hemoglobin Beta Fragment",
            scientificName = "Beta-Globin N-Terminal Chain",
            difficulty = BuildingDifficulty.HARD,
            sequence = listOf("Val", "His", "Leu", "Thr", "Pro", "Glu", "Glu", "Lys"),
            biologyHint = "Hydrophobic Val-His-Leu, Thr, rigid Pro, two negative Glu residues, and positive Lys.",
            biologicalFunction = "The N-terminus of beta-globin; position 6 (Glu) is critical for blood cell stability.",
            structureLevelTested = "Quaternary Subunit & Point Mutation Site",
            educationalFact = "In Sickle Cell Anemia, the Glu at position 6 is mutated to Val, causing mutant hemoglobin to polymerize into sickle-shaped fiber rods."
        ),
        ProteinRecipe(
            id = "hard_3",
            name = "Zinc Finger DNA Binder",
            scientificName = "C2H2 Zinc Finger Motif Segment",
            difficulty = BuildingDifficulty.HARD,
            sequence = listOf("Tyr", "Lys", "Cys", "Pro", "Glu", "Cys", "Gly", "Lys", "Ser"),
            biologyHint = "Aromatic Tyr, Lys, Cys for zinc coordination, Pro kink, Glu, second Cys, and C-terminal basic tail.",
            biologicalFunction = "Coordinates a divalent zinc ion (Zn2+) to insert an alpha helix into the major groove of DNA.",
            structureLevelTested = "Tertiary Metal-Ion Coordination Domain",
            educationalFact = "Zinc fingers are the most common DNA-binding protein motifs in the human genome, controlling gene transcription."
        )
    )
}
