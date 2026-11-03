
/* RmcDcCommonReqHandler::handleQualifiedNetworkTypeChanged_V2(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcDcCommonReqHandler::handleQualifiedNetworkTypeChanged_V2(RmcDcCommonReqHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  bool bVar3;
  bool bVar4;
  int iVar5;
  int iVar6;
  int iVar7;
  int iVar8;
  RfxAtLine *this_00;
  ulong uVar9;
  char *pcVar10;
  size_t sVar11;
  char *pcVar12;
  int iVar13;
  int iVar14;
  long *local_920;
  long *local_918;
  int local_90c;
  undefined8 local_908 [3];
  int local_8f0;
  int local_2cc;
  int local_288 [6];
  char local_270 [504];
  ulong uStack_78;
  long local_70;
  
  lVar2 = tpidr_el0;
  local_70 = *(long *)(lVar2 + 0x28);
  local_90c = 0;
  uVar1 = *(undefined4 *)(this + 8);
  pcVar12 = *(char **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  this_00 = operator_new(0x18);
  RfxAtLine::RfxAtLine(this_00,pcVar12,(RfxAtLine *)0x0);
  uVar9 = __rfx_is_gt_mode();
  if ((uVar9 & 1) == 0) {
    mtkLogI("RmcDcCommon","[%d][%s] urc=%s",uVar1,"handleQualifiedNetworkTypeChanged_V2",pcVar12);
  }
  else {
    local_908[0] = 0;
    android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
    mtkLogI(local_908[0],"[%d][%s] urc=%s",uVar1,"handleQualifiedNetworkTypeChanged_V2",pcVar12);
    android::String8::~String8((String8 *)local_908);
  }
  RfxAtLine::atTokStart(this_00,&local_90c);
  if (local_90c < 0) goto LAB_00511128;
  pcVar12 = (char *)RfxAtLine::atTokNextstr(this_00,&local_90c);
  if (local_90c < 0) {
    uVar9 = __rfx_is_gt_mode();
    if ((uVar9 & 1) == 0) {
      mtkLogE("RmcDcCommon","[%d][%s] ERROR occurs when parsing cmd",uVar1,
              "handleQualifiedNetworkTypeChanged_V2");
    }
    else {
      local_908[0] = 0;
      android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
      mtkLogE(local_908[0],"[%d][%s] ERROR occurs when parsing cmd",uVar1,
              "handleQualifiedNetworkTypeChanged_V2");
      android::String8::~String8((String8 *)local_908);
    }
    goto LAB_00511128;
  }
  local_918 = (long *)0x0;
  iVar5 = getPdnTableSize(this);
  iVar6 = strncmp("setup",pcVar12,5);
  if (iVar6 == 0) {
    uStack_78 = 0;
    local_270[0x1f0] = '\0';
    local_270[0x1f1] = '\0';
    local_270[0x1f2] = '\0';
    local_270[499] = '\0';
    local_270[500] = '\0';
    local_270[0x1f5] = '\0';
    local_270[0x1f6] = '\0';
    local_270[0x1f7] = '\0';
    local_270[0x1e8] = '\0';
    local_270[0x1e9] = '\0';
    local_270[0x1ea] = '\0';
    local_270[0x1eb] = '\0';
    local_270[0x1ec] = '\0';
    local_270[0x1ed] = '\0';
    local_270[0x1ee] = '\0';
    local_270[0x1ef] = '\0';
    local_270[0x1e0] = '\0';
    local_270[0x1e1] = '\0';
    local_270[0x1e2] = '\0';
    local_270[0x1e3] = '\0';
    local_270[0x1e4] = '\0';
    local_270[0x1e5] = '\0';
    local_270[0x1e6] = '\0';
    local_270[0x1e7] = '\0';
    local_270[0x1d8] = '\0';
    local_270[0x1d9] = '\0';
    local_270[0x1da] = '\0';
    local_270[0x1db] = '\0';
    local_270[0x1dc] = '\0';
    local_270[0x1dd] = '\0';
    local_270[0x1de] = '\0';
    local_270[0x1df] = '\0';
    local_270[0x1d0] = '\0';
    local_270[0x1d1] = '\0';
    local_270[0x1d2] = '\0';
    local_270[0x1d3] = '\0';
    local_270[0x1d4] = '\0';
    local_270[0x1d5] = '\0';
    local_270[0x1d6] = '\0';
    local_270[0x1d7] = '\0';
    local_270[0x1c8] = '\0';
    local_270[0x1c9] = '\0';
    local_270[0x1ca] = '\0';
    local_270[0x1cb] = '\0';
    local_270[0x1cc] = '\0';
    local_270[0x1cd] = '\0';
    local_270[0x1ce] = '\0';
    local_270[0x1cf] = '\0';
    local_270[0x1c0] = '\0';
    local_270[0x1c1] = '\0';
    local_270[0x1c2] = '\0';
    local_270[0x1c3] = '\0';
    local_270[0x1c4] = '\0';
    local_270[0x1c5] = '\0';
    local_270[0x1c6] = '\0';
    local_270[0x1c7] = '\0';
    local_270[0x1b8] = '\0';
    local_270[0x1b9] = '\0';
    local_270[0x1ba] = '\0';
    local_270[0x1bb] = '\0';
    local_270[0x1bc] = '\0';
    local_270[0x1bd] = '\0';
    local_270[0x1be] = '\0';
    local_270[0x1bf] = '\0';
    local_270[0x1b0] = '\0';
    local_270[0x1b1] = '\0';
    local_270[0x1b2] = '\0';
    local_270[0x1b3] = '\0';
    local_270[0x1b4] = '\0';
    local_270[0x1b5] = '\0';
    local_270[0x1b6] = '\0';
    local_270[0x1b7] = '\0';
    local_270[0x1a8] = '\0';
    local_270[0x1a9] = '\0';
    local_270[0x1aa] = '\0';
    local_270[0x1ab] = '\0';
    local_270[0x1ac] = '\0';
    local_270[0x1ad] = '\0';
    local_270[0x1ae] = '\0';
    local_270[0x1af] = '\0';
    local_270[0x1a0] = '\0';
    local_270[0x1a1] = '\0';
    local_270[0x1a2] = '\0';
    local_270[0x1a3] = '\0';
    local_270[0x1a4] = '\0';
    local_270[0x1a5] = '\0';
    local_270[0x1a6] = '\0';
    local_270[0x1a7] = '\0';
    local_270[0x198] = '\0';
    local_270[0x199] = '\0';
    local_270[0x19a] = '\0';
    local_270[0x19b] = '\0';
    local_270[0x19c] = '\0';
    local_270[0x19d] = '\0';
    local_270[0x19e] = '\0';
    local_270[0x19f] = '\0';
    local_270[400] = '\0';
    local_270[0x191] = '\0';
    local_270[0x192] = '\0';
    local_270[0x193] = '\0';
    local_270[0x194] = '\0';
    local_270[0x195] = '\0';
    local_270[0x196] = '\0';
    local_270[0x197] = '\0';
    local_270[0x188] = '\0';
    local_270[0x189] = '\0';
    local_270[0x18a] = '\0';
    local_270[0x18b] = '\0';
    local_270[0x18c] = '\0';
    local_270[0x18d] = '\0';
    local_270[0x18e] = '\0';
    local_270[399] = '\0';
    local_270[0x180] = '\0';
    local_270[0x181] = '\0';
    local_270[0x182] = '\0';
    local_270[0x183] = '\0';
    local_270[0x184] = '\0';
    local_270[0x185] = '\0';
    local_270[0x186] = '\0';
    local_270[0x187] = '\0';
    local_270[0x178] = '\0';
    local_270[0x179] = '\0';
    local_270[0x17a] = '\0';
    local_270[0x17b] = '\0';
    local_270[0x17c] = '\0';
    local_270[0x17d] = '\0';
    local_270[0x17e] = '\0';
    local_270[0x17f] = '\0';
    local_270[0x170] = '\0';
    local_270[0x171] = '\0';
    local_270[0x172] = '\0';
    local_270[0x173] = '\0';
    local_270[0x174] = '\0';
    local_270[0x175] = '\0';
    local_270[0x176] = '\0';
    local_270[0x177] = '\0';
    local_270[0x168] = '\0';
    local_270[0x169] = '\0';
    local_270[0x16a] = '\0';
    local_270[0x16b] = '\0';
    local_270[0x16c] = '\0';
    local_270[0x16d] = '\0';
    local_270[0x16e] = '\0';
    local_270[0x16f] = '\0';
    local_270[0x160] = '\0';
    local_270[0x161] = '\0';
    local_270[0x162] = '\0';
    local_270[0x163] = '\0';
    local_270[0x164] = '\0';
    local_270[0x165] = '\0';
    local_270[0x166] = '\0';
    local_270[0x167] = '\0';
    local_270[0x158] = '\0';
    local_270[0x159] = '\0';
    local_270[0x15a] = '\0';
    local_270[0x15b] = '\0';
    local_270[0x15c] = '\0';
    local_270[0x15d] = '\0';
    local_270[0x15e] = '\0';
    local_270[0x15f] = '\0';
    local_270[0x150] = '\0';
    local_270[0x151] = '\0';
    local_270[0x152] = '\0';
    local_270[0x153] = '\0';
    local_270[0x154] = '\0';
    local_270[0x155] = '\0';
    local_270[0x156] = '\0';
    local_270[0x157] = '\0';
    local_270[0x148] = '\0';
    local_270[0x149] = '\0';
    local_270[0x14a] = '\0';
    local_270[0x14b] = '\0';
    local_270[0x14c] = '\0';
    local_270[0x14d] = '\0';
    local_270[0x14e] = '\0';
    local_270[0x14f] = '\0';
    local_270[0x140] = '\0';
    local_270[0x141] = '\0';
    local_270[0x142] = '\0';
    local_270[0x143] = '\0';
    local_270[0x144] = '\0';
    local_270[0x145] = '\0';
    local_270[0x146] = '\0';
    local_270[0x147] = '\0';
    local_270[0x138] = '\0';
    local_270[0x139] = '\0';
    local_270[0x13a] = '\0';
    local_270[0x13b] = '\0';
    local_270[0x13c] = '\0';
    local_270[0x13d] = '\0';
    local_270[0x13e] = '\0';
    local_270[0x13f] = '\0';
    local_270[0x130] = '\0';
    local_270[0x131] = '\0';
    local_270[0x132] = '\0';
    local_270[0x133] = '\0';
    local_270[0x134] = '\0';
    local_270[0x135] = '\0';
    local_270[0x136] = '\0';
    local_270[0x137] = '\0';
    local_270[0x128] = '\0';
    local_270[0x129] = '\0';
    local_270[0x12a] = '\0';
    local_270[299] = '\0';
    local_270[300] = '\0';
    local_270[0x12d] = '\0';
    local_270[0x12e] = '\0';
    local_270[0x12f] = '\0';
    local_270[0x120] = '\0';
    local_270[0x121] = '\0';
    local_270[0x122] = '\0';
    local_270[0x123] = '\0';
    local_270[0x124] = '\0';
    local_270[0x125] = '\0';
    local_270[0x126] = '\0';
    local_270[0x127] = '\0';
    local_270[0x118] = '\0';
    local_270[0x119] = '\0';
    local_270[0x11a] = '\0';
    local_270[0x11b] = '\0';
    local_270[0x11c] = '\0';
    local_270[0x11d] = '\0';
    local_270[0x11e] = '\0';
    local_270[0x11f] = '\0';
    local_270[0x110] = '\0';
    local_270[0x111] = '\0';
    local_270[0x112] = '\0';
    local_270[0x113] = '\0';
    local_270[0x114] = '\0';
    local_270[0x115] = '\0';
    local_270[0x116] = '\0';
    local_270[0x117] = '\0';
    local_270[0x108] = '\0';
    local_270[0x109] = '\0';
    local_270[0x10a] = '\0';
    local_270[0x10b] = '\0';
    local_270[0x10c] = '\0';
    local_270[0x10d] = '\0';
    local_270[0x10e] = '\0';
    local_270[0x10f] = '\0';
    local_270[0x100] = '\0';
    local_270[0x101] = '\0';
    local_270[0x102] = '\0';
    local_270[0x103] = '\0';
    local_270[0x104] = '\0';
    local_270[0x105] = '\0';
    local_270[0x106] = '\0';
    local_270[0x107] = '\0';
    local_270[0xf8] = '\0';
    local_270[0xf9] = '\0';
    local_270[0xfa] = '\0';
    local_270[0xfb] = '\0';
    local_270[0xfc] = '\0';
    local_270[0xfd] = '\0';
    local_270[0xfe] = '\0';
    local_270[0xff] = '\0';
    local_270[0xf0] = '\0';
    local_270[0xf1] = '\0';
    local_270[0xf2] = '\0';
    local_270[0xf3] = '\0';
    local_270[0xf4] = '\0';
    local_270[0xf5] = '\0';
    local_270[0xf6] = '\0';
    local_270[0xf7] = '\0';
    local_270[0xe8] = '\0';
    local_270[0xe9] = '\0';
    local_270[0xea] = '\0';
    local_270[0xeb] = '\0';
    local_270[0xec] = '\0';
    local_270[0xed] = '\0';
    local_270[0xee] = '\0';
    local_270[0xef] = '\0';
    local_270[0xe0] = '\0';
    local_270[0xe1] = '\0';
    local_270[0xe2] = '\0';
    local_270[0xe3] = '\0';
    local_270[0xe4] = '\0';
    local_270[0xe5] = '\0';
    local_270[0xe6] = '\0';
    local_270[0xe7] = '\0';
    local_270[0xd8] = '\0';
    local_270[0xd9] = '\0';
    local_270[0xda] = '\0';
    local_270[0xdb] = '\0';
    local_270[0xdc] = '\0';
    local_270[0xdd] = '\0';
    local_270[0xde] = '\0';
    local_270[0xdf] = '\0';
    local_270[0xd0] = '\0';
    local_270[0xd1] = '\0';
    local_270[0xd2] = '\0';
    local_270[0xd3] = '\0';
    local_270[0xd4] = '\0';
    local_270[0xd5] = '\0';
    local_270[0xd6] = '\0';
    local_270[0xd7] = '\0';
    local_270[200] = '\0';
    local_270[0xc9] = '\0';
    local_270[0xca] = '\0';
    local_270[0xcb] = '\0';
    local_270[0xcc] = '\0';
    local_270[0xcd] = '\0';
    local_270[0xce] = '\0';
    local_270[0xcf] = '\0';
    local_270[0xc0] = '\0';
    local_270[0xc1] = '\0';
    local_270[0xc2] = '\0';
    local_270[0xc3] = '\0';
    local_270[0xc4] = '\0';
    local_270[0xc5] = '\0';
    local_270[0xc6] = '\0';
    local_270[199] = '\0';
    local_270[0xb8] = '\0';
    local_270[0xb9] = '\0';
    local_270[0xba] = '\0';
    local_270[0xbb] = '\0';
    local_270[0xbc] = '\0';
    local_270[0xbd] = '\0';
    local_270[0xbe] = '\0';
    local_270[0xbf] = '\0';
    local_270[0xb0] = '\0';
    local_270[0xb1] = '\0';
    local_270[0xb2] = '\0';
    local_270[0xb3] = '\0';
    local_270[0xb4] = '\0';
    local_270[0xb5] = '\0';
    local_270[0xb6] = '\0';
    local_270[0xb7] = '\0';
    local_270[0xa8] = '\0';
    local_270[0xa9] = '\0';
    local_270[0xaa] = '\0';
    local_270[0xab] = '\0';
    local_270[0xac] = '\0';
    local_270[0xad] = '\0';
    local_270[0xae] = '\0';
    local_270[0xaf] = '\0';
    local_270[0xa0] = '\0';
    local_270[0xa1] = '\0';
    local_270[0xa2] = '\0';
    local_270[0xa3] = '\0';
    local_270[0xa4] = '\0';
    local_270[0xa5] = '\0';
    local_270[0xa6] = '\0';
    local_270[0xa7] = '\0';
    local_270[0x98] = '\0';
    local_270[0x99] = '\0';
    local_270[0x9a] = '\0';
    local_270[0x9b] = '\0';
    local_270[0x9c] = '\0';
    local_270[0x9d] = '\0';
    local_270[0x9e] = '\0';
    local_270[0x9f] = '\0';
    local_270[0x90] = '\0';
    local_270[0x91] = '\0';
    local_270[0x92] = '\0';
    local_270[0x93] = '\0';
    local_270[0x94] = '\0';
    local_270[0x95] = '\0';
    local_270[0x96] = '\0';
    local_270[0x97] = '\0';
    local_270[0x88] = '\0';
    local_270[0x89] = '\0';
    local_270[0x8a] = '\0';
    local_270[0x8b] = '\0';
    local_270[0x8c] = '\0';
    local_270[0x8d] = '\0';
    local_270[0x8e] = '\0';
    local_270[0x8f] = '\0';
    local_270[0x80] = '\0';
    local_270[0x81] = '\0';
    local_270[0x82] = '\0';
    local_270[0x83] = '\0';
    local_270[0x84] = '\0';
    local_270[0x85] = '\0';
    local_270[0x86] = '\0';
    local_270[0x87] = '\0';
    local_270[0x78] = '\0';
    local_270[0x79] = '\0';
    local_270[0x7a] = '\0';
    local_270[0x7b] = '\0';
    local_270[0x7c] = '\0';
    local_270[0x7d] = '\0';
    local_270[0x7e] = '\0';
    local_270[0x7f] = '\0';
    local_270[0x70] = '\0';
    local_270[0x71] = '\0';
    local_270[0x72] = '\0';
    local_270[0x73] = '\0';
    local_270[0x74] = '\0';
    local_270[0x75] = '\0';
    local_270[0x76] = '\0';
    local_270[0x77] = '\0';
    local_270[0x68] = '\0';
    local_270[0x69] = '\0';
    local_270[0x6a] = '\0';
    local_270[0x6b] = '\0';
    local_270[0x6c] = '\0';
    local_270[0x6d] = '\0';
    local_270[0x6e] = '\0';
    local_270[0x6f] = '\0';
    local_270[0x60] = '\0';
    local_270[0x61] = '\0';
    local_270[0x62] = '\0';
    local_270[99] = '\0';
    local_270[100] = '\0';
    local_270[0x65] = '\0';
    local_270[0x66] = '\0';
    local_270[0x67] = '\0';
    local_270[0x58] = '\0';
    local_270[0x59] = '\0';
    local_270[0x5a] = '\0';
    local_270[0x5b] = '\0';
    local_270[0x5c] = '\0';
    local_270[0x5d] = '\0';
    local_270[0x5e] = '\0';
    local_270[0x5f] = '\0';
    local_270[0x50] = '\0';
    local_270[0x51] = '\0';
    local_270[0x52] = '\0';
    local_270[0x53] = '\0';
    local_270[0x54] = '\0';
    local_270[0x55] = '\0';
    local_270[0x56] = '\0';
    local_270[0x57] = '\0';
    local_270[0x48] = '\0';
    local_270[0x49] = '\0';
    local_270[0x4a] = '\0';
    local_270[0x4b] = '\0';
    local_270[0x4c] = '\0';
    local_270[0x4d] = '\0';
    local_270[0x4e] = '\0';
    local_270[0x4f] = '\0';
    local_270[0x40] = '\0';
    local_270[0x41] = '\0';
    local_270[0x42] = '\0';
    local_270[0x43] = '\0';
    local_270[0x44] = '\0';
    local_270[0x45] = '\0';
    local_270[0x46] = '\0';
    local_270[0x47] = '\0';
    local_270[0x38] = '\0';
    local_270[0x39] = '\0';
    local_270[0x3a] = '\0';
    local_270[0x3b] = '\0';
    local_270[0x3c] = '\0';
    local_270[0x3d] = '\0';
    local_270[0x3e] = '\0';
    local_270[0x3f] = '\0';
    local_270[0x30] = '\0';
    local_270[0x31] = '\0';
    local_270[0x32] = '\0';
    local_270[0x33] = '\0';
    local_270[0x34] = '\0';
    local_270[0x35] = '\0';
    local_270[0x36] = '\0';
    local_270[0x37] = '\0';
    local_270[0x28] = '\0';
    local_270[0x29] = '\0';
    local_270[0x2a] = '\0';
    local_270[0x2b] = '\0';
    local_270[0x2c] = '\0';
    local_270[0x2d] = '\0';
    local_270[0x2e] = '\0';
    local_270[0x2f] = '\0';
    local_270[0x20] = '\0';
    local_270[0x21] = '\0';
    local_270[0x22] = '\0';
    local_270[0x23] = '\0';
    local_270[0x24] = '\0';
    local_270[0x25] = '\0';
    local_270[0x26] = '\0';
    local_270[0x27] = '\0';
    local_270[0x18] = '\0';
    local_270[0x19] = '\0';
    local_270[0x1a] = '\0';
    local_270[0x1b] = '\0';
    local_270[0x1c] = '\0';
    local_270[0x1d] = '\0';
    local_270[0x1e] = '\0';
    local_270[0x1f] = '\0';
    local_270[0x10] = '\0';
    local_270[0x11] = '\0';
    local_270[0x12] = '\0';
    local_270[0x13] = '\0';
    local_270[0x14] = '\0';
    local_270[0x15] = '\0';
    local_270[0x16] = '\0';
    local_270[0x17] = '\0';
    local_270[8] = '\0';
    local_270[9] = '\0';
    local_270[10] = '\0';
    local_270[0xb] = '\0';
    local_270[0xc] = '\0';
    local_270[0xd] = '\0';
    local_270[0xe] = '\0';
    local_270[0xf] = '\0';
    local_270[0] = '\0';
    local_270[1] = '\0';
    local_270[2] = '\0';
    local_270[3] = '\0';
    local_270[4] = '\0';
    local_270[5] = '\0';
    local_270[6] = '\0';
    local_270[7] = '\0';
    pcVar12 = (char *)RfxAtLine::atTokNextstr(this_00,&local_90c);
    iVar6 = local_90c;
    uVar9 = __rfx_is_gt_mode();
    if (iVar6 < 0) {
      if ((uVar9 & 1) != 0) {
        local_908[0] = 0;
        android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
        pcVar12 = "[%d][%s] ERROR occurs when parsing type";
        goto LAB_00510f64;
      }
      pcVar12 = "[%d][%s] ERROR occurs when parsing type";
    }
    else {
      if ((uVar9 & 1) == 0) {
        mtkLogI("RmcDcCommon","[%d][%s] type=%s",uVar1,"handleQualifiedNetworkTypeChanged_V2",
                pcVar12);
      }
      else {
        local_908[0] = 0;
        android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
        mtkLogI(local_908[0],"[%d][%s] type=%s",uVar1,"handleQualifiedNetworkTypeChanged_V2",pcVar12
               );
        android::String8::~String8((String8 *)local_908);
      }
      iVar13 = 2;
      pcVar10 = (char *)RmcDcUtility::getApnType(*(int *)(this + 8),2);
      strncpy(local_270,pcVar10,0x1ff);
      uStack_78 = uStack_78 & 0xffffffffffffff;
      sVar11 = __strlen_chk(local_270,0x200);
      iVar6 = strncmp(local_270,pcVar12,sVar11);
      if (iVar6 != 0) {
        iVar13 = 0x40;
        pcVar10 = (char *)RmcDcUtility::getApnType(*(int *)(this + 8),0x40);
        strncpy(local_270,pcVar10,0x1ff);
        uStack_78 = uStack_78 & 0xffffffffffffff;
        sVar11 = __strlen_chk(local_270,0x200);
        iVar6 = strncmp(local_270,pcVar12,sVar11);
        if (iVar6 != 0) {
          iVar13 = 0x200;
          pcVar10 = (char *)RmcDcUtility::getApnType(*(int *)(this + 8),0x200);
          strncpy(local_270,pcVar10,0x1ff);
          uStack_78 = uStack_78 & 0xffffffffffffff;
          sVar11 = __strlen_chk(local_270,0x200);
          iVar6 = strncmp(local_270,pcVar12,sVar11);
          if (iVar6 != 0) {
            uVar9 = __rfx_is_gt_mode();
            if ((uVar9 & 1) != 0) {
              local_908[0] = 0;
              android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
              pcVar12 = "[%d][%s] no match APN type.";
              goto LAB_00510f64;
            }
            pcVar12 = "[%d][%s] no match APN type.";
            goto LAB_005110d8;
          }
        }
      }
      uVar9 = __rfx_is_gt_mode();
      if ((uVar9 & 1) == 0) {
        pcVar12 = "handleQualifiedNetworkTypeChanged_V2";
        mtkLogI("RmcDcCommon","[%d][%s] matchApnType=%d",uVar1,
                "handleQualifiedNetworkTypeChanged_V2",iVar13);
      }
      else {
        local_908[0] = 0;
        android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
        pcVar12 = "handleQualifiedNetworkTypeChanged_V2";
        mtkLogI(local_908[0],"[%d][%s] matchApnType=%d",uVar1,"handleQualifiedNetworkTypeChanged_V2"
                ,iVar13);
        android::String8::~String8((String8 *)local_908);
      }
      iVar6 = RfxAtLine::atTokNextint(this_00,&local_90c);
      if (-1 < local_90c) {
        local_288[4] = 0;
        local_288[5] = 0;
        local_288[2] = 0;
        local_288[3] = 0;
        if (0 < iVar6) {
          bVar3 = false;
          iVar14 = 2;
          iVar8 = iVar6;
          do {
            iVar7 = RfxAtLine::atTokNextint(this_00,&local_90c);
            if (local_90c < 0) goto LAB_005113cc;
            if (iVar7 - 2U < 5) {
              bVar4 = !bVar3;
              bVar3 = true;
              if (bVar4) {
                iVar7 = 2;
                bVar3 = true;
                goto LAB_00511388;
              }
            }
            else {
LAB_00511388:
              local_288[iVar14] = iVar7;
              iVar14 = iVar14 + 1;
            }
            iVar8 = iVar8 + -1;
          } while (iVar8 != 0);
        }
        if (0 < iVar5) {
          iVar14 = 0;
          do {
            iVar8 = getAid(this,iVar14);
            if (iVar8 != -1) {
              getPdnInfo((int)this);
              iVar7 = local_2cc;
              iVar8 = local_8f0;
              uVar9 = __rfx_is_gt_mode();
              pcVar12 = "handleQualifiedNetworkTypeChanged_V2";
              if ((uVar9 & 1) == 0) {
                mtkLogI("RmcDcCommon","[%d][%s] pdnInfo.profileId=%d, pdn_info.active=%d",uVar1,
                        "handleQualifiedNetworkTypeChanged_V2",iVar7,iVar8);
              }
              else {
                local_908[0] = 0;
                android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
                mtkLogI(local_908[0],"[%d][%s] pdnInfo.profileId=%d, pdn_info.active=%d",uVar1,
                        "handleQualifiedNetworkTypeChanged_V2",iVar7,iVar8);
                android::String8::~String8((String8 *)local_908);
              }
              iVar7 = RmcDcUtility::getApnTypeId(iVar7);
              if ((iVar13 == iVar7) && (iVar8 == 2)) {
                uVar9 = __rfx_is_gt_mode();
                if ((uVar9 & 1) == 0) {
                  mtkLogI("RmcDcCommon","[%d][%s] apn state is active skip to notify",uVar1,
                          "handleQualifiedNetworkTypeChanged_V2");
                  goto LAB_005110e8;
                }
                local_908[0] = 0;
                android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
                mtkLogI(local_908[0],"[%d][%s] apn state is active skip to notify",uVar1,
                        "handleQualifiedNetworkTypeChanged_V2");
                goto LAB_00510f74;
              }
            }
            iVar14 = iVar14 + 1;
          } while (iVar5 != iVar14);
        }
        iVar5 = *(int *)(this + 8);
        local_288[0] = 1;
        local_288[1] = iVar13;
        RfxIntsData::RfxIntsData((RfxIntsData *)local_908,local_288,iVar6 + 2);
        RfxMclMessage::obtainUrc
                  ((RfxMclMessage *)0xcd49,iVar5,(int)local_908,(RfxBaseData *)pcVar12);
        android::sp<RfxMclMessage>::operator=((sp<RfxMclMessage> *)&local_918,(sp *)&local_920);
        if (local_920 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_920 + *(long *)(*local_920 + -0x18)),&local_920);
        }
        RfxIntsData::~RfxIntsData((RfxIntsData *)local_908);
        RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_918);
        goto LAB_005110e8;
      }
LAB_005113cc:
      uVar9 = __rfx_is_gt_mode();
      if ((uVar9 & 1) != 0) {
        local_908[0] = 0;
        android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
        pcVar12 = "[%d][%s] ERROR occurs when parsing priorityListForSetup";
        goto LAB_00510f64;
      }
      pcVar12 = "[%d][%s] ERROR occurs when parsing priorityListForSetup";
    }
LAB_005110d8:
    mtkLogE("RmcDcCommon",pcVar12,uVar1,"handleQualifiedNetworkTypeChanged_V2");
  }
  else {
    uVar9 = __rfx_is_gt_mode();
    if ((uVar9 & 1) == 0) {
      pcVar12 = "[%d][%s] unknown <cmd> for handleQualifiedNetworkTypeChanged_V2!";
      goto LAB_005110d8;
    }
    local_908[0] = 0;
    android::String8::format("%s%s",local_908,&DAT_001fbebf,"RmcDcCommon");
    pcVar12 = "[%d][%s] unknown <cmd> for handleQualifiedNetworkTypeChanged_V2!";
LAB_00510f64:
    mtkLogE(local_908[0],pcVar12,uVar1,"handleQualifiedNetworkTypeChanged_V2");
LAB_00510f74:
    android::String8::~String8((String8 *)local_908);
  }
LAB_005110e8:
  if (local_918 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_918 + *(long *)(*local_918 + -0x18)),&local_918);
  }
LAB_00511128:
  RfxAtLine::~RfxAtLine(this_00);
  operator_delete(this_00);
  if (*(long *)(lVar2 + 0x28) != local_70) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


