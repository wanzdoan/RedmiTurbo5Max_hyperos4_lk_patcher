// ffff0000907ad9d0  size=660  FUN_ffff0000907ad9d0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907ad9d0(long param_1,undefined4 param_2)

{
  undefined4 uVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  undefined4 uVar5;
  long local_158;
  long local_150;
  undefined8 local_148;
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
  undefined1 auStack_e8 [8];
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
  
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = DAT_ffff000090804a28;
  uStack_d8 = DAT_ffff000090804a30;
  iVar2 = FUN_ffff0000907167d4();
  local_f0 = 0;
  auStack_e8 = (undefined1  [8])0x0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  local_140 = 0;
  uStack_138 = 0;
  uStack_128 = 0x200000000000;
  local_148 = (ulong)CONCAT14(0x20,param_2);
  local_158 = param_1;
  local_150 = param_1;
  FUN_ffff00009077e858((long)&local_148 + 5,0xffff00009090caf4,0x20);
  FUN_ffff00009077e858((long)&uStack_128 + 6,0xffff00009090cb58,0x20);
  FUN_ffff00009077e858((long)&uStack_108 + 6,&local_e0,0x10);
  auStack_e8[6] = 0;
  if (1 < _DAT_ffff00009090cba0) {
    FUN_ffff00009077e858((long)&uStack_108 + 6,0xffff00009090cb7c,_DAT_ffff00009090cb8c);
    FUN_ffff00009077e858((long)&uStack_f8 + 6,0xffff00009090cb90,_DAT_ffff00009090cba0);
    auStack_e8[6] = (char)_DAT_ffff00009090cba0;
  }
  if (_DAT_ffff00009090c518 == 0) {
    return 0;
  }
  uVar5 = 1;
  if (_DAT_ffff00009090c51c == 1) {
    uVar5 = 2;
  }
  uVar1 = 3;
  if (_DAT_ffff00009090c51c != 2) {
    uVar1 = uVar5;
  }
  FUN_ffff00009077de54
            (s___s__Choose_decode_mode_as__u__ffff0000907d2221,&DAT_ffff0000907f20e7,uVar1);
  iVar3 = FUN_ffff0000907a2730(&local_158,uVar1,_DAT_ffff00009090c518);
  if (iVar3 != 0) {
    return iVar3;
  }
  iVar3 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s___s__Decode_partition___d_bytes_i_ffff0000907f3a15,&DAT_ffff0000907f20e7,param_2,
             iVar3 - iVar2,0);
  iVar2 = FUN_ffff0000907167d4();
  iVar3 = FUN_ffff00009077e820(0xffff00009090cb18,&local_d0,0x40);
  if (iVar3 == 0) {
    return 0;
  }
  iVar3 = FUN_ffff0000907a262c(param_1,param_2,&local_d0,_DAT_ffff00009090c50c);
  if (iVar3 != 0) {
    return iVar3;
  }
  iVar3 = FUN_ffff00009077e820(&local_d0,0xffff00009090cb18,0x40);
  if (iVar3 != 0) {
    local_60 = 0;
    uStack_58 = 0;
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    local_90 = 0;
    uStack_88 = 0;
    FUN_ffff00009077de54(s___s__SW__FW_decode_check__ffff0000907fe48e,&DAT_ffff0000907f20e7);
    if (((param_1 == 0) ||
        (iVar3 = FUN_ffff0000907a26fc(param_1,param_2,&local_90,_DAT_ffff00009090c50c), iVar3 != 0))
       || (iVar3 = FUN_ffff00009077e820(&local_90,0xffff00009090cb18,0x40), iVar3 != 0)) {
      iVar3 = 0x10c007;
      goto LAB_ffff0000907adc3c;
    }
  }
  iVar3 = 0;
LAB_ffff0000907adc3c:
  iVar4 = FUN_ffff0000907167d4();
  FUN_ffff00009077de54
            (s___s__Check_partition___d_bytes_i_ffff0000907ebaf2,&DAT_ffff0000907f20e7,param_2,
             iVar4 - iVar2,iVar3);
  return iVar3;
}


