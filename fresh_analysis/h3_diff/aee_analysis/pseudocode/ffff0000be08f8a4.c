
undefined4 FUN_ffff0000be08f8a4(long param_1)

{
  long lVar1;
  long lVar2;
  int iVar3;
  long *plVar4;
  long *plVar5;
  char *pcVar6;
  long local_48;
  
  plVar4 = (long *)FUN_ffff0000be01cb9c(s_security_dice_mtee_ffff0000be0bca9f,0);
  if (plVar4 == (long *)0x0) {
    FUN_ffff0000be05fd98
              (s__s_mblock_query___s__failed__ffff0000be0ae188,s__DICE__ffff0000be0bd9dd,
               s_security_dice_mtee_ffff0000be0bca9f);
    return 0x10d009;
  }
  plVar5 = (long *)FUN_ffff0000be01cb9c(s_security_dice_pvmfw_ffff0000be0c6d80,0);
  lVar1 = *plVar4;
  local_48 = lVar1 + -0x1000000000000;
  if (lVar1 == 0) {
    pcVar6 = s__s_Wrong_address_in__s_ffff0000be0ac16c;
  }
  else {
    if (plVar4[1] != 0) {
      iVar3 = FUN_ffff0000be05d9a8
                        (PTR_DAT_ffff0000be0f5570,s_vm_dice_reserved_ffff0000be0a8a79,plVar4[1],
                         &local_48,0xc,lVar1,1,0);
      lVar1 = local_48;
      if (iVar3 != 0) {
        FUN_ffff0000be05fd98
                  (s__s_VM_mapping_failed__err___d_ffff0000be0a9cd0,s__DICE__ffff0000be0bd9dd,iVar3)
        ;
        return 0x10d00f;
      }
      if (local_48 == 0) {
        return 0x10d00f;
      }
      lVar2 = *plVar5;
      local_48 = lVar2 + -0x1000000000000;
      if (lVar2 == 0) {
        pcVar6 = s__s_Wrong_address_in__s_ffff0000be0ac16c;
      }
      else {
        if (plVar5[1] != 0) {
          iVar3 = FUN_ffff0000be05d9a8
                            (PTR_DAT_ffff0000be0f5570,s_vm_dice_reserved_ffff0000be0a8a79,plVar5[1],
                             &local_48,0xc,lVar2,1,0);
          lVar2 = local_48;
          if (iVar3 != 0) {
            FUN_ffff0000be05fd98
                      (s__s_VM_mapping_failed__err___d_ffff0000be0a9cd0,s__DICE__ffff0000be0bd9dd,
                       iVar3);
            return 0x10d00f;
          }
          if (local_48 != 0) {
            FUN_ffff0000be087428
                      (lVar1 + (ulong)DAT_ffff0000be1b7d74 + 1,param_1 + (ulong)DAT_ffff0000be1b7d74
                       ,DAT_ffff0000be1b7d78);
            FUN_ffff0000be087428
                      (lVar2 + (ulong)DAT_ffff0000be1b7d74 + 1,param_1 + (ulong)DAT_ffff0000be1b7d74
                       ,DAT_ffff0000be1b7d78);
            iVar3 = FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,lVar1);
            if ((iVar3 == 0) &&
               (iVar3 = FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,lVar2), iVar3 == 0)) {
              return 0;
            }
            FUN_ffff0000be05fd98
                      (s__s_VM_unmapping_failed__err___d_ffff0000be0a7861,s__DICE__ffff0000be0bd9dd,
                       iVar3);
            return 0x10d010;
          }
          return 0x10d00f;
        }
        pcVar6 = s__s_Wrong_size_in__s_ffff0000be0c6d94;
      }
      FUN_ffff0000be05fd98(pcVar6,s__DICE__ffff0000be0bd9dd,s_vm_map_ffff0000be0c4aed);
      return 0x10d00f;
    }
    pcVar6 = s__s_Wrong_size_in__s_ffff0000be0c6d94;
  }
  FUN_ffff0000be05fd98(pcVar6,s__DICE__ffff0000be0bd9dd,s_vm_map_ffff0000be0c4aed);
  return 0x10d00f;
}


