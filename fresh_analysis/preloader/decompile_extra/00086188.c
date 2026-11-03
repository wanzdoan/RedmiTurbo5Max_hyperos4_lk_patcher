// File offset entry: 0x86188

ulong FUN_02086088(ulong param_1,int param_2,int param_3,uint param_4)

{
  undefined1 in_ZR;
  uint uVar1;
  undefined8 uVar2;
  ulong uVar3;
  ulong uVar4;
  undefined1 extraout_w1;
  ulong uVar5;
  ulong uVar6;
  undefined1 auVar7 [16];
  unkbyte10 Var8;
  undefined2 auStack_10c [6];
  undefined8 **ppuStack_100;
  undefined8 uStack_f8;
  ulong uStack_f0;
  ulong uStack_e8;
  undefined1 auStack_dc [12];
  undefined8 **ppuStack_d0;
  undefined8 uStack_c8;
  ulong uStack_c0;
  ulong uStack_b8;
  ushort auStack_ac [6];
  undefined1 **ppuStack_a0;
  undefined8 uStack_98;
  ulong uStack_90;
  ulong uStack_88;
  byte abStack_7c [4];
  undefined8 uStack_78;
  undefined1 *puStack_70;
  undefined8 uStack_68;
  ulong uStack_60;
  ulong uStack_58;
  byte abStack_4c [4];
  undefined8 uStack_48;
  
  param_1 = param_1 & 0xffffffff;
  uStack_48 = *puRam0000000002103280;
  abStack_4c[0] = 0;
  uVar1 = func_0x02090068(uRam000000000211d8f0,param_1,abStack_4c,1);
  uVar5 = (ulong)uVar1;
  if (uVar1 == 0) {
    abStack_4c[0] =
         abStack_4c[0] & ((byte)(param_3 << (ulong)(param_4 & 0x1f)) ^ 0xff) |
         (byte)(param_2 << (ulong)(param_4 & 0x1f));
    uVar1 = func_0x020900a0(uRam000000000211d8f0,param_1,abStack_4c,1);
    uVar5 = (ulong)uVar1;
    if (uVar1 == 0) goto LAB_02086134;
  }
  uVar2 = func_0x02087e84();
  FUN_0208ada0(uVar2,0x20c019d,param_1,uVar5);
LAB_02086134:
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar5;
  }
  uVar3 = FUN_02091090();
  uStack_68 = 0x2086160;
  uVar3 = uVar3 & 0xffffffff;
  uStack_78 = *puRam0000000002103280;
  abStack_7c[0] = 0;
  puStack_70 = &stack0xffffffffffffffc0;
  uStack_60 = uVar5;
  uStack_58 = param_1;
  uVar1 = func_0x02090068(uRam000000000211d8f0,uVar3,abStack_7c,1);
  if (uVar1 == 0) {
    uVar5 = (ulong)abStack_7c[0];
  }
  else {
    uVar5 = (ulong)uVar1;
    uVar2 = func_0x02087e84();
    FUN_0208ada0(uVar2,0x20bbe86,uVar3,uVar5);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar5;
  }
  uVar4 = FUN_02091090();
  uStack_98 = 0x20861f0;
  uVar6 = uVar4 & 0xffffffff;
  ppuStack_a0 = &puStack_70;
  uStack_90 = uVar3;
  uStack_88 = uVar5;
  func_0x02087e68(puRam0000000002103280,uVar4,uVar6,auStack_ac,2);
  auStack_ac[0] = 0;
  uVar1 = func_0x02090068();
  if (uVar1 == 0) {
    uVar5 = (ulong)auStack_ac[0];
  }
  else {
    uVar5 = (ulong)uVar1;
    uVar2 = func_0x02087e84();
    FUN_0208ada0(uVar2,0x20d11ec,uVar6,uVar5);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar5;
  }
  auVar7 = FUN_02091090();
  uStack_c8 = 0x2086274;
  uVar3 = auVar7._0_8_ & 0xffffffff;
  ppuStack_d0 = &ppuStack_a0;
  uStack_c0 = uVar6;
  uStack_b8 = uVar5;
  uVar2 = func_0x02087e68(puRam0000000002103280,auVar7._0_8_,auVar7._8_8_,auStack_dc,1);
  auStack_dc[0] = extraout_w1;
  uVar1 = func_0x020900a0(uVar2,uVar3);
  uVar5 = (ulong)uVar1;
  if (uVar1 != 0) {
    uVar2 = func_0x02087e84();
    FUN_0208ada0(uVar2,0x20bcf43,uVar3,uVar5);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar5;
  }
  auVar7 = FUN_02091090();
  uStack_f8 = 0x20862f0;
  uVar4 = auVar7._0_8_ & 0xffffffff;
  ppuStack_100 = &ppuStack_d0;
  uStack_f0 = uVar5;
  uStack_e8 = uVar3;
  Var8 = func_0x02087e68(puRam0000000002103280,auVar7._0_8_,auVar7._8_8_,auStack_10c,2);
  auStack_10c[0] = (undefined2)((unkuint10)Var8 >> 0x40);
  uVar5 = func_0x020900a0((long)Var8,uVar4);
  if ((int)uVar5 != 0) {
    uVar2 = func_0x02087e84();
    FUN_0208ada0(uVar2,0x20beefd,uVar4,uVar5 & 0xffffffff);
  }
  func_0x02087e10();
  if ((bool)in_ZR) {
    return uVar5 & 0xffffffff;
  }
  FUN_02091090();
  return (ulong)uRam000000000211d900;
}


