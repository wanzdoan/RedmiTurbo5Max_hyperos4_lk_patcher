
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000b8070908(undefined8 param_1,undefined4 param_2,uint param_3)

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
  if (DAT_ffff0000b81b9a78 == -1) {
    FUN_ffff0000b804c33c
              (s__s_tee_addr__0x_llX_tee_error____ffff0000b809c078,s__DICE__ffff0000b8099de7,
               0xffffffffffffffff,DAT_ffff0000b81b9a84,DAT_ffff0000b81b9a88,DAT_ffff0000b81b9a8c);
    lVar15 = 0;
    lVar14 = 0;
    iVar5 = 0;
    goto LAB_ffff0000b8070a10;
  }
  iVar5 = 0x10d011;
  if (3 < param_3) goto LAB_ffff0000b80709a8;
  iVar4 = FUN_ffff0000b8067a20(&DAT_ffff0000b81b99fc);
  if (iVar4 == 0) {
    DAT_ffff0000b81b9a00 = 0x20;
    if (DAT_ffff0000b81b99fc != 0) {
      DAT_ffff0000b81b9a00 = 0x30;
    }
    DAT_ffff0000b81b9ac4 = 0x40;
    if (DAT_ffff0000b81b99fc != 0) {
      DAT_ffff0000b81b9ac4 = 0x60;
    }
    iVar4 = FUN_ffff0000b8067a90();
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
        if ((uVar1 == 0) || (uVar12 == 0)) goto LAB_ffff0000b8070ad4;
      }
      puVar9 = &DAT_ffff0000b80a2aa0;
      if (DAT_ffff0000b81b99fc != 0) {
        puVar9 = &DAT_ffff0000b80a2b70;
      }
      puVar11 = (undefined *)0xffff0000b80a2a40;
      puVar10 = (undefined *)0xffff0000b80a2ad0;
      if (DAT_ffff0000b81b99fc != 0) {
        puVar11 = &DAT_ffff0000b80a2a00;
        puVar10 = &DAT_ffff0000b80a29f0;
      }
      if (3 < param_3) goto LAB_ffff0000b80709a8;
      uVar12 = 0x86868586;
    }
    else {
LAB_ffff0000b8070ad4:
      puVar9 = &DAT_ffff0000b80a2aa0;
      if (DAT_ffff0000b81b99fc != 0) {
        puVar9 = &DAT_ffff0000b80a2b70;
      }
      puVar10 = (undefined *)0xffff0000b80a2a10;
      puVar11 = (undefined *)0xffff0000b80a2a50;
      if (DAT_ffff0000b81b99fc != 0) {
        puVar10 = &DAT_ffff0000b80a29e0;
        puVar11 = &DAT_ffff0000b80a2b10;
      }
      if (3 < param_3) {
LAB_ffff0000b80709a8:
        lVar15 = 0;
        lVar14 = 0;
        goto LAB_ffff0000b8070a10;
      }
      uVar12 = 0x85858485;
    }
    uVar7 = -(ulong)(param_3 >> 0x1f) & 0xfffffffc00000000 | uVar13 << 2;
    uVar8 = -(ulong)(param_3 >> 0x1f) & 0xfffffff800000000 | uVar13 << 3;
    DAT_ffff0000b81b9aa4 = *(int *)(&DAT_ffff0000b80a2af0 + uVar7);
    DAT_ffff0000b81b9ab8 = *(undefined4 *)(puVar9 + uVar7);
    DAT_ffff0000b81b9aa0 = *(uint *)(&DAT_ffff0000b80a2a70 + uVar7);
    PTR_DAT_ffff0000b8107708 = *(undefined **)((long)&PTR_s_BL2EXTPVMFW_ffff0000b8107720 + uVar8);
    DAT_ffff0000b81b9ab4 = *(uint *)(puVar10 + uVar7);
    DAT_ffff0000b81b9a74 = *(int *)(puVar11 + uVar7);
    PTR_DAT_ffff0000b8107710 =
         *(undefined **)((long)&PTR_s_security_dice_pvmfw_ffff0000b8107740 + uVar8);
    DAT_ffff0000b81b9aac = *(undefined4 *)(&DAT_ffff0000b80a2ab0 + uVar7);
    DAT_ffff0000b81b9aa8 = *(undefined4 *)(&DAT_ffff0000b80a2ac0 + uVar7);
    DAT_ffff0000b81b9ab0 = (undefined1)(uVar12 >> (ulong)((param_3 & 3) << 3));
    lVar14 = FUN_ffff0000b804bd60(DAT_ffff0000b81b9a74,1);
    lVar15 = FUN_ffff0000b804bd60(DAT_ffff0000b81b9aa0,1);
    iVar5 = FUN_ffff0000b80615f4(param_1,param_2,&local_c0,DAT_ffff0000b81b99fc);
    if (iVar5 != 0) goto LAB_ffff0000b8070a10;
    FUN_ffff0000b8069a2c(lVar15,&local_c0,DAT_ffff0000b81b9a00);
    uVar12 = DAT_ffff0000b81b9a00;
    puVar9 = PTR_DAT_ffff0000b8107708;
    local_70 = 0;
    local_80 = lVar15 + (ulong)DAT_ffff0000b81b9a00;
    uVar2 = 2;
    if (param_3 == 0) {
      uVar2 = 3;
    }
    local_78 = (ulong)(DAT_ffff0000b81b9aa0 - DAT_ffff0000b81b9a00);
    FUN_ffff0000b806b9c8(uVar2,&local_80);
    FUN_ffff0000b806b5c8(0xfffffffffffeee8e,&local_80);
    FUN_ffff0000b806b8fc(puVar9,&local_80);
    FUN_ffff0000b806b5c8(0xfffffffffffeee8d,&local_80);
    FUN_ffff0000b806b5c8(1,&local_80);
    if (param_3 == 0) {
      FUN_ffff0000b806b5c8(0xfffffffffffeee8a,&local_80);
      FUN_ffff0000b806b9e0(&local_80);
    }
    uVar7 = local_70;
    if ((local_70 == 0xffffffffffffffff) || (local_78 < local_70)) {
      iVar5 = 0x10d002;
      FUN_ffff0000b804c33c
                (s__s_DICE_Config_DescriptEncode_fa_ffff0000b809881b,s__DICE__ffff0000b8099de7,
                 0x10d002);
      goto LAB_ffff0000b8070a10;
    }
    local_1a4 = 0;
    iVar5 = FUN_ffff0000b8067a5c(&local_1a4);
    if ((iVar5 != 0) ||
       (iVar5 = FUN_ffff0000b806795c(&local_100,DAT_ffff0000b81b9a00,local_1a4), iVar5 != 0))
    goto LAB_ffff0000b8070a10;
    uVar12 = uVar12 + (int)uVar7;
    FUN_ffff0000b8069a2c(lVar15 + (ulong)uVar12,&local_100,DAT_ffff0000b81b9a00);
    uVar12 = DAT_ffff0000b81b9a00 + uVar12;
    uVar3 = FUN_ffff0000b80678a8();
    uVar1 = uVar12 + 0x41;
    *(undefined1 *)(lVar15 + (ulong)uVar12) = uVar3;
    if (DAT_ffff0000b81b9aa0 < uVar1) {
      FUN_ffff0000b804c33c
                (s__s_buf_idx___d__g_dice_input_dat_ffff0000b808f70d,s__DICE__ffff0000b8099de7,uVar1
                );
      iVar5 = 0x10d006;
      goto LAB_ffff0000b8070a10;
    }
    FUN_ffff0000b8069a2c(&local_120,&DAT_ffff00001800d700,0x20);
    FUN_ffff0000b8069a2c(&local_140,&DAT_ffff00001800d720,0x20);
    plVar6 = (long *)FUN_ffff0000b800db20(PTR_DAT_ffff0000b8107710,0);
    if (plVar6 == (long *)0x0) {
      FUN_ffff0000b804c33c
                (s__s_mblock_query___s__failed__ffff0000b808d97c,s__DICE__ffff0000b8099de7,
                 PTR_DAT_ffff0000b8107710);
      iVar5 = 0x10d009;
      goto LAB_ffff0000b8070a10;
    }
    DAT_ffff0000b8107700 = FUN_ffff0000b807107c(*plVar6,plVar6[1]);
    if (DAT_ffff0000b8107700 == 0) {
      iVar5 = 0x10d00f;
      goto LAB_ffff0000b8070a10;
    }
    DAT_ffff0000b8107718 = *plVar6;
    iVar5 = FUN_ffff0000b80706d0
                      (lVar15 + (ulong)(DAT_ffff0000b81b9aa4 + DAT_ffff0000b81b9a00),
                       uVar1 - (DAT_ffff0000b81b9aa4 + DAT_ffff0000b81b9a00),&local_140,0x20,1,
                       &local_180,uVar13);
    if ((((iVar5 != 0) ||
         (iVar5 = FUN_ffff0000b8069c34(DAT_ffff0000b81b9aa8,&local_180,0x20,0,0), iVar5 != 0)) ||
        (iVar5 = FUN_ffff0000b80706d0(lVar15,uVar1,&local_120,0x20,0,&local_160,uVar13), iVar5 != 0)
        ) || ((iVar5 = FUN_ffff0000b8069c34
                                 (DAT_ffff0000b81b9aac,&local_160,0x20,&local_c0,
                                  DAT_ffff0000b81b9a00), iVar5 != 0 ||
              (iVar5 = FUN_ffff0000b8071134(lVar15,&local_160,&local_120,lVar14), iVar5 != 0))))
    goto LAB_ffff0000b8070a10;
    FUN_ffff0000b804d070(DAT_ffff0000b8107700,DAT_ffff0000b81b9ab0,1);
    FUN_ffff0000b8069a2c
              (DAT_ffff0000b8107700 + (ulong)DAT_ffff0000b81b9ab4 + 1,
               lVar14 + (ulong)DAT_ffff0000b81b9ab4,DAT_ffff0000b81b9ab8);
    iVar5 = FUN_ffff0000b8071b4c(DAT_ffff0000b8107700);
    if (iVar5 != 0) {
      iVar5 = 0x10d010;
      goto LAB_ffff0000b8070a10;
    }
    if (param_3 != 1) {
      iVar5 = 0;
      goto LAB_ffff0000b8070a50;
    }
    iVar5 = FUN_ffff0000b8071bc0(lVar14);
    if (iVar5 != 0) goto LAB_ffff0000b8070a10;
  }
  else {
    lVar15 = 0;
    lVar14 = 0;
    iVar5 = 0x10d007;
LAB_ffff0000b8070a10:
    FUN_ffff0000b8069a2c(&DAT_ffff00001800d700,&local_1a0,0x20);
    FUN_ffff0000b8069a2c(&DAT_ffff00001800d720,&local_1a0,0x20);
    DAT_ffff0000b8107718 = -1;
    DAT_ffff0000b81b9a74 = iVar5;
LAB_ffff0000b8070a50:
    DAT_ffff0000b81b9a80 = DAT_ffff0000b81b99fc;
    if (1 < param_3 - 1) {
      if (param_3 == 0) {
        DAT_ffff0000b81b9a8c = DAT_ffff0000b81b9a74 + 1;
      }
      else if (param_3 == 3) {
        DAT_ffff0000b81b9a88 = DAT_ffff0000b81b9a74 + 1;
      }
      goto LAB_ffff0000b8071034;
    }
  }
  DAT_ffff0000b81b9a84 = DAT_ffff0000b81b9a74 + 1;
  DAT_ffff0000b81b9a78 = DAT_ffff0000b8107718;
  DAT_ffff0000b81b9a80 = DAT_ffff0000b81b99fc;
LAB_ffff0000b8071034:
  DAT_ffff0000b81b99fc = DAT_ffff0000b81b9a80;
  if (lVar15 != 0) {
    thunk_FUN_ffff0000b8082558(lVar15);
  }
  if (lVar14 != 0) {
    thunk_FUN_ffff0000b8082558(lVar14);
  }
  return iVar5;
}


