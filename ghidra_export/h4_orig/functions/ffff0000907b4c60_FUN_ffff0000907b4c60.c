// ffff0000907b4c60  size=184  FUN_ffff0000907b4c60

long FUN_ffff0000907b4c60(long param_1,long param_2)

{
  int iVar1;
  char *pcVar2;
  long local_18;
  
  local_18 = param_1 + -0x1000000000000;
  if (param_1 == 0) {
    pcVar2 = s__s_Wrong_address_in__s_ffff0000907dbd1a;
  }
  else {
    if (param_2 != 0) {
      iVar1 = FUN_ffff00009077ba64
                        (DAT_ffff000090839928,s_vm_dice_reserved_ffff0000907d70ab,param_2,&local_18,
                         0xc,param_1,1,0);
      if (iVar1 == 0) {
        return local_18;
      }
      FUN_ffff00009077e11c
                (s__s_VM_mapping_failed__err___d_ffff0000907d89c0,s__DICE__ffff0000907f55ec,iVar1);
      return 0;
    }
    pcVar2 = s__s_Wrong_size_in__s_ffff000090803596;
  }
  FUN_ffff00009077e11c(pcVar2,s__DICE__ffff0000907f55ec,s_vm_map_ffff000090800124);
  return 0;
}


