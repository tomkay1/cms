// JavaScript Document

//����ͼƬ�б�
function showPic(num)
{
    //�����е�li��ʽ��ֵΪ��
    var objUl=FulS();
    for(var i=0;i<objUl.length;i++)
    {
        objUl[i].className="";
    }

    //�Ե����Ľ�����ʽӦ��
    FliS(num).className="tsSelectImg";

    //�õ��������ͼƬֵ
    var src=Fpic(num).getAttribute("tsImgS");

    //���и�ֵ
    var Objimg=FimgS();

    Objimg.src=Fpic(num).src;


    document.getElementById("tsImgS").getElementsByTagName("a")[0].href=src;

    //ͼƬ�ȱ���
    tsScrollResize();

    //���õ���
    tsScrollDh(num);


    //����ͼƬ��λ
    FulSs().style.marginLeft="-"+(tsNum()*tsRowNum()*FliS(0).offsetWidth)+"px";


}
//��һҳ
function tsScrollArrLeft()
{
    if(tsNum()+1>1)
    {
        //���õ���
        tsScrollDh((tsNum()-1)*tsRowNum());

        //����ͼƬ��λ
        FulSs().style.marginLeft="-"+(tsNum())*tsRowNum()*FliS(0).offsetWidth+"px";

    }
}

//��һҳ
function tsScrollArrRight()
{
    if(tsNum()+2<=tsRowCount())
    {
        //���õ���
        tsScrollDh((tsNum()+1)*tsRowNum());
        //����ͼƬ��λ
        FulSs().style.marginLeft="-"+(tsNum())*tsRowNum()*FliS(0).offsetWidth+"px";

    }
}



//���õ���,������������Img���в���,��ôimgno��Ҫ�в�������
function tsScrollDh(i)
{
    //������һҳ����
    document.getElementById("tsImgSArrL").setAttribute("showPicNum",i);

    //������һҳ����
    document.getElementById("tsImgSArrR").setAttribute("showPicNum",i);

}


function tsScrollResize()
{
    var maxWidth=300;
    var maxHeight=300;

    var myimg = FimgS();

    var imgNew = new Image();
    imgNew.src = myimg.src;

    //��myimg���������൱��һ����������Ȼ�첽��ʱ��ִ��̫�죬һֱ�����һ��ͼ
    imgNew.preImg=myimg;


    //�����Ϊ�˷����ε��������ͼƬ���߼�Ϊ0ִ��
    if (imgNew.width == 0 || imgNew.height == 0) {
        imgNew.onload=function(){
            tsScrollResizeHd(this,maxWidth,maxHeight,this.preImg);
        };
    }
    else
    {
        tsScrollResizeHd(imgNew,maxWidth,maxHeight,myimg);
    }

}

function tsScrollResizeHd(imgNew,maxWidth,maxHeight,myimg)
{
    var hRatio;
    var wRatio;
    var Ratio = 1;
    var w = imgNew.width;
    var h = imgNew.height;
    wRatio = maxWidth / w;
    hRatio = maxHeight / h;
    if (maxWidth == 0 && maxHeight == 0) {
        Ratio = 1;
    } else if (maxWidth == 0) {
        if (hRatio < 1) Ratio = hRatio;
    } else if (maxHeight == 0) {
        if (wRatio < 1) Ratio = wRatio;
    } else if (wRatio < 1 || hRatio < 1) {
        Ratio = (wRatio <= hRatio ? wRatio: hRatio);
    }
    if (Ratio < 1) {

        w = w * Ratio;
        h = h * Ratio;
    }

    if(h%2!=0)
    {
        h=h-1;
    }

    myimg.height = h;
    myimg.width = w;


    var tsImgsBox=document.getElementById("tsImgS");
    if(myimg.height<300)
    {
        var TopBottom=(300-myimg.height)/2;
        tsImgsBox.style.paddingTop=TopBottom+"px";
        tsImgsBox.style.paddingBottom=TopBottom+"px";
    }
    else
    {
        tsImgsBox.style.paddingTop="0px";
        tsImgsBox.style.paddingBottom="0px";
    }
}

//һ����ʾ����
function tsRowNum()
{
    return document.getElementById("tsImgSCon").offsetWidth/FliS(0).offsetWidth;
}

//�ڼ��� ��0��ʼ
function tsNum()
{
    return Math.floor(document.getElementById("tsImgSArrL").getAttribute("showPicNum")/tsRowNum());
}
//������
function tsRowCount()
{
    return Math.ceil(FulS().length/tsRowNum());
}

//����ͼƬ����
function Fpic(i)
{
    var tsImgSCon=document.getElementById("tsImgSCon").getElementsByTagName("li");
    return src=tsImgSCon.item(i).getElementsByTagName("img")[0];
}
//����li����
function FliS(i)
{
    return document.getElementById("tsImgSCon").getElementsByTagName("li")[i];
}

//����ͼƬ�б����
function FulS()
{
    return document.getElementById("tsImgSCon").getElementsByTagName("li");
}
//��������ͼ
function FimgS(){
    return document.getElementById("tsImgS").getElementsByTagName("img")[0];
}
//����Ul����
function FulSs()
{
    return document.getElementById("tsImgSCon").getElementsByTagName("ul")[0];
}

//ͼƬ�������DIV��
document.getElementById("tsImgSCon").style.width=FliS(0).offsetWidth*4+"px";

//Ul��
FulSs().style.width=FliS(0).offsetWidth*FulS().length+"px";

//ͼƬ�ȱ���
tsScrollResize();