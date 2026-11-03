// ffff0000907b5134  size=588  FUN_ffff0000907b5134

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined4 FUN_ffff0000907b5134(long param_1)

{
  long lVar1;
  long lVar2;
  int iVar3;
  long *plVar4;
  long *plVar5;
  char *pcVar6;
  long local_48;
  
  plVar4 = (long *)FUN_ffff00009071a204(s_security_dice_mtee_ffff0000907f3aca,0);
  if (plVar4 == (long *)0x0) {
    FUN_ffff00009077de54
              (s__s_mblock_query___s__failed__ffff0000907defeb,s__DICE__ffff0000907f5468,
               s_security_dice_mtee_ffff0000907f3aca);
    return 0x10d009;
  }
  plVar5 = (long *)FUN_ffff00009071a204(s_security_dice_pvmfw_ffff00009080335a,0);
  lVar1 = *plVar4;
  local_48 = lVar1 + -0x1000000000000;
  if (lVar1 == 0) {
    pcVar6 = s__s_Wrong_address_in__s_ffff0000907dbbf8;
  }
  else {
    if (plVar4[1] != 0) {
      iVar3 = FUN_ffff00009077b79c
                        (DAT_ffff000090839908,s_vm_dice_reserved_ffff0000907d7015,plVar4[1],
                         &local_48,0xc,lVar1,1,0);
      lVar1 = local_48;
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s__s_VM_mapping_failed__err___d_ffff0000907d893f,s__DICE__ffff0000907f5468,iVar3)
        ;
        return 0x10d00f;
      }
      if (local_48 == 0) {
        return 0x10d00f;
      }
      lVar2 = *plVar5;
      local_48 = lVar2 + -0x1000000000000;
      if (lVar2 == 0) {
        pcVar6 = s__s_Wrong_address_in__s_ffff0000907dbbf8;
      }
      else {
        if (plVar5[1] != 0) {
          iVar3 = FUN_ffff00009077b79c
                            (DAT_ffff000090839908,s_vm_dice_reserved_ffff0000907d7015,plVar5[1],
                             &local_48,0xc,lVar2,1,0);
          lVar2 = local_48;
          if (iVar3 != 0) {
            FUN_ffff00009077de54
                      (s__s_VM_mapping_failed__err___d_ffff0000907d893f,s__DICE__ffff0000907f5468,
                       iVar3);
            return 0x10d00f;
          }
          if (local_48 != 0) {
            FUN_ffff0000907acf74
                      (lVar1 + (ulong)_DAT_ffff00009090d7f4 + 1,
                       param_1 + (ulong)_DAT_ffff00009090d7f4,_DAT_ffff00009090d7f8);
            FUN_ffff0000907acf74
                      (lVar2 + (ulong)_DAT_ffff00009090d7f4 + 1,
                       param_1 + (ulong)_DAT_ffff00009090d7f4,_DAT_ffff00009090d7f8);
            iVar3 = FUN_ffff00009077bb68(DAT_ffff000090839908,lVar1);
            if ((iVar3 == 0) &&
               (iVar3 = FUN_ffff00009077bb68(DAT_ffff000090839908,lVar2), iVar3 == 0)) {
              return 0;
            }
            FUN_ffff00009077de54
                      (s__s_VM_unmapping_failed__err___d_ffff0000907d53f3,s__DICE__ffff0000907f5468,
                       iVar3);
            return 0x10d010;
          }
          return 0x10d00f;
        }
        pcVar6 = s__s_Wrong_size_in__s_ffff00009080336e;
      }
      FUN_ffff00009077de54(pcVar6,s__DICE__ffff0000907f5468,s_vm_map_ffff0000907fff7c);
      return 0x10d00f;
    }
    pcVar6 = s__s_Wrong_size_in__s_ffff00009080336e;
  }
  FUN_ffff00009077de54(pcVar6,s__DICE__ffff0000907f5468,s_vm_map_ffff0000907fff7c);
  return 0x10d00f;
}


