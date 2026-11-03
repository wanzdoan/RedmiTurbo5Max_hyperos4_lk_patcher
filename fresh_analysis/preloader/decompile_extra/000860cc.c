// File offset entry: 0x860cc

ulong FUN_02085fcc(int param_1,undefined1 *param_2,int param_3,uint param_4)

{
  undefined1 in_ZR;
  uint uVar1;
  uint uVar2;
  ulong uVar3;
  undefined8 uVar4;
  ulong uVar5;
  undefined1 extraout_w1;
  int iVar6;
  ulong uVar7;
  ulong uVar8;
  undefined1 auVar9 [16];
  undefined1 auVar10 [12];
  unkbyte10 Var11;
  undefined2 auStack_15c [6];
  undefined8 **ppuStack_150;
  undefined8 uStack_148;
  ulong uStack_140;
  ulong uStack_138;
  undefined1 auStack_12c [12];
  undefined8 **ppuStack_120;
  undefined8 uStack_118;
  ulong uStack_110;
  ulong uStack_108;
  ushort auStack_fc [6];
  undefined8 **ppuStack_f0;
  undefined8 uStack_e8;
  ulong uStack_e0;
  ulong uStack_d8;
  byte abStack_cc [4];
  undefined8 uStack_c8;
  undefined1 **ppuStack_c0;
  undefined8 uStack_b8;
  ulong uStack_b0;
  ulong uStack_a8;
  byte abStack_9c [4];
  undefined8 uStack_98;
  undefined1 *puStack_90;
  code *pcStack_88;
  byte abStack_4c [4];
  undefined8 uStack_48;
  
  iVar6 = (int)abStack_4c;
  uVar2 = 1;
  uStack_48 = *puRam0000000002103280;
  abStack_4c[0] = 0;
  uVar3 = FUN_02090068(uRam000000000211d8f0,param_1);
  if ((uint)uVar3 == 0) {
    *param_2 = (char)((param_3 << (ulong)(param_4 & 0x1f) & (uint)abStack_4c[0]) >>
                     (ulong)(param_4 & 0x1f));
  }
  else {
    uVar4 = FUN_02087e84();
    uVar2 = (uint)uVar3;
    iVar6 = param_1;
    FUN_0208ada0(uVar4,0x20b2d20);
  }
  FUN_02087e10();
  if ((bool)in_ZR) {
    return uVar3 & 0xffffffff;
  }
  auVar10 = FUN_02091090();
  pcStack_88 = FUN_02086088;
  uVar3 = auVar10._0_8_ & 0xffffffff;
  uStack_98 = *puRam0000000002103280;
  abStack_9c[0] = 0;
  puStack_90 = &stack0xffffffffffffffc0;
  uVar1 = FUN_02090068(uRam000000000211d8f0,uVar3,abStack_9c,1);
  uVar7 = (ulong)uVar1;
  if (uVar1 == 0) {
    abStack_9c[0] =
         abStack_9c[0] & ((byte)(iVar6 << (ulong)(uVar2 & 0x1f)) ^ 0xff) |
         (byte)(auVar10._8_4_ << (ulong)(uVar2 & 0x1f));
    uVar2 = FUN_020900a0(uRam000000000211d8f0,uVar3,abStack_9c,1);
    uVar7 = (ulong)uVar2;
    if (uVar2 == 0) goto LAB_02086134;
  }
  uVar4 = FUN_02087e84();
  FUN_0208ada0(uVar4,0x20c019d,uVar3,uVar7);
LAB_02086134:
  FUN_02087e10();
  if ((bool)in_ZR) {
    return uVar7;
  }
  uVar5 = FUN_02091090();
  uStack_b8 = 0x2086160;
  uVar5 = uVar5 & 0xffffffff;
  uStack_c8 = *puRam0000000002103280;
  abStack_cc[0] = 0;
  ppuStack_c0 = &puStack_90;
  uStack_b0 = uVar7;
  uStack_a8 = uVar3;
  uVar2 = FUN_02090068(uRam000000000211d8f0,uVar5,abStack_cc,1);
  if (uVar2 == 0) {
    uVar3 = (ulong)abStack_cc[0];
  }
  else {
    uVar3 = (ulong)uVar2;
    uVar4 = FUN_02087e84();
    FUN_0208ada0(uVar4,0x20bbe86,uVar5,uVar3);
  }
  FUN_02087e10();
  if ((bool)in_ZR) {
    return uVar3;
  }
  uVar7 = FUN_02091090();
  uStack_e8 = 0x20861f0;
  uVar8 = uVar7 & 0xffffffff;
  ppuStack_f0 = &ppuStack_c0;
  uStack_e0 = uVar5;
  uStack_d8 = uVar3;
  FUN_02087e68(puRam0000000002103280,uVar7,uVar8,auStack_fc,2);
  auStack_fc[0] = 0;
  uVar2 = FUN_02090068();
  if (uVar2 == 0) {
    uVar3 = (ulong)auStack_fc[0];
  }
  else {
    uVar3 = (ulong)uVar2;
    uVar4 = FUN_02087e84();
    FUN_0208ada0(uVar4,0x20d11ec,uVar8,uVar3);
  }
  FUN_02087e10();
  if ((bool)in_ZR) {
    return uVar3;
  }
  auVar9 = FUN_02091090();
  uStack_118 = 0x2086274;
  uVar7 = auVar9._0_8_ & 0xffffffff;
  ppuStack_120 = &ppuStack_f0;
  uStack_110 = uVar8;
  uStack_108 = uVar3;
  uVar4 = FUN_02087e68(puRam0000000002103280,auVar9._0_8_,auVar9._8_8_,auStack_12c,1);
  auStack_12c[0] = extraout_w1;
  uVar2 = FUN_020900a0(uVar4,uVar7);
  uVar3 = (ulong)uVar2;
  if (uVar2 != 0) {
    uVar4 = FUN_02087e84();
    FUN_0208ada0(uVar4,0x20bcf43,uVar7,uVar3);
  }
  FUN_02087e10();
  if ((bool)in_ZR) {
    return uVar3;
  }
  auVar9 = FUN_02091090();
  uStack_148 = 0x20862f0;
  uVar5 = auVar9._0_8_ & 0xffffffff;
  ppuStack_150 = &ppuStack_120;
  uStack_140 = uVar3;
  uStack_138 = uVar7;
  Var11 = FUN_02087e68(puRam0000000002103280,auVar9._0_8_,auVar9._8_8_,auStack_15c,2);
  auStack_15c[0] = (undefined2)((unkuint10)Var11 >> 0x40);
  uVar3 = FUN_020900a0((long)Var11,uVar5);
  if ((int)uVar3 != 0) {
    uVar4 = FUN_02087e84();
    FUN_0208ada0(uVar4,0x20beefd,uVar5,uVar3 & 0xffffffff);
  }
  FUN_02087e10();
  if ((bool)in_ZR) {
    return uVar3 & 0xffffffff;
  }
  FUN_02091090();
  return (ulong)uRam000000000211d900;
}


