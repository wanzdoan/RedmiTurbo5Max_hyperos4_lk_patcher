
undefined8 FUN_ffff0000907971d4(long *param_1)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  long lVar5;
  uint uVar6;
  long lVar7;
  uint uVar8;
  uint *puVar9;
  ulong uVar10;
  undefined8 *puVar11;
  undefined8 local_120;
  undefined8 local_118;
  undefined8 local_110;
  undefined8 local_108;
  undefined8 local_100;
  undefined8 local_f8;
  undefined8 local_f0;
  undefined8 local_e8;
  undefined8 local_e0;
  undefined8 local_d8;
  undefined8 local_d0;
  undefined8 local_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  lVar5 = *param_1;
  puVar11 = (undefined8 *)param_1[0xf];
  if (*(uint *)(param_1 + 0x56) < 0x10450100) {
    local_c8 = 0;
    local_c0 = 0;
    local_d8 = 0;
    local_d0 = 0;
    local_e8 = 0;
    local_e0 = 0;
    local_f8 = 0;
    local_f0 = 0;
    local_108 = 0;
    local_100 = 0;
    local_118 = 0;
    local_110 = 0;
    local_120 = 0;
    if (*(uint *)(param_1 + 0x56) >> 0x12 < 0x411) goto LAB_ffff0000907972b4;
  }
  else {
    local_c8 = 0;
    local_c0 = 0;
    local_d8 = 0;
    local_d0 = 0;
    local_e8 = 0;
    local_e0 = 0;
    local_f8 = 0;
    local_f0 = 0;
    local_108 = 0;
    local_100 = 0;
    local_118 = 0;
    local_110 = 0;
    local_120 = 0;
  }
  uVar6 = 0;
  uVar8 = 0;
  do {
    uVar2 = uVar8 >> 1;
    uVar4 = uVar8 & 1;
    uVar8 = uVar8 + 1;
    uVar3 = *(uint *)(lVar5 + 0x1fc) & 0xff00ffff | uVar6;
    uVar6 = uVar6 + 0x10000;
    *(uint *)(lVar5 + 0x1fc) = uVar3;
    uVar3 = *(uint *)(lVar5 + 0x1fc) & 0xffff;
    if (uVar4 != 0) {
      uVar3 = *(uint *)(lVar5 + 0x1fc) << 0x10;
    }
    (&local_120)[uVar2] = (&local_120)[uVar2] | (ulong)uVar3;
  } while (uVar8 != 0x1a);
LAB_ffff0000907972b4:
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  local_c8 = 0;
  local_e0 = 0;
  local_d8 = 0;
  local_f0 = 0;
  local_e8 = 0;
  local_100 = 0;
  local_f8 = 0;
  local_110 = 0;
  local_108 = 0;
  local_120 = 0;
  local_118 = 0;
  FUN_ffff00009077ee50(&local_120,0,0xe0);
  lVar7 = *param_1;
  lVar5 = 0;
  puVar9 = &DAT_ffff000090812824;
  do {
    uVar10 = (ulong)*puVar9;
    lVar1 = lVar5 + 0x10;
    *(undefined4 *)((long)&local_120 + lVar5) = *(undefined4 *)(lVar7 + uVar10);
    *(undefined4 *)((long)&local_120 + lVar5 + 4) = *(undefined4 *)(lVar7 + uVar10);
    *(undefined4 *)((long)&local_118 + lVar5) = *(undefined4 *)(lVar7 + uVar10);
    *(undefined4 *)((long)&local_118 + lVar5 + 4) = *(undefined4 *)(lVar7 + uVar10);
    lVar5 = lVar1;
    puVar9 = puVar9 + 5;
  } while (lVar1 != 0xe0);
  FUN_ffff00009077e11c
            (s__s_Address___3_2_1_0_7_6_5_4_B_A_ffff0000907e6feb,s_UFS_UTRD__ffff0000907d8855);
  FUN_ffff00009077cffc(*puVar11,0x20);
  FUN_ffff00009077e11c
            (s__s_Address__3_2_1_0_7_6_5_4_B_A_9_ffff0000907ed51e,s_UFS_UCD_REQ__ffff0000907ebba0);
  FUN_ffff00009077cffc(puVar11[1],0x20);
  FUN_ffff00009077e11c
            (s__s_Address__3_2_1_0_7_6_5_4_B_A_9_ffff0000907ed51e,s_UFS_UCD_RSP__ffff0000907dd533);
  FUN_ffff00009077cffc(puVar11[2],0x34);
  FUN_ffff00009077e11c
            (s__s_Address__3_2_1_0_7_6_5_4_B_A_9_ffff0000907ed51e,s_UFS_UCD_PRDT__ffff0000907ed555);
  FUN_ffff00009077cffc(puVar11[3],0x10);
  FUN_ffff00009077cffc(*param_1,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x10,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x20,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x30,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x50,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x60,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x70,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x80,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x90,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x100,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x110,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x120,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x130,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x140,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x150,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x160,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x170,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x300,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x2100,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x2200,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x2210,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x2220,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x2230,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x2240,0xf);
  lVar5 = *param_1;
  if (*(uint *)(param_1 + 0x56) >> 0x11 < 0x81b) {
    *(undefined4 *)(lVar5 + 0x22c0) = 0x20;
  }
  else {
    *(undefined4 *)(lVar5 + 0x22c0) = 0x820820;
    *(undefined4 *)(lVar5 + 0x22d0) = 0;
    *(undefined4 *)(lVar5 + 0x22d4) = 0x55555555;
    *(undefined4 *)(lVar5 + 0x22d8) = 0xaaaaaaaa;
    *(undefined4 *)(lVar5 + 0x22dc) = 0xffffffff;
  }
  FUN_ffff00009077cffc(lVar5 + 0x22c8,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x22d8,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x22e8,0xf);
  FUN_ffff00009077cffc(*param_1 + 0x22f8,0xf);
  return 0;
}


