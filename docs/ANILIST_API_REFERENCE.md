# AniList GraphQL API - Referencia Rápida

Este documento contiene los tipos más importantes del API de AniList para desarrollo.

## Archivos Generados

- `anilist_schema.json` - Schema completo en formato JSON (introspection)
- `anilist_schema.graphql` - Schema completo en formato GraphQL SDL (legible)
- Este archivo - Referencia rápida de tipos principales

---

## TIPOS PRINCIPALES

### Query

**Campos principales:**

- **Page**: `Page`
- **Media**: `Media` - Media query
- **MediaTrend**: `MediaTrend` - Media Trend query
- **AiringSchedule**: `AiringSchedule` - Airing schedule query
- **Character**: `Character` - Character query
- **Staff**: `Staff` - Staff query
- **MediaList**: `MediaList` - Media list query
- **MediaListCollection**: `MediaListCollection` - Media list collection query, provides list pre-grouped by status & custom lists. User ID and Media Type arguments required.
- **GenreCollection**: `[String]` - Collection of all the possible media genres
- **MediaTagCollection**: `[MediaTag]` - Collection of all the possible media tags
- **User**: `User` - User query
- **Viewer**: `User` - Get the currently authenticated user
- **Notification**: `NotificationUnion` - Notification query
- **Studio**: `Studio` - Studio query
- **Review**: `Review` - Review query
- **Activity**: `ActivityUnion` - Activity query
- **ActivityReply**: `ActivityReply` - Activity reply query
- **Following**: `User` - Following query
- **Follower**: `User` - Follower query
- **Thread**: `Thread` - Thread query
- **ThreadComment**: `[ThreadComment]` - Comment query
- **Recommendation**: `Recommendation` - Recommendation query
- **Like**: `User` - Like query
- **Markdown**: `ParsedMarkdown` - Provide AniList markdown to be converted to html (Requires auth)
- **AniChartUser**: `AniChartUser`
- **SiteStatistics**: `SiteStatistics` - Site statistics query
- **ExternalLinkSourceCollection**: `[MediaExternalLink]` - ExternalLinkSource collection query

---

### Mutation

**Campos principales:**

- **UpdateUser**: `User`
- **SaveMediaListEntry**: `MediaList` - Create or update a media list entry
- **UpdateMediaListEntries**: `[MediaList]` - Update multiple media list entries to the same values
- **DeleteMediaListEntry**: `Deleted` - Delete a media list entry
- **DeleteCustomList**: `Deleted` - Delete a custom list and remove the list entries from it
- **SaveTextActivity**: `TextActivity` - Create or update text activity for the currently authenticated user
- **SaveMessageActivity**: `MessageActivity` - Create or update message activity for the currently authenticated user
- **SaveListActivity**: `ListActivity` - Update list activity (Mod Only)
- **DeleteActivity**: `Deleted` - Delete an activity item of the authenticated users
- **ToggleActivityPin**: `ActivityUnion` - Toggle activity to be pinned to the top of the user's activity feed
- **ToggleActivitySubscription**: `ActivityUnion` - Toggle the subscription of an activity item
- **SaveActivityReply**: `ActivityReply` - Create or update an activity reply
- **DeleteActivityReply**: `Deleted` - Delete an activity reply of the authenticated users
- **ToggleLike**: `[User]` - Add or remove a like from a likeable type.
 Returns all the users who liked the same model
- **ToggleLikeV2**: `LikeableUnion` - Add or remove a like from a likeable type.
- **ToggleFollow**: `User` - Toggle the un/following of a user
- **ToggleFavourite**: `Favourites` - Favourite or unfavourite an anime, manga, character, staff member, or studio
- **UpdateFavouriteOrder**: `Favourites` - Update the order favourites are displayed in
- **SaveReview**: `Review` - Create or update a review
- **DeleteReview**: `Deleted` - Delete a review
- **RateReview**: `Review` - Rate a review
- **SaveRecommendation**: `Recommendation` - Recommendation a media
- **SaveThread**: `Thread` - Create or update a forum thread
- **DeleteThread**: `Deleted` - Delete a thread
- **ToggleThreadSubscription**: `Thread` - Toggle the subscription of a forum thread
- **SaveThreadComment**: `ThreadComment` - Create or update a thread comment
- **DeleteThreadComment**: `Deleted` - Delete a thread comment
- **UpdateAniChartSettings**: `Json`
- **UpdateAniChartHighlights**: `Json`

---

### Media

*Anime or Manga*

**Campos principales:**

- **id**: `Int!` - The id of the media
- **idMal**: `Int` - The mal id of the media
- **title**: `MediaTitle` - The official titles of the media in various languages
- **type**: `MediaType` - The type of the media; anime or manga
- **format**: `MediaFormat` - The format the media was released in
- **status**: `MediaStatus` - The current releasing status of the media
- **description**: `String` - Short description of the media's story and characters
- **startDate**: `FuzzyDate` - The first official release date of the media
- **endDate**: `FuzzyDate` - The last official release date of the media
- **season**: `MediaSeason` - The season the media was initially released in
- **seasonYear**: `Int` - The season year the media was initially released in
- **seasonInt**: `Int` - The year & season the media was initially released in
- **episodes**: `Int` - The amount of episodes the anime has when complete
- **duration**: `Int` - The general length of each anime episode in minutes
- **chapters**: `Int` - The amount of chapters the manga has when complete
- **volumes**: `Int` - The amount of volumes the manga has when complete
- **countryOfOrigin**: `CountryCode` - Where the media was created. (ISO 3166-1 alpha-2)
- **isLicensed**: `Boolean` - If the media is officially licensed or a self-published doujin release
- **source**: `MediaSource` - Source type the media was adapted from.
- **hashtag**: `String` - Official Twitter hashtags for the media
- **trailer**: `MediaTrailer` - Media trailer or advertisement
- **updatedAt**: `Int` - When the media's data was last updated
- **coverImage**: `MediaCoverImage` - The cover images of the media
- **bannerImage**: `String` - The banner image of the media
- **genres**: `[String]` - The genres of the media
- **synonyms**: `[String]` - Alternative titles of the media
- **averageScore**: `Int` - A weighted average score of all the user's scores of the media
- **meanScore**: `Int` - Mean score of all the user's scores of the media
- **popularity**: `Int` - The number of users with the media on their list
- **isLocked**: `Boolean` - Locked media may not be added to lists our favorited. This may be due to the entry pending for deletion or other reasons.

*... y 25 campos más (ver schema completo)*

---

### MediaTitle

*The official titles of the media in various languages*

**Campos principales:**

- **romaji**: `String` - The romanization of the native language title
- **english**: `String` - The official english title
- **native**: `String` - Official title in it's native language
- **userPreferred**: `String` - The currently authenticated users preferred title language. Default romaji for non-authenticated

---

### MediaCoverImage

**Campos principales:**

- **extraLarge**: `String` - The cover image url of the media at its largest size. If this size isn't available, large will be provided instead.
- **large**: `String` - The cover image url of the media at a large size
- **medium**: `String` - The cover image url of the media at medium size
- **color**: `String` - Average #hex color of cover image

---

### MediaTrailer

*Media trailer or advertisement*

**Campos principales:**

- **id**: `String` - The trailer video id
- **site**: `String` - The site the video is hosted by (Currently either youtube or dailymotion)
- **thumbnail**: `String` - The url for the thumbnail image of the video

---

### MediaExternalLink

*An external link to another site related to the media or staff member*

**Campos principales:**

- **id**: `Int!` - The id of the external link
- **url**: `String` - The url of the external link or base url of link source
- **site**: `String!` - The links website site name
- **siteId**: `Int` - The links website site id
- **type**: `ExternalLinkType`
- **language**: `String` - Language the site content is in. See Staff language field for values.
- **color**: `String`
- **icon**: `String` - The icon image url of the site. Not available for all links. Transparent PNG 64x64
- **notes**: `String`
- **isDisabled**: `Boolean`

---

### MediaTag

*A tag that describes a theme or element of the media*

**Campos principales:**

- **id**: `Int!` - The id of the tag
- **name**: `String!` - The name of the tag
- **description**: `String` - A general description of the tag
- **category**: `String` - The categories of tags this tag belongs to
- **rank**: `Int` - The relevance ranking of the tag out of the 100 for this media
- **isGeneralSpoiler**: `Boolean` - If the tag could be a spoiler for any media
- **isMediaSpoiler**: `Boolean` - If the tag is a spoiler for this media
- **isAdult**: `Boolean` - If the tag is only for adult 18+ media
- **userId**: `Int` - The user who submitted the tag

---

### MediaStats

*A media's statistics*

**Campos principales:**

- **scoreDistribution**: `[ScoreDistribution]`
- **statusDistribution**: `[StatusDistribution]`
- **airingProgression**: `[AiringProgression]`

---

### MediaRank

*The ranking of a media in a particular time span and format compared to other media*

**Campos principales:**

- **id**: `Int!` - The id of the rank
- **rank**: `Int!` - The numerical rank of the media
- **type**: `MediaRankType!` - The type of ranking
- **format**: `MediaFormat!` - The format the media is ranked within
- **year**: `Int` - The year the media is ranked within
- **season**: `MediaSeason` - The season the media is ranked within
- **allTime**: `Boolean` - If the ranking is based on all time instead of a season/year
- **context**: `String!` - String that gives context to the ranking type and time span

---

### MediaList

*List of anime or manga*

**Campos principales:**

- **id**: `Int!` - The id of the list entry
- **userId**: `Int!` - The id of the user owner of the list entry
- **mediaId**: `Int!` - The id of the media
- **status**: `MediaListStatus` - The watching/reading status
- **score**: `Float` - The score of the entry
- **progress**: `Int` - The amount of episodes/chapters consumed by the user
- **progressVolumes**: `Int` - The amount of volumes read by the user
- **repeat**: `Int` - The amount of times the user has rewatched/read the media
- **priority**: `Int` - Priority of planning
- **private**: `Boolean` - If the entry should only be visible to authenticated user
- **notes**: `String` - Text notes
- **hiddenFromStatusLists**: `Boolean` - If the entry shown be hidden from non-custom lists
- **customLists**: `Json` - Map of booleans for which custom lists the entry are in
- **advancedScores**: `Json` - Map of advanced scores with name keys
- **startedAt**: `FuzzyDate` - When the entry was started by the user
- **completedAt**: `FuzzyDate` - When the entry was completed by the user
- **updatedAt**: `Int` - When the entry data was last updated
- **createdAt**: `Int` - When the entry data was created
- **media**: `Media`
- **user**: `User`

---

### Character

*A character that features in an anime or manga*

**Campos principales:**

- **id**: `Int!` - The id of the character
- **name**: `CharacterName` - The names of the character
- **image**: `CharacterImage` - Character images
- **description**: `String` - A general description of the character
- **gender**: `String` - The character's gender. Usually Male, Female, or Non-binary but can be any string.
- **dateOfBirth**: `FuzzyDate` - The character's birth date
- **age**: `String` - The character's age. Note this is a string, not an int, it may contain further text and additional ages.
- **bloodType**: `String` - The characters blood type
- **isFavourite**: `Boolean!` - If the character is marked as favourite by the currently authenticated user
- **isFavouriteBlocked**: `Boolean!` - If the character is blocked from being added to favourites
- **siteUrl**: `String` - The url for the character page on the AniList website
- **media**: `MediaConnection` - Media that includes the character
- **updatedAt**: `Int`
- **favourites**: `Int` - The amount of user's who have favourited the character
- **modNotes**: `String` - Notes for site moderators

---

### CharacterName

*The names of the character*

**Campos principales:**

- **first**: `String` - The character's given name
- **middle**: `String` - The character's middle name
- **last**: `String` - The character's surname
- **full**: `String` - The character's first and last name
- **native**: `String` - The character's full name in their native language
- **alternative**: `[String]` - Other names the character might be referred to as
- **alternativeSpoiler**: `[String]` - Other names the character might be referred to as but are spoilers
- **userPreferred**: `String` - The currently authenticated users preferred name language. Default romaji for non-authenticated

---

### CharacterImage

**Campos principales:**

- **large**: `String` - The character's image of media at its largest size
- **medium**: `String` - The character's image of media at medium size

---

### Staff

*Voice actors or production staff*

**Campos principales:**

- **id**: `Int!` - The id of the staff member
- **name**: `StaffName` - The names of the staff member
- **language**: `StaffLanguage` - The primary language the staff member dub's in
- **languageV2**: `String` - The primary language of the staff member. Current values: Japanese, English, Korean, Italian, Spanish, Portuguese, French, German, Hebrew, Hungarian, Chinese, Arabic, Filipino, Catalan, Finnish, Turkish, Dutch, Swedish, Thai, Tagalog, Malaysian, Indonesian, Vietnamese, Nepali, Hindi, Urdu
- **image**: `StaffImage` - The staff images
- **description**: `String` - A general description of the staff member
- **primaryOccupations**: `[String]` - The person's primary occupations
- **gender**: `String` - The staff's gender. Usually Male, Female, or Non-binary but can be any string.
- **dateOfBirth**: `FuzzyDate`
- **dateOfDeath**: `FuzzyDate`
- **age**: `Int` - The person's age in years
- **yearsActive**: `[Int]` - [startYear, endYear] (If the 2nd value is not present staff is still active)
- **homeTown**: `String` - The persons birthplace or hometown
- **bloodType**: `String` - The persons blood type
- **isFavourite**: `Boolean!` - If the staff member is marked as favourite by the currently authenticated user
- **isFavouriteBlocked**: `Boolean!` - If the staff member is blocked from being added to favourites
- **siteUrl**: `String` - The url for the staff page on the AniList website
- **staffMedia**: `MediaConnection` - Media where the staff member has a production role
- **characters**: `CharacterConnection` - Characters voiced by the actor
- **characterMedia**: `MediaConnection` - Media the actor voiced characters in. (Same data as characters with media as node instead of characters)
- **updatedAt**: `Int`
- **staff**: `Staff` - Staff member that the submission is referencing
- **submitter**: `User` - Submitter for the submission
- **submissionStatus**: `Int` - Status of the submission
- **submissionNotes**: `String` - Inner details of submission status
- **favourites**: `Int` - The amount of user's who have favourited the staff member
- **modNotes**: `String` - Notes for site moderators

---

### StaffName

*The names of the staff member*

**Campos principales:**

- **first**: `String` - The person's given name
- **middle**: `String` - The person's middle name
- **last**: `String` - The person's surname
- **full**: `String` - The person's first and last name
- **native**: `String` - The person's full name in their native language
- **alternative**: `[String]` - Other names the staff member might be referred to as (pen names)
- **userPreferred**: `String` - The currently authenticated users preferred name language. Default romaji for non-authenticated

---

### StaffImage

**Campos principales:**

- **large**: `String` - The person's image of media at its largest size
- **medium**: `String` - The person's image of media at medium size

---

### Studio

*Animation or production company*

**Campos principales:**

- **id**: `Int!` - The id of the studio
- **name**: `String!` - The name of the studio
- **isAnimationStudio**: `Boolean!` - If the studio is an animation studio or a different kind of company
- **media**: `MediaConnection` - The media the studio has worked on
- **siteUrl**: `String` - The url for the studio page on the AniList website
- **isFavourite**: `Boolean!` - If the studio is marked as favourite by the currently authenticated user
- **favourites**: `Int` - The amount of user's who have favourited the studio

---

### AiringSchedule

*Media Airing Schedule. NOTE: We only aim to guarantee that FUTURE airing data is present and accurate.*

**Campos principales:**

- **id**: `Int!` - The id of the airing schedule item
- **airingAt**: `Int!` - The time the episode airs at
- **timeUntilAiring**: `Int!` - Seconds until episode starts airing
- **episode**: `Int!` - The airing episode number
- **mediaId**: `Int!` - The associate media id of the airing episode
- **media**: `Media` - The associate media of the airing episode

---

### User

*A user*

**Campos principales:**

- **id**: `Int!` - The id of the user
- **name**: `String!` - The name of the user
- **about**: `String` - The bio written by user (Markdown)
- **avatar**: `UserAvatar` - The user's avatar images
- **bannerImage**: `String` - The user's banner images
- **isFollowing**: `Boolean` - If the authenticated user if following this user
- **isFollower**: `Boolean` - If this user if following the authenticated user
- **isBlocked**: `Boolean` - If the user is blocked by the authenticated user
- **bans**: `Json`
- **options**: `UserOptions` - The user's general options
- **mediaListOptions**: `MediaListOptions` - The user's media list options
- **favourites**: `Favourites` - The users favourites
- **statistics**: `UserStatisticTypes` - The users anime & manga list statistics
- **unreadNotificationCount**: `Int` - The number of unread notifications the user has
- **siteUrl**: `String` - The url for the user page on the AniList website
- **donatorTier**: `Int` - The donation tier of the user
- **donatorBadge**: `String` - Custom donation badge text
- **moderatorRoles**: `[ModRole]` - The user's moderator roles if they are a site moderator
- **createdAt**: `Int` - When the user's account was created. (Does not exist for accounts created before 2020)
- **updatedAt**: `Int` - When the user's data was last updated
- **stats**: `UserStats` - The user's statistics
- **moderatorStatus**: `String` - If the user is a moderator or data moderator
- **previousNames**: `[UserPreviousName]` - The user's previously used names.

---

### MediaTrend

*Daily media statistics*

**Campos principales:**

- **mediaId**: `Int!` - The id of the tag
- **date**: `Int!` - The day the data was recorded (timestamp)
- **trending**: `Int!` - The amount of media activity on the day
- **averageScore**: `Int` - A weighted average score of all the user's scores of the media
- **popularity**: `Int` - The number of users with the media on their list
- **inProgress**: `Int` - The number of users with watching/reading the media
- **releasing**: `Boolean!` - If the media was being released at this time
- **episode**: `Int` - The episode number of the anime released on this day
- **media**: `Media` - The related media

---

### Review

*A Review that features in an anime or manga*

**Campos principales:**

- **id**: `Int!` - The id of the review
- **userId**: `Int!` - The id of the review's creator
- **mediaId**: `Int!` - The id of the review's media
- **mediaType**: `MediaType` - For which type of media the review is for
- **summary**: `String` - A short summary of the review
- **body**: `String` - The main review body text
- **rating**: `Int` - The total user rating of the review
- **ratingAmount**: `Int` - The amount of user ratings of the review
- **userRating**: `ReviewRating` - The rating of the review by currently authenticated user
- **score**: `Int` - The review score of the media
- **private**: `Boolean` - If the review is not yet publicly published and is only viewable by creator
- **siteUrl**: `String` - The url for the review page on the AniList website
- **createdAt**: `Int!` - The time of the thread creation
- **updatedAt**: `Int!` - The time of the thread last update
- **user**: `User` - The creator of the review
- **media**: `Media` - The media the review is of

---

### Recommendation

*Media recommendation*

**Campos principales:**

- **id**: `Int!` - The id of the recommendation
- **rating**: `Int` - Users rating of the recommendation
- **userRating**: `RecommendationRating` - The rating of the recommendation by currently authenticated user
- **media**: `Media` - The media the recommendation is from
- **mediaRecommendation**: `Media` - The recommended media
- **user**: `User` - The user that first created the recommendation

---

### FuzzyDate

*Date object that allows for incomplete date values (fuzzy)*

**Campos principales:**

- **year**: `Int` - Numeric Year (2017)
- **month**: `Int` - Numeric Month (3)
- **day**: `Int` - Numeric Day (24)

---

## ENUMS PRINCIPALES

### MediaType

*Media type enum, anime or manga.*

**Valores:**

- `ANIME` - Japanese Anime
- `MANGA` - Asian comic

---

### MediaFormat

*The format the media was released in*

**Valores:**

- `TV` - Anime broadcast on television
- `TV_SHORT` - Anime which are under 15 minutes in length and broadcast on television
- `MOVIE` - Anime movies with a theatrical release
- `SPECIAL` - Special episodes that have been included in DVD/Blu-ray releases, picture dramas, pilots, etc
- `OVA` - (Original Video Animation) Anime that have been released directly on DVD/Blu-ray without originally going through a theatrical release or television broadcast
- `ONA` - (Original Net Animation) Anime that have been originally released online or are only available through streaming services.
- `MUSIC` - Short anime released as a music video
- `MANGA` - Professionally published manga with more than one chapter
- `NOVEL` - Written books released as a series of light novels
- `ONE_SHOT` - Manga with just one chapter

---

### MediaStatus

*The current releasing status of the media*

**Valores:**

- `FINISHED` - Has completed and is no longer being released
- `RELEASING` - Currently releasing
- `NOT_YET_RELEASED` - To be released at a later date
- `CANCELLED` - Ended before the work could be finished
- `HIATUS` - Version 2 only. Is currently paused from releasing and will resume at a later date

---

### MediaSeason

**Valores:**

- `WINTER` - Predominantly started airing between January and March
- `SPRING` - Predominantly started airing between April and June
- `SUMMER` - Predominantly started airing between July and September
- `FALL` - Predominantly started airing between October and November

---

### MediaSource

*Source type the media was adapted from*

**Valores:**

- `ORIGINAL` - An original production not based of another work
- `MANGA` - Asian comic book
- `LIGHT_NOVEL` - Written work published in volumes
- `VISUAL_NOVEL` - Video game driven primary by text and narrative
- `VIDEO_GAME` - Video game
- `OTHER` - Other
- `NOVEL` - Version 2+ only. Written works not published in volumes
- `DOUJINSHI` - Version 2+ only. Self-published works
- `ANIME` - Version 2+ only. Japanese Anime
- `WEB_NOVEL` - Version 3 only. Written works published online
- `LIVE_ACTION` - Version 3 only. Live action media such as movies or TV show
- `GAME` - Version 3 only. Games excluding video games
- `COMIC` - Version 3 only. Comics excluding manga
- `MULTIMEDIA_PROJECT` - Version 3 only. Multimedia project
- `PICTURE_BOOK` - Version 3 only. Picture book

---

### MediaSort

*Media sort enums*

**Valores:**

- `ID`
- `ID_DESC`
- `TITLE_ROMAJI`
- `TITLE_ROMAJI_DESC`
- `TITLE_ENGLISH`
- `TITLE_ENGLISH_DESC`
- `TITLE_NATIVE`
- `TITLE_NATIVE_DESC`
- `TYPE`
- `TYPE_DESC`
- `FORMAT`
- `FORMAT_DESC`
- `START_DATE`
- `START_DATE_DESC`
- `END_DATE`
- `END_DATE_DESC`
- `SCORE`
- `SCORE_DESC`
- `POPULARITY`
- `POPULARITY_DESC`
- `TRENDING`
- `TRENDING_DESC`
- `EPISODES`
- `EPISODES_DESC`
- `DURATION`
- `DURATION_DESC`
- `STATUS`
- `STATUS_DESC`
- `CHAPTERS`
- `CHAPTERS_DESC`
- `VOLUMES`
- `VOLUMES_DESC`
- `UPDATED_AT`
- `UPDATED_AT_DESC`
- `SEARCH_MATCH`
- `FAVOURITES`
- `FAVOURITES_DESC`

---

### MediaListStatus

*Media list watching/reading status enum.*

**Valores:**

- `CURRENT` - Currently watching/reading
- `PLANNING` - Planning to watch/read
- `COMPLETED` - Finished watching/reading
- `DROPPED` - Stopped watching/reading before completing
- `PAUSED` - Paused watching/reading
- `REPEATING` - Re-watching/reading

---

### CharacterRole

*The role the character plays in the media*

**Valores:**

- `MAIN` - A primary character role in the media
- `SUPPORTING` - A supporting character role in the media
- `BACKGROUND` - A background character in the media

---

### StaffLanguage

*The primary language of the voice actor*

**Valores:**

- `JAPANESE` - Japanese
- `ENGLISH` - English
- `KOREAN` - Korean
- `ITALIAN` - Italian
- `SPANISH` - Spanish
- `PORTUGUESE` - Portuguese
- `FRENCH` - French
- `GERMAN` - German
- `HEBREW` - Hebrew
- `HUNGARIAN` - Hungarian

---

## QUERIES ÚTILES

### Buscar Anime por ID

```graphql
query GetAnimeById($id: Int) {
 Media(id: $id, type: ANIME) {
 id
 title {
 romaji
 english
 native
 }
 description
 coverImage {
 large
 medium
 }
 bannerImage
 genres
 averageScore
 episodes
 status
 season
 seasonYear
 }
}
```

### Buscar Characters

```graphql
query GetCharacter($id: Int) {
 Character(id: $id) {
 id
 name {
 full
 native
 }
 image {
 large
 medium
 }
 description
 gender
 dateOfBirth {
 year
 month
 day
 }
 }
}
```

## ENDPOINTS

- **GraphQL API**: `https://graphql.anilist.co`
- **GraphiQL Explorer**: `https://anilist.co/graphiql`
- **Documentación**: `https://docs.anilist.co`

## RATE LIMITS

- **Rate Limit**: 90 requests per minute
- **Authentication**: No se requiere para queries públicas
- **Headers**: Se recomienda usar `Content-Type: application/json` y `Accept: application/json`
