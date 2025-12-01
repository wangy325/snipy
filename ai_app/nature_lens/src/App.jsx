import React, { useState, useRef, useEffect } from 'react';
import { Camera, Upload, ArrowLeft, Share2, Leaf, Search, ChevronRight, Award, AlertCircle, Bird, PawPrint, RefreshCw, ImageOff, X, ChevronLeft, ImageIcon, ZoomIn, SmileIcon, Binoculars } from 'lucide-react';

// --- 全局配置 ---
const apiKey = "AIzaSyC-EXVc1aDFlJpRfau83XYjb_kTy1_pWZ8"; // 运行时环境会自动注入 API Key

// --- 工具函数：文件转 Base64 ---
const fileToBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      const base64Data = reader.result.split(',')[1];
      resolve(base64Data);
    };
    reader.onerror = (error) => reject(error);
  });
};

const App = () => {
  const [currentScreen, setCurrentScreen] = useState('home'); // home, scanning, result, error
  const [selectedImage, setSelectedImage] = useState(null);
  const [scanProgress, setScanProgress] = useState(0);
  const [resultData, setResultData] = useState(null);
  const [galleryImages, setGalleryImages] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");

  // Lightbox 状态
  const [lightboxOpen, setLightboxOpen] = useState(false);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [zoomLevel, setZoomLevel] = useState(1);
  const [panPosition, setPanPosition] = useState({ x: 0, y: 0 });
  const [swipeOffset, setSwipeOffset] = useState(0); // 新增：用于未放大时的滑动偏移
  const [isDragging, setIsDragging] = useState(false);
  const [dragStartTime, setDragStartTime] = useState(0);

  const fileInputRef = useRef(null);
  const cameraInputRef = useRef(null);
  const touchStartRef = useRef(null);
  const dragStartRef = useRef({ x: 0, y: 0 });
  const lastTapTimeRef = useRef(0);

  // 重置 Lightbox 状态当切换图片时
  useEffect(() => {
    setZoomLevel(1);
    setPanPosition({ x: 0, y: 0 });
    setSwipeOffset(0);
  }, [currentImageIndex]);

  // 处理图片上传
  const handleImageSelect = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const imageUrl = URL.createObjectURL(file);
    setSelectedImage(imageUrl);
    setCurrentScreen('scanning');
    setScanProgress(0);
    setErrorMsg("");
    setGalleryImages([]);

    const progressInterval = setInterval(() => {
      setScanProgress((prev) => {
        if (prev >= 90) return 90;
        return prev + 2;
      });
    }, 50);

    try {
      const base64Image = await fileToBase64(file);
      const data = await identifyImageWithGemini(base64Image);

      clearInterval(progressInterval);
      setScanProgress(100);

      setTimeout(async () => {
        if (data && data.type !== 'unknown') {
          setResultData(data);
          setCurrentScreen('result');

          const fetchSimilars = async () => {
            if (data.similars && data.similars.length > 0) {
              const updatedSimilars = await Promise.all(
                data.similars.map(async (sim) => {
                  const imgUrl = await fetchWikiImage(sim.latin || sim.name);
                  return { ...sim, imageUrl: imgUrl };
                })
              );
              setResultData(prev => ({ ...prev, similars: updatedSimilars }));
            }
          };

          const fetchGallery = async () => {
            // 传入拉丁名作为主要搜索关键词，因为文件名通常包含拉丁名
            const images = await fetchWikiGallery(data.latinName, data.name);
            setGalleryImages(images);
          };

          Promise.all([fetchSimilars(), fetchGallery()]).catch(err => console.error("后台图片获取部分错误", err));

        } else {
          setErrorMsg("未能识别出具体的植物或动物，请尝试更清晰的照片。");
          setCurrentScreen('error');
        }
      }, 500);

    } catch (error) {
      clearInterval(progressInterval);
      console.error("识别失败:", error);
      setErrorMsg("网络连接或识别服务出错，请稍后重试。");
      setCurrentScreen('error');
    }
  };

  const identifyImageWithGemini = async (base64Image) => {
    const prompt = `
      你是一位专业的自然学家。请分析这张图片，识别其中的植物或动物。
      请严格按照以下 JSON 格式返回结果（不要包含 Markdown 代码块标记，直接返回 JSON）。
      
      {
        "type": "plant" 或 "animal" (如果都不是，返回 "unknown"),
        "name": "中文通用名称",
        "latinName": "拉丁学名",
        "confidence": 0-100之间的整数,
        "taxonomy": {
          "kingdom": "界", "phylum": "门", "class": "纲", "order": "目", "family": "科", "genus": "属", "species": "种"
        },
        "description": "一段约80-120字的中文简介。",
        "conservationStatus": "保护状态 (如 '无危', '濒危')",
        "habitat": "主要栖息地",
        "diet": "食性",
        "moreCharacteristics": [{"key": "寿命", "value": "..."}],
        "characteristics": ["特征1", "特征2"],
        "similars": [
          { 
            "name": "相似物种中文名", 
            "latin": "相似物种拉丁名", 
            "score": 0-100
          },
          { 
            "name": "相似物种中文名", 
            "latin": "相似物种拉丁名", 
            "score": 0-100
          }
        ]
      }
    `;

    const response = await fetch(
      `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-09-2025:generateContent?key=${apiKey}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          contents: [{
            parts: [
              { text: prompt },
              { inlineData: { mimeType: "image/jpeg", data: base64Image } }
            ]
          }],
          generationConfig: { responseMimeType: "application/json" }
        })
      }
    );

    const result = await response.json();
    const text = result.candidates?.[0]?.content?.parts?.[0]?.text;

    try {
      const cleanedText = text.replace(/```json|```/g, '').trim();
      return JSON.parse(cleanedText);
    } catch (e) {
      return JSON.parse(text);
    }
  };

  const identifySpeciesByName = async (name, latinName) => {
    const prompt = `
      你是一位专业的自然学家。请详细介绍这个物种：${name} (拉丁名: ${latinName || '未知'})。
      请严格按照以下 JSON 格式返回结果（不要包含 Markdown 代码块标记，直接返回 JSON）。
      
      {
        "type": "plant" 或 "animal",
        "name": "${name}",
        "latinName": "${latinName || '拉丁学名'}",
        "confidence": 100,
        "taxonomy": {
          "kingdom": "界", "phylum": "门", "class": "纲", "order": "目", "family": "科", "genus": "属", "species": "种"
        },
        "description": "一段约80-120字的中文简介。",
        "conservationStatus": "保护状态 (如 '无危', '濒危')",
        "habitat": "主要栖息地",
        "diet": "食性",
        "moreCharacteristics": [{"key": "寿命", "value": "..."}],
        "characteristics": ["特征1", "特征2"],
        "similars": [
          { 
            "name": "相似物种中文名", 
            "latin": "相似物种拉丁名", 
            "score": 0-100
          },
          { 
            "name": "相似物种中文名", 
            "latin": "相似物种拉丁名", 
            "score": 0-100
          }
        ]
      }
    `;

    const response = await fetch(
      `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-09-2025:generateContent?key=${apiKey}`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          contents: [{
            parts: [
              { text: prompt }
            ]
          }],
          generationConfig: { responseMimeType: "application/json" }
        })
      }
    );

    const result = await response.json();
    const text = result.candidates?.[0]?.content?.parts?.[0]?.text;

    try {
      const cleanedText = text.replace(/```json|```/g, '').trim();
      return JSON.parse(cleanedText);
    } catch (e) {
      return JSON.parse(text);
    }
  };

  const fetchWikiImage = async (query) => {
    if (!query) return null;
    try {
      const searchUrl = `https://en.wikipedia.org/w/api.php?action=query&format=json&origin=*&generator=search&gsrnamespace=0&gsrlimit=1&gsrsearch=${encodeURIComponent(query)}&prop=pageimages&pithumbsize=300`;
      const response = await fetch(searchUrl);
      const data = await response.json();
      if (data.query && data.query.pages) {
        const pageId = Object.keys(data.query.pages)[0];
        return data.query.pages[pageId]?.thumbnail?.source || null;
      }
      return null;
    } catch (error) {
      return null;
    }
  };

  // 升级版画廊获取：增加过滤逻辑
  const fetchWikiGallery = async (latinName, commonName) => {
    const query = latinName || commonName;
    if (!query) return [];
    try {
      // 1. 搜索页面
      const searchRes = await fetch(`https://en.wikipedia.org/w/api.php?action=query&format=json&origin=*&list=search&srsearch=${encodeURIComponent(query)}&srlimit=1`);
      const searchData = await searchRes.json();
      if (!searchData.query?.search?.length) return [];
      const pageId = searchData.query.search[0].pageid;

      // 2. 获取页面图片 (增加获取数量以供筛选)
      const imagesRes = await fetch(`https://en.wikipedia.org/w/api.php?action=query&format=json&origin=*&generator=images&pageids=${pageId}&gimlimit=30&prop=imageinfo&iiprop=url|mime&iiurlwidth=1200`);
      const imagesData = await imagesRes.json();

      const pages = imagesData.query?.pages || {};

      // 提取拉丁名属名用于过滤 (例如 "Hydrochoerus hydrochaeris" -> "Hydrochoerus")
      const genus = latinName ? latinName.split(' ')[0].toLowerCase() : '';
      const common = commonName ? commonName.toLowerCase().split(' ')[0] : '';

      const validImages = Object.values(pages)
        .filter(p => {
          const mime = p.imageinfo?.[0]?.mime;
          const url = p.imageinfo?.[0]?.url;
          if (!url) return false;
          const lowerUrl = url.toLowerCase();
          const fileName = lowerUrl.split('/').pop();

          // A. 基础格式过滤 (增强版)
          if (lowerUrl.endsWith('.svg') ||
            lowerUrl.endsWith('.ogv') ||
            lowerUrl.endsWith('.ogg') ||
            lowerUrl.endsWith('.mp3') ||
            lowerUrl.endsWith('.webm') ||
            lowerUrl.includes('icon') ||
            lowerUrl.includes('map') ||
            lowerUrl.includes('range') ||
            lowerUrl.includes('flag') ||
            lowerUrl.includes('logo')) {
            return false;
          }

          // A2. MIME 类型过滤
          if (mime && (mime.includes('audio') || mime.includes('video') || mime.includes('application'))) {
            return false;
          }

          // B. 内容相关性过滤 (关键修复：解决斑马/水豚问题)
          // 只有当文件名包含拉丁名(属名)或常用名时才认为是相关图片
          // 如果没有拉丁名，则稍微放宽限制
          if (genus && !fileName.includes(genus) && !fileName.includes(common)) {
            return false;
          }

          return true;
        })
        .map(p => p.imageinfo[0].thumburl || p.imageinfo[0].url);

      // 如果过滤后没有图片，尝试直接用主图
      if (validImages.length === 0) {
        const mainImg = await fetchWikiImage(query);
        return mainImg ? [mainImg] : [];
      }

      // 3. 限制数量为 6
      return validImages.slice(0, 6);
    } catch (e) {
      return [];
    }
  };

  const handleSimilarClick = async (similar) => {
    if (!similar.name) return;

    // 如果有图片，先展示图片；如果没有，保留当前背景或显示占位
    if (similar.imageUrl) {
      setSelectedImage(similar.imageUrl);
    }

    setCurrentScreen('scanning');
    setScanProgress(0);
    setErrorMsg("");
    setGalleryImages([]);
    setResultData(null); // 清除旧数据

    const progressInterval = setInterval(() => {
      setScanProgress((prev) => {
        if (prev >= 90) return 90;
        return prev + 2;
      });
    }, 50);

    try {
      // 使用名称进行识别
      const data = await identifySpeciesByName(similar.name, similar.latin);

      clearInterval(progressInterval);
      setScanProgress(100);

      setTimeout(async () => {
        if (data) {
          setResultData(data);
          setCurrentScreen('result');

          // 并行获取图片资源
          const fetchSimilars = async () => {
            if (data.similars && data.similars.length > 0) {
              const updatedSimilars = await Promise.all(
                data.similars.map(async (sim) => {
                  const imgUrl = await fetchWikiImage(sim.latin || sim.name);
                  return { ...sim, imageUrl: imgUrl };
                })
              );
              setResultData(prev => ({ ...prev, similars: updatedSimilars }));
            }
          };

          const fetchGallery = async () => {
            const images = await fetchWikiGallery(data.latinName, data.name);
            setGalleryImages(images);
            // 如果之前没有设置主图（比如similar item没图），这里可以用画廊第一张图作为主图
            if (!similar.imageUrl && images.length > 0) {
              setSelectedImage(images[0]);
            }
          };

          Promise.all([fetchSimilars(), fetchGallery()]).catch(err => console.error("后台图片获取部分错误", err));

        } else {
          setErrorMsg("未能获取该物种的详细信息。");
          setCurrentScreen('error');
        }
      }, 500);

    } catch (error) {
      clearInterval(progressInterval);
      console.error("识别失败:", error);
      setErrorMsg("网络连接或服务出错，请稍后重试。");
      setCurrentScreen('error');
    }
  };

  const resetApp = () => {
    setCurrentScreen('home');
    setSelectedImage(null);
    setResultData(null);
    setScanProgress(0);
    setErrorMsg("");
    setGalleryImages([]);
    if (fileInputRef.current) fileInputRef.current.value = '';
    if (cameraInputRef.current) cameraInputRef.current.value = '';
  };

  // --- Lightbox 逻辑优化 (带滑动跟手) ---
  const openLightbox = (index) => {
    setCurrentImageIndex(index);
    setLightboxOpen(true);
  };
  const closeLightbox = () => setLightboxOpen(false);

  const nextLightboxImage = () => {
    if (zoomLevel > 1) return;
    setCurrentImageIndex((prev) => (prev + 1) % galleryImages.length);
  };

  const prevLightboxImage = () => {
    if (zoomLevel > 1) return;
    setCurrentImageIndex((prev) => (prev - 1 + galleryImages.length) % galleryImages.length);
  };

  const handleDoubleTap = (e) => {
    if (e && e.stopPropagation) e.stopPropagation();
    if (zoomLevel > 1) {
      setZoomLevel(1);
      setPanPosition({ x: 0, y: 0 });
      setSwipeOffset(0);
    } else {
      setZoomLevel(2.5);
    }
  };

  // 统一指针事件处理
  const handlePointerDown = (e) => {
    const clientX = e.touches ? e.touches[0].clientX : e.clientX;
    const clientY = e.touches ? e.touches[0].clientY : e.clientY;

    const now = Date.now();
    if (now - lastTapTimeRef.current < 300) {
      handleDoubleTap(e);
      lastTapTimeRef.current = 0;
      return;
    }
    lastTapTimeRef.current = now;

    touchStartRef.current = { x: clientX, y: clientY };
    setIsDragging(true);
    setDragStartTime(Date.now());

    // 记录拖动起始位置 (用于缩放平移 或 普通滑动)
    dragStartRef.current = { x: clientX, y: clientY };
  };

  const handlePointerMove = (e) => {
    if (!isDragging || !touchStartRef.current) return;

    const clientX = e.touches ? e.touches[0].clientX : e.clientX;
    const clientY = e.touches ? e.touches[0].clientY : e.clientY;
    const diffX = clientX - dragStartRef.current.x;
    const diffY = clientY - dragStartRef.current.y;

    e.preventDefault(); // 防止页面滚动

    if (zoomLevel > 1) {
      // 放大模式：平移图片
      setPanPosition(prev => ({ x: prev.x + (clientX - touchStartRef.current.x), y: prev.y + (clientY - touchStartRef.current.y) }));
      touchStartRef.current = { x: clientX, y: clientY }; // 更新基准点
    } else {
      // 普通模式：设置滑动偏移量 (视觉跟手)
      setSwipeOffset(diffX);
    }
  };

  const handlePointerUp = (e) => {
    if (!isDragging) return;

    const dragDuration = Date.now() - dragStartTime;
    const isQuickSwipe = dragDuration < 300;
    const threshold = window.innerWidth * 0.25; // 滑动阈值

    if (zoomLevel === 1) {
      // 处理切图逻辑
      if (Math.abs(swipeOffset) > threshold || (isQuickSwipe && Math.abs(swipeOffset) > 50)) {
        if (swipeOffset > 0) prevLightboxImage(); // 向右滑 -> 上一张
        else nextLightboxImage(); // 向左滑 -> 下一张
      }
      // 归位
      setSwipeOffset(0);
    }

    setIsDragging(false);
    touchStartRef.current = null;
  };


  // --- 界面组件 ---

  const HomeScreen = () => (
    <div className="flex flex-col h-full bg-gradient-to-b from-emerald-50 to-white relative overflow-hidden">
      <div className="absolute top-0 right-0 w-64 h-64 bg-emerald-200 rounded-full mix-blend-multiply filter blur-3xl opacity-30 animate-blob"></div>
      <div className="absolute bottom-0 left-0 w-64 h-64 bg-teal-200 rounded-full mix-blend-multiply filter blur-3xl opacity-30 animate-blob animation-delay-2000"></div>

      <div className="flex-1 flex flex-col justify-center items-center px-6 z-10">
        <div className="w-24 h-24 bg-emerald-100 rounded-3xl flex items-center justify-center mb-6 shadow-lg transform rotate-3">
          <Leaf className="w-12 h-12 text-emerald-600" />
        </div>
        <h1 className="text-3xl font-bold text-gray-800 mb-2">NatureLens AI</h1>
        <p className="text-gray-500 text-center mb-12 max-w-xs">
          搭载 Gemini 视觉引擎。探索自然的奥秘。
        </p>

        <div className="w-full max-w-xs space-y-4">
          <button onClick={() => cameraInputRef.current.click()} className="w-full bg-emerald-600 hover:bg-emerald-700 text-white py-4 rounded-2xl shadow-lg shadow-emerald-200 flex items-center justify-center transition-all transform active:scale-95 group">
            <div className="bg-white/20 p-2 rounded-full mr-3 group-hover:rotate-12 transition-transform"><Camera className="w-6 h-6" /></div>
            <span className="font-semibold text-lg">拍照识别</span>
          </button>
          <button onClick={() => fileInputRef.current.click()} className="w-full bg-white border-2 border-gray-100 text-gray-700 py-4 rounded-2xl flex items-center justify-center hover:bg-gray-50 transition-all active:scale-95">
            <Upload className="w-5 h-5 mr-3 text-gray-400" /><span className="font-medium">从相册上传</span>
          </button>
          <input type="file" accept="image/*" capture="environment" ref={cameraInputRef} className="hidden" onChange={handleImageSelect} />
          <input type="file" accept="image/*" ref={fileInputRef} className="hidden" onChange={handleImageSelect} />
        </div>
      </div>
      <div className="py-6 text-center text-gray-400 text-xs"><p>Powered by Gemini & Wikipedia • v3.2</p></div>
    </div>
  );

  const ScanningScreen = () => (
    <div className="flex flex-col h-full bg-gradient-to-b from-emerald-50 to-white relative overflow-hidden">
      <div className="absolute top-0 right-0 w-64 h-64 bg-emerald-200 rounded-full mix-blend-multiply filter blur-3xl opacity-30 animate-blob"></div>
      <div className="absolute bottom-0 left-0 w-64 h-64 bg-teal-200 rounded-full mix-blend-multiply filter blur-3xl opacity-30 animate-blob animation-delay-2000"></div>
      <div className="flex-1 flex flex-col items-center justify-center z-10 w-full">
        <div className="relative w-[80%] aspect-square max-w-sm mx-auto rounded-3xl overflow-hidden shadow-2xl border-4 border-white bg-gray-100">
          <img src={selectedImage} alt="Analyzing" className="absolute inset-0 w-full h-full object-cover" />
          <div className="absolute inset-0 bg-black/10 z-10">
            <div className="absolute top-0 left-0 w-full h-1 bg-emerald-400 shadow-[0_0_20px_rgba(52,211,153,1)] animate-[scan_2s_ease-in-out_infinite] z-20"></div>
            <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.2)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.2)_1px,transparent_1px)] bg-[size:40px_40px]"></div>
            <div className="absolute top-4 left-4 w-8 h-8 border-t-4 border-l-4 border-emerald-400 rounded-tl-lg"></div>
            <div className="absolute top-4 right-4 w-8 h-8 border-t-4 border-r-4 border-emerald-400 rounded-tr-lg"></div>
            <div className="absolute bottom-4 left-4 w-8 h-8 border-b-4 border-l-4 border-emerald-400 rounded-bl-lg"></div>
            <div className="absolute bottom-4 right-4 w-8 h-8 border-b-4 border-r-4 border-emerald-400 rounded-br-lg"></div>
          </div>
        </div>
        <div className="mt-10 text-center px-8 w-full max-w-md">
          <div className="flex items-center justify-between text-emerald-800/70 text-sm mb-2 font-bold font-mono">
            <span className="flex items-center"><RefreshCw className="w-3 h-3 mr-2 animate-spin" /> 正在识别特征...</span>
            <span>{scanProgress}%</span>
          </div>
          <div className="w-full bg-gray-200/80 rounded-full h-3 overflow-hidden shadow-inner">
            <div className="bg-emerald-500 h-3 rounded-full transition-all duration-300 ease-out" style={{ width: `${scanProgress}%` }}></div>
          </div>
        </div>
      </div>
    </div>
  );

  const ErrorScreen = () => (
    <div className="flex flex-col h-full bg-gradient-to-b from-emerald-50 to-white relative overflow-hidden">
      <div className="absolute top-0 right-0 w-64 h-64 bg-red-200 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob"></div>
      <div className="absolute bottom-0 left-0 w-64 h-64 bg-orange-200 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-2000"></div>
      <div className="flex-1 flex flex-col items-center justify-center z-10 w-full">
        <div className="relative w-[80%] aspect-square max-w-sm mx-auto rounded-3xl overflow-hidden shadow-xl border-4 border-white bg-gray-100 mb-8">
          {selectedImage && (<img src={selectedImage} alt="Unrecognized" className="absolute inset-0 w-full h-full object-cover grayscale-[0.5] opacity-80" />)}
          <div className="absolute inset-0 bg-red-500/10 z-10 flex items-center justify-center">
            <div className="bg-white/90 backdrop-blur-sm p-4 rounded-full shadow-lg"><AlertCircle className="w-10 h-10 text-red-500" /></div>
          </div>
        </div>
        <div className="text-center px-8 w-full max-w-md">
          <h2 className="text-2xl font-bold text-gray-800 mb-2">未能成功识别</h2>
          <p className="text-gray-600 text-sm mb-8 leading-relaxed bg-white/60 p-4 rounded-xl border border-white shadow-sm backdrop-blur-sm">{errorMsg}</p>
          <div className="space-y-3 w-full">
            <button onClick={resetApp} className="w-full py-3.5 bg-emerald-600 text-white rounded-xl font-semibold hover:bg-emerald-700 transition-all shadow-lg shadow-emerald-200 flex items-center justify-center"><RefreshCw className="w-5 h-5 mr-2" />重新拍摄</button>
            <button onClick={resetApp} className="w-full py-3.5 bg-white text-gray-600 border border-gray-200 rounded-xl font-medium hover:bg-gray-50 transition-all">换一张试试</button>
          </div>
        </div>
      </div>
    </div>
  );

  const ResultScreen = () => {
    if (!resultData) return null;
    const isPlant = resultData.type === 'plant';
    const themeBg = isPlant ? 'bg-emerald-100' : 'bg-orange-100';
    const themeColor = isPlant ? 'text-emerald-600' : 'text-orange-600';

    return (
      <div className="flex flex-col h-full bg-gray-50 relative overflow-y-auto pb-6 animate-in fade-in duration-500">
        {/* 顶部大图 */}
        <div className="relative h-80 w-full shrink-0">
          <img src={selectedImage} alt="Identified" className="w-full h-full object-cover" />
          <div className="absolute inset-0 bg-gradient-to-t from-gray-900/80 via-transparent to-transparent"></div>
          <div className="absolute top-0 left-0 right-0 p-4 flex justify-between items-center text-white z-10">
            <button onClick={resetApp} className="p-2 bg-black/20 backdrop-blur-md rounded-full hover:bg-black/30 transition-colors"><ArrowLeft className="w-6 h-6" /></button>
            <button className="p-2 bg-black/20 backdrop-blur-md rounded-full hover:bg-black/30 transition-colors"><Share2 className="w-5 h-5" /></button>
          </div>
          <div className="absolute bottom-0 left-0 w-full p-6 text-white">
            <div className="flex items-center space-x-2 mb-2">
              <span className={`px-2 py-1 rounded-md text-xs font-bold bg-white/20 backdrop-blur-md uppercase tracking-wider border border-white/10`}>{isPlant ? '植物 · Plant' : '动物 · Animal'}</span>
              {resultData.confidence && (<div className="flex items-center text-xs bg-emerald-500/80 px-2 py-1 rounded-md backdrop-blur-md"><Award className="w-3 h-3 mr-1" />{resultData.confidence}% 确信</div>)}
            </div>
            <h1 className="text-3xl font-bold mb-1 drop-shadow-sm">{resultData.name}</h1>
            <p className="text-lg italic font-serif text-white/90 opacity-90">{resultData.latinName}</p>
          </div>
        </div>

        <div className="-mt-6 px-4 relative z-10 flex flex-col gap-4">

          {/* 1. 物种简介 */}
          <div className="bg-white rounded-2xl p-5 shadow-sm border border-gray-100">
            <h3 className="font-bold text-gray-800 mb-3 flex items-center"><div className={`w-1 h-5 rounded-full ${isPlant ? 'bg-emerald-500' : 'bg-orange-500'} mr-2`}></div>物种简介</h3>
            <p className="text-gray-600 text-sm leading-relaxed text-justify">{resultData.description || '暂无详细简介。'}</p>
            <div className="flex flex-wrap gap-2 mt-4">{resultData.characteristics?.map((tag, i) => (<span key={i} className="px-3 py-1 bg-gray-100 text-gray-600 rounded-full text-xs font-medium">#{tag}</span>))}</div>
          </div>

          {/* 2. 分类学信息 */}
          <div className="bg-white rounded-2xl p-5 shadow-sm border border-gray-100">
            <div className="flex items-center mb-4"><div className={`p-2 rounded-lg ${themeBg} ${themeColor} mr-3`}><Search className="w-5 h-5" /></div><h3 className="font-bold text-gray-800">分类学信息</h3></div>
            <div className="grid grid-cols-2 gap-y-3 text-sm">
              {Object.entries(resultData.taxonomy || {}).map(([key, value]) => (<div key={key}><span className="text-gray-400 block text-xs transform scale-90 origin-top-left">{key.toUpperCase()}</span><span className="font-medium text-gray-700">{value || '-'}</span></div>))}
            </div>
          </div>

          {/* 3. 生态特征 */}
          <div className="bg-white rounded-2xl p-5 shadow-sm border border-gray-100">
            <div className="flex items-center mb-4"><div className={`p-2 rounded-lg ${themeBg} ${themeColor} mr-3`}>{isPlant ? <Leaf className="w-5 h-5" /> : <Bird className="w-5 h-5" />}</div><h3 className="font-bold text-gray-800">生态特征</h3></div>
            <div className="space-y-4 text-sm">
              {resultData.conservationStatus && (<div className="flex items-center border-b border-gray-50 pb-3"><span className="text-gray-400 w-24 shrink-0 whitespace-nowrap">保护状态</span><span className="font-medium text-gray-700 text-left flex-1">{resultData.conservationStatus}</span></div>)}
              {resultData.habitat && (<div className="flex items-center border-b border-gray-50 pb-3"><span className="text-gray-400 w-24 shrink-0 whitespace-nowrap">栖息地</span><span className="font-medium text-gray-700 text-left flex-1">{resultData.habitat}</span></div>)}
              {resultData.diet && (<div className="flex items-center border-b border-gray-50 pb-3"><span className="text-gray-400 w-24 shrink-0 whitespace-nowrap">食性</span><span className="font-medium text-gray-700 text-left flex-1">{resultData.diet}</span></div>)}
              {resultData.moreCharacteristics?.map((item, i) => (<div key={i} className="flex items-center border-b border-gray-50 pb-3 last:border-0"><span className="text-gray-400 w-24 shrink-0 whitespace-nowrap">{item.key}</span><span className="font-medium text-gray-700 text-left flex-1">{item.value}</span></div>))}
            </div>
          </div>

          {/* 4. 物种图片 */}
          {galleryImages.length > 0 && (
            <div className="bg-white rounded-2xl p-5 shadow-sm border border-gray-100">
              <div className="flex items-center mb-4 justify-between">
                <div className="flex items-center">
                  <div className={`p-2 rounded-lg ${themeBg} ${themeColor} mr-3`}><ImageIcon className="w-5 h-5" /></div>
                  <h3 className="font-bold text-gray-800">物种图片</h3>
                </div>
                <span className="text-xs text-gray-400">{galleryImages.length} 张</span>
              </div>
              {/* 横向滚动容器：增加 cursor-grab 和 active:cursor-grabbing 提示 */}
              <div className="flex space-x-3 overflow-x-auto pb-2 -mx-2 px-2 scrollbar-hide cursor-grab active:cursor-grabbing">
                {galleryImages.map((img, idx) => (
                  <div key={idx} className="relative flex-shrink-0 w-32 h-32 rounded-xl overflow-hidden border border-gray-100 shadow-sm cursor-pointer hover:opacity-90 transition-opacity" onClick={() => openLightbox(idx)}>
                    <img src={img} alt={`Gallery ${idx}`} className="w-full h-full object-cover pointer-events-none" />
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* 相似物种 */}
          <div className="bg-white rounded-2xl p-5 shadow-sm border border-gray-100 mb-6">
            <div className="flex justify-between items-center mb-4">
              <div className='flex items-center'>
                <div className={`p-2 rounded-lg ${themeBg} ${themeColor} mr-3`}><Binoculars className="w-5 h-5" /></div>
                <h3 className="font-bold text-gray-800">相似物种</h3>
              </div>
              <span className="text-xs text-gray-400">图片来源：Wikipedia</span>
            </div>
            <div className="space-y-3">
              {resultData.similars?.map((item, idx) => (
                <div key={idx} onClick={() => handleSimilarClick(item)} className="flex items-center justify-between p-3 rounded-xl hover:bg-gray-50 border border-transparent hover:border-gray-100 transition-colors group cursor-pointer active:scale-[0.98] active:bg-gray-100">
                  <div className="flex items-center flex-1">
                    <div className="w-12 h-12 rounded-xl bg-gray-100 flex items-center justify-center mr-4 overflow-hidden shrink-0 border border-gray-200 relative">
                      {item.imageUrl ? (<img src={item.imageUrl} alt={item.name} className="w-full h-full object-cover animate-in fade-in duration-700" />) : (<div className="flex flex-col items-center justify-center text-gray-300"><ImageOff className="w-4 h-4" /></div>)}
                    </div>
                    <div><div className="font-bold text-gray-800 text-sm group-hover:text-emerald-600 transition-colors">{item.name}</div><div className="text-xs text-gray-400 italic mb-1">{item.latin}</div></div>
                  </div>
                  <div className="flex items-center pl-2">
                    <span className="text-xs font-medium text-gray-400 mr-2">{item.score || 0}% 相似</span>
                    <div className="w-12 h-1.5 bg-gray-100 rounded-full overflow-hidden"><div className={`h-full rounded-full ${isPlant ? 'bg-emerald-400' : 'bg-orange-400'}`} style={{ width: `${item.score || 50}%` }}></div></div>
                    <ChevronRight className="w-4 h-4 text-gray-300 ml-2 group-hover:text-emerald-500 transition-colors" />
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Lightbox 增强版 (带跟手滑动) */}
        {lightboxOpen && (
          <div className="fixed inset-0 z-50 bg-black bg-opacity-95 flex items-center justify-center overflow-hidden touch-none"
            onTouchStart={handlePointerDown}
            onTouchMove={handlePointerMove}
            onTouchEnd={handlePointerUp}
            onMouseDown={handlePointerDown}
            onMouseMove={handlePointerMove}
            onMouseUp={handlePointerUp}
            onMouseLeave={handlePointerUp}
          >
            <button
              className="absolute top-6 right-6 text-white/80 hover:text-white z-50 p-2"
              onClick={closeLightbox}
              onPointerDown={(e) => e.stopPropagation()}
              onMouseDown={(e) => e.stopPropagation()}
              onTouchStart={(e) => e.stopPropagation()}
            >
              <X className="w-8 h-8" />
            </button>

            {/* 图片容器 */}
            <div
              className="w-full h-full flex items-center justify-center overflow-hidden relative"
            >
              <img
                src={galleryImages[currentImageIndex]}
                alt="Full View"
                className="max-w-full max-h-full object-contain transition-transform duration-75 ease-out" // 动画时间缩短以跟手
                style={{
                  // 当未放大时，应用 swipeOffset 实现跟手平移；放大时应用 Zoom 和 Pan
                  transform: zoomLevel === 1
                    ? `translateX(${swipeOffset}px)`
                    : `scale(${zoomLevel}) translate(${panPosition.x / zoomLevel}px, ${panPosition.y / zoomLevel}px)`,
                  cursor: zoomLevel > 1 ? 'grab' : 'grab'
                }}
                draggable={false}
              />
            </div>

            {/* 提示信息 (仅在未放大时显示) */}
            {zoomLevel === 1 && (
              <>
                <div className="absolute bottom-20 text-white/50 text-xs bg-black/30 px-3 py-1 rounded-full backdrop-blur-md pointer-events-none">
                  双击缩放 • 左右滑动切换
                </div>
                {/* 左右切换按钮 (在桌面端辅助) */}
                <button className="absolute left-4 top-1/2 -translate-y-1/2 text-white/60 hover:text-white p-4 rounded-full hidden md:block" onClick={(e) => { e.stopPropagation(); prevLightboxImage(); }}><ChevronLeft className="w-8 h-8" /></button>
                <button className="absolute right-4 top-1/2 -translate-y-1/2 text-white/60 hover:text-white p-4 rounded-full hidden md:block" onClick={(e) => { e.stopPropagation(); nextLightboxImage(); }}><ChevronRight className="w-8 h-8" /></button>

                <div className="absolute bottom-8 left-0 right-0 flex justify-center gap-2">
                  {galleryImages.map((_, idx) => (
                    <div key={idx} className={`w-2 h-2 rounded-full transition-all ${idx === currentImageIndex ? 'bg-white w-4' : 'bg-white/40'}`} />
                  ))}
                </div>
              </>
            )}
          </div>
        )}
      </div>
    );
  };

  return (
    <div className="w-full max-w-md mx-auto h-[100dvh] shadow-2xl overflow-hidden bg-white">
      {currentScreen === 'home' && <HomeScreen />}
      {currentScreen === 'scanning' && <ScanningScreen />}
      {currentScreen === 'result' && <ResultScreen />}
      {currentScreen === 'error' && <ErrorScreen />}

      <style>{`
        @keyframes scan { 0% { top: 0%; opacity: 0; } 10% { opacity: 1; } 90% { opacity: 1; } 100% { top: 100%; opacity: 0; } }
        @keyframes blob { 0% { transform: translate(0px, 0px) scale(1); } 33% { transform: translate(30px, -50px) scale(1.1); } 66% { transform: translate(-20px, 20px) scale(0.9); } 100% { transform: translate(0px, 0px) scale(1); } }
        .animation-delay-2000 { animation-delay: 2s; }
        .animate-blob { animation: blob 7s infinite; }
        .scrollbar-hide::-webkit-scrollbar { display: none; }
        .scrollbar-hide { -ms-overflow-style: none; scrollbar-width: none; }
      `}</style>
    </div>
  );
};

export default App;