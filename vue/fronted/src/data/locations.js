// 地址级联选择器数据
export const locationOptions = [
  {
    value: 'china',
    label: '中国',
    children: [
      // 直辖市
      {
        value: 'beijing',
        label: '北京市',
        children: [
          { value: 'dongcheng', label: '东城区' },
          { value: 'xicheng', label: '西城区' },
          { value: 'chaoyang', label: '朝阳区' },
          { value: 'fengtai', label: '丰台区' },
          { value: 'shijingshan', label: '石景山区' },
          { value: 'haidian', label: '海淀区' },
          { value: 'mentougou', label: '门头沟区' },
          { value: 'fangshan', label: '房山区' },
          { value: 'tongzhou', label: '通州区' },
          { value: 'shunyi', label: '顺义区' },
          { value: 'changping', label: '昌平区' },
          { value: 'daxing', label: '大兴区' },
          { value: 'huairou', label: '怀柔区' },
          { value: 'pinggu', label: '平谷区' },
          { value: 'miyun', label: '密云区' },
          { value: 'yanqing', label: '延庆区' }
        ]
      },
      {
        value: 'shanghai',
        label: '上海市',
        children: [
          { value: 'huangpu', label: '黄浦区' },
          { value: 'xuhui', label: '徐汇区' },
          { value: 'changning', label: '长宁区' },
          { value: 'jingan', label: '静安区' },
          { value: 'putuo', label: '普陀区' },
          { value: 'hongkou', label: '虹口区' },
          { value: 'yangpu', label: '杨浦区' },
          { value: 'minhang', label: '闵行区' },
          { value: 'baoshan', label: '宝山区' },
          { value: 'jiading', label: '嘉定区' },
          { value: 'pudong', label: '浦东新区' },
          { value: 'jinshan', label: '金山区' },
          { value: 'songjiang', label: '松江区' },
          { value: 'qingpu', label: '青浦区' },
          { value: 'fengxian', label: '奉贤区' },
          { value: 'chongming', label: '崇明区' }
        ]
      },
      {
        value: 'tianjin',
        label: '天津市',
        children: [
          { value: 'heping', label: '和平区' },
          { value: 'hedong', label: '河东区' },
          { value: 'hexi', label: '河西区' },
          { value: 'nankai', label: '南开区' },
          { value: 'hebei', label: '河北区' },
          { value: 'hongqiao', label: '红桥区' },
          { value: 'dongli', label: '东丽区' },
          { value: 'xiqing', label: '西青区' },
          { value: 'jinnan', label: '津南区' },
          { value: 'beichen', label: '北辰区' },
          { value: 'wuqing', label: '武清区' },
          { value: 'baodi', label: '宝坻区' },
          { value: 'binhai', label: '滨海新区' },
          { value: 'ninghe', label: '宁河区' },
          { value: 'jinghai', label: '静海区' },
          { value: 'jizhou', label: '蓟州区' }
        ]
      },
      {
        value: 'chongqing',
        label: '重庆市',
        children: [
          { value: 'wanzhou', label: '万州区' },
          { value: 'fuling', label: '涪陵区' },
          { value: 'yuzhong', label: '渝中区' },
          { value: 'dadukou', label: '大渡口区' },
          { value: 'jiangbei', label: '江北区' },
          { value: 'shapingba', label: '沙坪坝区' },
          { value: 'jiulongpo', label: '九龙坡区' },
          { value: 'nanan', label: '南岸区' },
          { value: 'beibei', label: '北碚区' },
          { value: 'qijiang', label: '綦江区' },
          { value: 'dazu', label: '大足区' },
          { value: 'yubei', label: '渝北区' },
          { value: 'banan', label: '巴南区' },
          { value: 'qianjiang', label: '黔江区' },
          { value: 'changshou', label: '长寿区' },
          { value: 'jiangjin', label: '江津区' },
          { value: 'hechuan', label: '合川区' },
          { value: 'yongchuan', label: '永川区' },
          { value: 'nanchuan', label: '南川区' },
          { value: 'bishan', label: '璧山区' },
          { value: 'tongliang', label: '铜梁区' },
          { value: 'tongnan', label: '潼南区' },
          { value: 'rongchang', label: '荣昌区' },
          { value: 'kaizhou', label: '开州区' },
          { value: 'liangping', label: '梁平区' },
          { value: 'wulong', label: '武隆区' }
        ]
      },
      // 华南地区
      {
        value: 'guangdong',
        label: '广东省',
        children: [
          {
            value: 'guangzhou',
            label: '广州市',
            children: [
              { value: 'liwan', label: '荔湾区' },
              { value: 'yuexiu', label: '越秀区' },
              { value: 'haizhu', label: '海珠区' },
              { value: 'tianhe', label: '天河区' },
              { value: 'baiyun', label: '白云区' },
              { value: 'huangpu_gz', label: '黄埔区' },
              { value: 'panyu', label: '番禺区' },
              { value: 'huadu', label: '花都区' },
              { value: 'nansha', label: '南沙区' },
              { value: 'conghua', label: '从化区' },
              { value: 'zengcheng', label: '增城区' }
            ]
          },
          {
            value: 'shenzhen',
            label: '深圳市',
            children: [
              { value: 'luohu', label: '罗湖区' },
              { value: 'futian', label: '福田区' },
              { value: 'nanshan', label: '南山区' },
              { value: 'bao_an', label: '宝安区' },
              { value: 'longgang', label: '龙岗区' },
              { value: 'yantian', label: '盐田区' },
              { value: 'longhua', label: '龙华区' },
              { value: 'pingshan', label: '坪山区' },
              { value: 'guangming', label: '光明区' },
              { value: 'dapeng', label: '大鹏新区' }
            ]
          },
          { value: 'zhuhai', label: '珠海市' },
          { value: 'shantou', label: '汕头市' },
          { value: 'foshan', label: '佛山市' },
          { value: 'shaoguan', label: '韶关市' },
          { value: 'zhanjiang', label: '湛江市' },
          { value: 'zhaoqing', label: '肇庆市' },
          { value: 'jiangmen', label: '江门市' },
          { value: 'maoming', label: '茂名市' },
          { value: 'huizhou', label: '惠州市' },
          { value: 'meizhou', label: '梅州市' },
          { value: 'shanwei', label: '汕尾市' },
          { value: 'heyuan', label: '河源市' },
          { value: 'yangjiang', label: '阳江市' },
          { value: 'qingyuan', label: '清远市' },
          { value: 'dongguan', label: '东莞市' },
          { value: 'zhongshan', label: '中山市' },
          { value: 'chaozhou', label: '潮州市' },
          { value: 'jieyang', label: '揭阳市' },
          { value: 'yunfu', label: '云浮市' }
        ]
      },
      {
        value: 'guangxi',
        label: '广西壮族自治区',
        children: [
          { value: 'nanning', label: '南宁市' },
          { value: 'liuzhou', label: '柳州市' },
          { value: 'guilin', label: '桂林市' },
          { value: 'wuzhou', label: '梧州市' },
          { value: 'beihai', label: '北海市' },
          { value: 'fangchenggang', label: '防城港市' },
          { value: 'qinzhou', label: '钦州市' },
          { value: 'guigang', label: '贵港市' },
          { value: 'yulin_gx', label: '玉林市' },
          { value: 'baise', label: '百色市' },
          { value: 'hezhou', label: '贺州市' },
          { value: 'hechi', label: '河池市' },
          { value: 'laibin', label: '来宾市' },
          { value: 'chongzuo', label: '崇左市' }
        ]
      },
      {
        value: 'hainan',
        label: '海南省',
        children: [
          { value: 'haikou', label: '海口市' },
          { value: 'sanya', label: '三亚市' },
          { value: 'sansha', label: '三沙市' },
          { value: 'danzhou', label: '儋州市' },
          { value: 'wuzhishan', label: '五指山市' },
          { value: 'qionghai', label: '琼海市' },
          { value: 'wenchang', label: '文昌市' },
          { value: 'wanning', label: '万宁市' },
          { value: 'dongfang', label: '东方市' },
          { value: 'ding_an', label: '定安县' },
          { value: 'tunchang', label: '屯昌县' },
          { value: 'chengmai', label: '澄迈县' },
          { value: 'lingao', label: '临高县' },
          { value: 'baisha', label: '白沙黎族自治县' },
          { value: 'changjiang', label: '昌江黎族自治县' },
          { value: 'ledong', label: '乐东黎族自治县' },
          { value: 'lingshui', label: '陵水黎族自治县' },
          { value: 'baoting', label: '保亭黎族苗族自治县' },
          { value: 'qiongzhong', label: '琼中黎族苗族自治县' }
        ]
      },
      // 华东地区
      {
        value: 'zhejiang',
        label: '浙江省',
        children: [
          {
            value: 'hangzhou',
            label: '杭州市',
            children: [
              { value: 'shangcheng', label: '上城区' },
              { value: 'gongshu', label: '拱墅区' },
              { value: 'xihu', label: '西湖区' },
              { value: 'binjiang', label: '滨江区' },
              { value: 'xiaoshan', label: '萧山区' },
              { value: 'yuhang', label: '余杭区' },
              { value: 'fuyang', label: '富阳区' },
              { value: 'linan', label: '临安区' }
            ]
          },
          {
            value: 'ningbo',
            label: '宁波市',
            children: [
              { value: 'haishu', label: '海曙区' },
              { value: 'jiangbei', label: '江北区' },
              { value: 'beilun', label: '北仑区' },
              { value: 'zhenhai', label: '镇海区' },
              { value: 'yinzhou', label: '鄞州区' },
              { value: 'fenghua', label: '奉化区' }
            ]
          },
          { value: 'wenzhou', label: '温州市' },
          { value: 'jiaxing', label: '嘉兴市' },
          { value: 'huzhou', label: '湖州市' },
          { value: 'shaoxing', label: '绍兴市' },
          { value: 'jinhua', label: '金华市' },
          { value: 'quzhou', label: '衢州市' },
          { value: 'zhoushan', label: '舟山市' },
          { value: 'taizhou_zj', label: '台州市' },
          { value: 'lishui', label: '丽水市' }
        ]
      },
      {
        value: 'jiangsu',
        label: '江苏省',
        children: [
          {
            value: 'nanjing',
            label: '南京市',
            children: [
              { value: 'xuanwu', label: '玄武区' },
              { value: 'qinhuai', label: '秦淮区' },
              { value: 'jianye', label: '建邺区' },
              { value: 'gulou', label: '鼓楼区' },
              { value: 'pukou', label: '浦口区' },
              { value: 'qixia', label: '栖霞区' },
              { value: 'yuhuatai', label: '雨花台区' },
              { value: 'jiangning', label: '江宁区' },
              { value: 'lishui_js', label: '溧水区' },
              { value: 'gaochun', label: '高淳区' }
            ]
          },
          {
            value: 'suzhou',
            label: '苏州市',
            children: [
              { value: 'gusu', label: '姑苏区' },
              { value: 'huqiu', label: '虎丘区' },
              { value: 'wuzhong', label: '吴中区' },
              { value: 'xiangcheng', label: '相城区' },
              { value: 'kunshan', label: '昆山市' },
              { value: 'changshu', label: '常熟市' }
            ]
          },
          { value: 'wuxi', label: '无锡市' },
          { value: 'xuzhou', label: '徐州市' },
          { value: 'changzhou', label: '常州市' },
          { value: 'nantong', label: '南通市' },
          { value: 'lianyungang', label: '连云港市' },
          { value: 'huaian', label: '淮安市' },
          { value: 'yancheng', label: '盐城市' },
          { value: 'yangzhou', label: '扬州市' },
          { value: 'zhenjiang', label: '镇江市' },
          { value: 'taizhou_js', label: '泰州市' },
          { value: 'suqian', label: '宿迁市' }
        ]
      },
      {
        value: 'anhui',
        label: '安徽省',
        children: [
          { value: 'hefei', label: '合肥市' },
          { value: 'wuhu', label: '芜湖市' },
          { value: 'bengbu', label: '蚌埠市' },
          { value: 'huainan', label: '淮南市' },
          { value: 'maanshan', label: '马鞍山市' },
          { value: 'huaibei', label: '淮北市' },
          { value: 'tongling', label: '铜陵市' },
          { value: 'anqing', label: '安庆市' },
          { value: 'huangshan', label: '黄山市' },
          { value: 'chuzhou', label: '滁州市' },
          { value: 'fuyang_ah', label: '阜阳市' },
          { value: 'suzhou_ah', label: '宿州市' },
          { value: 'luan', label: '六安市' },
          { value: 'bozhou', label: '亳州市' },
          { value: 'chizhou', label: '池州市' },
          { value: 'xuancheng', label: '宣城市' }
        ]
      },
      {
        value: 'fujian',
        label: '福建省',
        children: [
          { value: 'fuzhou', label: '福州市' },
          { value: 'xiamen', label: '厦门市' },
          { value: 'putian', label: '莆田市' },
          { value: 'sanming', label: '三明市' },
          { value: 'quanzhou', label: '泉州市' },
          { value: 'zhangzhou', label: '漳州市' },
          { value: 'nanping', label: '南平市' },
          { value: 'longyan', label: '龙岩市' },
          { value: 'ningde', label: '宁德市' }
        ]
      },
      {
        value: 'jiangxi',
        label: '江西省',
        children: [
          { value: 'nanchang', label: '南昌市' },
          { value: 'jingdezhen', label: '景德镇市' },
          { value: 'pingxiang', label: '萍乡市' },
          { value: 'jiujiang', label: '九江市' },
          { value: 'xinyu', label: '新余市' },
          { value: 'yingtan', label: '鹰潭市' },
          { value: 'ganzhou', label: '赣州市' },
          { value: 'jian', label: '吉安市' },
          { value: 'yichun_jx', label: '宜春市' },
          { value: 'fuzhou_jx', label: '抚州市' },
          { value: 'shangrao', label: '上饶市' }
        ]
      },
      {
        value: 'shandong',
        label: '山东省',
        children: [
          { value: 'jinan', label: '济南市' },
          { value: 'qingdao', label: '青岛市' },
          { value: 'zibo', label: '淄博市' },
          { value: 'zaozhuang', label: '枣庄市' },
          { value: 'dongying', label: '东营市' },
          { value: 'yantai', label: '烟台市' },
          { value: 'weifang', label: '潍坊市' },
          { value: 'jining', label: '济宁市' },
          { value: 'taian', label: '泰安市' },
          { value: 'weihai', label: '威海市' },
          { value: 'rizhao', label: '日照市' },
          { value: 'binzhou', label: '滨州市' },
          { value: 'dezhou', label: '德州市' },
          { value: 'liaocheng', label: '聊城市' },
          { value: 'linyi', label: '临沂市' },
          { value: 'heze', label: '菏泽市' }
        ]
      },
      // 华中地区
      {
        value: 'henan',
        label: '河南省',
        children: [
          { value: 'zhengzhou', label: '郑州市' },
          { value: 'kaifeng', label: '开封市' },
          { value: 'luoyang', label: '洛阳市' },
          { value: 'pingdingshan', label: '平顶山市' },
          { value: 'anyang', label: '安阳市' },
          { value: 'hebi', label: '鹤壁市' },
          { value: 'xinxiang', label: '新乡市' },
          { value: 'jiaozuo', label: '焦作市' },
          { value: 'puyang', label: '濮阳市' },
          { value: 'xuchang', label: '许昌市' },
          { value: 'luohe', label: '漯河市' },
          { value: 'sanmenxia', label: '三门峡市' },
          { value: 'nanyang', label: '南阳市' },
          { value: 'shangqiu', label: '商丘市' },
          { value: 'xinyang', label: '信阳市' },
          { value: 'zhoukou', label: '周口市' },
          { value: 'zhumadian', label: '驻马店市' },
          { value: 'jiyuan', label: '济源市' }
        ]
      },
      {
        value: 'hubei',
        label: '湖北省',
        children: [
          { value: 'wuhan', label: '武汉市' },
          { value: 'huangshi', label: '黄石市' },
          { value: 'shiyan', label: '十堰市' },
          { value: 'yichang', label: '宜昌市' },
          { value: 'xiangyang', label: '襄阳市' },
          { value: 'ezhou', label: '鄂州市' },
          { value: 'jingmen', label: '荆门市' },
          { value: 'xiaogan', label: '孝感市' },
          { value: 'jingzhou', label: '荆州市' },
          { value: 'huanggang', label: '黄冈市' },
          { value: 'xianning', label: '咸宁市' },
          { value: 'suizhou', label: '随州市' },
          { value: 'enshi', label: '恩施土家族苗族自治州' },
          { value: 'shennongjia', label: '神农架林区' }
        ]
      },
      {
        value: 'hunan',
        label: '湖南省',
        children: [
          { value: 'changsha', label: '长沙市' },
          { value: 'zhuzhou', label: '株洲市' },
          { value: 'xiangtan', label: '湘潭市' },
          { value: 'hengyang', label: '衡阳市' },
          { value: 'shaoyang', label: '邵阳市' },
          { value: 'yueyang', label: '岳阳市' },
          { value: 'changde', label: '常德市' },
          { value: 'zhangjiajie', label: '张家界市' },
          { value: 'yiyang', label: '益阳市' },
          { value: 'chenzhou', label: '郴州市' },
          { value: 'yongzhou', label: '永州市' },
          { value: 'huaihua', label: '怀化市' },
          { value: 'loudi', label: '娄底市' },
          { value: 'xiangxi', label: '湘西土家族苗族自治州' }
        ]
      },
      // 华北地区
      {
        value: 'hebei',
        label: '河北省',
        children: [
          { value: 'shijiazhuang', label: '石家庄市' },
          { value: 'tangshan', label: '唐山市' },
          { value: 'qinhuangdao', label: '秦皇岛市' },
          { value: 'handan', label: '邯郸市' },
          { value: 'xingtai', label: '邢台市' },
          { value: 'baoding', label: '保定市' },
          { value: 'zhangjiakou', label: '张家口市' },
          { value: 'chengde', label: '承德市' },
          { value: 'cangzhou', label: '沧州市' },
          { value: 'langfang', label: '廊坊市' },
          { value: 'hengshui', label: '衡水市' }
        ]
      },
      {
        value: 'shanxi',
        label: '山西省',
        children: [
          { value: 'taiyuan', label: '太原市' },
          { value: 'datong', label: '大同市' },
          { value: 'yangquan', label: '阳泉市' },
          { value: 'changzhi', label: '长治市' },
          { value: 'jincheng', label: '晋城市' },
          { value: 'shuozhou', label: '朔州市' },
          { value: 'jinzhong', label: '晋中市' },
          { value: 'yuncheng', label: '运城市' },
          { value: 'xinzhou', label: '忻州市' },
          { value: 'linfen', label: '临汾市' },
          { value: 'lvliang', label: '吕梁市' }
        ]
      },
      {
        value: 'neimenggu',
        label: '内蒙古自治区',
        children: [
          { value: 'hohhot', label: '呼和浩特市' },
          { value: 'baotou', label: '包头市' },
          { value: 'wuhai', label: '乌海市' },
          { value: 'chifeng', label: '赤峰市' },
          { value: 'tongliao', label: '通辽市' },
          { value: 'ordos', label: '鄂尔多斯市' },
          { value: 'hulunbuir', label: '呼伦贝尔市' },
          { value: 'bayannur', label: '巴彦淖尔市' },
          { value: 'ulanqab', label: '乌兰察布市' },
          { value: 'xingan', label: '兴安盟' },
          { value: 'xilingol', label: '锡林郭勒盟' },
          { value: 'alxa', label: '阿拉善盟' }
        ]
      },
      // 西南地区
      {
        value: 'sichuan',
        label: '四川省',
        children: [
          {
            value: 'chengdu',
            label: '成都市',
            children: [
              { value: 'jinjiang', label: '锦江区' },
              { value: 'qingyang', label: '青羊区' },
              { value: 'jinniu', label: '金牛区' },
              { value: 'wuhou', label: '武侯区' },
              { value: 'chenghua', label: '成华区' },
              { value: 'longquanyi', label: '龙泉驿区' },
              { value: 'qingbaijiang', label: '青白江区' },
              { value: 'xindu', label: '新都区' },
              { value: 'wenjiang', label: '温江区' },
              { value: 'shuangliu', label: '双流区' },
              { value: 'pidu', label: '郫都区' }
            ]
          },
          { value: 'zigong', label: '自贡市' },
          { value: 'panzhihua', label: '攀枝花市' },
          { value: 'luzhou', label: '泸州市' },
          { value: 'deyang', label: '德阳市' },
          { value: 'mianyang', label: '绵阳市' },
          { value: 'guangyuan', label: '广元市' },
          { value: 'suining', label: '遂宁市' },
          { value: 'neijiang', label: '内江市' },
          { value: 'leshan', label: '乐山市' },
          { value: 'nanchong', label: '南充市' },
          { value: 'meishan', label: '眉山市' },
          { value: 'yibin', label: '宜宾市' },
          { value: 'guangan', label: '广安市' },
          { value: 'dazhou', label: '达州市' },
          { value: 'yaan', label: '雅安市' },
          { value: 'bazhong', label: '巴中市' },
          { value: 'ziyang', label: '资阳市' },
          { value: 'aba', label: '阿坝藏族羌族自治州' },
          { value: 'ganzi', label: '甘孜藏族自治州' },
          { value: 'liangshan', label: '凉山彝族自治州' }
        ]
      },
      {
        value: 'guizhou',
        label: '贵州省',
        children: [
          { value: 'guiyang', label: '贵阳市' },
          { value: 'liupanshui', label: '六盘水市' },
          { value: 'zunyi', label: '遵义市' },
          { value: 'anshun', label: '安顺市' },
          { value: 'bijie', label: '毕节市' },
          { value: 'tongren', label: '铜仁市' },
          { value: 'qianxinan', label: '黔西南布依族苗族自治州' },
          { value: 'qiandongnan', label: '黔东南苗族侗族自治州' },
          { value: 'qiannan', label: '黔南布依族苗族自治州' }
        ]
      },
      {
        value: 'yunnan',
        label: '云南省',
        children: [
          { value: 'kunming', label: '昆明市' },
          { value: 'qujing', label: '曲靖市' },
          { value: 'yuxi', label: '玉溪市' },
          { value: 'baoshan_yn', label: '保山市' },
          { value: 'zhaotong', label: '昭通市' },
          { value: 'lijiang', label: '丽江市' },
          { value: 'puer', label: '普洱市' },
          { value: 'lincang', label: '临沧市' },
          { value: 'chuxiong', label: '楚雄彝族自治州' },
          { value: 'honghe', label: '红河哈尼族彝族自治州' },
          { value: 'wenshan', label: '文山壮族苗族自治州' },
          { value: 'xishuangbanna', label: '西双版纳傣族自治州' },
          { value: 'dali', label: '大理白族自治州' },
          { value: 'dehong', label: '德宏傣族景颇族自治州' },
          { value: 'nujiang', label: '怒江傈僳族自治州' },
          { value: 'diqing', label: '迪庆藏族自治州' }
        ]
      },
      {
        value: 'xizang',
        label: '西藏自治区',
        children: [
          { value: 'lhasa', label: '拉萨市' },
          { value: 'rikaze', label: '日喀则市' },
          { value: 'changdu', label: '昌都市' },
          { value: 'linzhi', label: '林芝市' },
          { value: 'shannan', label: '山南市' },
          { value: 'naqu', label: '那曲市' },
          { value: 'ali', label: '阿里地区' }
        ]
      },
      // 西北地区
      {
        value: 'shaanxi',
        label: '陕西省',
        children: [
          { value: 'xian', label: '西安市' },
          { value: 'tongchuan', label: '铜川市' },
          { value: 'baoji', label: '宝鸡市' },
          { value: 'xianyang', label: '咸阳市' },
          { value: 'weinan', label: '渭南市' },
          { value: 'yanan', label: '延安市' },
          { value: 'hanzhong', label: '汉中市' },
          { value: 'yulin_sx', label: '榆林市' },
          { value: 'ankang', label: '安康市' },
          { value: 'shangluo', label: '商洛市' }
        ]
      },
      {
        value: 'gansu',
        label: '甘肃省',
        children: [
          { value: 'lanzhou', label: '兰州市' },
          { value: 'jiayuguan', label: '嘉峪关市' },
          { value: 'jinchang', label: '金昌市' },
          { value: 'baiyin', label: '白银市' },
          { value: 'tianshui', label: '天水市' },
          { value: 'wuwei', label: '武威市' },
          { value: 'zhangye', label: '张掖市' },
          { value: 'pingliang', label: '平凉市' },
          { value: 'jiuquan', label: '酒泉市' },
          { value: 'qingyang_gs', label: '庆阳市' },
          { value: 'dingxi', label: '定西市' },
          { value: 'longnan', label: '陇南市' },
          { value: 'linxia', label: '临夏回族自治州' },
          { value: 'gannan', label: '甘南藏族自治州' }
        ]
      },
      {
        value: 'qinghai',
        label: '青海省',
        children: [
          { value: 'xining', label: '西宁市' },
          { value: 'haidong', label: '海东市' },
          { value: 'haibei', label: '海北藏族自治州' },
          { value: 'huangnan', label: '黄南藏族自治州' },
          { value: 'hainan_qh', label: '海南藏族自治州' },
          { value: 'golog', label: '果洛藏族自治州' },
          { value: 'yushu', label: '玉树藏族自治州' },
          { value: 'haixi', label: '海西蒙古族藏族自治州' }
        ]
      },
      {
        value: 'ningxia',
        label: '宁夏回族自治区',
        children: [
          { value: 'yinchuan', label: '银川市' },
          { value: 'shizuishan', label: '石嘴山市' },
          { value: 'wuzhong_nx', label: '吴忠市' },
          { value: 'guyuan', label: '固原市' },
          { value: 'zhongwei', label: '中卫市' }
        ]
      },
      {
        value: 'xinjiang',
        label: '新疆维吾尔自治区',
        children: [
          { value: 'urumqi', label: '乌鲁木齐市' },
          { value: 'karamay', label: '克拉玛依市' },
          { value: 'turpan', label: '吐鲁番市' },
          { value: 'hami', label: '哈密市' },
          { value: 'changji', label: '昌吉回族自治州' },
          { value: 'bortala', label: '博尔塔拉蒙古自治州' },
          { value: 'bayingolin', label: '巴音郭楞蒙古自治州' },
          { value: 'aksu', label: '阿克苏地区' },
          { value: 'kizilsu', label: '克孜勒苏柯尔克孜自治州' },
          { value: 'kashgar', label: '喀什地区' },
          { value: 'hotan', label: '和田地区' },
          { value: 'ili', label: '伊犁哈萨克自治州' },
          { value: 'tacheng', label: '塔城地区' },
          { value: 'altay', label: '阿勒泰地区' }
        ]
      },
      // 东北地区
      {
        value: 'liaoning',
        label: '辽宁省',
        children: [
          { value: 'shenyang', label: '沈阳市' },
          { value: 'dalian', label: '大连市' },
          { value: 'anshan', label: '鞍山市' },
          { value: 'fushun', label: '抚顺市' },
          { value: 'benxi', label: '本溪市' },
          { value: 'dandong', label: '丹东市' },
          { value: 'jinzhou', label: '锦州市' },
          { value: 'yingkou', label: '营口市' },
          { value: 'fuxin', label: '阜新市' },
          { value: 'liaoyang', label: '辽阳市' },
          { value: 'panjin', label: '盘锦市' },
          { value: 'tieling', label: '铁岭市' },
          { value: 'chaoyang_ln', label: '朝阳市' },
          { value: 'huludao', label: '葫芦岛市' }
        ]
      },
      {
        value: 'jilin',
        label: '吉林省',
        children: [
          { value: 'changchun', label: '长春市' },
          { value: 'jilin_city', label: '吉林市' },
          { value: 'siping', label: '四平市' },
          { value: 'liaoyuan', label: '辽源市' },
          { value: 'tonghua', label: '通化市' },
          { value: 'baishan', label: '白山市' },
          { value: 'songyuan', label: '松原市' },
          { value: 'baicheng', label: '白城市' },
          { value: 'yanbian', label: '延边朝鲜族自治州' }
        ]
      },
      {
        value: 'heilongjiang',
        label: '黑龙江省',
        children: [
          { value: 'harbin', label: '哈尔滨市' },
          { value: 'qiqihar', label: '齐齐哈尔市' },
          { value: 'jixi', label: '鸡西市' },
          { value: 'hegang', label: '鹤岗市' },
          { value: 'shuangyashan', label: '双鸭山市' },
          { value: 'daqing', label: '大庆市' },
          { value: 'yichun_hlj', label: '伊春市' },
          { value: 'jiamusi', label: '佳木斯市' },
          { value: 'qitaihe', label: '七台河市' },
          { value: 'mudanjiang', label: '牡丹江市' },
          { value: 'heihe', label: '黑河市' },
          { value: 'suihua', label: '绥化市' },
          { value: 'daxinganling', label: '大兴安岭地区' }
        ]
      },
      // 特别行政区
      {
        value: 'hongkong',
        label: '香港特别行政区',
        children: [
          { value: 'central_western', label: '中西区' },
          { value: 'wan_chai', label: '湾仔区' },
          { value: 'eastern', label: '东区' },
          { value: 'southern', label: '南区' },
          { value: 'yau_tsim_mong', label: '油尖旺区' },
          { value: 'sham_shui_po', label: '深水埗区' },
          { value: 'kowloon_city', label: '九龙城区' },
          { value: 'wong_tai_sin', label: '黄大仙区' },
          { value: 'kwun_tong', label: '观塘区' },
          { value: 'tsuen_wan', label: '荃湾区' },
          { value: 'tuen_mun', label: '屯门区' },
          { value: 'yuen_long', label: '元朗区' },
          { value: 'north', label: '北区' },
          { value: 'tai_po', label: '大埔区' },
          { value: 'sha_tin', label: '沙田区' },
          { value: 'sai_kung', label: '西贡区' },
          { value: 'kwai_tsing', label: '葵青区' },
          { value: 'islands', label: '离岛区' }
        ]
      },
      {
        value: 'macau',
        label: '澳门特别行政区',
        children: [
          { value: 'macau_peninsula', label: '澳门半岛' },
          { value: 'taipa', label: '氹仔' },
          { value: 'coloane', label: '路环' }
        ]
      },
      {
        value: 'taiwan',
        label: '台湾省',
        children: [
          { value: 'taipei', label: '台北市' },
          { value: 'new_taipei', label: '新北市' },
          { value: 'taoyuan', label: '桃园市' },
          { value: 'taichung', label: '台中市' },
          { value: 'tainan', label: '台南市' },
          { value: 'kaohsiung', label: '高雄市' },
          { value: 'keelung', label: '基隆市' },
          { value: 'hsinchu_city', label: '新竹市' },
          { value: 'chiayi_city', label: '嘉义市' },
          { value: 'hsinchu_county', label: '新竹县' },
          { value: 'miaoli', label: '苗栗县' },
          { value: 'changhua', label: '彰化县' },
          { value: 'nantou', label: '南投县' },
          { value: 'yunlin', label: '云林县' },
          { value: 'chiayi_county', label: '嘉义县' },
          { value: 'pingtung', label: '屏东县' },
          { value: 'yilan', label: '宜兰县' },
          { value: 'hualien', label: '花莲县' },
          { value: 'taitung', label: '台东县' },
          { value: 'penghu', label: '澎湖县' },
          { value: 'kinmen', label: '金门县' },
          { value: 'lienchiang', label: '连江县' }
        ]
      }
    ]
  },
  {
    value: 'usa',
    label: '美国',
    children: [
      {
        value: 'california',
        label: '加利福尼亚州',
        children: [
          { value: 'los_angeles', label: '洛杉矶' },
          { value: 'san_francisco', label: '旧金山' },
          { value: 'san_diego', label: '圣地亚哥' },
          { value: 'sacramento', label: '萨克拉门托' },
          { value: 'san_jose', label: '圣何塞' },
          { value: 'fresno', label: '弗雷斯诺' }
        ]
      },
      {
        value: 'new_york',
        label: '纽约州',
        children: [
          { value: 'new_york_city', label: '纽约市' },
          { value: 'albany', label: '奥尔巴尼' },
          { value: 'buffalo', label: '布法罗' },
          { value: 'rochester', label: '罗切斯特' }
        ]
      },
      {
        value: 'texas',
        label: '德克萨斯州',
        children: [
          { value: 'houston', label: '休斯顿' },
          { value: 'dallas', label: '达拉斯' },
          { value: 'austin', label: '奥斯汀' },
          { value: 'san_antonio', label: '圣安东尼奥' }
        ]
      }
    ]
  },
  {
    value: 'japan',
    label: '日本',
    children: [
      {
        value: 'tokyo',
        label: '东京都',
        children: [
          { value: 'shibuya', label: '涩谷区' },
          { value: 'shinjuku', label: '新宿区' },
          { value: 'minato', label: '港区' },
          { value: 'chiyoda', label: '千代田区' },
          { value: 'chuo', label: '中央区' },
          { value: 'taito', label: '台东区' }
        ]
      },
      {
        value: 'osaka',
        label: '大阪府',
        children: [
          { value: 'osaka_city', label: '大阪市' },
          { value: 'sakai', label: '堺市' },
          { value: 'higashiosaka', label: '东大阪市' }
        ]
      },
      {
        value: 'kyoto',
        label: '京都府',
        children: [
          { value: 'kyoto_city', label: '京都市' },
          { value: 'uji', label: '宇治市' }
        ]
      }
    ]
  },
  {
    value: 'uk',
    label: '英国',
    children: [
      {
        value: 'england',
        label: '英格兰',
        children: [
          { value: 'london', label: '伦敦' },
          { value: 'manchester', label: '曼彻斯特' },
          { value: 'birmingham', label: '伯明翰' },
          { value: 'liverpool', label: '利物浦' },
          { value: 'leeds', label: '利兹' },
          { value: 'sheffield', label: '谢菲尔德' }
        ]
      },
      {
        value: 'scotland',
        label: '苏格兰',
        children: [
          { value: 'edinburgh', label: '爱丁堡' },
          { value: 'glasgow', label: '格拉斯哥' },
          { value: 'aberdeen', label: '阿伯丁' }
        ]
      },
      {
        value: 'wales',
        label: '威尔士',
        children: [
          { value: 'cardiff', label: '卡迪夫' },
          { value: 'swansea', label: '斯旺西' }
        ]
      }
    ]
  },
  {
    value: 'canada',
    label: '加拿大',
    children: [
      {
        value: 'ontario',
        label: '安大略省',
        children: [
          { value: 'toronto', label: '多伦多' },
          { value: 'ottawa', label: '渥太华' },
          { value: 'hamilton', label: '汉密尔顿' }
        ]
      },
      {
        value: 'quebec',
        label: '魁北克省',
        children: [
          { value: 'montreal', label: '蒙特利尔' },
          { value: 'quebec_city', label: '魁北克市' }
        ]
      },
      {
        value: 'british_columbia',
        label: '不列颠哥伦比亚省',
        children: [
          { value: 'vancouver', label: '温哥华' },
          { value: 'victoria', label: '维多利亚' }
        ]
      }
    ]
  }
]
