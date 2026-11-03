
undefined4 FUN_ffff0000b8071bc0(long param_1)

{
  long lVar1;
  long lVar2;
  int iVar3;
  long *plVar4;
  long *plVar5;
  char *pcVar6;
  long local_48;
  
  plVar4 = (long *)FUN_ffff0000b800db20(s_security_dice_mtee_ffff0000b8098f50,0);
  if (plVar4 == (long *)0x0) {
    FUN_ffff0000b804c33c
              (s__s_mblock_query___s__failed__ffff0000b808d97c,s__DICE__ffff0000b8099de7,
               s_security_dice_mtee_ffff0000b8098f50);
    return 0x10d009;
  }
  plVar5 = (long *)FUN_ffff0000b800db20(s_security_dice_pvmfw_ffff0000b80a126d,0);
  lVar1 = *plVar4;
  local_48 = lVar1 + -0x1000000000000;
  if (lVar1 == 0) {
    pcVar6 = s__s_Wrong_address_in__s_ffff0000b808c13e;
  }
  else {
    if (plVar4[1] != 0) {
      iVar3 = FUN_ffff0000b804a5f8
                        (PTR_DAT_ffff0000b81086d8,s_vm_dice_reserved_ffff0000b808978c,plVar4[1],
                         &local_48,0xc,lVar1,1,0);
      lVar1 = local_48;
      if (iVar3 != 0) {
        FUN_ffff0000b804c33c
                  (s__s_VM_mapping_failed__err___d_ffff0000b808a594,s__DICE__ffff0000b8099de7,iVar3)
        ;
        return 0x10d00f;
      }
      if (local_48 == 0) {
        return 0x10d00f;
      }
      lVar2 = *plVar5;
      local_48 = lVar2 + -0x1000000000000;
      if (lVar2 == 0) {
        pcVar6 = s__s_Wrong_address_in__s_ffff0000b808c13e;
      }
      else {
        if (plVar5[1] != 0) {
          iVar3 = FUN_ffff0000b804a5f8
                            (PTR_DAT_ffff0000b81086d8,s_vm_dice_reserved_ffff0000b808978c,plVar5[1],
                             &local_48,0xc,lVar2,1,0);
          lVar2 = local_48;
          if (iVar3 != 0) {
            FUN_ffff0000b804c33c
                      (s__s_VM_mapping_failed__err___d_ffff0000b808a594,s__DICE__ffff0000b8099de7,
                       iVar3);
            return 0x10d00f;
          }
          if (local_48 != 0) {
            FUN_ffff0000b8069a2c
                      (lVar1 + (ulong)DAT_ffff0000b81b9ab4 + 1,param_1 + (ulong)DAT_ffff0000b81b9ab4
                       ,DAT_ffff0000b81b9ab8);
            FUN_ffff0000b8069a2c
                      (lVar2 + (ulong)DAT_ffff0000b81b9ab4 + 1,param_1 + (ulong)DAT_ffff0000b81b9ab4
                       ,DAT_ffff0000b81b9ab8);
            iVar3 = FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,lVar1);
            if ((iVar3 == 0) &&
               (iVar3 = FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,lVar2), iVar3 == 0)) {
              return 0;
            }
            FUN_ffff0000b804c33c
                      (s__s_VM_unmapping_failed__err___d_ffff0000b808885e,s__DICE__ffff0000b8099de7,
                       iVar3);
            return 0x10d010;
          }
          return 0x10d00f;
        }
        pcVar6 = s__s_Wrong_size_in__s_ffff0000b80a18f2;
      }
      FUN_ffff0000b804c33c(pcVar6,s__DICE__ffff0000b8099de7,s_vm_map_ffff0000b809ff60);
      return 0x10d00f;
    }
    pcVar6 = s__s_Wrong_size_in__s_ffff0000b80a18f2;
  }
  FUN_ffff0000b804c33c(pcVar6,s__DICE__ffff0000b8099de7,s_vm_map_ffff0000b809ff60);
  return 0x10d00f;
}


