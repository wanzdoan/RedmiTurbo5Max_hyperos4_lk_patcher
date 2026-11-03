// File offset entry: 0x8652c

ulong FUN_0208642c(void)

{
  undefined1 in_ZR;
  uint uVar1;
  int iVar2;
  ulong uVar3;
  ulong uVar4;
  undefined8 uVar5;
  undefined1 extraout_w1;
  undefined8 *extraout_x8;
  undefined8 *extraout_x8_00;
  ulong unaff_x19;
  ulong uVar6;
  ulong uVar7;
  undefined1 auVar8 [16];
  unkbyte10 Var9;
  undefined2 auStack_19c [6];
  undefined8 **ppuStack_190;
  undefined8 uStack_188;
  ulong uStack_180;
  ulong uStack_178;
  undefined1 auStack_16c [12];
  undefined8 **ppuStack_160;
  undefined8 uStack_158;
  ulong uStack_150;
  ulong uStack_148;
  ushort auStack_13c [6];
  undefined8 **ppuStack_130;
  undefined8 uStack_128;
  ulong uStack_120;
  ulong uStack_118;
  byte abStack_10c [4];
  undefined8 uStack_108;
  undefined8 **ppuStack_100;
  undefined8 uStack_f8;
  ulong uStack_f0;
  undefined8 uStack_e8;
  byte abStack_dc [4];
  undefined8 uStack_d8;
  undefined8 **ppuStack_d0;
  undefined8 uStack_c8;
  undefined1 **ppuStack_80;
  undefined8 uStack_78;
  ulong uStack_70;
  byte bStack_5c;
  undefined8 uStack_58;
  undefined1 *puStack_50;
  undefined8 uStack_48;
  undefined8 uStack_40;
  ulong uStack_38;
  byte bStack_2c;
  undefined8 uStack_28;
  
  func_0x02087e30();
  uStack_28 = *extraout_x8;
  bStack_2c = 0;
  iVar2 = func_0x02085fcc();
  if (iVar2 == 0) {
    unaff_x19 = (ulong)(1 - bStack_2c);
  }
  else {
    uVar5 = func_0x02087e50();
    FUN_0208ada0(uVar5,0x20c75f9,unaff_x19 & 0xffffffff);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return unaff_x19 & 0xffffffff;
  }
  FUN_02091090();
  uStack_48 = 0x20864a4;
  uStack_40 = 1;
  puStack_50 = &stack0xffffffffffffffe0;
  uStack_38 = unaff_x19;
  func_0x02087e30();
  uStack_58 = *extraout_x8_00;
  bStack_5c = 0;
  iVar2 = func_0x02085fcc();
  if (iVar2 == 0) {
    unaff_x19 = (ulong)(1 - bStack_5c);
  }
  else {
    uVar5 = func_0x02087e50();
    FUN_0208ada0(uVar5,0x20c760e,unaff_x19 & 0xffffffff);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return unaff_x19 & 0xffffffff;
  }
  FUN_02091090();
  uStack_78 = 0x208651c;
  ppuStack_80 = &puStack_50;
  uStack_70 = unaff_x19;
  func_0x02087e30();
  iVar2 = func_0x02085fcc();
  if (iVar2 == 0) {
    unaff_x19 = 0;
    FUN_0208ada0(0x20c8574,0);
  }
  else {
    uVar5 = func_0x02087e50();
    FUN_0208ada0(uVar5,0x20b2d34,unaff_x19 & 0xffffffff);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return unaff_x19 & 0xffffffff;
  }
  FUN_02091090();
  FUN_02086088(0xa0f,0,3,4);
  FUN_02086088(0xa0e,1,1,2);
  uStack_c8 = 0x208659c;
  uStack_d8 = *puRam0000000002103280;
  abStack_dc[0] = 0;
  ppuStack_d0 = &ppuStack_80;
  uVar1 = func_0x02090068(uRam000000000211d8f0,0xa0f,abStack_dc,1);
  uVar6 = (ulong)uVar1;
  if (uVar1 == 0) {
    abStack_dc[0] = abStack_dc[0] & 0x3f | 0x40;
    uVar1 = func_0x020900a0(uRam000000000211d8f0,0xa0f,abStack_dc,1);
    uVar6 = (ulong)uVar1;
    if (uVar1 == 0) goto LAB_02086134;
  }
  uVar5 = func_0x02087e84();
  FUN_0208ada0(uVar5,0x20c019d,0xa0f,uVar6);
LAB_02086134:
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar6;
  }
  uVar3 = FUN_02091090();
  uStack_f8 = 0x2086160;
  uStack_e8 = 0xa0f;
  uVar3 = uVar3 & 0xffffffff;
  uStack_108 = *puRam0000000002103280;
  abStack_10c[0] = 0;
  ppuStack_100 = &ppuStack_d0;
  uStack_f0 = uVar6;
  uVar1 = func_0x02090068(uRam000000000211d8f0,uVar3,abStack_10c,1);
  if (uVar1 == 0) {
    uVar6 = (ulong)abStack_10c[0];
  }
  else {
    uVar6 = (ulong)uVar1;
    uVar5 = func_0x02087e84();
    FUN_0208ada0(uVar5,0x20bbe86,uVar3,uVar6);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar6;
  }
  uVar4 = FUN_02091090();
  uStack_128 = 0x20861f0;
  uVar7 = uVar4 & 0xffffffff;
  ppuStack_130 = &ppuStack_100;
  uStack_120 = uVar3;
  uStack_118 = uVar6;
  func_0x02087e68(puRam0000000002103280,uVar4,uVar7,auStack_13c,2);
  auStack_13c[0] = 0;
  uVar1 = func_0x02090068();
  if (uVar1 == 0) {
    uVar6 = (ulong)auStack_13c[0];
  }
  else {
    uVar6 = (ulong)uVar1;
    uVar5 = func_0x02087e84();
    FUN_0208ada0(uVar5,0x20d11ec,uVar7,uVar6);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar6;
  }
  auVar8 = FUN_02091090();
  uStack_158 = 0x2086274;
  uVar3 = auVar8._0_8_ & 0xffffffff;
  ppuStack_160 = &ppuStack_130;
  uStack_150 = uVar7;
  uStack_148 = uVar6;
  uVar5 = func_0x02087e68(puRam0000000002103280,auVar8._0_8_,auVar8._8_8_,auStack_16c,1);
  auStack_16c[0] = extraout_w1;
  uVar1 = func_0x020900a0(uVar5,uVar3);
  uVar6 = (ulong)uVar1;
  if (uVar1 != 0) {
    uVar5 = func_0x02087e84();
    FUN_0208ada0(uVar5,0x20bcf43,uVar3,uVar6);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar6;
  }
  auVar8 = FUN_02091090();
  uStack_188 = 0x20862f0;
  uVar4 = auVar8._0_8_ & 0xffffffff;
  ppuStack_190 = &ppuStack_160;
  uStack_180 = uVar6;
  uStack_178 = uVar3;
  Var9 = func_0x02087e68(puRam0000000002103280,auVar8._0_8_,auVar8._8_8_,auStack_19c,2);
  auStack_19c[0] = (undefined2)((unkuint10)Var9 >> 0x40);
  uVar6 = func_0x020900a0((long)Var9,uVar4);
  if ((int)uVar6 != 0) {
    uVar5 = func_0x02087e84();
    FUN_0208ada0(uVar5,0x20beefd,uVar4,uVar6 & 0xffffffff);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar6 & 0xffffffff;
  }
  FUN_02091090();
  return (ulong)uRam000000000211d900;
}


