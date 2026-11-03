// ffff0000907b3e7c  size=1908  FUN_ffff0000907b3e7c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907b3e7c(undefined8 param_1,undefined4 param_2,uint param_3)

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
  if (_DAT_ffff00009090d7b8 == -1) {
    FUN_ffff00009077de54
              (s__s_tee_addr__0x_llX_tee_error____ffff0000907f8f96,s__DICE__ffff0000907f5468,
               0xffffffffffffffff,_DAT_ffff00009090d7c4,_DAT_ffff00009090d7c8,_DAT_ffff00009090d7cc)
    ;
    lVar15 = 0;
    lVar14 = 0;
    iVar5 = 0;
    goto LAB_ffff0000907b3f84;
  }
  iVar5 = 0x10d011;
  if (3 < param_3) goto LAB_ffff0000907b3f1c;
  iVar4 = FUN_ffff0000907aa12c(&DAT_ffff00009090d73c);
  if (iVar4 == 0) {
    _DAT_ffff00009090d740 = 0x20;
    if (_DAT_ffff00009090d73c != 0) {
      _DAT_ffff00009090d740 = 0x30;
    }
    _DAT_ffff00009090d804 = 0x40;
    if (_DAT_ffff00009090d73c != 0) {
      _DAT_ffff00009090d804 = 0x60;
    }
    iVar4 = FUN_ffff0000907aa19c();
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
        if ((uVar1 == 0) || (uVar12 == 0)) goto LAB_ffff0000907b4048;
      }
      puVar9 = &DAT_ffff000090804a68;
      if (_DAT_ffff00009090d73c != 0) {
        puVar9 = &DAT_ffff000090804b38;
      }
      puVar11 = (undefined *)0xffff000090804a08;
      puVar10 = (undefined *)0xffff000090804a98;
      if (_DAT_ffff00009090d73c != 0) {
        puVar11 = &DAT_ffff0000908049b8;
        puVar10 = &DAT_ffff0000908049a8;
      }
      if (3 < param_3) goto LAB_ffff0000907b3f1c;
      uVar12 = 0x86868586;
    }
    else {
LAB_ffff0000907b4048:
      puVar9 = &DAT_ffff000090804a68;
      if (_DAT_ffff00009090d73c != 0) {
        puVar9 = &DAT_ffff000090804b38;
      }
      puVar10 = (undefined *)0xffff0000908049c8;
      puVar11 = (undefined *)0xffff000090804a18;
      if (_DAT_ffff00009090d73c != 0) {
        puVar10 = &DAT_ffff000090804998;
        puVar11 = &DAT_ffff000090804ad8;
      }
      if (3 < param_3) {
LAB_ffff0000907b3f1c:
        lVar15 = 0;
        lVar14 = 0;
        goto LAB_ffff0000907b3f84;
      }
      uVar12 = 0x85858485;
    }
    uVar7 = -(ulong)(param_3 >> 0x1f) & 0xfffffffc00000000 | uVar13 << 2;
    uVar8 = -(ulong)(param_3 >> 0x1f) & 0xfffffff800000000 | uVar13 << 3;
    _DAT_ffff00009090d7e4 = *(int *)(&DAT_ffff000090804ab8 + uVar7);
    _DAT_ffff00009090d7f8 = *(undefined4 *)(puVar9 + uVar7);
    _DAT_ffff00009090d7e0 = *(uint *)(&DAT_ffff000090804a38 + uVar7);
    PTR_DAT_ffff000090837ec0 = *(undefined **)((long)&PTR_s_BL2EXTPVMFW_ffff000090837ed8 + uVar8);
    _DAT_ffff00009090d7f4 = *(uint *)(puVar10 + uVar7);
    _DAT_ffff00009090d7b4 = *(int *)(puVar11 + uVar7);
    PTR_DAT_ffff000090837ec8 =
         *(undefined **)((long)&PTR_s_security_dice_pvmfw_ffff000090837ef8 + uVar8);
    _DAT_ffff00009090d7ec = *(undefined4 *)(&DAT_ffff000090804a78 + uVar7);
    _DAT_ffff00009090d7e8 = *(undefined4 *)(&DAT_ffff000090804a88 + uVar7);
    DAT_ffff00009090d7f0 = (undefined1)(uVar12 >> (ulong)((param_3 & 3) << 3));
    lVar14 = FUN_ffff00009077d2e0(_DAT_ffff00009090d7b4,1);
    lVar15 = FUN_ffff00009077d2e0(_DAT_ffff00009090d7e0,1);
    iVar5 = FUN_ffff0000907a262c(param_1,param_2,&local_c0,_DAT_ffff00009090d73c);
    if (iVar5 != 0) goto LAB_ffff0000907b3f84;
    FUN_ffff0000907acf74(lVar15,&local_c0,_DAT_ffff00009090d740);
    uVar12 = _DAT_ffff00009090d740;
    puVar9 = PTR_DAT_ffff000090837ec0;
    local_70 = 0;
    local_80 = lVar15 + (ulong)_DAT_ffff00009090d740;
    uVar2 = 2;
    if (param_3 == 0) {
      uVar2 = 3;
    }
    local_78 = (ulong)(_DAT_ffff00009090d7e0 - _DAT_ffff00009090d740);
    FUN_ffff0000907aef08(uVar2,&local_80);
    FUN_ffff0000907aeb08(0xfffffffffffeee8e,&local_80);
    FUN_ffff0000907aee3c(puVar9,&local_80);
    FUN_ffff0000907aeb08(0xfffffffffffeee8d,&local_80);
    FUN_ffff0000907aeb08(1,&local_80);
    if (param_3 == 0) {
      FUN_ffff0000907aeb08(0xfffffffffffeee8a,&local_80);
      FUN_ffff0000907aef20(&local_80);
    }
    uVar7 = local_70;
    if ((local_70 == 0xffffffffffffffff) || (local_78 < local_70)) {
      iVar5 = 0x10d002;
      FUN_ffff00009077de54
                (s__s_DICE_Config_DescriptEncode_fa_ffff0000907f211c,s__DICE__ffff0000907f5468,
                 0x10d002);
      goto LAB_ffff0000907b3f84;
    }
    local_1a4 = 0;
    iVar5 = FUN_ffff0000907aa168(&local_1a4);
    if ((iVar5 != 0) ||
       (iVar5 = FUN_ffff0000907aa068(&local_100,_DAT_ffff00009090d740,local_1a4), iVar5 != 0))
    goto LAB_ffff0000907b3f84;
    uVar12 = uVar12 + (int)uVar7;
    FUN_ffff0000907acf74(lVar15 + (ulong)uVar12,&local_100,_DAT_ffff00009090d740);
    uVar12 = _DAT_ffff00009090d740 + uVar12;
    uVar3 = FUN_ffff0000907a9fb4();
    uVar1 = uVar12 + 0x41;
    *(undefined1 *)(lVar15 + (ulong)uVar12) = uVar3;
    if (_DAT_ffff00009090d7e0 < uVar1) {
      FUN_ffff00009077de54
                (s__s_buf_idx___d__g_dice_input_dat_ffff0000907e221c,s__DICE__ffff0000907f5468,uVar1
                );
      iVar5 = 0x10d006;
      goto LAB_ffff0000907b3f84;
    }
    FUN_ffff0000907acf74(&local_120,&DAT_ffff00001800d700,0x20);
    FUN_ffff0000907acf74(&local_140,&DAT_ffff00001800d720,0x20);
    plVar6 = (long *)FUN_ffff00009071a204(PTR_DAT_ffff000090837ec8,0);
    if (plVar6 == (long *)0x0) {
      FUN_ffff00009077de54
                (s__s_mblock_query___s__failed__ffff0000907defeb,s__DICE__ffff0000907f5468,
                 PTR_DAT_ffff000090837ec8);
      iVar5 = 0x10d009;
      goto LAB_ffff0000907b3f84;
    }
    DAT_ffff000090837eb8 = FUN_ffff0000907b45f0(*plVar6,plVar6[1]);
    if (DAT_ffff000090837eb8 == 0) {
      iVar5 = 0x10d00f;
      goto LAB_ffff0000907b3f84;
    }
    DAT_ffff000090837ed0 = *plVar6;
    iVar5 = FUN_ffff0000907b3c44
                      (lVar15 + (ulong)(_DAT_ffff00009090d7e4 + _DAT_ffff00009090d740),
                       uVar1 - (_DAT_ffff00009090d7e4 + _DAT_ffff00009090d740),&local_140,0x20,1,
                       &local_180,uVar13);
    if ((((iVar5 != 0) ||
         (iVar5 = FUN_ffff0000907ad17c(_DAT_ffff00009090d7e8,&local_180,0x20,0,0), iVar5 != 0)) ||
        (iVar5 = FUN_ffff0000907b3c44(lVar15,uVar1,&local_120,0x20,0,&local_160,uVar13), iVar5 != 0)
        ) || ((iVar5 = FUN_ffff0000907ad17c
                                 (_DAT_ffff00009090d7ec,&local_160,0x20,&local_c0,
                                  _DAT_ffff00009090d740), iVar5 != 0 ||
              (iVar5 = FUN_ffff0000907b46a8(lVar15,&local_160,&local_120,lVar14), iVar5 != 0))))
    goto LAB_ffff0000907b3f84;
    FUN_ffff00009077eb88(DAT_ffff000090837eb8,DAT_ffff00009090d7f0,1);
    FUN_ffff0000907acf74
              (DAT_ffff000090837eb8 + (ulong)_DAT_ffff00009090d7f4 + 1,
               lVar14 + (ulong)_DAT_ffff00009090d7f4,_DAT_ffff00009090d7f8);
    iVar5 = FUN_ffff0000907b50c0(DAT_ffff000090837eb8);
    if (iVar5 != 0) {
      iVar5 = 0x10d010;
      goto LAB_ffff0000907b3f84;
    }
    if (param_3 != 1) {
      iVar5 = 0;
      goto LAB_ffff0000907b3fc4;
    }
    iVar5 = FUN_ffff0000907b5134(lVar14);
    if (iVar5 != 0) goto LAB_ffff0000907b3f84;
  }
  else {
    lVar15 = 0;
    lVar14 = 0;
    iVar5 = 0x10d007;
LAB_ffff0000907b3f84:
    FUN_ffff0000907acf74(&DAT_ffff00001800d700,&local_1a0,0x20);
    FUN_ffff0000907acf74(&DAT_ffff00001800d720,&local_1a0,0x20);
    DAT_ffff000090837ed0 = -1;
    _DAT_ffff00009090d7b4 = iVar5;
LAB_ffff0000907b3fc4:
    _DAT_ffff00009090d7c0 = _DAT_ffff00009090d73c;
    if (1 < param_3 - 1) {
      if (param_3 == 0) {
        _DAT_ffff00009090d7cc = _DAT_ffff00009090d7b4 + 1;
      }
      else if (param_3 == 3) {
        _DAT_ffff00009090d7c8 = _DAT_ffff00009090d7b4 + 1;
      }
      goto LAB_ffff0000907b45a8;
    }
  }
  _DAT_ffff00009090d7c4 = _DAT_ffff00009090d7b4 + 1;
  _DAT_ffff00009090d7b8 = DAT_ffff000090837ed0;
  _DAT_ffff00009090d7c0 = _DAT_ffff00009090d73c;
LAB_ffff0000907b45a8:
  _DAT_ffff00009090d73c = _DAT_ffff00009090d7c0;
  if (lVar15 != 0) {
    thunk_FUN_ffff0000907c2ad8(lVar15);
  }
  if (lVar14 != 0) {
    thunk_FUN_ffff0000907c2ad8(lVar14);
  }
  return iVar5;
}


