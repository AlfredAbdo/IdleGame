package alfredabdo.android.games.idlegame.data.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

@Serializable
class AchievementDTO(
    @SerialName("id") val id: UInt,
    @SerialName("description") val description: String?,
    @SerialName("popupText") val popupText: String?,
    @SerialName("conditions") val conditions: List<Condition>?,
) {
    @Serializable
    class Condition(
        @SerialName("coins") val coinsCondition: ValueComparison<Double>?,
        @SerialName("item") val itemsCondition: ItemConditionData?,
    ) {
        @Serializable
        class ItemConditionData(
            @SerialName("index") val index: Int,
            @SerialName("level") val level: ValueComparison<Int>?,
            @SerialName("unlocked") val unlocked: Boolean?,
            @SerialName("fillRateMs") val fillRateMs: ValueComparison<Long>?,
            @SerialName("gain") val gain: ValueComparison<Double>?,
            @SerialName("upgradeCost") val upgradeCost: ValueComparison<Double>?,
        ) {
            companion object {
                const val ANY_INDEX = -1
                const val ALL_INDEX = -2
            }
        }

        @Serializable(ValueComparison.Serializer::class)
        sealed class ValueComparison<T : Number>(open val value: T) {
            class LessThan<T : Number>(override val value: T) : ValueComparison<T>(value)
            class LessThanOrEqual<T : Number>(override val value: T) : ValueComparison<T>(value)
            class Equals<T : Number>(override val value: T) : ValueComparison<T>(value)
            class GreaterThan<T : Number>(override val value: T) : ValueComparison<T>(value)
            class GreaterThanOrEqual<T : Number>(override val value: T) : ValueComparison<T>(value)


            object Serializer : KSerializer<ValueComparison<*>> {
                override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ValueComparison") {
                    element("lt", PrimitiveSerialDescriptor("lt", PrimitiveKind.DOUBLE), isOptional = true)
                    element("leq", PrimitiveSerialDescriptor("leq", PrimitiveKind.DOUBLE), isOptional = true)
                    element("eq", PrimitiveSerialDescriptor("eq", PrimitiveKind.DOUBLE), isOptional = true)
                    element("gt", PrimitiveSerialDescriptor("gt", PrimitiveKind.DOUBLE), isOptional = true)
                    element("geq", PrimitiveSerialDescriptor("geq", PrimitiveKind.DOUBLE), isOptional = true)
                }

                override fun serialize(encoder: Encoder, value: ValueComparison<*>) {
                    encoder.encodeStructure(descriptor) {
                        val number = value.value.toDouble()
                        when (value) {
                            is LessThan -> encodeDoubleElement(descriptor, 0, number)
                            is LessThanOrEqual -> encodeDoubleElement(descriptor, 1, number)
                            is Equals -> encodeDoubleElement(descriptor, 2, number)
                            is GreaterThan -> encodeDoubleElement(descriptor, 3, number)
                            is GreaterThanOrEqual -> encodeDoubleElement(descriptor, 4, number)
                        }
                    }
                }

                override fun deserialize(decoder: Decoder): ValueComparison<*> {
                    return decoder.decodeStructure(descriptor) {
                        when (val index = decodeElementIndex(descriptor)) {
                            0 -> LessThan(decodeDoubleElement(descriptor, index))
                            1 -> LessThanOrEqual(decodeDoubleElement(descriptor, index))
                            2 -> Equals(decodeDoubleElement(descriptor, index))
                            3 -> GreaterThan(decodeDoubleElement(descriptor, index))
                            4 -> GreaterThanOrEqual(decodeDoubleElement(descriptor, index))
                            CompositeDecoder.DECODE_DONE -> null
                            else -> throw SerializationException("Unknown element index: $index")
                        }
                    } ?: throw SerializationException("Missing required comparison operator in JSON object")
                }
            }
        }
    }
}