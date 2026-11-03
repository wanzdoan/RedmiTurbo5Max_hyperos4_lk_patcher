
int FUN_ffff0000907adc98(long param_1,undefined4 param_2)

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
  local_e0 = DAT_ffff000090804c50;
  uStack_d8 = DAT_ffff000090804c58;
  iVar2 = FUN_ffff000090716918();
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
  FUN_ffff00009077eb20((long)&local_148 + 5,&DAT_ffff00009090cbb4,0x20);
  FUN_ffff00009077eb20((long)&uStack_128 + 6,&DAT_ffff00009090cc18,0x20);
  FUN_ffff00009077eb20((long)&uStack_108 + 6,&local_e0,0x10);
  auStack_e8[6] = 0;
  if (1 < DAT_ffff00009090cc60) {
    FUN_ffff00009077eb20((long)&uStack_108 + 6,&DAT_ffff00009090cc3c,DAT_ffff00009090cc4c);
    FUN_ffff00009077eb20((long)&uStack_f8 + 6,&DAT_ffff00009090cc50,DAT_ffff00009090cc60);
    auStack_e8[6] = (char)DAT_ffff00009090cc60;
  }
  if (DAT_ffff00009090c5d8 == 0) {
    return 0;
  }
  uVar5 = 1;
  if (DAT_ffff00009090c5dc == 1) {
    uVar5 = 2;
  }
  uVar1 = 3;
  if (DAT_ffff00009090c5dc != 2) {
    uVar1 = uVar5;
  }
  FUN_ffff00009077e11c
            (s___s__Choose_decode_mode_as__u__ffff0000907d225e,&DAT_ffff0000907f228a,uVar1);
  iVar3 = FUN_ffff0000907a29f0(&local_158,uVar1,DAT_ffff00009090c5d8);
  if (iVar3 != 0) {
    return iVar3;
  }
  iVar3 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s___s__Decode_partition___d_bytes_i_ffff0000907f3bb8,&DAT_ffff0000907f228a,param_2,
             iVar3 - iVar2,0);
  iVar2 = FUN_ffff000090716918();
  iVar3 = FUN_ffff00009077eae8(&DAT_ffff00009090cbd8,&local_d0,0x40);
  if (iVar3 == 0) {
    return 0;
  }
  iVar3 = FUN_ffff0000907a28ec(param_1,param_2,&local_d0,DAT_ffff00009090c5cc);
  if (iVar3 != 0) {
    return iVar3;
  }
  iVar3 = FUN_ffff00009077eae8(&local_d0,&DAT_ffff00009090cbd8,0x40);
  if (iVar3 != 0) {
    local_60 = 0;
    uStack_58 = 0;
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    local_90 = 0;
    uStack_88 = 0;
    FUN_ffff00009077e11c(s___s__SW__FW_decode_check__ffff0000907fe636,&DAT_ffff0000907f228a);
    if (((param_1 == 0) ||
        (iVar3 = FUN_ffff0000907a29bc(param_1,param_2,&local_90,DAT_ffff00009090c5cc), iVar3 != 0))
       || (iVar3 = FUN_ffff00009077eae8(&local_90,&DAT_ffff00009090cbd8,0x40), iVar3 != 0)) {
      iVar3 = 0x10c007;
      goto LAB_ffff0000907adf04;
    }
  }
  iVar3 = 0;
LAB_ffff0000907adf04:
  iVar4 = FUN_ffff000090716918();
  FUN_ffff00009077e11c
            (s___s__Check_partition___d_bytes_i_ffff0000907ebc8c,&DAT_ffff0000907f228a,param_2,
             iVar4 - iVar2,iVar3);
  return iVar3;
}


