package com.example.data

data class LessonStep(
    val title: String,
    val explanation: String,
    val analogy: String,
    val characterEmoji: String,
    val visualActionTip: String
)

data class Lesson(
    val id: String,
    val title: String,
    val category: String,
    val character: String,
    val iconEmoji: String,
    val shortDescription: String,
    val rewardXp: Int = 50,
    val steps: List<LessonStep>
)

object LessonsData {
    val lessonsList = listOf(
        Lesson(
            id = "intro",
            title = "What is AI?",
            category = "Basics",
            character = "Sparky the Robot",
            iconEmoji = "🤖",
            shortDescription = "Meet Sparky and learn how robots get smart! Is it magic, or something else?",
            steps = listOf(
                LessonStep(
                    title = "Rules vs. Examples",
                    explanation = "Traditional programs follow strict recipes: 'Do this, then do that!' but AI is different. AI learns by looking at thousands of examples, just like how you learned what a dog is by seeing real dogs!",
                    analogy = "An ordinary computer is like a train on tracks—it can only go where the tracks are. AI is like a little explorer balloon that learns to fly and navigate on its own!",
                    characterEmoji = "🤖",
                    visualActionTip = "AI doesn't just copy; it discovers patterns in numbers, shapes, and words!"
                ),
                LessonStep(
                    title = "Brain Power (Neural Networks)",
                    explanation = "AI uses virtual 'thinking nodes' similar to tiny neurons in your brain! When these nodes connect, they pass information, like passing a ball in a team, until they figure out the answer together.",
                    analogy = "It is like a big team of friendly detectives. One looks at the colors, another looks at the size, and together they shout 'It's a Dinosaur!'",
                    characterEmoji = "🧠",
                    visualActionTip = "The millions of nodes are called a neural network!"
                ),
                LessonStep(
                    title = "Is AI Alive?",
                    explanation = "AI doesn't have a heart, feelings, or real thoughts. It doesn't sleep or eat ice cream! It is a super powerful calculator that is incredibly good at finding patterns.",
                    analogy = "It's like an ultra-smart mirror that reflects all the books, pictures, and words humans have shared, showing us answers to our questions.",
                    characterEmoji = "⚡",
                    visualActionTip = "AI is a helper tool. You hold the controls!"
                )
            )
        ),
        Lesson(
            id = "supervised",
            title = "How Computers Learn",
            category = "Machine Learning",
            character = "Detective Pixel",
            iconEmoji = "🔍",
            shortDescription = "Play sorting games to teach AI how to recognize Apples, Bananas, and Kittens!",
            steps = listOf(
                LessonStep(
                    title = "What is Supervised Learning?",
                    explanation = "We teach AI by showing it 'labeled' pictures. We show Detective Pixel 100 apples labeled 'Food' and 100 cats labeled 'Animal'. Over time, Pixel recognizes them instantly!",
                    analogy = "Think of flashcards! On the front is a picture, and on the back is the answer. The more flashcards you study, the smarter you get!",
                    characterEmoji = "🔍",
                    visualActionTip = "AI uses tags (labels) to connect images with meanings!"
                ),
                LessonStep(
                    title = "AI Feedback Loop",
                    explanation = "When AI makes a guess, we tell it if it was right or wrong. This feedback adjusts the weights in its network, meaning it updates its formulas to avoid making the same mistake twice!",
                    analogy = "Like learning to throw a paper airplane. If it falls too fast, you fold the wings differently, testing until it flies across the room!",
                    characterEmoji = "🔁",
                    visualActionTip = "AI gets smarter through trial, error, and human guidance!"
                )
            )
        ),
        Lesson(
            id = "generative",
            title = "Creating Brand New Things",
            category = "Generative AI",
            character = "Paint Penny",
            iconEmoji = "🎨",
            shortDescription = "Find out how computers write stories, paint drawings, and create songs!",
            steps = listOf(
                LessonStep(
                    title = "What is GenAI?",
                    explanation = "Generative AI models read millions of books and see millions of paintings. Then, when you ask, they combine ideas to compose new text or images that have never existed before!",
                    analogy = "Imagine a giant bucket of colored building blocks. You ask for a 'Castle on the Water', and the bucket instantly clicks the blocks together to create it!",
                    characterEmoji = "🎨",
                    visualActionTip = "It predicts the very next word or pixel to build a masterpiece!"
                ),
                LessonStep(
                    title = "Prompt Engineering",
                    explanation = "The instructions we give to GenAI are called 'prompts'. Giving clear, detailed details—like 'draw a small cat with a yellow party hat'—helps the AI paint exactly what you imagined!",
                    analogy = "It is like a wand in a magic school! The more specific your spell, the more spectacular the magic!",
                    characterEmoji = "🪄",
                    visualActionTip = "Prompting is the art of talking to machines in human language!"
                )
            )
        )
    )
}
