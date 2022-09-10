package unk.prolib.canesvenatici.tg;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class BotCredentials {
    @NonNull private final String username;
    @NonNull private final String token;
}
