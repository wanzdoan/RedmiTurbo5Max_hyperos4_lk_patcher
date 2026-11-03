
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 * FUN_ffff0000907b71b0(undefined4 *param_1)

{
  undefined *puVar1;
  undefined4 uVar2;
  undefined *puVar3;
  undefined *puVar4;
  undefined *puVar5;
  int iVar6;
  undefined4 uVar7;
  undefined4 uVar8;
  undefined4 *puVar9;
  undefined8 *puVar10;
  undefined4 local_90;
  undefined4 uStack_8c;
  undefined4 local_88;
  undefined4 local_84;
  undefined4 local_7c;
  undefined4 local_78;
  undefined4 local_74;
  undefined4 local_70;
  undefined4 local_6c;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined4 local_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_90 = 0;
  uStack_8c = 0;
  local_88 = 0;
  local_84 = 0;
  local_7c = 1;
  iVar6 = FUN_ffff0000907b65c4(&local_7c);
  puVar9 = param_1;
  if (iVar6 == 0) {
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    local_58 = 0;
    local_68 = 0;
    uStack_60 = 0;
    local_78 = thunk_FUN_ffff00009074b22c(0xc);
    local_74 = thunk_FUN_ffff00009074b22c(0xd);
    local_70 = thunk_FUN_ffff00009074b22c(0xe);
    local_6c = thunk_FUN_ffff00009074b22c(0xf);
    puVar3 = PTR_DAT_ffff000090839ab8;
    if (*(short *)(PTR_DAT_ffff000090839ab8 + 0x12) == 2) {
      puVar10 = &local_50;
      iVar6 = FUN_ffff0000907a4318(&local_78,0x10,&local_50);
      if (iVar6 != 0) {
        return param_1;
      }
    }
    else {
      puVar10 = &local_68;
      FUN_ffff0000907a4118(&local_78,0x10,&local_68);
    }
    FUN_ffff00009077eb20(&local_90,puVar10,0x10);
    puVar5 = PTR_DAT_ffff000090839ad0;
    puVar4 = PTR_DAT_ffff000090839ac8;
    *param_1 = *(undefined4 *)PTR_DAT_ffff000090839ac8;
    param_1[1] = *(undefined4 *)puVar5;
    uVar7 = FUN_ffff0000907aa2e4();
    param_1[4] = local_90;
    param_1[2] = uVar7;
    param_1[3] = local_7c;
    puVar1 = PTR_DAT_ffff000090839ac0 + 0x68;
    if (*(short *)(puVar3 + 0x12) != 2) {
      puVar1 = puVar3 + 0x2a9;
    }
    *(ulong *)(param_1 + 5) = CONCAT44(local_88,uStack_8c);
    param_1[7] = local_84;
    FUN_ffff00009077f2ac(param_1 + 8,puVar1,0x10);
    puVar9 = param_1 + 0x14;
    param_1[0xc] = _DAT_ffff000013260090;
    param_1[0xd] = _DAT_ffff0000132600a0;
    param_1[0xe] = _DAT_ffff0000132600b0;
    param_1[0xf] = _DAT_ffff0000132600c0;
    param_1[0x10] = _DAT_ffff0000132600d0;
    param_1[0x11] = _DAT_ffff0000132600e0;
    param_1[0x12] = _DAT_ffff0000132600f0;
    param_1[0x13] = _DAT_ffff000013260100;
    FUN_ffff00009077e11c(s_start_dump_lk_masp_atag_ffff0000907f0a8b);
    uVar7 = *(undefined4 *)puVar4;
    uVar2 = *(undefined4 *)puVar5;
    uVar8 = FUN_ffff0000907aa2e4();
    FUN_ffff00009077e11c(s_dump_sw_sbc_0x_x__sw_sdl_0x_x___h_ffff0000907f5a46,uVar7,uVar2,uVar8);
    FUN_ffff00009077e11c(s_dump_lock_state__0x_x_ffff0000907f0aa5,local_7c);
    FUN_ffff00009077e11c
              (s_dump_rid__0x_x__0x_x__0x_x__0x_x_ffff0000907fad9f,local_90,uStack_8c,local_88,
               local_84);
  }
  return puVar9;
}


