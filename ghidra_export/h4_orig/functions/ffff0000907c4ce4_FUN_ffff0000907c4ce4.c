// ffff0000907c4ce4  size=504  FUN_ffff0000907c4ce4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907c4ce4(void)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  undefined8 local_168;
  undefined8 uStack_160;
  undefined8 local_158;
  undefined8 uStack_150;
  int local_144;
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
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  uStack_38 = 0;
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
  local_130 = 0;
  uStack_128 = 0;
  local_140 = 0;
  uStack_138 = 0;
  local_144 = 1;
  local_158 = 0;
  uStack_150 = 0;
  local_168 = 0;
  uStack_160 = 0;
  FUN_ffff00009077e11c(s_sending_root_of_trust_info____ffff0000907da3da);
  iVar1 = thunk_FUN_ffff0000907af224(&local_120,0x100);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c(s_fail_to_get_public_key_info_ffff0000907f3d74);
  }
  iVar1 = thunk_FUN_ffff0000907a4318(&local_120,0x100,&local_140);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c(s_fail_to_calculate_public_key_has_ffff0000907f0b65);
  }
  iVar1 = thunk_FUN_ffff0000907b65c4(&local_144);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c(s_fail_to_get_device_lock_state_ffff0000908001d2);
  }
  uVar2 = local_140;
  uVar3 = uStack_138;
  uVar4 = local_130;
  uVar5 = uStack_128;
  if (local_144 == 0) {
    uVar2 = 0;
    uVar3 = 0;
    uVar4 = 0;
    uVar5 = 0;
  }
  FUN_ffff00009071ebf4(0xc2000120,uVar2,uVar3,uVar4,uVar5,0,0,0,&local_168);
  FUN_ffff00009071ebf4
            (0xc2000120,(long)local_144,_DAT_ffff00009090d9c0,_DAT_ffff00009090d990,
             _DAT_ffff00009090d994,0,0,0,&local_168);
  FUN_ffff00009071ebf4
            (0xc2000120,_DAT_ffff00009090d9a0,_DAT_ffff00009090d9a8,_DAT_ffff00009090d9b0,
             _DAT_ffff00009090d9b8,0,0,0,&local_168);
  FUN_ffff00009071ebf4(0xc2000120,_DAT_ffff00009090d998,_DAT_ffff00009090d99c,0,0,0,0,0,&local_168);
  FUN_ffff00009071ebf4(0xc2000120,0,0,0,0,0,0,0,&local_168);
  return;
}


