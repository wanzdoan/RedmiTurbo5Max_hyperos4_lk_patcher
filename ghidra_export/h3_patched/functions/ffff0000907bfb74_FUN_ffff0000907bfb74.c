// ffff0000907bfb74  size=572  FUN_ffff0000907bfb74

int FUN_ffff0000907bfb74(long param_1,long *param_2,undefined8 param_3,long param_4)

{
  int iVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  ulong uVar4;
  long lVar5;
  long lVar6;
  long lVar7;
  undefined8 local_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  char local_54 [4];
  long local_38;
  
  lVar6 = *param_2;
  while( true ) {
    if (lVar6 == 0) {
      return 0;
    }
    local_38 = 0;
    local_54[0] = '\0';
    local_68 = 0;
    uStack_60 = 0;
    local_78 = 0;
    uStack_70 = 0;
    local_80 = 0;
    uVar2 = thunk_FUN_ffff00009077ef88(lVar6);
    uVar3 = thunk_FUN_ffff00009077ef88(param_3);
    uVar4 = FUN_ffff0000907c0d10(&local_78,0x20,lVar6,uVar2,param_3,uVar3);
    if ((uVar4 & 1) == 0) {
      uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      FUN_ffff0000907c0874
                (uVar2,&DAT_ffff0000907ebe0c,&DAT_ffff00009080341e,s___ERROR__ffff0000907e0722,
                 s_Partition_name_and_suffix_does_n_ffff0000908033ba,0);
      iVar1 = 6;
      goto joined_r0xffff0000907bfd24;
    }
    iVar1 = (**(code **)(param_1 + 0x58))(param_1,&local_78,&local_80);
    if (iVar1 != 0) break;
    iVar1 = FUN_ffff0000907c068c(param_1,&local_78,local_80,&local_38,local_54);
    if (iVar1 != 0) goto joined_r0xffff0000907bfd24;
    lVar6 = *(long *)(param_4 + 0x20);
    if (lVar6 == 0x20) {
      uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      FUN_ffff0000907c0874
                (uVar2,&DAT_ffff0000907ebe0c,&DAT_ffff0000907ef092,s___ERROR__ffff0000907e0722,
                 &local_78,s___Too_many_loaded_partitions__ffff0000907d0736,0);
      goto LAB_ffff0000907bfd68;
    }
    lVar7 = *(long *)(param_4 + 0x18);
    *(long *)(param_4 + 0x20) = lVar6 + 1;
    lVar5 = FUN_ffff0000907c101c(*param_2);
    *(long *)(lVar7 + lVar6 * 0x20) = lVar5;
    if (lVar5 == 0) {
      iVar1 = 1;
      goto joined_r0xffff0000907bfd24;
    }
    lVar7 = lVar7 + lVar6 * 0x20;
    *(undefined8 *)(lVar7 + 0x10) = local_80;
    *(char *)(lVar7 + 0x18) = local_54[0];
    *(long *)(lVar7 + 8) = local_38;
    lVar6 = param_2[1];
    param_2 = param_2 + 1;
  }
  if (iVar1 != 1) {
    uVar2 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
    FUN_ffff0000907c0874
              (uVar2,&DAT_ffff0000907ebe0c,&DAT_ffff0000907e22bc,s___ERROR__ffff0000907e0722,
               &local_78,s___Error_determining_partition_si_ffff0000907facab,0);
    iVar1 = 2;
    goto joined_r0xffff0000907bfd24;
  }
LAB_ffff0000907bfd68:
  iVar1 = 1;
joined_r0xffff0000907bfd24:
  if (local_38 == 0) {
    return iVar1;
  }
  if (local_54[0] != '\0') {
    return iVar1;
  }
  FUN_ffff0000907c09a0();
  return iVar1;
}


