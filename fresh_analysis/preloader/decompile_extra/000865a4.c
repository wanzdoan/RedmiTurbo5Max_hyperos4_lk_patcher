// File offset entry: 0x865a4

ulong FUN_020864a4(void)

{
  undefined1 in_ZR;
  uint uVar1;
  int iVar2;
  ulong uVar3;
  ulong uVar4;
  undefined8 uVar5;
  undefined1 extraout_w1;
  undefined8 *extraout_x8;
  ulong unaff_x19;
  ulong uVar6;
  ulong uVar7;
  undefined1 auVar8 [16];
  unkbyte10 Var9;
  undefined2 auStack_16c [6];
  undefined8 **ppuStack_160;
  undefined8 uStack_158;
  ulong uStack_150;
  ulong uStack_148;
  undefined1 auStack_13c [12];
  undefined8 **ppuStack_130;
  undefined8 uStack_128;
  ulong uStack_120;
  ulong uStack_118;
  ushort auStack_10c [6];
  undefined8 **ppuStack_100;
  undefined8 uStack_f8;
  ulong uStack_f0;
  ulong uStack_e8;
  byte abStack_dc [4];
  undefined8 uStack_d8;
  undefined8 **ppuStack_d0;
  undefined8 uStack_c8;
  ulong uStack_c0;
  undefined8 uStack_b8;
  byte abStack_ac [4];
  undefined8 uStack_a8;
  undefined1 **ppuStack_a0;
  undefined8 uStack_98;
  undefined1 *puStack_50;
  undefined8 uStack_48;
  ulong uStack_40;
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
    FUN_0208ada0(uVar5,0x20c760e,unaff_x19 & 0xffffffff);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return unaff_x19 & 0xffffffff;
  }
  FUN_02091090();
  uStack_48 = 0x208651c;
  puStack_50 = &stack0xffffffffffffffe0;
  uStack_40 = unaff_x19;
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
  uStack_98 = 0x208659c;
  uStack_a8 = *puRam0000000002103280;
  abStack_ac[0] = 0;
  ppuStack_a0 = &puStack_50;
  uVar1 = func_0x02090068(uRam000000000211d8f0,0xa0f,abStack_ac,1);
  uVar6 = (ulong)uVar1;
  if (uVar1 == 0) {
    abStack_ac[0] = abStack_ac[0] & 0x3f | 0x40;
    uVar1 = func_0x020900a0(uRam000000000211d8f0,0xa0f,abStack_ac,1);
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
  uStack_c8 = 0x2086160;
  uStack_b8 = 0xa0f;
  uVar3 = uVar3 & 0xffffffff;
  uStack_d8 = *puRam0000000002103280;
  abStack_dc[0] = 0;
  ppuStack_d0 = &ppuStack_a0;
  uStack_c0 = uVar6;
  uVar1 = func_0x02090068(uRam000000000211d8f0,uVar3,abStack_dc,1);
  if (uVar1 == 0) {
    uVar6 = (ulong)abStack_dc[0];
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
  uStack_f8 = 0x20861f0;
  uVar7 = uVar4 & 0xffffffff;
  ppuStack_100 = &ppuStack_d0;
  uStack_f0 = uVar3;
  uStack_e8 = uVar6;
  func_0x02087e68(puRam0000000002103280,uVar4,uVar7,auStack_10c,2);
  auStack_10c[0] = 0;
  uVar1 = func_0x02090068();
  if (uVar1 == 0) {
    uVar6 = (ulong)auStack_10c[0];
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
  uStack_128 = 0x2086274;
  uVar3 = auVar8._0_8_ & 0xffffffff;
  ppuStack_130 = &ppuStack_100;
  uStack_120 = uVar7;
  uStack_118 = uVar6;
  uVar5 = func_0x02087e68(puRam0000000002103280,auVar8._0_8_,auVar8._8_8_,auStack_13c,1);
  auStack_13c[0] = extraout_w1;
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
  uStack_158 = 0x20862f0;
  uVar4 = auVar8._0_8_ & 0xffffffff;
  ppuStack_160 = &ppuStack_130;
  uStack_150 = uVar6;
  uStack_148 = uVar3;
  Var9 = func_0x02087e68(puRam0000000002103280,auVar8._0_8_,auVar8._8_8_,auStack_16c,2);
  auStack_16c[0] = (undefined2)((unkuint10)Var9 >> 0x40);
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


