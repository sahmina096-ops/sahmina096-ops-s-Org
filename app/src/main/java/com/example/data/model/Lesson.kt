package com.example.data.model

data class LessonTerm(
    val term: String,
    val definition: String
)

data class LessonMiniQuiz(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class Lesson(
    val id: Int,
    val chapterNumber: Int,
    val title: String,
    val subtitle: String,
    val summary: String,
    val mainContent: String,
    val visualDiagramDescription: String,
    val keyTerms: List<LessonTerm>,
    val realWorldExample: String,
    val quickFact: String,
    val miniQuiz: LessonMiniQuiz
)

object LessonData {
    val lessons: List<Lesson> = listOf(
        Lesson(
            id = 1,
            chapterNumber = 1,
            title = "What are Proteins?",
            subtitle = "The Molecular Machines of Life",
            summary = "Proteins are versatile macromolecules composed of amino acid chains that execute nearly all cellular functions.",
            mainContent = """
                Proteins are large, complex biopolymers that carry out the lion's share of functional work in living cells. While DNA holds the blueprint and lipids build membranes, proteins are the nanomachines that build, maintain, defend, and operate organisms.
                
                Proteins serve diverse vital functions:
                • Enzymes: Catalyze chemical reactions millions of times faster than baseline (e.g., DNA Polymerase, Amylase).
                • Transport: Ferry molecules across cell membranes or through bodily fluids (e.g., Hemoglobin transporting O2).
                • Structural: Form physical scaffolding for tissues and cells (e.g., Collagen in skin, Keratin in hair).
                • Defense: Target and neutralize foreign pathogens (e.g., Immunoglobulin Antibodies).
                • Signaling: Coordinate physiological communication (e.g., Insulin controlling glucose uptake).
                • Movement: Power mechanical motion in muscle and cilia (e.g., Actin and Myosin).
            """.trimIndent(),
            visualDiagramDescription = "A panoramic microscopic cell view showing enzymes cutting substrates, hemoglobin carrying red oxygen atoms, and antibodies tagging viruses.",
            keyTerms = listOf(
                LessonTerm("Protein", "Macromolecule made of one or more folded polypeptide chains."),
                LessonTerm("Enzyme", "A biological catalyst that speeds up chemical reactions without being consumed."),
                LessonTerm("Macromolecule", "A giant molecule consisting of thousands of covalently bonded atoms.")
            ),
            realWorldExample = "Hemoglobin inside human red blood cells binds up to 4 oxygen molecules in your lungs and delivers them to exercising muscles.",
            quickFact = "Over 50% of the dry weight of most living cells consists of proteins!",
            miniQuiz = LessonMiniQuiz(
                question = "Which class of proteins accelerates biochemical reactions without being consumed?",
                options = listOf("Structural proteins", "Enzymes", "Antibodies", "Transport proteins"),
                correctIndex = 1,
                explanation = "Enzymes are biological catalysts that lower activation energy, drastically speeding up metabolic reactions."
            )
        ),
        Lesson(
            id = 2,
            chapterNumber = 2,
            title = "What are Amino Acids?",
            subtitle = "The 20 Building Blocks",
            summary = "Amino acids are organic compounds containing an amino group, carboxyl group, hydrogen, and a distinct R-side chain.",
            mainContent = """
                Every protein in all known life is constructed from just 20 standard proteinogenic amino acids.
                
                Each amino acid shares a fundamental universal chemical skeleton:
                1. Central Alpha-Carbon (Cα)
                2. Amino Group (-NH2) on one side
                3. Carboxyl Group (-COOH) on the other
                4. Single Hydrogen Atom (-H)
                5. Variable Side Chain ('R-Group')
                
                The R-group gives each amino acid its unique chemical personality. Some R-groups are hydrophobic (water-fearing) like Leucine, others are hydrophilic (water-loving) and polar like Serine, while others carry positive or negative electrical charges like Lysine (+) or Glutamate (-).
                
                Humans can synthesize 11 amino acids internally, but 9 cannot be synthesized and must be ingested in our food: these are the Essential Amino Acids (Histidine, Isoleucine, Leucine, Lysine, Methionine, Phenylalanine, Threonine, Tryptophan, Valine).
            """.trimIndent(),
            visualDiagramDescription = "Central Cα carbon linked to -NH2, -COOH, -H, and a glowing colorful R-group that swaps into 20 variations.",
            keyTerms = listOf(
                LessonTerm("R-Group (Side Chain)", "The variable chemical moiety attached to the alpha-carbon that defines amino acid identity."),
                LessonTerm("Essential Amino Acids", "The 9 amino acids that must be acquired from dietary sources because the human body cannot produce them."),
                LessonTerm("Alpha-Carbon", "The central chiral carbon atom to which all functional groups bond.")
            ),
            realWorldExample = "Tryptophan is an essential amino acid from food that your brain converts into serotonin to regulate sleep and mood.",
            quickFact = "Glycine is the only amino acid without chirality because its R-group is simply another hydrogen atom!",
            miniQuiz = LessonMiniQuiz(
                question = "What component distinguishes one amino acid from all others?",
                options = listOf("The Amino group", "The Carboxyl group", "The R-group side chain", "The Alpha-carbon"),
                correctIndex = 2,
                explanation = "All 20 amino acids share the same backbone; only their R-group side chain varies in structure and charge."
            )
        ),
        Lesson(
            id = 3,
            chapterNumber = 3,
            title = "Peptide Bonds",
            subtitle = "The Covalent Glue of Polypeptides",
            summary = "Amino acids link covalently via dehydration condensation reactions, releasing a water molecule (H2O).",
            mainContent = """
                To build a protein, amino acids must be covalently stitched together head-to-tail.
                
                This linkage is called a Peptide Bond. It forms when:
                • The Carboxyl group (-COOH) of the first amino acid reacts with
                • The Amino group (-NH2) of the second amino acid.
                
                During this reaction, an -OH is pulled from the carboxyl group and an -H is pulled from the amino group, forming a free molecule of Water (H2O). Because water is released, this is called a Dehydration Synthesis or Condensation Reaction.
                
                The resulting -CO-NH- bond has partial double-bond character due to resonance, making the peptide backbone rigid and planar, which heavily guides subsequent protein folding!
            """.trimIndent(),
            visualDiagramDescription = "Two amino acids approaching; an -OH and -H join into an H2O bubble that pops away, leaving a sturdy golden peptide bond.",
            keyTerms = listOf(
                LessonTerm("Peptide Bond", "Covalent amide linkage formed between the α-carboxyl carbon and α-amino nitrogen."),
                LessonTerm("Dehydration Synthesis", "Chemical reaction where two molecules join with the concurrent elimination of water."),
                LessonTerm("Resonance", "Delocalization of electrons that gives the peptide bond planar stiffness.")
            ),
            realWorldExample = "Proteases in your stomach and pancreas reverse this reaction by adding water back to hydrolyze and digest dietary proteins into free amino acids.",
            quickFact = "Ribosomes in your cells synthesize peptide bonds at a blistering pace of ~20 amino acids every single second!",
            miniQuiz = LessonMiniQuiz(
                question = "What small molecule is released when a peptide bond forms between two amino acids?",
                options = listOf("Carbon Dioxide (CO2)", "Water (H2O)", "Ammonia (NH3)", "Glucose"),
                correctIndex = 1,
                explanation = "Peptide bond formation is a dehydration synthesis reaction that liberates a molecule of H2O."
            )
        ),
        Lesson(
            id = 4,
            chapterNumber = 4,
            title = "Polypeptide Chains",
            subtitle = "From Oligopeptide to Giant Polymer",
            summary = "Chains have an N-terminus and C-terminus, creating directional molecular polymers.",
            mainContent = """
                When amino acids continuously link via peptide bonds, they form a Polypeptide Chain.
                
                Every polypeptide chain exhibits strict directional polarity:
                • The N-Terminus (Amino-terminus): The start of the chain, having a free -NH2 (or -NH3+) group.
                • The C-Terminus (Carboxy-terminus): The end of the chain, having a free -COOH (or -COO-) group.
                
                By universal biological convention, protein sequences are read and written from the N-terminus to the C-terminus (N -> C).
                
                The continuous sequence of N-Cα-C atoms forms the repeating 'backbone' of the protein, while the distinctive R-group side chains project outwards alternately into the solvent, ready to interact.
            """.trimIndent(),
            visualDiagramDescription = "A linear chain of colorful beads with an N-terminal blue flag on the left and a C-terminal amber flag on the right.",
            keyTerms = listOf(
                LessonTerm("N-Terminus", "The end of a peptide chain that has an unbonded amine group."),
                LessonTerm("C-Terminus", "The end of a peptide chain that has an unbonded carboxyl group."),
                LessonTerm("Backbone", "The repeating covalent chain sequence (-N-Cα-C-) disregarding side chains.")
            ),
            realWorldExample = "Oxytocin, the social bonding hormone, is a short polypeptide called a nonapeptide because it is made of exactly 9 amino acids.",
            quickFact = "Titin, the protein responsible for muscle elasticity, is the longest known polypeptide chain, spanning over 34,000 amino acids!",
            miniQuiz = LessonMiniQuiz(
                question = "In what direction are polypeptide chains written and synthesized in biology?",
                options = listOf("C-terminus to N-terminus", "N-terminus to C-terminus", "R-group to backbone", "Inside to outside"),
                correctIndex = 1,
                explanation = "Ribosomes synthesize proteins strictly from the N-terminus (amino end) to the C-terminus (carboxyl end)."
            )
        ),
        Lesson(
            id = 5,
            chapterNumber = 5,
            title = "Protein Folding",
            subtitle = "How Chains Become Functional Machines",
            summary = "The hydrophobic effect and molecular interactions drive linear chains into precise 3D architectures.",
            mainContent = """
                A straight, unfolded string of amino acids cannot do work. To become biologically active, the polypeptide must fold spontaneously into a precise three-dimensional conformation.
                
                The Primary Driving Force: The Hydrophobic Effect.
                In an aqueous cellular environment:
                • Hydrophobic (non-polar) amino acids (like Val, Leu, Ile, Phe) hate water and collapse into the sheltered interior core of the protein.
                • Hydrophilic (polar and charged) amino acids (like Ser, Lys, Glu) face outward to interact happily with water molecules.
                
                Additional stabilizing forces include:
                1. Hydrogen Bonds between backbone and side chain atoms
                2. Ionic Bonds (Salt Bridges) between opposite charges (e.g., Lys+ and Asp-)
                3. Disulfide Bridges: Strong covalent bonds between two Cysteine sulfur atoms (Cys-S-S-Cys)
                4. Van der Waals attractions
                
                If extreme heat or pH disrupts these delicate bonds, the protein unfolds (denatures) and irreversibly loses function.
            """.trimIndent(),
            visualDiagramDescription = "A water-filled box where hydrophobic blue spheres pack into the center while green polar spheres stay on the outside.",
            keyTerms = listOf(
                LessonTerm("Hydrophobic Effect", "The thermodynamic tendency of non-polar molecules to aggregate together in aqueous solution."),
                LessonTerm("Denaturation", "The loss of a protein's functional 3D shape due to heat, chemical disruption, or pH extremes."),
                LessonTerm("Disulfide Bridge", "A covalent bond formed between two cysteine thiol groups that locks folded structures in place.")
            ),
            realWorldExample = "When you fry an egg, the transparent albumin protein denatures and coagulates into a white solid because heat irreversibly unfolds its structure.",
            quickFact = "Molecular chaperones are helper proteins that act like folding chambers, preventing sticky unfolded chains from clumping together!",
            miniQuiz = LessonMiniQuiz(
                question = "Where are non-polar, hydrophobic amino acids typically located in a soluble folded protein?",
                options = listOf("Exposed on the exterior surface", "Buried inside the internal core", "Only at the N-terminus", "Outside floating freely"),
                correctIndex = 1,
                explanation = "Driven by the hydrophobic effect, non-polar side chains tuck safely away from water into the protein's interior core."
            )
        ),
        Lesson(
            id = 6,
            chapterNumber = 6,
            title = "Protein Structures",
            subtitle = "The 4 Hierarchical Levels",
            summary = "Primary (sequence), Secondary (helices/sheets), Tertiary (3D fold), and Quaternary (multi-subunit complexes).",
            mainContent = """
                Protein architecture is organized into four distinct structural tiers:
                
                1. PRIMARY STRUCTURE (1°):
                The linear sequence of amino acids held strictly by covalent peptide bonds. This sequence dictates all downstream folding.
                
                2. SECONDARY STRUCTURE (2°):
                Local regular repeating patterns in the backbone held by hydrogen bonds between carbonyl oxygen (C=O) and amine hydrogen (N-H).
                • Alpha-Helix: A tight spiral rod (e.g., in keratin).
                • Beta-Pleated Sheet: Stretched strands running parallel or antiparallel forming pleated ribbons (e.g., in silk fibroin).
                
                3. TERTIARY STRUCTURE (3°):
                The overall 3D geometric folding of a single complete polypeptide chain, stabilized by interactions between variable R-groups (hydrophobic core, salt bridges, hydrogen bonds, disulfide bridges).
                
                4. QUATERNARY STRUCTURE (4°):
                The spatial assembly of two or more independent polypeptide subunits functioning together as a multi-protein machine (e.g., Hemoglobin composed of 4 subunits: 2 alpha + 2 beta).
            """.trimIndent(),
            visualDiagramDescription = "Interactive ladder showing 1° sequence -> 2° corkscrew helix & pleated sheets -> 3° folded blob -> 4° four interlocking subunits.",
            keyTerms = listOf(
                LessonTerm("Alpha-Helix", "A right-handed coiled secondary structure stabilized by intrachain hydrogen bonds every 4th residue."),
                LessonTerm("Beta-Sheet", "Secondary structure where polypeptide strands lie side-by-side joined by hydrogen bonds."),
                LessonTerm("Quaternary Structure", "The arrangement of multiple distinct polypeptide subunits into a single functional complex.")
            ),
            realWorldExample = "Hemoglobin has quaternary structure with 4 subunits. If just one amino acid in the primary sequence is mutated (Glu -> Val), red blood cells sickle and deform.",
            quickFact = "Linus Pauling won the Nobel Prize in Chemistry partly for discovering the Alpha-Helix structure using simple paper models!",
            miniQuiz = LessonMiniQuiz(
                question = "Which level of protein structure involves hydrogen bonding along the backbone to form alpha-helices and beta-sheets?",
                options = listOf("Primary", "Secondary", "Tertiary", "Quaternary"),
                correctIndex = 1,
                explanation = "Secondary structure refers specifically to regular local conformations (alpha-helices, beta-sheets) stabilized by backbone hydrogen bonds."
            )
        ),
        Lesson(
            id = 7,
            chapterNumber = 7,
            title = "Protein Functions",
            subtitle = "Enzymes, Scaffolds, and Transporters",
            summary = "Proteins operate as enzymes, transport channels, structural anchors, immune defenders, and molecular motors.",
            mainContent = """
                Form follows function: the exact 3D shape of a protein creates specialized binding pockets that permit precise biological tasks.
                
                The Major Functional Classes:
                1. ENZYMES:
                Possess an 'Active Site' custom-shaped to fit specific substrate molecules. By stabilizing the transition state, enzymes accelerate biochemical reactions by factors of 10^6 to 10^12! (e.g., Lactase breaking lactose sugar).
                
                2. STRUCTURAL PROTEINS:
                Fibrous, insoluble cables providing mechanical resistance and elasticity (e.g., Collagen, Keratin, Elastin, Tubulin).
                
                3. TRANSPORT & STORAGE:
                Carry ions and ligands (e.g., Hemoglobin carrying O2, Ferritin storing iron, Ion channels gating sodium/potassium in neurons).
                
                4. IMMUNE DEFENSE:
                Antibodies (immunoglobulins) have hypervariable binding loops that latch onto foreign bacterial and viral antigens with astonishing specificity.
                
                5. HORMONES & RECEPTORS:
                Chemical messengers that transmit systemic signals (e.g., Insulin binding to receptor tyrosine kinases to lower blood sugar).
            """.trimIndent(),
            visualDiagramDescription = "Enzyme lock-and-key pocket accepting a substrate, alongside a Y-shaped antibody grabbing a spike protein.",
            keyTerms = listOf(
                LessonTerm("Active Site", "The catalytic cleft on an enzyme where substrate molecules bind and undergo reaction."),
                LessonTerm("Antibody", "A Y-shaped defensive protein that binds specifically to an antigen."),
                LessonTerm("Ligand", "Any small molecule that binds specifically to a receptor protein site.")
            ),
            realWorldExample = "Spider dragline silk is stronger than high-grade steel of equal weight thanks to crystalline beta-sheets of fibroin protein.",
            quickFact = "Your body produces billions of unique antibodies, each with tailored protein loops evolved to recognize different germs!",
            miniQuiz = LessonMiniQuiz(
                question = "What is the specific region of an enzyme called where chemical reactions are catalyzed?",
                options = listOf("Subunit pocket", "Active site", "Hydrophobic core", "Allosteric tail"),
                correctIndex = 1,
                explanation = "The active site is the precise three-dimensional pocket on an enzyme where substrate binding and catalysis happen."
            )
        ),
        Lesson(
            id = 8,
            chapterNumber = 8,
            title = "Protein Synthesis",
            subtitle = "Transcription, Translation, and Ribosomes",
            summary = "The Central Dogma: DNA in the nucleus is transcribed into mRNA, then translated into amino acids by ribosomes.",
            mainContent = """
                How does a cell turn genetic code into physical, functioning proteins? Through the Central Dogma of Molecular Biology:
                
                DNA -> RNA -> Protein
                
                STEP 1: TRANSCRIPTION (Inside Nucleus)
                RNA Polymerase reads the DNA gene sequence and writes a complementary messenger RNA (mRNA) transcript. RNA uses Uracil (U) instead of Thymine (T).
                
                STEP 2: TRANSLATION (At the Ribosome)
                The mRNA travels into the cytoplasm and docks inside a Ribosome—the cellular protein factory.
                The ribosome reads mRNA in triplets of nucleotides called Codons.
                
                Each codon corresponds to one specific amino acid:
                • AUG is the universal START codon (codes for Methionine).
                • Transfer RNA (tRNA) molecules carry specific amino acids and possess an anticodon loop that pairs with the mRNA codon.
                • The ribosome catalyzes the formation of peptide bonds one-by-one as tRNAs deliver their amino acids.
                • UAA, UAG, and UGA are STOP codons that signal release of the completed polypeptide chain!
            """.trimIndent(),
            visualDiagramDescription = "Ribosome clamp rolling along an mRNA ribbon while tRNAs drop off amino acids into a growing protein chain.",
            keyTerms = listOf(
                LessonTerm("Codon", "A triplet of mRNA nucleotides that specifies a particular amino acid or stop signal."),
                LessonTerm("Ribosome", "The ribonucleoprotein machine that synthesizes proteins by translating mRNA."),
                LessonTerm("tRNA (Transfer RNA)", "Small RNA adaptor that carries an amino acid and matches an mRNA codon via its anticodon.")
            ),
            realWorldExample = "mRNA vaccines against COVID-19 deliver instructions directly to your ribosomes to temporarily produce harmless viral spike proteins, training your immune system.",
            quickFact = "There are 64 possible codon triplets (4^3), but only 20 amino acids, so the genetic code is redundant (multiple codons code for the same amino acid)!",
            miniQuiz = LessonMiniQuiz(
                question = "Which cellular machine translates messenger RNA codons into an amino acid polypeptide chain?",
                options = listOf("Lysosome", "Mitochondria", "Ribosome", "Golgi apparatus"),
                correctIndex = 2,
                explanation = "Ribosomes read mRNA codons and assemble amino acids into polypeptide chains through peptide bond formation."
            )
        )
    )
}
