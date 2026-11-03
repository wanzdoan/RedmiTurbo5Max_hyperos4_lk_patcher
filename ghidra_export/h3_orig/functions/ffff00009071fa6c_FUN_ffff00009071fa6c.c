// ffff00009071fa6c  size=680  FUN_ffff00009071fa6c

int FUN_ffff00009071fa6c(long param_1)

{
  ushort uVar1;
  int iVar2;
  int iVar3;
  undefined4 uVar4;
  long lVar5;
  char *pcVar6;
  undefined *puVar7;
  undefined8 uVar8;
  ushort *puVar9;
  uint uVar10;
  undefined4 local_54;
  undefined8 local_50;
  undefined8 local_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  if (param_1 == 0) {
    FUN_ffff00009077de54
              (s__s__d___LK_AB___s_check_args_fai_ffff0000907f5ef6,
               s_set_active_slot_ffff0000907d288f,0x170,s_set_active_slot_ffff0000907d288f);
    return -1;
  }
  iVar2 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907d3dfd);
  if (iVar2 == 0) {
    uVar10 = 0;
  }
  else {
    iVar3 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907ed942);
    if (iVar3 != 0) {
      FUN_ffff00009077de54
                (s__s__d___LK_AB__unknown_slot_suff_ffff0000907fb2bb,
                 s_check_suffix_with_slot_ffff0000907d7656,0x6a);
      FUN_ffff00009077de54
                (s__s__d___LK_AB___s_failed__slot__0_ffff0000907f0ebd,
                 s_set_active_slot_ffff0000907d288f,0x176,s_set_active_slot_ffff0000907d288f,
                 0xffffffff);
      return -1;
    }
    uVar10 = 1;
  }
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  local_48 = 0;
  iVar3 = FUN_ffff00009071f6f0(&local_50,0);
  if (iVar3 < 0) {
    pcVar6 = s__s__d___LK_AB__partition_read_fa_ffff0000907ddb2c;
    uVar8 = 0x181;
    goto LAB_ffff00009071fcf0;
  }
  local_50 = CONCAT44(0x42414342,(undefined4)local_50);
  local_48 = CONCAT53(local_48._3_5_,CONCAT21(local_48._1_2_ & 0xfff8,1)) | 0x200;
  iVar3 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907d3dfd);
  if (iVar3 == 0) {
    puVar7 = &DAT_ffff0000907d3dfd;
LAB_ffff00009071fbd4:
    FUN_ffff00009077e858(&local_50,puVar7,2);
  }
  else {
    iVar3 = FUN_ffff00009077ede4(param_1,&DAT_ffff0000907ed942);
    if (iVar3 == 0) {
      puVar7 = &DAT_ffff0000907ed942;
      goto LAB_ffff00009071fbd4;
    }
    FUN_ffff00009077de54
              (s__s__d___LK_AB__unknown_slot_suff_ffff0000907fb2bb,
               s_set_active_slot_ffff0000907d288f,0x18d);
  }
  puVar9 = (ushort *)((long)&local_48 + 4U | (ulong)(iVar2 == 0) << 1);
  *(undefined1 *)((long)&local_48 + 4U | (ulong)uVar10 << 1) = 0x77;
  uVar1 = *puVar9;
  if (6 < (uVar1 & 0xf)) {
    *puVar9 = uVar1 & 0xfff0 | 6;
  }
  uVar4 = FUN_ffff00009076c7c4(0,&local_50,0x1c);
  local_54 = 1;
  if (iVar2 != 0) {
    local_54 = 2;
  }
  uStack_38 = CONCAT44(uVar4,(undefined4)uStack_38);
  lVar5 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
  if (lVar5 == 0) {
    FUN_ffff00009077de54
              (s__s__d___LK_AB__open_bdev_fail_ffff0000907e273d,s_set_active_slot_ffff0000907d288f,
               0x1a2);
    return -0x13;
  }
  iVar2 = FUN_ffff00009077bf48(lVar5,0x100,&local_54);
  if (iVar2 < 0) {
    FUN_ffff00009077de54
              (s__s__d___LK_AB__set_to_slot__d_fa_ffff0000907d8f07,
               s_set_active_slot_ffff0000907d288f,0x1a9,uVar10,iVar2);
    FUN_ffff00009077bce4(lVar5);
    return iVar2;
  }
  FUN_ffff00009077bce4(lVar5);
  iVar3 = FUN_ffff00009071f6f0(&local_50,1);
  if (-1 < iVar3) {
    return 0;
  }
  pcVar6 = s__s__d___LK_AB__partition_write_f_ffff0000907ddb5d;
  uVar8 = 0x1b2;
LAB_ffff00009071fcf0:
  FUN_ffff00009077de54(pcVar6,s_set_active_slot_ffff0000907d288f,uVar8,iVar3);
  return iVar3;
}


