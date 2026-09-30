package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AaAcidicNegative
import com.example.ui.theme.AaBasicPositive
import com.example.ui.theme.AaNonPolar
import com.example.ui.theme.AaPolar
import com.example.ui.theme.AaSpecial

enum class AminoAcidCategory(val displayName: String) {
    NON_POLAR("Non-polar Aliphatic"),
    POLAR_UNCHARGED("Polar Uncharged"),
    POSITIVELY_CHARGED("Positively Charged (Basic)"),
    NEGATIVELY_CHARGED("Negatively Charged (Acidic)"),
    AROMATIC("Aromatic"),
    SPECIAL("Special Conformation")
}

data class AminoAcid(
    val name: String,
    val code3: String,
    val code1: String,
    val category: AminoAcidCategory,
    val isEssential: Boolean,
    val sideChain: String,
    val molecularWeight: Double, // g/mol
    val codons: List<String>,
    val description: String,
    val biologicalRelevance: String,
    val formula: String
) {
    val badgeColor: Color
        get() = when (category) {
            AminoAcidCategory.NON_POLAR -> AaNonPolar
            AminoAcidCategory.POLAR_UNCHARGED -> AaPolar
            AminoAcidCategory.POSITIVELY_CHARGED -> AaBasicPositive
            AminoAcidCategory.NEGATIVELY_CHARGED -> AaAcidicNegative
            AminoAcidCategory.AROMATIC -> AaNonPolar
            AminoAcidCategory.SPECIAL -> AaSpecial
        }
}

object AminoAcidData {
    val allAminoAcids: List<AminoAcid> = listOf(
        AminoAcid(
            name = "Alanine",
            code3 = "Ala",
            code1 = "A",
            category = AminoAcidCategory.NON_POLAR,
            isEssential = false,
            sideChain = "-CH3 (Methyl group)",
            molecularWeight = 89.09,
            codons = listOf("GCU", "GCC", "GCA", "GCG"),
            description = "A simple hydrophobic amino acid with a small non-reactive methyl side chain.",
            biologicalRelevance = "Crucial in the alanine-glucose cycle between muscles and liver during metabolic fasting.",
            formula = "C3H7NO2"
        ),
        AminoAcid(
            name = "Arginine",
            code3 = "Arg",
            code1 = "R",
            category = AminoAcidCategory.POSITIVELY_CHARGED,
            isEssential = false, // conditionally essential
            sideChain = "-CH2-CH2-CH2-NH-C(=NH2+)-NH2 (Guanidinium)",
            molecularWeight = 174.20,
            codons = listOf("CGU", "CGC", "CGA", "CGG", "AGA", "AGG"),
            description = "Highly basic amino acid with a positively charged guanidinium group at physiological pH.",
            biologicalRelevance = "Key precursor for nitric oxide (NO) synthesis, which regulates vascular dilation, and urea cycle function.",
            formula = "C6H14N4O2"
        ),
        AminoAcid(
            name = "Asparagine",
            code3 = "Asn",
            code1 = "N",
            category = AminoAcidCategory.POLAR_UNCHARGED,
            isEssential = false,
            sideChain = "-CH2-CONH2 (Amide)",
            molecularWeight = 132.12,
            codons = listOf("AAU", "AAC"),
            description = "Amide derivative of aspartic acid. Readily engages in hydrogen bonding via its amide moiety.",
            biologicalRelevance = "Primary site for N-linked glycosylation in eukaryotic glycoproteins (Asn-X-Ser/Thr motifs).",
            formula = "C4H8N2O3"
        ),
        AminoAcid(
            name = "Aspartic Acid",
            code3 = "Asp",
            code1 = "D",
            category = AminoAcidCategory.NEGATIVELY_CHARGED,
            isEssential = false,
            sideChain = "-CH2-COO- (Carboxylate)",
            molecularWeight = 133.10,
            codons = listOf("GAU", "GAC"),
            description = "Carries a negative charge at physiological pH, creating ionic bonds and coordinating metal ions.",
            biologicalRelevance = "Critical catalytic residue in many enzyme active sites and an intermediate in the urea cycle.",
            formula = "C4H7NO4"
        ),
        AminoAcid(
            name = "Cysteine",
            code3 = "Cys",
            code1 = "C",
            category = AminoAcidCategory.SPECIAL,
            isEssential = false,
            sideChain = "-CH2-SH (Thiol)",
            molecularWeight = 121.16,
            codons = listOf("UGU", "UGC"),
            description = "Contains a reactive sulfhydryl (-SH) thiol group capable of covalent oxidation.",
            biologicalRelevance = "Forms covalent disulfide bridges (-S-S-) that stabilize protein tertiary and quaternary structures.",
            formula = "C3H7NO2S"
        ),
        AminoAcid(
            name = "Glutamic Acid",
            code3 = "Glu",
            code1 = "E",
            category = AminoAcidCategory.NEGATIVELY_CHARGED,
            isEssential = false,
            sideChain = "-CH2-CH2-COO- (Carboxylate)",
            molecularWeight = 147.13,
            codons = listOf("GAA", "GAG"),
            description = "Negatively charged amino acid with a flexible two-carbon spacer before its carboxylate group.",
            biologicalRelevance = "The primary excitatory neurotransmitter in the vertebrate central nervous system (Glutamate).",
            formula = "C5H9NO4"
        ),
        AminoAcid(
            name = "Glutamine",
            code3 = "Gln",
            code1 = "Q",
            category = AminoAcidCategory.POLAR_UNCHARGED,
            isEssential = false,
            sideChain = "-CH2-CH2-CONH2 (Amide)",
            molecularWeight = 146.15,
            codons = listOf("CAA", "CAG"),
            description = "Amide derivative of glutamic acid, neutral and polar with high water solubility.",
            biologicalRelevance = "The most abundant free amino acid in human blood; acts as a non-toxic nitrogen transporter.",
            formula = "C5H10N2O3"
        ),
        AminoAcid(
            name = "Glycine",
            code3 = "Gly",
            code1 = "G",
            category = AminoAcidCategory.SPECIAL,
            isEssential = false,
            sideChain = "-H (Hydrogen atom)",
            molecularWeight = 75.07,
            codons = listOf("GGU", "GGC", "GGA", "GGG"),
            description = "The smallest and only achiral amino acid. Lacks an asymmetric alpha-carbon.",
            biologicalRelevance = "Confers extreme conformational flexibility to protein loops and tight turns (e.g., collagen triple helix).",
            formula = "C2H5NO2"
        ),
        AminoAcid(
            name = "Histidine",
            code3 = "His",
            code1 = "H",
            category = AminoAcidCategory.POSITIVELY_CHARGED,
            isEssential = true,
            sideChain = "-CH2-Imidazole ring",
            molecularWeight = 155.16,
            codons = listOf("CAU", "CAC"),
            description = "Its imidazole side chain has a pKa ~6.0, allowing it to easily gain or lose protons at physiological pH.",
            biologicalRelevance = "Prototypical general acid/base catalyst in enzyme active sites (e.g., chymotrypsin) and binds iron in heme.",
            formula = "C6H9N3O2"
        ),
        AminoAcid(
            name = "Isoleucine",
            code3 = "Ile",
            code1 = "I",
            category = AminoAcidCategory.NON_POLAR,
            isEssential = true,
            sideChain = "-CH(CH3)-CH2-CH3 (sec-Butyl)",
            molecularWeight = 131.17,
            codons = listOf("AUU", "AUC", "AUA"),
            description = "Hydrophobic branched-chain amino acid possessing two chiral stereocenters.",
            biologicalRelevance = "Essential branched-chain amino acid (BCAA) used in muscle tissue repair and immune regulation.",
            formula = "C6H13NO2"
        ),
        AminoAcid(
            name = "Leucine",
            code3 = "Leu",
            code1 = "L",
            category = AminoAcidCategory.NON_POLAR,
            isEssential = true,
            sideChain = "-CH2-CH(CH3)2 (Isobutyl)",
            molecularWeight = 131.17,
            codons = listOf("UUA", "UUG", "CUU", "CUC", "CUA", "CUG"),
            description = "Branched-chain hydrophobic amino acid commonly buried in interior protein cores.",
            biologicalRelevance = "Directly triggers the mTOR signaling pathway to stimulate ribosomal muscle protein synthesis.",
            formula = "C6H13NO2"
        ),
        AminoAcid(
            name = "Lysine",
            code3 = "Lys",
            code1 = "K",
            category = AminoAcidCategory.POSITIVELY_CHARGED,
            isEssential = true,
            sideChain = "-CH2-CH2-CH2-CH2-NH3+ (Butylamine)",
            molecularWeight = 146.19,
            codons = listOf("AAA", "AAG"),
            description = "Possesses a flexible aliphatic four-carbon chain terminated with a basic primary amino group.",
            biologicalRelevance = "Target for post-translational modifications including ubiquitination, methylation, and acetylation.",
            formula = "C6H14N2O2"
        ),
        AminoAcid(
            name = "Methionine",
            code3 = "Met",
            code1 = "M",
            category = AminoAcidCategory.NON_POLAR,
            isEssential = true,
            sideChain = "-CH2-CH2-S-CH3 (Thioether)",
            molecularWeight = 149.21,
            codons = listOf("AUG"),
            description = "Sulfur-containing hydrophobic amino acid encoded by the universal eukaryotic start codon (AUG).",
            biologicalRelevance = "First amino acid incorporated in almost all newly synthesized eukaryotic protein chains; donor for S-adenosylmethionine (SAM).",
            formula = "C5H11NO2S"
        ),
        AminoAcid(
            name = "Phenylalanine",
            code3 = "Phe",
            code1 = "F",
            category = AminoAcidCategory.AROMATIC,
            isEssential = true,
            sideChain = "-CH2-Phenyl (Benzyl ring)",
            molecularWeight = 165.19,
            codons = listOf("UUU", "UUC"),
            description = "Bulky, highly hydrophobic aromatic amino acid with a non-polar benzene ring.",
            biologicalRelevance = "Metabolic precursor for tyrosine, dopamine, norepinephrine, epinephrine, and melanin pigments.",
            formula = "C9H11NO2"
        ),
        AminoAcid(
            name = "Proline",
            code3 = "Pro",
            code1 = "P",
            category = AminoAcidCategory.SPECIAL,
            isEssential = false,
            sideChain = "Pyrrolidine ring (Cyclic imino)",
            molecularWeight = 115.13,
            codons = listOf("CCU", "CCC", "CCA", "CCG"),
            description = "The only standard amino acid where the side chain cycles back and bonds covalently to the amine backbone.",
            biologicalRelevance = "Acts as a rigid structural 'helix breaker', introducing kinks and turns in polypeptide backbones.",
            formula = "C5H9NO2"
        ),
        AminoAcid(
            name = "Serine",
            code3 = "Ser",
            code1 = "S",
            category = AminoAcidCategory.POLAR_UNCHARGED,
            isEssential = false,
            sideChain = "-CH2-OH (Hydroxymethyl)",
            molecularWeight = 105.09,
            codons = listOf("UCU", "UCC", "UCA", "UCG", "AGU", "AGC"),
            description = "Small polar amino acid bearing an active primary alcohol hydroxyl (-OH) group.",
            biologicalRelevance = "Major target for regulatory phosphorylation by protein kinases, switching enzyme activities on and off.",
            formula = "C3H7NO3"
        ),
        AminoAcid(
            name = "Threonine",
            code3 = "Thr",
            code1 = "T",
            category = AminoAcidCategory.POLAR_UNCHARGED,
            isEssential = true,
            sideChain = "-CH(OH)-CH3 (Ethanol group)",
            molecularWeight = 119.12,
            codons = listOf("ACU", "ACC", "ACA", "ACG"),
            description = "Polar amino acid containing a secondary alcohol and two stereocenters.",
            biologicalRelevance = "Site for O-linked glycosylation and regulatory Ser/Thr kinase phosphorylation.",
            formula = "C4H9NO3"
        ),
        AminoAcid(
            name = "Tryptophan",
            code3 = "Trp",
            code1 = "W",
            category = AminoAcidCategory.AROMATIC,
            isEssential = true,
            sideChain = "-CH2-Indole ring (Bicyclic)",
            molecularWeight = 204.23,
            codons = listOf("UGG"),
            description = "The largest standard amino acid, featuring a bicyclic indole ring system with intrinsic UV fluorescence.",
            biologicalRelevance = "Essential dietary precursor for the neurotransmitter serotonin, hormone melatonin, and vitamin B3 (niacin).",
            formula = "C11H12N2O2"
        ),
        AminoAcid(
            name = "Tyrosine",
            code3 = "Tyr",
            code1 = "Y",
            category = AminoAcidCategory.AROMATIC,
            isEssential = false,
            sideChain = "-CH2-Phenol (-C6H4-OH)",
            molecularWeight = 181.19,
            codons = listOf("UAU", "UAC"),
            description = "Aromatic amino acid with a reactive phenolic hydroxyl group.",
            biologicalRelevance = "Crucial for signal transduction via receptor tyrosine kinases (RTKs) and precursor to thyroid hormones.",
            formula = "C9H11NO3"
        ),
        AminoAcid(
            name = "Valine",
            code3 = "Val",
            code1 = "V",
            category = AminoAcidCategory.NON_POLAR,
            isEssential = true,
            sideChain = "-CH(CH3)2 (Isopropyl)",
            molecularWeight = 117.15,
            codons = listOf("GUU", "GUC", "GUA", "GUG"),
            description = "Hydrophobic branched-chain amino acid that stabilizes compact hydrophobic protein interiors.",
            biologicalRelevance = "A single Glu-to-Val substitution at position 6 of hemoglobin beta causes sickle-cell anemia.",
            formula = "C5H11NO2"
        )
    )
}
