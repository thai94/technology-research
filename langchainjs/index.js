import { YoutubeLoader } from "langchain/document_loaders/web/youtube";
import { CheerioWebBaseLoader } from "langchain/document_loaders/web/cheerio";
import { HtmlToTextTransformer } from "langchain/document_transformers/html_to_text";

const ytbWithoutTime = async () => {
  const loader = YoutubeLoader.createFromUrl("https://www.youtube.com/watch?v=5MgBikgcWnY", {
    language: "en",
    addVideoInfo: true,
  });
  
  const docs = await loader.load();
  
  console.log(docs);
}

ytbWithoutTime()

const website = async () => {
  const loader = new CheerioWebBaseLoader(
    "https://vnexpress.net/niem-vui-cua-tu-nhan-palestine-duoc-israel-phong-thich-4681107.html"
  );
  
  const docs = await loader.load();
  const transformer = new HtmlToTextTransformer();
  const newDocuments = await transformer.invoke(docs)
  console.log(console.log(newDocuments));
}

// website()