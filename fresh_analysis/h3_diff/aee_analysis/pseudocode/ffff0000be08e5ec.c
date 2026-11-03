
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000be08e5ec(undefined8 param_1,undefined4 param_2,uint param_3)

{
  uint uVar1;
  undefined8 uVar2;
  undefined1 uVar3;
  int iVar4;
  int iVar5;
  long *plVar6;
  ulong uVar7;
  ulong uVar8;
  undefined *puVar9;
  undefined *puVar10;
  undefined *puVar11;
  uint uVar12;
  ulong uVar13;
  long lVar14;
  long lVar15;
  undefined4 local_1a4;
  undefined8 local_1a0;
  undefined8 uStack_198;
  undefined8 local_190;
  undefined8 uStack_188;
  undefined8 local_180;
  undefined8 uStack_178;
  undefined8 local_170;
  undefined8 uStack_168;
  undefined8 local_160;
  undefined8 uStack_158;
  undefined8 local_150;
  undefined8 uStack_148;
  undefined8 local_140;
  undefined8 uStack_138;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  long local_80;
  ulong local_78;
  ulong local_70;
  
  uVar13 = (ulong)param_3;
  local_130 = 0;
  uStack_128 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_140 = 0;
  uStack_138 = 0;
  local_150 = 0;
  uStack_148 = 0;
  local_160 = 0;
  uStack_158 = 0;
  local_170 = 0;
  uStack_168 = 0;
  local_180 = 0;
  uStack_178 = 0;
  local_190 = 0;
  uStack_188 = 0;
  local_1a0 = 0;
  uStack_198 = 0;
  if (DAT_ffff0000be1b7d38 == -1) {
    FUN_ffff0000be05fd98
              (s__s_tee_addr__0x_llX_tee_error____ffff0000be0c0238,s__DICE__ffff0000be0bd9dd,
               0xffffffffffffffff,DAT_ffff0000be1b7d44,DAT_ffff0000be1b7d48,DAT_ffff0000be1b7d4c);
    lVar15 = 0;
    lVar14 = 0;
    iVar5 = 0;
    goto LAB_ffff0000be08e6f4;
  }
  iVar5 = 0x10d011;
  if (3 < param_3) goto LAB_ffff0000be08e68c;
  iVar4 = FUN_ffff0000be0853e0(&DAT_ffff0000be1b7cbc);
  if (iVar4 == 0) {
    DAT_ffff0000be1b7cc0 = 0x20;
    if (DAT_ffff0000be1b7cbc != 0) {
      DAT_ffff0000be1b7cc0 = 0x30;
    }
    DAT_ffff0000be1b7d84 = 0x40;
    if (DAT_ffff0000be1b7cbc != 0) {
      DAT_ffff0000be1b7d84 = 0x60;
    }
    iVar4 = FUN_ffff0000be085450();
    if (iVar4 == 0) {
      uVar12 = _DAT_ffff00001002d8f0 & 0x40000;
      uVar1 = _DAT_ffff00001002d8f0 & 0x80000;
      if ((_DAT_ffff000013260058 & 3) != 0) {
        uVar12 = _DAT_ffff000013260058 & 2;
        uVar1 = _DAT_ffff000013260058 & 1;
      }
      if ((uVar1 == 0) || (uVar12 != 0)) {
        uVar12 = _DAT_ffff00001002d8f0 & 0x40000;
        uVar1 = _DAT_ffff00001002d8f0 & 0x80000;
        if ((_DAT_ffff000013260058 & 3) != 0) {
          uVar12 = _DAT_ffff000013260058 & 2;
          uVar1 = _DAT_ffff000013260058 & 1;
        }
        if ((uVar1 == 0) || (uVar12 == 0)) goto LAB_ffff0000be08e7b8;
      }
      puVar9 = &DAT_ffff0000be0c8250;
      if (DAT_ffff0000be1b7cbc != 0) {
        puVar9 = &DAT_ffff0000be0c8320;
      }
      puVar11 = (undefined *)0xffff0000be0c81f0;
      puVar10 = (undefined *)0xffff0000be0c8280;
      if (DAT_ffff0000be1b7cbc != 0) {
        puVar11 = &DAT_ffff0000be0c81b0;
        puVar10 = &DAT_ffff0000be0c81a0;
      }
      if (3 < param_3) goto LAB_ffff0000be08e68c;
      uVar12 = 0x86868586;
    }
    else {
LAB_ffff0000be08e7b8:
      puVar9 = &DAT_ffff0000be0c8250;
      if (DAT_ffff0000be1b7cbc != 0) {
        puVar9 = &DAT_ffff0000be0c8320;
      }
      puVar10 = (undefined *)0xffff0000be0c81c0;
      puVar11 = (undefined *)0xffff0000be0c8200;
      if (DAT_ffff0000be1b7cbc != 0) {
        puVar10 = &DAT_ffff0000be0c8190;
        puVar11 = &DAT_ffff0000be0c82c0;
      }
      if (3 < param_3) {
LAB_ffff0000be08e68c:
        lVar15 = 0;
        lVar14 = 0;
        goto LAB_ffff0000be08e6f4;
      }
      uVar12 = 0x85858485;
    }
    uVar7 = -(ulong)(param_3 >> 0x1f) & 0xfffffffc00000000 | uVar13 << 2;
    uVar8 = -(ulong)(param_3 >> 0x1f) & 0xfffffff800000000 | uVar13 << 3;
    DAT_ffff0000be1b7d64 = *(int *)(&DAT_ffff0000be0c82a0 + uVar7);
    DAT_ffff0000be1b7d78 = *(undefined4 *)(puVar9 + uVar7);
    DAT_ffff0000be1b7d60 = *(uint *)(&DAT_ffff0000be0c8220 + uVar7);
    PTR_DAT_ffff0000be0f40b8 = *(undefined **)((long)&PTR_s_BL2EXTPVMFW_ffff0000be0f40d0 + uVar8);
    DAT_ffff0000be1b7d74 = *(uint *)(puVar10 + uVar7);
    DAT_ffff0000be1b7d34 = *(int *)(puVar11 + uVar7);
    PTR_DAT_ffff0000be0f40c0 =
         *(undefined **)((long)&PTR_s_security_dice_pvmfw_ffff0000be0f40f0 + uVar8);
    DAT_ffff0000be1b7d6c = *(undefined4 *)(&DAT_ffff0000be0c8260 + uVar7);
    DAT_ffff0000be1b7d68 = *(undefined4 *)(&DAT_ffff0000be0c8270 + uVar7);
    DAT_ffff0000be1b7d70 = (undefined1)(uVar12 >> (ulong)((param_3 & 3) << 3));
    lVar14 = FUN_ffff0000be05f250(DAT_ffff0000be1b7d34,1);
    lVar15 = FUN_ffff0000be05f250(DAT_ffff0000be1b7d60,1);
    iVar5 = FUN_ffff0000be07efb4(param_1,param_2,&local_c0,DAT_ffff0000be1b7cbc);
    if (iVar5 != 0) goto LAB_ffff0000be08e6f4;
    FUN_ffff0000be087428(lVar15,&local_c0,DAT_ffff0000be1b7cc0);
    uVar12 = DAT_ffff0000be1b7cc0;
    puVar9 = PTR_DAT_ffff0000be0f40b8;
    local_70 = 0;
    local_80 = lVar15 + (ulong)DAT_ffff0000be1b7cc0;
    uVar2 = 2;
    if (param_3 == 0) {
      uVar2 = 3;
    }
    local_78 = (ulong)(DAT_ffff0000be1b7d60 - DAT_ffff0000be1b7cc0);
    FUN_ffff0000be0893c4(uVar2,&local_80);
    FUN_ffff0000be088fc4(0xfffffffffffeee8e,&local_80);
    FUN_ffff0000be0892f8(puVar9,&local_80);
    FUN_ffff0000be088fc4(0xfffffffffffeee8d,&local_80);
    FUN_ffff0000be088fc4(1,&local_80);
    if (param_3 == 0) {
      FUN_ffff0000be088fc4(0xfffffffffffeee8a,&local_80);
      FUN_ffff0000be0893dc(&local_80);
    }
    uVar7 = local_70;
    if ((local_70 == 0xffffffffffffffff) || (local_78 < local_70)) {
      iVar5 = 0x10d002;
      FUN_ffff0000be05fd98
                (s__s_DICE_Config_DescriptEncode_fa_ffff0000be0bb846,s__DICE__ffff0000be0bd9dd,
                 0x10d002);
      goto LAB_ffff0000be08e6f4;
    }
    local_1a4 = 0;
    iVar5 = FUN_ffff0000be08541c(&local_1a4);
    if ((iVar5 != 0) ||
       (iVar5 = FUN_ffff0000be08531c(&local_100,DAT_ffff0000be1b7cc0,local_1a4), iVar5 != 0))
    goto LAB_ffff0000be08e6f4;
    uVar12 = uVar12 + (int)uVar7;
    FUN_ffff0000be087428(lVar15 + (ulong)uVar12,&local_100,DAT_ffff0000be1b7cc0);
    uVar12 = DAT_ffff0000be1b7cc0 + uVar12;
    uVar3 = FUN_ffff0000be085268();
    uVar1 = uVar12 + 0x41;
    *(undefined1 *)(lVar15 + (ulong)uVar12) = uVar3;
    if (DAT_ffff0000be1b7d60 < uVar1) {
      FUN_ffff0000be05fd98
                (s__s_buf_idx___d__g_dice_input_dat_ffff0000be0b06c5,s__DICE__ffff0000be0bd9dd,uVar1
                );
      iVar5 = 0x10d006;
      goto LAB_ffff0000be08e6f4;
    }
    FUN_ffff0000be087428(&local_120,&DAT_ffff00001800d700,0x20);
    FUN_ffff0000be087428(&local_140,&DAT_ffff00001800d720,0x20);
    plVar6 = (long *)FUN_ffff0000be01cb9c(PTR_DAT_ffff0000be0f40c0,0);
    if (plVar6 == (long *)0x0) {
      FUN_ffff0000be05fd98
                (s__s_mblock_query___s__failed__ffff0000be0ae188,s__DICE__ffff0000be0bd9dd,
                 PTR_DAT_ffff0000be0f40c0);
      iVar5 = 0x10d009;
      goto LAB_ffff0000be08e6f4;
    }
    DAT_ffff0000be0f40b0 = FUN_ffff0000be08ed60(*plVar6,plVar6[1]);
    if (DAT_ffff0000be0f40b0 == 0) {
      iVar5 = 0x10d00f;
      goto LAB_ffff0000be08e6f4;
    }
    DAT_ffff0000be0f40c8 = *plVar6;
    iVar5 = FUN_ffff0000be08e3b4
                      (lVar15 + (ulong)(DAT_ffff0000be1b7d64 + DAT_ffff0000be1b7cc0),
                       uVar1 - (DAT_ffff0000be1b7d64 + DAT_ffff0000be1b7cc0),&local_140,0x20,1,
                       &local_180,uVar13);
    if ((((iVar5 != 0) ||
         (iVar5 = FUN_ffff0000be087630(DAT_ffff0000be1b7d68,&local_180,0x20,0,0), iVar5 != 0)) ||
        (iVar5 = FUN_ffff0000be08e3b4(lVar15,uVar1,&local_120,0x20,0,&local_160,uVar13), iVar5 != 0)
        ) || ((iVar5 = FUN_ffff0000be087630
                                 (DAT_ffff0000be1b7d6c,&local_160,0x20,&local_c0,
                                  DAT_ffff0000be1b7cc0), iVar5 != 0 ||
              (iVar5 = FUN_ffff0000be08ee18(lVar15,&local_160,&local_120,lVar14), iVar5 != 0))))
    goto LAB_ffff0000be08e6f4;
    FUN_ffff0000be060ae0(DAT_ffff0000be0f40b0,DAT_ffff0000be1b7d70,1);
    FUN_ffff0000be087428
              (DAT_ffff0000be0f40b0 + (ulong)DAT_ffff0000be1b7d74 + 1,
               lVar14 + (ulong)DAT_ffff0000be1b7d74,DAT_ffff0000be1b7d78);
    iVar5 = FUN_ffff0000be08f830(DAT_ffff0000be0f40b0);
    if (iVar5 != 0) {
      iVar5 = 0x10d010;
      goto LAB_ffff0000be08e6f4;
    }
    if (param_3 != 1) {
      iVar5 = 0;
      goto LAB_ffff0000be08e734;
    }
    iVar5 = FUN_ffff0000be08f8a4(lVar14);
    if (iVar5 != 0) goto LAB_ffff0000be08e6f4;
  }
  else {
    lVar15 = 0;
    lVar14 = 0;
    iVar5 = 0x10d007;
LAB_ffff0000be08e6f4:
    FUN_ffff0000be087428(&DAT_ffff00001800d700,&local_1a0,0x20);
    FUN_ffff0000be087428(&DAT_ffff00001800d720,&local_1a0,0x20);
    DAT_ffff0000be0f40c8 = -1;
    DAT_ffff0000be1b7d34 = iVar5;
LAB_ffff0000be08e734:
    DAT_ffff0000be1b7d40 = DAT_ffff0000be1b7cbc;
    if (1 < param_3 - 1) {
      if (param_3 == 0) {
        DAT_ffff0000be1b7d4c = DAT_ffff0000be1b7d34 + 1;
      }
      else if (param_3 == 3) {
        DAT_ffff0000be1b7d48 = DAT_ffff0000be1b7d34 + 1;
      }
      goto LAB_ffff0000be08ed18;
    }
  }
  DAT_ffff0000be1b7d44 = DAT_ffff0000be1b7d34 + 1;
  DAT_ffff0000be1b7d38 = DAT_ffff0000be0f40c8;
  DAT_ffff0000be1b7d40 = DAT_ffff0000be1b7cbc;
LAB_ffff0000be08ed18:
  DAT_ffff0000be1b7cbc = DAT_ffff0000be1b7d40;
  if (lVar15 != 0) {
    thunk_FUN_ffff0000be09937c(lVar15);
  }
  if (lVar14 != 0) {
    thunk_FUN_ffff0000be09937c(lVar14);
  }
  return iVar5;
}


