
undefined4 FUN_ffff0000907b57a4(long param_1)

{
  long lVar1;
  long lVar2;
  int iVar3;
  long *plVar4;
  long *plVar5;
  char *pcVar6;
  long local_48;
  
  plVar4 = (long *)FUN_ffff00009071a348(s_security_dice_mtee_ffff0000907f3c6d,0);
  if (plVar4 == (long *)0x0) {
    FUN_ffff00009077e11c
              (s__s_mblock_query___s__failed__ffff0000907df0a5,s__DICE__ffff0000907f55ec,
               s_security_dice_mtee_ffff0000907f3c6d);
    return 0x10d009;
  }
  plVar5 = (long *)FUN_ffff00009071a348(s_security_dice_pvmfw_ffff000090803582,0);
  lVar1 = *plVar4;
  local_48 = lVar1 + -0x1000000000000;
  if (lVar1 == 0) {
    pcVar6 = s__s_Wrong_address_in__s_ffff0000907dbd1a;
  }
  else {
    if (plVar4[1] != 0) {
      iVar3 = FUN_ffff00009077ba64
                        (PTR_DAT_ffff000090839928,s_vm_dice_reserved_ffff0000907d70ab,plVar4[1],
                         &local_48,0xc,lVar1,1,0);
      lVar1 = local_48;
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s__s_VM_mapping_failed__err___d_ffff0000907d89c0,s__DICE__ffff0000907f55ec,iVar3)
        ;
        return 0x10d00f;
      }
      if (local_48 == 0) {
        return 0x10d00f;
      }
      lVar2 = *plVar5;
      local_48 = lVar2 + -0x1000000000000;
      if (lVar2 == 0) {
        pcVar6 = s__s_Wrong_address_in__s_ffff0000907dbd1a;
      }
      else {
        if (plVar5[1] != 0) {
          iVar3 = FUN_ffff00009077ba64
                            (PTR_DAT_ffff000090839928,s_vm_dice_reserved_ffff0000907d70ab,plVar5[1],
                             &local_48,0xc,lVar2,1,0);
          lVar2 = local_48;
          if (iVar3 != 0) {
            FUN_ffff00009077e11c
                      (s__s_VM_mapping_failed__err___d_ffff0000907d89c0,s__DICE__ffff0000907f55ec,
                       iVar3);
            return 0x10d00f;
          }
          if (local_48 != 0) {
            FUN_ffff0000907ad234
                      (lVar1 + (ulong)DAT_ffff00009090d8b4 + 1,param_1 + (ulong)DAT_ffff00009090d8b4
                       ,DAT_ffff00009090d8b8);
            FUN_ffff0000907ad234
                      (lVar2 + (ulong)DAT_ffff00009090d8b4 + 1,param_1 + (ulong)DAT_ffff00009090d8b4
                       ,DAT_ffff00009090d8b8);
            iVar3 = FUN_ffff00009077be30(PTR_DAT_ffff000090839928,lVar1);
            if ((iVar3 == 0) &&
               (iVar3 = FUN_ffff00009077be30(PTR_DAT_ffff000090839928,lVar2), iVar3 == 0)) {
              return 0;
            }
            FUN_ffff00009077e11c
                      (s__s_VM_unmapping_failed__err___d_ffff0000907d5430,s__DICE__ffff0000907f55ec,
                       iVar3);
            return 0x10d010;
          }
          return 0x10d00f;
        }
        pcVar6 = s__s_Wrong_size_in__s_ffff000090803596;
      }
      FUN_ffff00009077e11c(pcVar6,s__DICE__ffff0000907f55ec,s_vm_map_ffff000090800124);
      return 0x10d00f;
    }
    pcVar6 = s__s_Wrong_size_in__s_ffff000090803596;
  }
  FUN_ffff00009077e11c(pcVar6,s__DICE__ffff0000907f55ec,s_vm_map_ffff000090800124);
  return 0x10d00f;
}


